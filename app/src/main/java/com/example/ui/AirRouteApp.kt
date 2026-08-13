package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.DataInfoDialog
import com.example.ui.components.LocationSearchDialog
import com.example.ui.components.WhyScoreDialog
import com.example.ui.screens.ActivityAdvisorScreen
import com.example.ui.screens.AirRouteAiExplanationDialog
import com.example.ui.screens.ForecastScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MapScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SignInScreen
import com.example.ui.screens.SignUpScreen
import com.example.ui.screens.WelcomeScreen

@Composable
fun AirRouteApp(
    viewModel: AirRouteViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.initializeWithContext(context)
    }

    val state by viewModel.uiState.collectAsState()
    val airData = viewModel.getAirQualityData()
    var showWhyScoreDialog by remember { mutableStateOf(false) }

    // Dialogs
    if (state.isDataInfoOpen) {
        DataInfoDialog(
            locationName = state.selectedLocation,
            onDismiss = { viewModel.toggleDataInfo(false) }
        )
    }

    if (state.isLocationSearchOpen) {
        LocationSearchDialog(
            currentLocation = state.selectedLocation,
            onLocationSelected = { locName ->
                viewModel.setLocation(locName)
                viewModel.toggleLocationSearchDialog(false)
            },
            onUseGpsLocation = {
                viewModel.setGpsLocation(17.3850, 78.4867, "GPS Location (Hyderabad)")
                viewModel.toggleLocationSearchDialog(false)
            },
            onDismiss = { viewModel.toggleLocationSearchDialog(false) }
        )
    }

    if (state.isAiExplanationOpen) {
        AirRouteAiExplanationDialog(
            data = airData,
            onDismiss = { viewModel.toggleAiExplanation(false) }
        )
    }

    if (showWhyScoreDialog) {
        WhyScoreDialog(
            scoreData = airData.outdoorScore,
            onDismiss = { showWhyScoreDialog = false }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Crossfade(
            targetState = state.authScreenState,
            animationSpec = tween(300),
            label = "authScreenCrossfade"
        ) { authState ->
            when (authState) {
                AuthScreenState.WELCOME -> {
                    WelcomeScreen(
                        onGetStarted = { viewModel.navigateToAuthScreen(AuthScreenState.SIGN_UP) },
                        onSignIn = { viewModel.navigateToAuthScreen(AuthScreenState.SIGN_IN) },
                        onContinueAsDemo = {
                            viewModel.handleSignInSuccess("Sai Charan", "sai@airroute.app")
                        }
                    )
                }

                AuthScreenState.SIGN_IN -> {
                    SignInScreen(
                        onSignInSuccess = { name, email -> viewModel.handleSignInSuccess(name, email) },
                        onGoogleSignIn = { viewModel.handleSignInSuccess("Sai Charan", "sai.charan@gmail.com") },
                        onCreateAccountClicked = { viewModel.navigateToAuthScreen(AuthScreenState.SIGN_UP) },
                        onForgotPasswordClicked = { viewModel.handleSignInSuccess("Sai Charan", "sai@airroute.app") }
                    )
                }

                AuthScreenState.SIGN_UP -> {
                    SignUpScreen(
                        onSignUpSuccess = { name, email -> viewModel.handleSignUpSuccess(name, email) },
                        onGoogleSignUp = { viewModel.handleSignUpSuccess("Sai Charan", "sai.charan@gmail.com") },
                        onSignInClicked = { viewModel.navigateToAuthScreen(AuthScreenState.SIGN_IN) }
                    )
                }

                AuthScreenState.ONBOARDING -> {
                    OnboardingScreen(
                        onOnboardingComplete = { loc, acts, time ->
                            viewModel.completeOnboarding(loc, acts, time)
                        }
                    )
                }

                AuthScreenState.MAIN_APP -> {
                    Scaffold(
                        bottomBar = {
                            BottomNavBar(
                                selectedTab = state.selectedTab,
                                onTabSelected = { viewModel.selectTab(it) }
                            )
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            Crossfade(
                                targetState = state.selectedTab,
                                animationSpec = tween(250),
                                label = "mainTabCrossfade"
                            ) { tab ->
                                when (tab) {
                                    com.example.ui.components.AirRouteTab.HOME -> {
                                        HomeScreen(
                                            userName = state.userProfile.name,
                                            data = airData,
                                            selectedActivityId = state.selectedActivityId,
                                            unreadAlertCount = airData.alerts.size,
                                            onActivitySelected = { viewModel.selectActivity(it) },
                                            onWhyScoreClicked = { showWhyScoreDialog = true },
                                            onWhyThisTimeClicked = { viewModel.toggleAiExplanation(true) },
                                            onViewAiExplanationClicked = { viewModel.toggleAiExplanation(true) },
                                            onNotificationClicked = { viewModel.selectTab(com.example.ui.components.AirRouteTab.ACTIVITY) },
                                            onProfileClicked = { viewModel.selectTab(com.example.ui.components.AirRouteTab.PROFILE) },
                                            onLocationSelectorClicked = { viewModel.toggleLocationSearchDialog(true) },
                                            onOpenDataInfo = { viewModel.toggleDataInfo(true) }
                                        )
                                    }

                                    com.example.ui.components.AirRouteTab.FORECAST -> {
                                        ForecastScreen(data = airData)
                                    }

                                    com.example.ui.components.AirRouteTab.MAP -> {
                                        MapScreen(
                                            data = airData,
                                            destinationName = state.destinationLocation,
                                            onDestinationChanged = { viewModel.setDestinationLocation(it) }
                                        )
                                    }

                                    com.example.ui.components.AirRouteTab.ACTIVITY -> {
                                        ActivityAdvisorScreen(
                                            data = airData,
                                            selectedActivityId = state.selectedActivityId,
                                            plannedActivity = airData.plannedActivity,
                                            activeSession = airData.activeSession,
                                            activityHistory = airData.activityHistory,
                                            onSelectActivity = { viewModel.selectActivity(it) },
                                            onPlanActivity = { id, dur, start, dest -> viewModel.planActivity(id, dur, start, dest) },
                                            onStartSession = { id, dur, dest -> viewModel.startActivitySession(id, dur, dest) },
                                            onPauseSession = { viewModel.pauseActivitySession() },
                                            onResumeSession = { viewModel.resumeActivitySession() },
                                            onFinishSession = { viewModel.finishActivitySession() },
                                            onScheduleReminder = { viewModel.scheduleActivityReminder(context) },
                                            onNavigateToMapWithDestination = { dest ->
                                                viewModel.setDestinationLocation(dest)
                                                viewModel.selectTab(com.example.ui.components.AirRouteTab.MAP)
                                            }
                                        )
                                    }

                                    com.example.ui.components.AirRouteTab.PROFILE -> {
                                        ProfileScreen(
                                            profile = state.userProfile,
                                            onOpenDataInfo = { viewModel.toggleDataInfo(true) },
                                            onSignOut = { viewModel.signOut() },
                                            onUpdateNotificationPreferences = { viewModel.updateNotificationPreferences(it) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
