package com.example.hskonewordaday

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

class MyAppWidget : GlanceAppWidget() {

    val hanziList = listOf("好", "今", "日", "一", "天", "解", "中", "国", "文", "化")
    override suspend fun provideGlance(context: Context, id: GlanceId) {


        provideContent {
            val hanzi = remember { mutableStateOf(getRandomHanzi(hanziList)) }
            MyContent(
                hanzi,
                modifier = GlanceModifier.fillMaxSize(),
                onClick = {
                    hanzi.value = getRandomHanzi(hanziList)
                }

            )
        }
    }

    fun getRandomHanzi(list: List<String>): String {
        return list.random()
    }
}

@Composable
private fun MyContent(
    hanzi: MutableState<String>,
    modifier: GlanceModifier = GlanceModifier,
    onClick: () -> Unit = {}
){
    Column(
        modifier = modifier.background(Color.LightGray),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hanzi.value,
            style = TextStyle(fontSize = 36.sp)
        )
        Text(
            text = "Translation",
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