package com.example.myfirstcomposeapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myfirstcomposeapp.ui.screens.AddScreen
import com.example.myfirstcomposeapp.ui.screens.EditScreen
import com.example.myfirstcomposeapp.ui.screens.ListScreen
import com.example.myfirstcomposeapp.ui.screens.create.CreateScreen
import com.example.myfirstcomposeapp.ui.screens.detail.ChangeDetailScreen
import com.example.myfirstcomposeapp.ui.screens.overview.OverviewScreen
import com.example.myfirstcomposeapp.ui.screens.trace.SavedFiltersScreen
import com.example.myfirstcomposeapp.ui.screens.trace.TraceScreen
import com.example.myfirstcomposeapp.ui.screens.work.WorkScreen
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel

@Composable
fun AppNavHost(vm: ChangeViewModel) {
    val navController = rememberNavController()

    HomeScaffold(navController = navController) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.OVERVIEW,
            modifier = Modifier.padding(innerPadding)
        ) {

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
                    onDelete = { id -> vm.delete(id) }
                )
            }

            composable(Routes.LEGACY_ADD) {
                AddScreen(
                    onSave = { draft ->
                        vm.add(draft)
                        navController.popBackStack()
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
                        vm.update(id, draft)
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() },
                    onDelete = {
                        vm.delete(id)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}