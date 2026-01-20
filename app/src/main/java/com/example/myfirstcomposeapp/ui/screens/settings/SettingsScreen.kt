package com.example.myfirstcomposeapp.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
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
                    Text("当前用户", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("角色：示例用户")
                    Text("权限：仅用于演示")
                }
            }

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("应用设置", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("• 通知设置（占位）")
                    Text("• 显示密度（占位）")
                    Text("• 语言设置（占位）")
                }
            }

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("关于", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("变化点管理 App")
                    Text("版本：Demo / UI-only")
                }
            }
        }
    }
}