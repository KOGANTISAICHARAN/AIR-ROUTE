package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AirQualityScenario
import com.example.data.AirQualityStateData
import com.example.data.NotificationPreferences
import com.example.data.RealAirQualityRepository
import com.example.data.UserProfile
import com.example.data.UserPreferencesRepository
import com.example.notifications.AirRouteNotificationManager
import com.example.ui.components.AirRouteTab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AuthScreenState {
    WELCOME,
    SIGN_IN,
    SIGN_UP,
    ONBOARDING,
    MAIN_APP
}

data class AirRouteUiState(
    val authScreenState: AuthScreenState = AuthScreenState.WELCOME,
    val selectedTab: AirRouteTab = AirRouteTab.HOME,
    val userProfile: UserProfile = UserProfile(),
    val currentScenario: AirQualityScenario? = null, // null = Live Real Data Mode
    val selectedLocation: String = "Hyderabad, Telangana",
    val destinationLocation: String = "KBR National Park",
    val userLat: Double? = null,
    val userLng: Double? = null,
    val selectedActivityId: String = "running",
    val isDataInfoOpen: Boolean = false,
    val isAiExplanationOpen: Boolean = false,
    val isLocationSearchOpen: Boolean = false,
    val isLoadingData: Boolean = false,
    val currentData: AirQualityStateData? = null,
    val dismissedAlertIds: Set<String> = emptySet()
)

class AirRouteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AirRouteUiState())
    val uiState: StateFlow<AirRouteUiState> = _uiState.asStateFlow()

    private var prefsRepo: UserPreferencesRepository? = null
    private var notifManager: AirRouteNotificationManager? = null

    fun initializeWithContext(context: Context) {
        if (prefsRepo == null) {
            val repo = UserPreferencesRepository(context)
            prefsRepo = repo
            notifManager = AirRouteNotificationManager(context)

            val profile = repo.getUserProfile()
            val startState = if (repo.isLoggedIn()) AuthScreenState.MAIN_APP else AuthScreenState.WELCOME

            _uiState.update {
                it.copy(
                    userProfile = profile,
                    selectedLocation = profile.location,
                    authScreenState = startState
                )
            }
            refreshData()
        }
    }

    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoadingData = true) }
            val state = _uiState.value
            val fetchedData = RealAirQualityRepository.fetchLiveData(
                scenario = state.currentScenario,
                selectedLocation = state.selectedLocation,
                selectedActivityId = state.selectedActivityId,
                destinationLocation = state.destinationLocation,
                userLat = state.userLat,
                userLng = state.userLng
            )

            val filteredAlerts = fetchedData.alerts.filter { !state.dismissedAlertIds.contains(it.id) }
            val finalData = fetchedData.copy(alerts = filteredAlerts)

            _uiState.update {
                it.copy(
                    isLoadingData = false,
                    currentData = finalData
                )
            }
        }
    }

    fun setDestinationLocation(destination: String) {
        _uiState.update { it.copy(destinationLocation = destination) }
        refreshData()
    }

    fun setGpsLocation(lat: Double, lng: Double, label: String = "Current Location") {
        _uiState.update {
            it.copy(
                userLat = lat,
                userLng = lng,
                selectedLocation = label
            )
        }
        refreshData()
    }

    fun toggleLocationSearchDialog(isOpen: Boolean) {
        _uiState.update { it.copy(isLocationSearchOpen = isOpen) }
    }

    fun navigateToAuthScreen(screen: AuthScreenState) {
        _uiState.update { it.copy(authScreenState = screen) }
    }

    fun selectTab(tab: AirRouteTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun handleSignInSuccess(name: String, email: String) {
        prefsRepo?.saveUserSession(name, email)
        _uiState.update {
            it.copy(
                userProfile = it.userProfile.copy(name = name, email = email, isLoggedIn = true),
                authScreenState = AuthScreenState.MAIN_APP
            )
        }
        refreshData()
    }

    fun handleSignUpSuccess(name: String, email: String) {
        prefsRepo?.saveUserSession(name, email)
        _uiState.update {
            it.copy(
                userProfile = it.userProfile.copy(name = name, email = email, isLoggedIn = true),
                authScreenState = AuthScreenState.ONBOARDING
            )
        }
    }

    fun completeOnboarding(location: String, activities: List<String>, notificationTime: String) {
        prefsRepo?.saveLocation(location)
        prefsRepo?.savePreferredActivities(activities)
        _uiState.update {
            it.copy(
                selectedLocation = location,
                userProfile = it.userProfile.copy(
                    location = location,
                    preferredActivities = activities
                ),
                authScreenState = AuthScreenState.MAIN_APP
            )
        }
        refreshData()
    }

    fun setScenario(scenario: AirQualityScenario?) {
        _uiState.update { it.copy(currentScenario = scenario) }
        refreshData()
    }

    fun setLocation(location: String) {
        prefsRepo?.saveLocation(location)
        _uiState.update {
            it.copy(
                userLat = null,
                userLng = null,
                selectedLocation = location,
                userProfile = it.userProfile.copy(location = location)
            )
        }
        refreshData()
    }

    private var sessionJob: kotlinx.coroutines.Job? = null

    fun selectActivity(activityId: String) {
        _uiState.update { it.copy(selectedActivityId = activityId) }
        refreshData()
    }

    fun planActivity(
        activityId: String,
        durationMins: Int,
        startOption: String,
        destination: String? = null
    ) {
        val data = getAirQualityData()
        val activity = com.example.ui.screens.ALL_ACTIVITIES.firstOrNull { it.id == activityId }
            ?: com.example.ui.screens.ALL_ACTIVITIES.first()

        val bestWindow = data.bestWindowTime.ifBlank { "4:00 PM – 5:00 PM" }
        val planned = com.example.data.PlannedActivityData(
            activityId = activity.id,
            title = activity.title,
            emoji = activity.emoji,
            durationMins = durationMins,
            startOption = startOption,
            scheduledTimeLabel = if (startOption == "NOW") "NOW" else bestWindow,
            locationName = data.locationName,
            destinationName = destination,
            bestWindowTime = bestWindow,
            currentAqi = data.currentAqi,
            currentPm25 = data.currentPm25,
            weatherSummary = "${data.weather.tempC}°C • ${data.weather.weatherCondition}",
            estimatedExposure = "LOWER ESTIMATED EXPOSURE",
            recommendationText = "Conditions are currently favorable for your planned ${activity.title.lowercase()}.",
            explanationText = "Live telemetry shows AQI ${data.currentAqi} (${data.statusLabel}). Atmospheric dispersion is favorable for your ${durationMins}-minute session."
        )

        _uiState.update {
            it.copy(
                selectedActivityId = activityId,
                currentData = it.currentData?.copy(plannedActivity = planned)
            )
        }
    }

    fun startActivitySession(
        activityId: String,
        durationMins: Int,
        destination: String? = null
    ) {
        val data = getAirQualityData()
        val activity = com.example.ui.screens.ALL_ACTIVITIES.firstOrNull { it.id == activityId }
            ?: com.example.ui.screens.ALL_ACTIVITIES.first()

        val session = com.example.data.ActiveActivitySession(
            activityId = activity.id,
            title = activity.title,
            emoji = activity.emoji,
            targetDurationMins = durationMins,
            elapsedSeconds = 0,
            locationName = data.locationName,
            destinationName = destination,
            startAqi = data.currentAqi,
            currentAqi = data.currentAqi,
            currentPm25 = data.currentPm25,
            weatherCondition = "${data.weather.tempC}°C • ${data.weather.weatherCondition}",
            isPaused = false
        )

        _uiState.update {
            it.copy(
                currentData = it.currentData?.copy(activeSession = session)
            )
        }

        startSessionTicker()
    }

    private fun startSessionTicker() {
        sessionJob?.cancel()
        sessionJob = viewModelScope.launch(Dispatchers.Main) {
            while (true) {
                kotlinx.coroutines.delay(1000L)
                val currentSession = _uiState.value.currentData?.activeSession ?: break
                if (!currentSession.isPaused) {
                    val newSeconds = currentSession.elapsedSeconds + 1
                    _uiState.update { state ->
                        state.copy(
                            currentData = state.currentData?.copy(
                                activeSession = currentSession.copy(elapsedSeconds = newSeconds)
                            )
                        )
                    }
                }
            }
        }
    }

    fun pauseActivitySession() {
        _uiState.update { state ->
            val session = state.currentData?.activeSession ?: return@update state
            state.copy(
                currentData = state.currentData.copy(
                    activeSession = session.copy(isPaused = true)
                )
            )
        }
    }

    fun resumeActivitySession() {
        _uiState.update { state ->
            val session = state.currentData?.activeSession ?: return@update state
            state.copy(
                currentData = state.currentData.copy(
                    activeSession = session.copy(isPaused = false)
                )
            )
        }
    }

    fun finishActivitySession() {
        sessionJob?.cancel()
        sessionJob = null

        val currentSession = _uiState.value.currentData?.activeSession ?: return
        val elapsedMins = (currentSession.elapsedSeconds / 60).coerceAtLeast(1)

        val dateFormat = java.text.SimpleDateFormat("dd MMM yyyy, h:mm a", java.util.Locale.getDefault())
        val dateText = dateFormat.format(java.util.Date())

        val historyItem = com.example.data.CompletedActivityItem(
            activityType = currentSession.title,
            emoji = currentSession.emoji,
            dateText = dateText,
            durationMins = elapsedMins,
            location = currentSession.locationName,
            destination = currentSession.destinationName,
            aqi = currentSession.currentAqi,
            pm25 = currentSession.currentPm25,
            weather = currentSession.weatherCondition,
            exposureCategory = "LOWER ESTIMATED EXPOSURE"
        )

        _uiState.update { state ->
            val existingHistory = state.currentData?.activityHistory ?: emptyList()
            state.copy(
                currentData = state.currentData?.copy(
                    activeSession = null,
                    activityHistory = listOf(historyItem) + existingHistory
                )
            )
        }
    }

    fun scheduleActivityReminder(context: Context) {
        val planned = _uiState.value.currentData?.plannedActivity ?: return
        com.example.utils.NotificationHelper.showNativeNotification(
            context = context,
            title = "AIRROUTE 🌿 Activity Reminder",
            body = "Your recommended ${planned.title} window starts shortly (${planned.bestWindowTime}). Current AQI: ${planned.currentAqi}.",
            targetScreen = "ACTIVITY"
        )

        _uiState.update { state ->
            state.copy(
                currentData = state.currentData?.copy(
                    plannedActivity = planned.copy(isReminderSet = true)
                )
            )
        }
    }

    fun updateNotificationPreferences(prefs: NotificationPreferences) {
        prefsRepo?.saveNotificationPreferences(prefs)
        _uiState.update {
            it.copy(userProfile = it.userProfile.copy(notificationPreferences = prefs))
        }
    }

    fun toggleDataInfo(isOpen: Boolean) {
        _uiState.update { it.copy(isDataInfoOpen = isOpen) }
    }

    fun toggleAiExplanation(isOpen: Boolean) {
        _uiState.update { it.copy(isAiExplanationOpen = isOpen) }
    }

    fun dismissAlert(alertId: String) {
        _uiState.update {
            val newDismissed = it.dismissedAlertIds + alertId
            val updatedData = it.currentData?.let { data ->
                data.copy(alerts = data.alerts.filter { a -> !newDismissed.contains(a.id) })
            }
            it.copy(dismissedAlertIds = newDismissed, currentData = updatedData)
        }
    }

    fun clearAllAlerts() {
        val currentData = getAirQualityData()
        val allIds = currentData.alerts.map { it.id }.toSet()
        _uiState.update {
            it.copy(
                dismissedAlertIds = allIds,
                currentData = it.currentData?.copy(alerts = emptyList())
            )
        }
    }

    fun triggerTestNotification() {
        val currentData = getAirQualityData()
        notifManager?.triggerBestWindowAlert("Running", currentData.bestWindowTime)
    }

    fun signOut() {
        prefsRepo?.clearSession()
        _uiState.update {
            AirRouteUiState(authScreenState = AuthScreenState.WELCOME)
        }
    }

    fun getAirQualityData(): AirQualityStateData {
        return _uiState.value.currentData ?: RealAirQualityRepository.buildFallbackLiveData(
            _uiState.value.selectedLocation,
            _uiState.value.selectedActivityId
        )
    }
}
