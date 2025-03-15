package com.example.hskonewordaday

import com.example.hskonewordaday.data.ChineseWordEntity
import com.example.hskonewordaday.domain.WordsRepository
import com.example.hskonewordaday.ui.MainScreenViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val fakeRepository = mockk<WordsRepository>(relaxed = true)
    private lateinit var viewModel: MainScreenViewModel

    @Before
    fun setUp() {
        viewModel = MainScreenViewModel(fakeRepository, testDispatcher)
    }

    @Test
    fun `Initial state check`() {
        // Verify that the StateFlow 'allWords' emits an empty list 
        // immediately upon initialization of the ViewModel.

        assertThat(viewModel.uiState.value.allWords).isEmpty()
        assertThat(viewModel.uiState.value.isLoading).isTrue()
        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `Words emission check`() = runTest {
        // Test that when the repository emits a list of 'ChineseWordEntity', 
        // the ViewModel correctly updates the 'allWords' StateFlow with the same list.

        val chineseWords = listOf(
            ChineseWordEntity(1, "HSK1", "一", "一", "yi", "yi1", "one"),
            ChineseWordEntity(2, "HSK2", "二", "二", "er", "er2", "two")
        )
        coEvery { fakeRepository.getAllWords() } returns flowOf(chineseWords)
        val viewModel = MainScreenViewModel(fakeRepository, testDispatcher)

        advanceUntilIdle()

        assertThat(viewModel.uiState.value.allWords).containsExactlyElementsIn(chineseWords)
        assertThat(viewModel.uiState.value.isLoading).isFalse()
        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `Empty words emission`() = runTest {
        // Verify that if the repository emits an empty list, the 'allWords' 
        // StateFlow is updated to an empty list as well.

        val emptyWords = emptyList<ChineseWordEntity>()
        coEvery { fakeRepository.getAllWords() } returns flowOf(emptyWords)
        val viewModel = MainScreenViewModel(fakeRepository, testDispatcher)

        advanceUntilIdle()
        assertThat(viewModel.uiState.value.allWords).isEmpty()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `Error during words retrieval`() {
        // Test the scenario where an error occurs during the retrieval of words 
        // from the repository, and ensure the StateFlow is still updated accordingly. (e.g empty list)

        val errorToThrow = Exception("Test exception")
        coEvery { fakeRepository.getAllWords() } throws errorToThrow
        val viewModel = MainScreenViewModel(fakeRepository, testDispatcher)

        assertThat(viewModel.uiState.value.allWords).isEmpty()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
        assertThat(viewModel.uiState.value.error).isEqualTo("Error loading words")
    }

}