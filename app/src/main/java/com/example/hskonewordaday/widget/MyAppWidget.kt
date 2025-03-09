package com.example.hskonewordaday.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.hskonewordaday.data.AppDatabase.Companion.getDatabase
import com.example.hskonewordaday.data.ChineseWordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val wordDao = getDatabase(context).chineseWordDao()
        val randomWord = wordDao.getRandomWord()

        provideContent {
            var hanzi by rememberSaveable { mutableStateOf(randomWord) }
            val scope = rememberCoroutineScope()

            GlanceTheme{
                MyContent(
                    hanzi,
                    modifier = GlanceModifier.fillMaxSize(),
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            hanzi = wordDao.getRandomWord()

                        }
                    }
                )
            }
        }
    }

}

@Composable
private fun MyContent(
    hanzi: ChineseWordEntity,
    modifier: GlanceModifier = GlanceModifier,
    onClick: () -> Unit = {}
){
    Column(
        modifier = modifier.background(Color.Black),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Simplified and Traditional
        Text(
            text = "${hanzi.chineseSimplified} [${hanzi.chineseTraditional}]",
            style = TextStyle(
                color = ColorProvider(color = Color.White),
                fontSize = 36.sp,
            )
        )
        // Pronunciation
        Text(
            text = "${hanzi.pronunciationSymbol} [${hanzi.pronunciationNumber}]",
            style = TextStyle(
                color = ColorProvider(color = Color.White),
                ),
            modifier = GlanceModifier.padding(12.dp)
        )
        // Meaning
        Text(
            text = hanzi.meaning,
            style = TextStyle(
                color = ColorProvider(color = Color.White),
            ),
            modifier = GlanceModifier.padding(12.dp)
        )
        // Refresh button
        Row(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                text = "Refresh",
                onClick = onClick
            )
        }
    }
}