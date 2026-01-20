package com.example.myfirstcomposeapp.ui.screens

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.model.ChangeDraft
import com.example.myfirstcomposeapp.model.ChangeType
import com.example.myfirstcomposeapp.ui.common.TypeDropdownField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    onSave: (ChangeDraft) -> Unit,
    onCancel: () -> Unit
) {
    var creator by remember { mutableStateOf("") }
    var type by remember { mutableStateOf<ChangeType?>(null) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var urgent by remember { mutableStateOf(false) }
    var line by remember { mutableStateOf("") }
    var equipment by remember { mutableStateOf("") }
    var process by remember { mutableStateOf("") }

    val canSave =
        creator.trim().isNotEmpty() &&
                type != null &&
                title.trim().isNotEmpty() &&
                content.trim().isNotEmpty()

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("新增变化点") },
                navigationIcon = { IconButton(onClick = onCancel) { Text("←") } }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 2.dp) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f)
                    ) { Text("取消") }

                    Button(
                        onClick = {
                            onSave(
                                ChangeDraft(
                                    creator = creator,
                                    type = type!!,
                                    title = title,
                                    content = content,
                                    urgent = urgent,
                                    line = line,
                                    equipment = equipment,
                                    process = process
                                )
                            )
                        },
                        modifier = Modifier.weight(1f),
                        enabled = canSave
                    ) { Text("保存") }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState) // 关键：允许上下滚动
                .imePadding()               // 关键：键盘弹出时内容不被遮挡
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ElevatedCard(shape = MaterialTheme.shapes.large) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "必填信息",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = creator,
                        onValueChange = { creator = it },
                        label = { Text("创建人 *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    TypeDropdownField(
                        selected = type,
                        onSelect = { type = it },
                        label = "变化点类型 *",
                        isError = type == null,
                        supportingText = if (type == null) "请选择类型" else null
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("变化点标题 *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("变化内容 *") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("是否紧急", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = urgent, onCheckedChange = { urgent = it })
                    }
                }
            }

            ElevatedCard(shape = MaterialTheme.shapes.large) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "范围信息（推荐）",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = line,
                        onValueChange = { line = it },
                        label = { Text("产线") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = equipment,
                        onValueChange = { equipment = it },
                        label = { Text("设备") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = process,
                        onValueChange = { process = it },
                        label = { Text("工序") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Text(
                "提示：此版本以 UI 为主（基础功能）。复杂校验与证据门槛留待下一阶段规划。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 让内容在底部按钮栏上方留出空间，避免最后一个输入框被遮挡
            Spacer(Modifier.height(96.dp))
        }
    }
}