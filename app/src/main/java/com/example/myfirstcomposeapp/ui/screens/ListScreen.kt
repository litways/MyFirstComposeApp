package com.example.myfirstcomposeapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.model.ChangeItem
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel

// Material Icons (androidx.compose.material:material-icons-extended)
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

/**
 * Legacy CRUD 列表（与旧 Add/Edit 入口保持一致）
 * - 数据源：vm.filteredItems（用于与「追溯」筛选联动）
 * - UI-only：删除前弹确认
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    vm: ChangeViewModel,
    onAdd: () -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    val items = vm.filteredItems.collectAsState().value

    var pendingDeleteId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("变化点列表") },
                actions = {
                    IconButton(onClick = onAdd) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "新增")
                    }
                }
            )
        }
    ) { padding ->
        if (items.isEmpty()) {
            EmptyListHint(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    ChangeItemRow(
                        item = item,
                        onClick = { onEdit(item.id) },
                        onEdit = { onEdit(item.id) },
                        onDelete = { pendingDeleteId = item.id }
                    )
                }

                item { Spacer(Modifier.height(72.dp)) }
            }
        }
    }

    val deleteId = pendingDeleteId
    if (deleteId != null) {
        AlertDialog(
            onDismissRequest = { pendingDeleteId = null },
            title = { Text("确认删除") },
            text = { Text("删除后无法恢复，确定要删除该变化点吗？") },
            confirmButton = {
                Button(onClick = {
                    onDelete(deleteId)
                    pendingDeleteId = null
                }) { Text("删除") }
            },
            dismissButton = {
                OutlinedButton(onClick = { pendingDeleteId = null }) { Text("取消") }
            }
        )
    }
}

@Composable
private fun ChangeItemRow(
    item: ChangeItem,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        "${item.type.name} · ${item.status.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (item.urgent) {
                    Text("紧急", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error)
                }
            }

            Text(
                "创建人：${item.creator}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "产线：${item.line} ｜ 设备：${item.equipment} ｜ 工序：${item.process}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) { Text("编辑") }
                TextButton(onClick = onDelete) { Text("删除") }
            }
        }
    }
}

@Composable
private fun EmptyListHint(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize().then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "暂无变化点记录\n可通过【发起】页面新增，或在【追溯】页面设置筛选后查看列表",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}