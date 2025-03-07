package com.example.hskonewordaday

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyAppWidget : GlanceAppWidget() {

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val wordDao = getDatabase(context).chineseWordDao()
        val randomWord = wordDao.getRandomWord()

        provideContent {
            val hanzi = rememberSaveable { mutableStateOf(randomWord) }
            MyContent(
                hanzi,
                modifier = GlanceModifier.fillMaxSize(),
                onClick = {
                    GlobalScope.launch(Dispatchers.IO) {
                        hanzi.value = wordDao.getRandomWord()

                    }
                }

            )
        }
    }

}

@Composable
private fun MyContent(
    hanzi: State<ChineseWordEntity>,
    modifier: GlanceModifier = GlanceModifier,
    onClick: () -> Unit = {}
){
    Column(
        modifier = modifier.background(Color.Black),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${hanzi.value.chineseSimplified} [${hanzi.value.chineseTraditional}]",
            style = TextStyle(
                color = ColorProvider(color = Color.White),
                fontSize = 36.sp,
            )
        )
        Text(
            text = "${hanzi.value.pronunciationSymbol} [${hanzi.value.pronunciationNumber}]",
            style = TextStyle(
                color = ColorProvider(color = Color.White),
                ),
            modifier = GlanceModifier.padding(12.dp)
        )
        Text(
            text = hanzi.value.meaning,
            style = TextStyle(
                color = ColorProvider(color = Color.White),
            ),
            modifier = GlanceModifier.padding(12.dp)
        )
        Row(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                text = "Refresh",
                onClick = onClick
            )
        }
    }
}