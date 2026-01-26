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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.model.ChangeItem
import com.example.myfirstcomposeapp.ui.theme.Dimens
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel

// Material Icons (androidx.compose.material:material-icons-extended)
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit

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
                title = { Text(stringResource(R.string.list_title)) },
                actions = {
                    IconButton(onClick = onAdd) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.action_add)
                        )
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
                contentPadding = PaddingValues(Dimens.spacingMd),
                verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
            ) {
                items(items) { item ->
                    ChangeItemRow(
                        item = item,
                        onClick = { onEdit(item.id) },
                        onEdit = { onEdit(item.id) },
                        onDelete = { pendingDeleteId = item.id }
                    )
                }

                item { Spacer(Modifier.height(Dimens.spacing4xl)) }
            }
        }
    }

    val deleteId = pendingDeleteId
    if (deleteId != null) {
        AlertDialog(
            onDismissRequest = { pendingDeleteId = null },
            title = { Text(stringResource(R.string.list_delete_title)) },
            text = { Text(stringResource(R.string.list_delete_message)) },
            confirmButton = {
                Button(onClick = {
                    onDelete(deleteId)
                    pendingDeleteId = null
                }) { Text(stringResource(R.string.action_delete)) }
            },
            dismissButton = {
                OutlinedButton(onClick = { pendingDeleteId = null }) {
                    Text(stringResource(R.string.action_cancel))
                }
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
        Column(
            Modifier.fillMaxWidth().padding(Dimens.spacingMd),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSm)) {
                        AssistChip(
                            onClick = {},
                            label = { Text(item.status.label) }
                        )
                        AssistChip(
                            onClick = {},
                            label = { Text(item.type.label) }
                        )
                        if (item.urgent) {
                            AssistChip(
                                onClick = {},
                                label = { Text(stringResource(R.string.urgent_label)) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    labelColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            )
                        }
                    }
                }
            }

            Text(
                stringResource(R.string.list_creator_label, item.creator),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                stringResource(
                    R.string.list_scope_label,
                    item.line,
                    item.equipment,
                    item.process
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.action_edit)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.action_delete)
                    )
                }
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
            stringResource(R.string.list_empty_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
