package com.ray.template.android.presentation.ui.main.home

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ray.template.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.homeDestination(
    navController: NavController
) {
    composable(
        route = HomeConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(HomeConstant.ROUTE_ARGUMENT_SCREEN) {
                type = NavType.StringType
                defaultValue = HomeType.values().first()
            }
        )
    ) {
        val viewModel: HomeViewModel = hiltViewModel()

        val argument: HomeArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val initialHomeType = viewModel.initialHomeType

            HomeArgument(
                state = state,
                initialHomeType = initialHomeType,
                event = viewModel.event,
                intent = viewModel::onIntent,
                handler = viewModel.handler
            )
        }

        ErrorObserver(viewModel)
        HomeScreen(
            navController = navController,
            argument = argument
        )
    }
}
