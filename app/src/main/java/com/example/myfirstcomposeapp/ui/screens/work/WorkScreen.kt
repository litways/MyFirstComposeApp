package com.example.myfirstcomposeapp.ui.screens.work

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.ui.navigation.Routes
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel

@Composable
fun WorkScreen(
    navController: NavController,
    vm: ChangeViewModel
) {
    val items = vm.filteredItems.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.nav_work), color = MaterialTheme.colorScheme.onPrimary)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(stringResource(R.string.work_title), style = MaterialTheme.typography.titleLarge)

            if (items.isEmpty()) {
                Text(
                    stringResource(R.string.work_empty),
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
                            Text(stringResource(R.string.work_item_meta, item.type.label, item.status.label))
                        }
                    }
                }
            }
        }
    }
}
