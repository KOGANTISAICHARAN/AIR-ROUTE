package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.DemoDataRepository
import com.example.ui.components.BottomNavBar
import com.example.ui.components.ScenarioSwitcherDialog
import com.example.ui.screens.ActivityAdvisorScreen
import com.example.ui.screens.AirRouteAiExplanationDialog
import com.example.ui.screens.AlertsScreen
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
    val state by viewModel.uiState.collectAsState()
    val airData = viewModel.getAirQualityData()
    var isLocationMenuExpanded by remember { mutableStateOf(false) }

    // Dialogs
    if (state.isDemoPanelOpen) {
        ScenarioSwitcherDialog(
            currentScenario = state.currentScenario,
            onScenarioSelected = { viewModel.setScenario(it) },
            onDismiss = { viewModel.toggleDemoPanel(false) }
        )
    }

    if (state.isAiExplanationOpen) {
        AirRouteAiExplanationDialog(
            data = airData,
            onDismiss = { viewModel.toggleAiExplanation(false) }
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
                        onGoogleSignIn = { viewModel.handleSignInSuccess("Sai Charan", "sai@airroute.app") },
                        onCreateAccountClicked = { viewModel.navigateToAuthScreen(AuthScreenState.SIGN_UP) },
                        onForgotPasswordClicked = { viewModel.handleSignInSuccess("Sai Charan", "sai@airroute.app") }
                    )
                }

                AuthScreenState.SIGN_UP -> {
                    SignUpScreen(
                        onSignUpSuccess = { name, email -> viewModel.handleSignUpSuccess(name, email) },
                        onGoogleSignUp = { viewModel.handleSignUpSuccess("Sai Charan", "sai@airroute.app") },
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
                                            onWhyThisTimeClicked = { viewModel.toggleAiExplanation(true) },
                                            onViewAiExplanationClicked = { viewModel.toggleAiExplanation(true) },
                                            onNotificationClicked = { viewModel.selectTab(com.example.ui.components.AirRouteTab.ACTIVITY) },
                                            onProfileClicked = { viewModel.selectTab(com.example.ui.components.AirRouteTab.PROFILE) },
                                            onLocationSelectorClicked = { isLocationMenuExpanded = true },
                                            onOpenDemoPanel = { viewModel.toggleDemoPanel(true) }
                                        )

                                        // Location selection dropdown menu
                                        DropdownMenu(
                                            expanded = isLocationMenuExpanded,
                                            onDismissRequest = { isLocationMenuExpanded = false }
                                        ) {
                                            DemoDataRepository.AVAILABLE_LOCATIONS.forEach { loc ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(
                                                            text = loc,
                                                            fontWeight = if (loc == state.selectedLocation) FontWeight.Bold else FontWeight.Normal
                                                        )
                                                    },
                                                    onClick = {
                                                        viewModel.setLocation(loc)
                                                        isLocationMenuExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    com.example.ui.components.AirRouteTab.FORECAST -> {
                                        ForecastScreen(data = airData)
                                    }

                                    com.example.ui.components.AirRouteTab.MAP -> {
                                        MapScreen(data = airData)
                                    }

                                    com.example.ui.components.AirRouteTab.ACTIVITY -> {
                                        ActivityAdvisorScreen(
                                            data = airData,
                                            preferredActivityId = state.selectedActivityId,
                                            onSelectActivity = { viewModel.selectActivity(it) }
                                        )
                                    }

                                    com.example.ui.components.AirRouteTab.PROFILE -> {
                                        ProfileScreen(
                                            profile = state.userProfile,
                                            onOpenDemoPanel = { viewModel.toggleDemoPanel(true) },
                                            onSignOut = { viewModel.signOut() }
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
