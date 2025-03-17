package com.example.hskonewordaday.ui

import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.hskonewordaday.MainActivity
import com.example.hskonewordaday.MainCoroutineRule
import com.example.hskonewordaday.data.ChineseWordEntity
import com.example.hskonewordaday.domain.WordsRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
//@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

//    @get:Rule(order = 2)
//    val mainCoroutineRule = MainCoroutineRule()

    private var fakeWordsRepository = mockk<WordsRepository>()

//    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: MainScreenViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
//        viewModel = MainScreenViewModel(fakeWordsRepository, testDispatcher)
//        composeTestRule.activity.setContent {
//            MainScreen()
//        }
    }

    private fun launchMainScreen(hskLevel: HskLevel = HskLevel.ALL){
        composeTestRule.setContent {


//            val uiState = viewModel.uiState.collectAsState()
//            androidx.compose.runtime.LaunchedEffect(key1 = uiState.value){
//                if(uiState.value is MainScreenUiState.Success){
//                    println("MainScreenTest: Success")
//                }
//            }
//            FakeMainScreen(
//                uiState = uiState,
//                showHskLevel = hskLevel
//            )
        }
    }

    @Test
    fun initial_state_loading_displayed(){
//        composeTestRule.setContent {
//            MainScreen()
//        }
        composeTestRule.onNodeWithText("Loading").assertIsDisplayed()
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

    @Test
    fun mainScreen() {}
}