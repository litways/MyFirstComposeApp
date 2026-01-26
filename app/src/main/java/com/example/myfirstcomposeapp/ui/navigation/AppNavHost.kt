package com.example.myfirstcomposeapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.ui.screens.AddScreen
import com.example.myfirstcomposeapp.ui.screens.EditScreen
import com.example.myfirstcomposeapp.ui.screens.ListScreen
import com.example.myfirstcomposeapp.ui.screens.create.CreateScreen
import com.example.myfirstcomposeapp.ui.screens.detail.ChangeDetailScreen
import com.example.myfirstcomposeapp.ui.screens.login.LoginScreen
import com.example.myfirstcomposeapp.ui.screens.overview.OverviewScreen
import com.example.myfirstcomposeapp.ui.screens.trace.SavedFiltersScreen
import com.example.myfirstcomposeapp.ui.screens.trace.TraceScreen
import com.example.myfirstcomposeapp.ui.screens.user.UserScreen
import com.example.myfirstcomposeapp.ui.screens.work.WorkScreen
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(vm: ChangeViewModel) {
    val navController = rememberNavController()
    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val saveSuccessMessage = stringResource(R.string.snackbar_save_success)
    val saveErrorMessage = stringResource(R.string.snackbar_save_error)
    val deleteSuccessMessage = stringResource(R.string.snackbar_delete_success)
    val deleteErrorMessage = stringResource(R.string.snackbar_delete_error)

    HomeScaffold(navController = navController, snackbarHostState = snackbarHostState) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLogin = {
                        navController.navigate(Routes.OVERVIEW) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            // ===== 4 Tabs =====
            composable(Routes.OVERVIEW) {
                OverviewScreen(vm = vm)
            }

            composable(Routes.WORK) {
                WorkScreen(vm = vm, navController = navController)
            }

            composable(Routes.CREATE) {
                CreateScreen(
                    onCreate = { navController.navigate(Routes.LEGACY_ADD) }
                )
            }

            composable(Routes.TRACE) {
                TraceScreen(
                    vm = vm,
                    onOpenTraceList = { navController.navigate(Routes.LEGACY_LIST) },
                    onOpenSavedFilters = { navController.navigate(Routes.SAVED_FILTERS) }
                )
            }

            composable(Routes.USER) {
                UserScreen(
                    vm = vm,
                    onLogout = {
                        navController.navigate(Routes.LOGIN) {
                            launchSingleTop = true
                            popUpTo(Routes.OVERVIEW) { inclusive = true }
                        }
                    }
                )
            }

            // ===== Trace / Saved filters（UI-only 占位） =====
            composable(Routes.SAVED_FILTERS) {
                SavedFiltersScreen(onBack = { navController.popBackStack() })
            }

            // ===== Detail =====
            composable(
                route = Routes.DETAIL_WITH_ARG,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("id") ?: return@composable
                ChangeDetailScreen(
                    id = id,
                    vm = vm,
                    onBack = { navController.popBackStack() }
                )
            }

            // ===== Legacy list/add/edit（你工程已有）=====
            composable(Routes.LEGACY_LIST) {
                ListScreen(
                    vm = vm,
                    onAdd = { navController.navigate(Routes.LEGACY_ADD) },
                    onEdit = { id -> navController.navigate(Routes.detail(id)) },
                    onDelete = { id ->
                        runCatching { vm.delete(id) }
                            .onSuccess {
                                scope.launch { snackbarHostState.showSnackbar(deleteSuccessMessage) }
                            }
                            .onFailure {
                                scope.launch { snackbarHostState.showSnackbar(deleteErrorMessage) }
                            }
                    }
                )
            }

            composable(Routes.LEGACY_ADD) {
                AddScreen(
                    onSave = { draft ->
                        runCatching { vm.add(draft) }
                            .onSuccess {
                                scope.launch { snackbarHostState.showSnackbar(saveSuccessMessage) }
                                navController.popBackStack()
                            }
                            .onFailure {
                                scope.launch { snackbarHostState.showSnackbar(saveErrorMessage) }
                            }
                    },
                    onCancel = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.LEGACY_EDIT_WITH_ARG,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("id") ?: return@composable
                EditScreen(
                    vm = vm,
                    id = id,
                    onSave = { draft ->
                        runCatching { vm.update(id, draft) }
                            .onSuccess {
                                scope.launch { snackbarHostState.showSnackbar(saveSuccessMessage) }
                                navController.popBackStack()
                            }
                            .onFailure {
                                scope.launch { snackbarHostState.showSnackbar(saveErrorMessage) }
                            }
                    },
                    onCancel = { navController.popBackStack() },
                    onDelete = {
                        runCatching { vm.delete(id) }
                            .onSuccess {
                                scope.launch { snackbarHostState.showSnackbar(deleteSuccessMessage) }
                                navController.popBackStack()
                            }
                            .onFailure {
                                scope.launch { snackbarHostState.showSnackbar(deleteErrorMessage) }
                            }
                    }
                )
            }
        }
    }
}
