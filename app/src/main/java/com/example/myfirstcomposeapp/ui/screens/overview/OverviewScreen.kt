package com.example.myfirstcomposeapp.ui.screens.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.model.ChangeStatus
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel

@Composable
fun OverviewScreen(vm: ChangeViewModel) {
    val items = vm.filteredItems.collectAsStateWithLifecycle().value

    val allCount = items.size
    val urgentCount = items.count { it.urgent }

    val statuses = listOf(
        ChangeStatus.DRAFT,
        ChangeStatus.SUBMITTED,
        ChangeStatus.CONFIRMED,
        ChangeStatus.RELEASED,
        ChangeStatus.CLOSED,
        ChangeStatus.REJECTED,
        ChangeStatus.REOPENED
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.nav_overview), color = MaterialTheme.colorScheme.onPrimary)
        }

        Card(shape = MaterialTheme.shapes.large) {
            Column(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(stringResource(R.string.overview_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(stringResource(R.string.overview_total, allCount))
                Text(stringResource(R.string.overview_urgent, urgentCount))

                Text(stringResource(R.string.overview_status_distribution), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                statuses.forEach { st ->
                    val c = items.count { it.status == st }
                    if (c > 0) Text(stringResource(R.string.overview_status_item, st.label, c))
                }

                Text(
                    stringResource(R.string.overview_hint),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
