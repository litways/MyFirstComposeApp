package com.example.myfirstcomposeapp.ui.screens.help

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("帮助说明") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("变化点管理是什么？", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "变化点管理用于记录、跟踪和审查生产现场在人员、设备、物料、方法、环境等方面的变更，" +
                                "以降低质量和安全风险。",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("当前版本支持内容", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("• 变化点登记与编辑")
                    Text("• 流程状态查看（草稿 / 已提交 / 已确认等）")
                    Text("• 操作记录时间轴")
                    Text("• 列表筛选与追溯查看")
                }
            }

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("后续规划", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("• 证据附件上传")
                    Text("• 报表导出")
                    Text("• 离线登记与同步")
                    Text("• 多角色权限控制")
                }
            }
        }
    }
}