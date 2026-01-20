package com.example.myfirstcomposeapp.ui.screens.report

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportCenterScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("报表中心") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("变化点统计报表", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "此页面用于集中展示变化点相关统计与分析结果。",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("可规划报表类型", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("• 按产线 / 设备统计变化点数量")
                    Text("• 按类型统计分布情况")
                    Text("• 按状态统计待办与关闭情况")
                    Text("• 周 / 月趋势分析")
                }
            }

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("说明", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "当前版本仅提供报表入口与结构展示，" +
                                "实际统计与导出功能将在后续版本中实现。",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}