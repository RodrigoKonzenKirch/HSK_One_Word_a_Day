package com.example.hskonewordaday.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hskonewordaday.R
import com.example.hskonewordaday.data.ChineseWordEntity
import com.example.hskonewordaday.ui.theme.HSKOneWordADayTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var showHskLevel by rememberSaveable { mutableStateOf(HskLevel.ALL) }

    val viewModel = hiltViewModel<MainScreenViewModel>()

    val uiState = viewModel.uiState.collectAsState()

    HSKOneWordADayTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        var expanded by remember { mutableStateOf(false) }

                        ExposedDropdownMenuBox(
                            modifier = Modifier.fillMaxWidth(),
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                readOnly = true,
                                value = showHskLevel.name,
                                onValueChange = {},
                                label = { Text(stringResource(R.string.hsk_level)) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                HskLevel.entries.forEach { hskLevel ->
                                    DropdownMenuItem(
                                        text = { Text(hskLevel.name) },
                                        onClick = {
                                            showHskLevel = hskLevel
                                            expanded = false
                                        },
                                        contentPadding =
                                            ExposedDropdownMenuDefaults
                                                .ItemContentPadding
                                    )

                                }

                            }
                        }
                    }
                )
            },
        ) { innerPadding ->
            Screen(uiState, showHskLevel, innerPadding)
        }
    }
}

enum class HskLevel {
    ALL, HSK1, HSK2, HSK3, HSK4, HSK5, HSK6
}

@Composable
fun Screen(
    uiState: State<MainScreenViewModel.MainScreenState>,
    showHskLevel: HskLevel,
    innerPadding: PaddingValues
) {
    if (uiState.value.error != null){
        Text(text = uiState.value.error!!)
    } else if (uiState.value.isLoading) {
        Text(text = stringResource(R.string.loading))
    } else if (uiState.value.allWords.isEmpty()) {
        Text(text = stringResource(R.string.no_words_found))
    } else {
        WordList(
            words = (if (showHskLevel == HskLevel.ALL) {
                uiState.value.allWords
            } else {
                uiState.value.allWords.filter { it.hskLevel.uppercase() == showHskLevel.name }
            }
                    ),
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun WordList(modifier: Modifier = Modifier, words: List<ChineseWordEntity>) {
    val listState = rememberLazyListState()

    LazyColumn(modifier = modifier, state = listState) {
        items(words) { word ->
            WordItem(word = word)
        }
    }
}

@Composable
fun WordItem(word: ChineseWordEntity) {
    val smallPadding = 8.dp
    Card( modifier = Modifier
        .fillMaxWidth()
        .padding(smallPadding)
    ) {
        Column(
            modifier = Modifier.padding(smallPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(smallPadding),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = word.hskLevel,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            // Simplified
            Text(
                text = word.chineseSimplified,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )
            // Traditional
            Text(
                text = word.chineseTraditional,
                style = MaterialTheme.typography.bodyLarge,
            )
            // Pronunciation
            Text(
                text = "${word.pronunciationSymbol} [${word.pronunciationNumber}]",
                style = MaterialTheme.typography.bodyMedium,
            )
            // Meaning
            Text(
                text = word.meaning,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun WordItemPreview() {
    val sampleWord = ChineseWordEntity(
        hskLevel = "HSK 1",
        chineseSimplified = "你好",
        chineseTraditional = "你好",
        pronunciationSymbol = "nǐ hǎo",
        pronunciationNumber = "ni3 hao3",
        meaning = "hello"
    )
    WordItem(word = sampleWord)
}