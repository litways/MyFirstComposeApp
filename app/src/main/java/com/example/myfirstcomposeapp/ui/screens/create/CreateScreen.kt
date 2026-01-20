package com.example.myfirstcomposeapp.ui.screens.create

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CreateScreen(
    onCreate: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ElevatedCard(shape = MaterialTheme.shapes.large) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("发起变化点", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    "此页为“发起入口页”（UI-only）。点击按钮进入新增表单。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(onClick = onCreate, modifier = Modifier.fillMaxWidth()) {
                    Text("新建变化点")
                }
            }
        }

        ElevatedCard(shape = MaterialTheme.shapes.large) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("表单提示", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("• 必填：创建人、变化点类型、变化点标题、变化内容", style = MaterialTheme.typography.bodySmall)
                Text("• 推荐：产线/设备/工序、是否紧急", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}