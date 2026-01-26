package com.example.myfirstcomposeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.model.ChangeDraft
import com.example.myfirstcomposeapp.model.ChangeType
import com.example.myfirstcomposeapp.ui.common.TypeDropdownField
import com.example.myfirstcomposeapp.ui.theme.Dimens

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
    var attemptedSubmit by remember { mutableStateOf(false) }

    val canSave =
        creator.trim().isNotEmpty() &&
                type != null &&
                title.trim().isNotEmpty() &&
                content.trim().isNotEmpty()

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_title)) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 2.dp) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(Dimens.spacingMd),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMd)
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f)
                    ) { Text(stringResource(R.string.action_cancel)) }

                    Button(
                        onClick = {
                            attemptedSubmit = true
                            if (!canSave) {
                                return@Button
                            }
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
                    ) { Text(stringResource(R.string.action_save)) }
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
                .padding(Dimens.spacingLg),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingMd)
        ) {
            ElevatedCard(shape = MaterialTheme.shapes.large) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(Dimens.spacingLg),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
                ) {
                    Text(
                        stringResource(R.string.add_required_section),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = creator,
                        onValueChange = { creator = it },
                        label = { Text(stringResource(R.string.label_creator_required)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = attemptedSubmit && creator.isBlank(),
                        supportingText = {
                            if (attemptedSubmit && creator.isBlank()) {
                                Text(stringResource(R.string.error_creator_required))
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    TypeDropdownField(
                        selected = type,
                        onSelect = { type = it },
                        label = stringResource(R.string.label_type_required),
                        isError = attemptedSubmit && type == null,
                        supportingText = if (attemptedSubmit && type == null) {
                            stringResource(R.string.error_type_required)
                        } else {
                            null
                        }
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(stringResource(R.string.label_title_required)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = attemptedSubmit && title.isBlank(),
                        supportingText = {
                            if (attemptedSubmit && title.isBlank()) {
                                Text(stringResource(R.string.error_title_required))
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text(stringResource(R.string.label_content_required)) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        isError = attemptedSubmit && content.isBlank(),
                        supportingText = {
                            if (attemptedSubmit && content.isBlank()) {
                                Text(stringResource(R.string.error_content_required))
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.label_urgent), style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = urgent, onCheckedChange = { urgent = it })
                    }
                }
            }

            ElevatedCard(shape = MaterialTheme.shapes.large) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(Dimens.spacingLg),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm)
                ) {
                    Text(
                        stringResource(R.string.add_scope_section),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = line,
                        onValueChange = { line = it },
                        label = { Text(stringResource(R.string.label_line)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    OutlinedTextField(
                        value = equipment,
                        onValueChange = { equipment = it },
                        label = { Text(stringResource(R.string.label_equipment)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    OutlinedTextField(
                        value = process,
                        onValueChange = { process = it },
                        label = { Text(stringResource(R.string.label_process)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )
                }
            }

            Text(
                stringResource(R.string.add_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 让内容在底部按钮栏上方留出空间，避免最后一个输入框被遮挡
            Spacer(Modifier.height(Dimens.spacing5xl))
        }
    }
}
