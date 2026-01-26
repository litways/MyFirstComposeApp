package com.example.myfirstcomposeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.model.ChangeDraft
import com.example.myfirstcomposeapp.model.ChangeType
import com.example.myfirstcomposeapp.ui.common.TypeDropdownField
import com.example.myfirstcomposeapp.ui.theme.Dimens
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
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.edit_title)) },
                    navigationIcon = {
                        IconButton(onClick = onCancel) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.action_back)
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(stringResource(R.string.detail_not_found))
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
    val titleErr = if (title.trim().isEmpty()) stringResource(R.string.error_title_required_edit) else null
    val contentErr = if (content.trim().isEmpty()) stringResource(R.string.error_content_required) else null
    val creatorErr = if (creator.trim().isEmpty()) stringResource(R.string.error_creator_required) else null
    val typeErr = if (type == null) stringResource(R.string.error_type_required) else null
    val ok = titleErr == null && contentErr == null && creatorErr == null && typeErr == null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_title)) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(Dimens.spacingLg),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingMd)
        ) {
            Card(shape = MaterialTheme.shapes.large) {
                Column(
                    Modifier.fillMaxWidth().padding(Dimens.spacingMd),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
                ) {
                    OutlinedTextField(
                        value = creator,
                        onValueChange = { creator = it },
                        label = { Text(stringResource(R.string.label_creator_required)) },
                        isError = creatorErr != null,
                        supportingText = { if (creatorErr != null) Text(creatorErr) }
                    )

                    TypeDropdownField(
                        selected = type,
                        onSelect = { type = it },
                        label = stringResource(R.string.label_type_required),
                        isError = typeErr != null,
                        supportingText = typeErr
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(stringResource(R.string.label_title_required_edit)) },
                        isError = titleErr != null,
                        supportingText = { if (titleErr != null) Text(titleErr) }
                    )

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text(stringResource(R.string.label_content_required)) },
                        minLines = 3,
                        isError = contentErr != null,
                        supportingText = { if (contentErr != null) Text(contentErr) }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMd)) {
                        FilterChip(
                            selected = urgent,
                            onClick = { urgent = !urgent },
                            label = { Text(stringResource(R.string.urgent_label)) }
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMd)) {
                        OutlinedTextField(
                            value = line,
                            onValueChange = { line = it },
                            label = { Text(stringResource(R.string.label_line)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = equipment,
                            onValueChange = { equipment = it },
                            label = { Text(stringResource(R.string.label_equipment)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = process,
                            onValueChange = { process = it },
                            label = { Text(stringResource(R.string.label_process)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMd)) {
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.action_cancel))
                }

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
                ) { Text(stringResource(R.string.action_save)) }

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text(stringResource(R.string.action_delete)) }
            }

            Text(stringResource(R.string.edit_hint), style = MaterialTheme.typography.bodySmall)
        }
    }
}
