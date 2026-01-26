package com.example.myfirstcomposeapp.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.model.ChangeItem
import com.example.myfirstcomposeapp.model.ChangeLog
import com.example.myfirstcomposeapp.ui.common.TextInputDialog
import com.example.myfirstcomposeapp.ui.theme.Dimens
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeDetailScreen(
    id: String,
    vm: ChangeViewModel,
    onBack: () -> Unit
) {
    val item = vm.observeById(id).collectAsStateWithLifecycle().value
    val logs = vm.observeLogs(id).collectAsStateWithLifecycle().value

    var showActorDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }
    var showReopenDialog by remember { mutableStateOf(false) }

    var actor by remember { mutableStateOf(vm.currentUser) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { showActorDialog = true }) {
                        Text(stringResource(R.string.detail_actor_label, actor))
                    }
                }
            )
        }
    ) { padding ->

        if (item == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.detail_not_found))
            }
            return@Scaffold
        }

        val actions = vm.allowedActions(item)

        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(Dimens.spacingLg),
                verticalArrangement = Arrangement.spacedBy(Dimens.spacingMd)
            ) {
                item { HeaderCard(item) }
                item { BasicInfoCard(item) }
                item { ContentCard(item) }

                item {
                    Text(
                        stringResource(R.string.detail_log_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                items(logs) { log -> LogCard(log) }

                item { Spacer(Modifier.height(Dimens.spacing5xl)) }
            }

            // 底部动作（UI-only：直接调用 vm 的流程方法）
            Card(Modifier.padding(Dimens.spacingMd)) {
                Column(
                    Modifier.padding(Dimens.spacingMd),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
                ) {
                    Text(
                        stringResource(
                            R.string.detail_status_role,
                            item.status.label,
                            stringResource(vm.currentRole.labelRes)
                        )
                    )
                    if (actions.isEmpty()) {
                        Text(
                            stringResource(R.string.detail_no_actions),
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        actions.forEach { act ->
                            Button(onClick = {
                                when (act) {
                                    com.example.myfirstcomposeapp.model.ActionType.SUBMIT -> vm.submit(item.id, actor)
                                    com.example.myfirstcomposeapp.model.ActionType.CONFIRM -> vm.confirm(item.id, actor)
                                    com.example.myfirstcomposeapp.model.ActionType.REJECT -> showRejectDialog = true
                                    com.example.myfirstcomposeapp.model.ActionType.RELEASE -> vm.release(item.id, actor)
                                    com.example.myfirstcomposeapp.model.ActionType.CLOSE -> vm.close(item.id, actor)
                                    com.example.myfirstcomposeapp.model.ActionType.REOPEN -> showReopenDialog = true
                                    else -> {}
                                }
                            }) { Text(actionText(act.name)) }
                        }
                    }
                }
            }
        }

        if (showActorDialog) {
            TextInputDialog(
                title = stringResource(R.string.detail_set_actor_title),
                placeholder = stringResource(R.string.detail_actor_placeholder),
                initialValue = actor,
                onCancel = { showActorDialog = false },
                onConfirm = {
                    actor = it.trim().ifBlank { actor }
                    showActorDialog = false
                }
            )
        }

        if (showRejectDialog) {
            TextInputDialog(
                title = stringResource(R.string.detail_reject_title),
                placeholder = stringResource(R.string.detail_reject_placeholder),
                initialValue = "",
                onCancel = { showRejectDialog = false },
                onConfirm = { reason ->
                    vm.reject(item.id, actor, reason.trim())
                    showRejectDialog = false
                }
            )
        }

        if (showReopenDialog) {
            TextInputDialog(
                title = stringResource(R.string.detail_reopen_title),
                placeholder = stringResource(R.string.detail_reopen_placeholder),
                initialValue = "",
                onCancel = { showReopenDialog = false },
                onConfirm = { reason ->
                    vm.reopen(item.id, actor, reason.trim())
                    showReopenDialog = false
                }
            )
        }
    }
}

@Composable
private fun HeaderCard(item: ChangeItem) {
    Card {
        Column(
            Modifier.padding(Dimens.spacingMd),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
        ) {
            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(stringResource(R.string.detail_id_label, item.id), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun BasicInfoCard(item: ChangeItem) {
    val df = remember { SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()) }
    Card {
        Column(
            Modifier.padding(Dimens.spacingMd),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
        ) {
            Text(stringResource(R.string.detail_creator_label, item.creator))
            Text(stringResource(R.string.detail_created_at, df.format(Date(item.createdAt))))
            Text(stringResource(R.string.detail_updated_at, df.format(Date(item.updatedAt))))
            Text(
                stringResource(
                    R.string.detail_type_urgent,
                    item.type.label,
                    stringResource(if (item.urgent) R.string.common_yes else R.string.common_no)
                )
            )
            Text(stringResource(R.string.detail_scope_label, item.line, item.equipment, item.process))
        }
    }
}

@Composable
private fun ContentCard(item: ChangeItem) {
    Card {
        Column(
            Modifier.padding(Dimens.spacingMd),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
        ) {
            Text(stringResource(R.string.detail_content_title), fontWeight = FontWeight.SemiBold)
            Text(item.content)
        }
    }
}

@Composable
private fun LogCard(log: ChangeLog) {
    val df = remember { SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()) }
    Card {
        Column(
            Modifier.padding(Dimens.spacingMd),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingXs)
        ) {
            Text(stringResource(R.string.detail_log_entry, log.action.name, log.actor), fontWeight = FontWeight.SemiBold)
            Text(df.format(Date(log.at)), style = MaterialTheme.typography.bodySmall)
            if (log.note.isNotBlank()) Text(log.note)
        }
    }
}

@Composable
private fun actionText(actionName: String): String =
    when (actionName) {
        "SUBMIT" -> stringResource(R.string.action_submit)
        "CONFIRM" -> stringResource(R.string.action_confirm)
        "REJECT" -> stringResource(R.string.action_reject)
        "RELEASE" -> stringResource(R.string.action_release)
        "CLOSE" -> stringResource(R.string.action_close)
        "REOPEN" -> stringResource(R.string.action_reopen)
        else -> actionName
    }
