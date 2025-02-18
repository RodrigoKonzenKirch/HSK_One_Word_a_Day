package com.example.hskonewordaday

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hskonewordaday.ui.theme.HSKOneWordADayTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val words = readHSKWords(this)
        setContent {
            var showHskLevel by remember { mutableStateOf(HskLevel.ALL) }

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
                                        modifier = Modifier.menuAnchor().fillMaxWidth().padding(8.dp),
                                        readOnly = true,
                                        value = showHskLevel.name,
                                        onValueChange = {},
                                        label = { Text("HSK Level") },
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
                    }
                ) { innerPadding ->

                    WordList(
                        words = (if (showHskLevel == HskLevel.ALL) {
                            words
                        } else {
                            // TODO: Make a better conversion
                            words.filter { it.hskLevel.uppercase() == showHskLevel.name }

                        }),
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

enum class HskLevel {
    ALL, HSK1, HSK2, HSK3, HSK4, HSK5, HSK6
}

fun readHSKWords(context: Context): List<ChineseWord> {
    val resourceId = R.raw.hsk1to6
    val chineseWords = mutableListOf<ChineseWord>()

    val inputStream = context.resources.openRawResource(resourceId)
    val reader = BufferedReader(InputStreamReader(inputStream))

    reader.useLines { lines ->
        lines.forEach { line ->
            val columns = line.split('\t')
            if (columns.size == 6) {
                chineseWords.add(
                    ChineseWord(
                        hskLevel = columns[0],
                        chineseSimplified = columns[1],
                        chineseTraditional = columns[2],
                        pronunciationNumber = columns[3],
                        pronunciationSymbol = columns[4],
                        meaning = columns[5]
                    )
                )
            } else {
                println("Skipping line with unexpected format: $line")
            }
        }
    }

    return chineseWords
}

data class ChineseWord(
    val hskLevel: String,
    val chineseSimplified: String,
    val chineseTraditional: String,
    val pronunciationNumber: String,
    val pronunciationSymbol: String,
    val meaning: String
)

@Composable
fun WordList(modifier: Modifier = Modifier, words: List<ChineseWord>) {
    LazyColumn(modifier = modifier) {
        items(words) { word ->
            WordItem(word = word)
        }
    }
}

@Composable
fun WordItem(word: ChineseWord) {
    Card( modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(
            modifier = Modifier.padding(8.dp),
            text =  "${word.hskLevel}\n" +
                    "Simplified: ${word.chineseSimplified}\n" +
                    "Traditional: ${word.chineseTraditional}\n" +
                    "Pronunciation (Number): ${word.pronunciationNumber}\n" +
                    "Pronunciation (Symbol): ${word.pronunciationSymbol}\n" +
                    "Meaning: ${word.meaning}"
        )
    }
}