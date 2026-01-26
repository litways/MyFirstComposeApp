package com.example.myfirstcomposeapp.ui.screens.evidence

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvidenceScreen(
    changeId: String,
    onBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 预先读取 stringResource，避免在 coroutine/lambda 中直接调用
    val placeholderMessage = stringResource(R.string.evidence_fab_message)
    val addLabel = stringResource(R.string.action_add)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.evidence_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(placeholderMessage)
                    }
                }
            ) {
                Text(addLabel)
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
                text = stringResource(R.string.evidence_change_id, changeId),
                style = MaterialTheme.typography.bodySmall
            )

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        stringResource(R.string.evidence_list_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.evidence_item_photo))
                    Text(stringResource(R.string.evidence_item_first_check))
                    Text(stringResource(R.string.evidence_item_confirm))
                    Text(stringResource(R.string.evidence_item_release))
                }
            }

            Card {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        stringResource(R.string.common_note_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.evidence_note),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}