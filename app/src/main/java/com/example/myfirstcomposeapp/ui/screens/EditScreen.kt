package com.example.myfirstcomposeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.model.ChangeDraft
import com.example.myfirstcomposeapp.model.ChangeType
import com.example.myfirstcomposeapp.ui.common.TypeDropdownField
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    vm: ChangeViewModel,
    id: String,
    onSave: (ChangeDraft) -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    val item = vm.observeById(id).collectAsState().value

    if (item == null) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("编辑变化点") }, navigationIcon = { IconButton(onClick = onCancel) { Text("←") } }) }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("未找到记录")
            }
        }
        return
    }

    var title by remember { mutableStateOf(item.title) }
    var content by remember { mutableStateOf(item.content) }
    var creator by remember { mutableStateOf(item.creator) }
    var type by remember { mutableStateOf<ChangeType?>(item.type) }
    var urgent by remember { mutableStateOf(item.urgent) }
    var line by remember { mutableStateOf(item.line) }
    var equipment by remember { mutableStateOf(item.equipment) }
    var process by remember { mutableStateOf(item.process) }

    // 校验
    val titleErr = if (title.trim().isEmpty()) "请输入变化点（标题）" else null
    val contentErr = if (content.trim().isEmpty()) "请输入变化内容" else null
    val creatorErr = if (creator.trim().isEmpty()) "请输入创建人" else null
    val typeErr = if (type == null) "请选择变化点类型" else null
    val ok = titleErr == null && contentErr == null && creatorErr == null && typeErr == null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("编辑变化点") },
                navigationIcon = { IconButton(onClick = onCancel) { Text("←") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(shape = MaterialTheme.shapes.large) {
                Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = creator,
                        onValueChange = { creator = it },
                        label = { Text("创建人*") },
                        isError = creatorErr != null,
                        supportingText = { if (creatorErr != null) Text(creatorErr) }
                    )

                    TypeDropdownField(
                        selected = type,
                        onSelect = { type = it },
                        label = "变化点类型*",
                        isError = typeErr != null,
                        supportingText = typeErr
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("变化点（标题）*") },
                        isError = titleErr != null,
                        supportingText = { if (titleErr != null) Text(titleErr) }
                    )

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("变化内容*") },
                        minLines = 3,
                        isError = contentErr != null,
                        supportingText = { if (contentErr != null) Text(contentErr) }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FilterChip(
                            selected = urgent,
                            onClick = { urgent = !urgent },
                            label = { Text("紧急") }
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = line,
                            onValueChange = { line = it },
                            label = { Text("产线") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = equipment,
                            onValueChange = { equipment = it },
                            label = { Text("设备") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = process,
                            onValueChange = { process = it },
                            label = { Text("工序") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) { Text("取消") }

                Button(
                    onClick = {
                        val draft = ChangeDraft(
                            title = title.trim(),
                            content = content.trim(),
                            creator = creator.trim(),
                            type = requireNotNull(type),
                            urgent = urgent,
                            line = line.trim(),
                            equipment = equipment.trim(),
                            process = process.trim()
                        )
                        onSave(draft)
                    },
                    enabled = ok,
                    modifier = Modifier.weight(1f)
                ) { Text("保存") }

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("删除") }
            }

            Text("说明：保存会更新“更新时间”。流程动作请在列表/详情页执行。", style = MaterialTheme.typography.bodySmall)
        }
    }
}