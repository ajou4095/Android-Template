package com.ray.template.presentation.ui.main.nonlogin.onboarding

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ray.template.presentation.common.util.ErrorObserver

fun NavGraphBuilder.onBoardingDestination(
    navController: NavController
) {
    composable(
        route = OnBoardingConstant.ROUTE
    ) {
        val viewModel: OnBoardingViewModel = hiltViewModel()

        val argument: OnBoardingArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            OnBoardingArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                handler = viewModel.handler
            )
        }

        ErrorObserver(viewModel)
        OnBoardingScreen(
            navController = navController,
            argument = argument
        )
    }
}
