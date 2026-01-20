package com.example.myfirstcomposeapp.ui.screens.work

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myfirstcomposeapp.ui.navigation.Routes
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel

@Composable
fun WorkScreen(
    navController: NavController,
    vm: ChangeViewModel
) {
    val items = vm.filteredItems.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("我的待办", style = MaterialTheme.typography.titleLarge)

        if (items.isEmpty()) {
            Text(
                "当前没有待处理的变化点",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            items.forEach { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate(Routes.detail(item.id)) }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(item.title, style = MaterialTheme.typography.titleMedium)
                        Text("${item.type} · ${item.status}")
                    }
                }
            }
        }
    }
}