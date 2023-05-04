package com.bangkit.mystoryapps.data.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.bangkit.mystoryapps.UI.Main.StoryAdapter
import com.bangkit.mystoryapps.data.local.Entity.StoryEntity
import com.bangkit.mystoryapps.data.repositories.StoryRepository
import com.bangkit.mystoryapps.utils.DataDummy
import com.bangkit.mystoryapps.utils.StoryPagingSource
import com.bangkit.mystoryapps.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var storyViewModel: StoryViewModel
    private val dummyStory = DataDummy.generateDummyStoryEntity()
    @Mock private lateinit var storyRepo: StoryRepository

    @Before
    fun setUp(){
        storyViewModel = StoryViewModel(storyRepo)
    }

    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUpDispatcher(){
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDownDispatcher(){
        Dispatchers.resetMain()
    }

    @Test
    fun `when paging data success`() = runTest{
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStory)
        val expectedRes = MutableLiveData<PagingData<StoryEntity>>()
        expectedRes.value = data
        `when`(storyRepo.getStoryPaging()).thenReturn(expectedRes)
        storyViewModel = StoryViewModel(storyRepo)
        val actualResult = storyViewModel.storyPaging().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdate,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualResult)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0].id, differ.snapshot().items[0].id)
    }

    @Test
    fun `when paging data is empty return no data`() = runTest {
        val data: PagingData<StoryEntity> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value = data
        `when`(storyRepo.getStoryPaging()).thenReturn(expectedStory)

        storyViewModel = StoryViewModel(storyRepo)
        val actualStory = storyViewModel.storyPaging().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdate,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }
    private val noopListUpdate = object : ListUpdateCallback{
        override fun onInserted(position: Int, count: Int) {
        }

        override fun onRemoved(position: Int, count: Int) {
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
        }

    }
}