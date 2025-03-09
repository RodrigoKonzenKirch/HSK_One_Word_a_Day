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

        assertThat(viewModel.allWords.value).isEmpty()
    }

    @Test
    fun `Words emission check`() = runTest {
        // Test that when the repository emits a list of 'ChineseWordEntity', 
        // the ViewModel correctly updates the 'allWords' StateFlow with the same list.

        val words = listOf(
            ChineseWordEntity(1, "HSK1", "一", "一", "yi", "yi1", "one"),
            ChineseWordEntity(2, "HSK2", "二", "二", "er", "er2", "two")
        )
        coEvery { fakeRepository.getAllWords() } returns flowOf(words)
        val vm = MainScreenViewModel(fakeRepository, testDispatcher)

        advanceUntilIdle()

        assertThat(vm.allWords.value).containsExactlyElementsIn(words)
    }

    @Test
    fun `Empty words emission`() = runTest {
        // Verify that if the repository emits an empty list, the 'allWords' 
        // StateFlow is updated to an empty list as well.

        val words = emptyList<ChineseWordEntity>()
        coEvery { fakeRepository.getAllWords() } returns flowOf(words)
        val vm = MainScreenViewModel(fakeRepository, testDispatcher)

        advanceUntilIdle()
        assertThat(vm.allWords.value).isEmpty()
    }

    @Test
    fun `Error during words retrieval`() {
        // Test the scenario where an error occurs during the retrieval of words 
        // from the repository, and ensure the StateFlow is still updated accordingly. (e.g empty list)

        val exception = Exception("Test exception")
        coEvery { fakeRepository.getAllWords() } throws exception
        val vm = MainScreenViewModel(fakeRepository, testDispatcher)

        assertThat(vm.allWords.value).isEmpty()
    }

}