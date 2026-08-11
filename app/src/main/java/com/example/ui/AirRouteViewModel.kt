package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.AirQualityScenario
import com.example.data.AirQualityStateData
import com.example.data.DemoDataRepository
import com.example.data.UserProfile
import com.example.ui.components.AirRouteTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
    val currentScenario: AirQualityScenario = AirQualityScenario.GOOD,
    val selectedLocation: String = "Hyderabad, Telangana",
    val selectedActivityId: String = "running",
    val isDemoPanelOpen: Boolean = false,
    val isAiExplanationOpen: Boolean = false,
    val dismissedAlertIds: Set<String> = emptySet()
)

class AirRouteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AirRouteUiState())
    val uiState: StateFlow<AirRouteUiState> = _uiState.asStateFlow()

    fun navigateToAuthScreen(screen: AuthScreenState) {
        _uiState.update { it.copy(authScreenState = screen) }
    }

    fun selectTab(tab: AirRouteTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun handleSignInSuccess(name: String, email: String) {
        _uiState.update {
            it.copy(
                userProfile = it.userProfile.copy(name = name, email = email),
                authScreenState = AuthScreenState.MAIN_APP
            )
        }
    }

    fun handleSignUpSuccess(name: String, email: String) {
        _uiState.update {
            it.copy(
                userProfile = it.userProfile.copy(name = name, email = email),
                authScreenState = AuthScreenState.ONBOARDING
            )
        }
    }

    fun completeOnboarding(location: String, activities: List<String>, notificationTime: String) {
        _uiState.update {
            it.copy(
                selectedLocation = location,
                userProfile = it.userProfile.copy(
                    location = location,
                    preferredActivities = activities,
                    notificationTime = notificationTime
                ),
                authScreenState = AuthScreenState.MAIN_APP
            )
        }
    }

    fun setScenario(scenario: AirQualityScenario) {
        _uiState.update { it.copy(currentScenario = scenario) }
    }

    fun setLocation(location: String) {
        _uiState.update {
            it.copy(
                selectedLocation = location,
                userProfile = it.userProfile.copy(location = location)
            )
        }
    }

    fun selectActivity(activityId: String) {
        _uiState.update { it.copy(selectedActivityId = activityId) }
    }

    fun toggleDemoPanel(isOpen: Boolean) {
        _uiState.update { it.copy(isDemoPanelOpen = isOpen) }
    }

    fun toggleAiExplanation(isOpen: Boolean) {
        _uiState.update { it.copy(isAiExplanationOpen = isOpen) }
    }

    fun dismissAlert(alertId: String) {
        _uiState.update {
            it.copy(dismissedAlertIds = it.dismissedAlertIds + alertId)
        }
    }

    fun clearAllAlerts() {
        val currentData = getAirQualityData()
        _uiState.update {
            it.copy(dismissedAlertIds = currentData.alerts.map { a -> a.id }.toSet())
        }
    }

    fun signOut() {
        _uiState.update {
            AirRouteUiState(authScreenState = AuthScreenState.WELCOME)
        }
    }

    fun getAirQualityData(): AirQualityStateData {
        val state = _uiState.value
        val baseData = DemoDataRepository.getScenarioData(
            scenario = state.currentScenario,
            selectedLocation = state.selectedLocation,
            selectedActivityId = state.selectedActivityId
        )
        val filteredAlerts = baseData.alerts.filter { !state.dismissedAlertIds.contains(it.id) }
        return baseData.copy(alerts = filteredAlerts)
    }
}
