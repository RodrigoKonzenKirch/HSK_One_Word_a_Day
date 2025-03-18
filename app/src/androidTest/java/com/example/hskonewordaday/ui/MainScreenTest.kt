package com.example.hskonewordaday.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.hskonewordaday.MainActivity
import com.example.hskonewordaday.data.ChineseWordEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun initial_state_menu_shows_ALL(){
        composeTestRule.activity.setContent {
            MainScreen()
        }
        composeTestRule.onNodeWithText("ALL").assertIsDisplayed()
    }

    @Test
    fun when_menu_is_clicked_shows_options(){
        composeTestRule.activity.setContent {
            MainScreen()
        }
        composeTestRule.onNodeWithTag("hskLevelDropdown").performClick()
        composeTestRule.onNodeWithText("HSK1").assertIsDisplayed()
        composeTestRule.onNodeWithText("HSK2").assertIsDisplayed()
        composeTestRule.onNodeWithText("HSK3").assertIsDisplayed()
        composeTestRule.onNodeWithText("HSK4").assertIsDisplayed()
        composeTestRule.onNodeWithText("HSK5").assertIsDisplayed()
        composeTestRule.onNodeWithText("HSK6").assertIsDisplayed()
    }

    @Test
    fun when_menu_option_is_selected_shows_selected_option(){
        composeTestRule.activity.setContent {
            MainScreen()
        }
        composeTestRule.onNodeWithTag("hskLevelDropdown").performClick()
        composeTestRule.onNodeWithText("HSK1").performClick()
        composeTestRule.onNodeWithText("HSK1").assertIsDisplayed()
    }

    @Test
    fun when_menu_option_is_selected_shows_only_words_with_that_hsk_level(){
        composeTestRule.activity.setContent {
            MainScreen()
        }
        composeTestRule.onNodeWithTag("hskLevelDropdown").performClick()
        composeTestRule.onNodeWithText("HSK1").performClick()
        composeTestRule.onAllNodesWithText("我").onFirst().assertExists()
        composeTestRule.onAllNodesWithText("知道").onFirst().assertDoesNotExist()
        composeTestRule.onNodeWithTag("hskLevelDropdown").performClick()
        composeTestRule.onNodeWithText("HSK2").performClick()
        composeTestRule.onAllNodesWithText("我").onFirst().assertDoesNotExist()
        composeTestRule.onAllNodesWithText("知道").onFirst().assertExists()
    }

    @Test
    fun word_item_show_items(){
        val word = ChineseWordEntity(
            hskLevel = "HSK1",
            chineseSimplified = "你好",
            chineseTraditional = "你好",
            pronunciationSymbol = "nǐ hǎo",
            pronunciationNumber = "ni3 hao3",
            meaning = "hello"
        )
        composeTestRule.activity.setContent {
            WordItem(word = word)
        }
        composeTestRule.onNodeWithText("HSK1").assertIsDisplayed()
    }

}