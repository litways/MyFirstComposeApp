package com.example.myfirstcomposeapp.ui.screens.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    Card(shape = MaterialTheme.shapes.large) {
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("总览", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("总数：$allCount")
            Text("紧急：$urgentCount")

            Text("状态分布", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            statuses.forEach { st ->
                val c = items.count { it.status == st }
                if (c > 0) Text("${statusText(st)}：$c")
            }

            Text(
                "提示：此页为 UI-only 展示。后续可接入更完整的统计口径。",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun statusText(status: ChangeStatus): String = when (status) {
    ChangeStatus.DRAFT -> "草稿"
    ChangeStatus.SUBMITTED -> "已提交"
    ChangeStatus.CONFIRMED -> "已确认"
    ChangeStatus.RELEASED -> "已放行"
    ChangeStatus.CLOSED -> "已关闭"
    ChangeStatus.REJECTED -> "已驳回"
    ChangeStatus.REOPENED -> "已重开"
}