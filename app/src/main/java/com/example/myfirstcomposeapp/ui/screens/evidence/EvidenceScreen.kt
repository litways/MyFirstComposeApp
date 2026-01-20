package com.example.myfirstcomposeapp.ui.screens.evidence

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvidenceScreen(
    changeId: String,
    onBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("证据包") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "当前版本为 UI 占位，后续支持上传照片、附件等证据"
                        )
                    }
                }
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "关联变化点 ID：$changeId",
                style = MaterialTheme.typography.bodySmall
            )

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("证据清单（占位）", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("• 现场照片")
                    Text("• 首件确认记录")
                    Text("• QA / 工艺确认截图")
                    Text("• 放行审批记录")
                }
            }

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("说明", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "证据包用于支撑变化点的审批与追溯。" +
                                "当前版本仅展示结构与入口，不涉及真实附件存储。",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}