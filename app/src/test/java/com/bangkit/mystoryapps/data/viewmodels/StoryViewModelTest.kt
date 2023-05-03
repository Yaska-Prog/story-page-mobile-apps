package com.bangkit.mystoryapps.data.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.liveData
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.local.Entity.StoryEntity
import com.bangkit.mystoryapps.data.local.database.MyStoryDatabase
import com.bangkit.mystoryapps.data.local.database.StoryRemoteMediator
import com.bangkit.mystoryapps.data.remote.retrofit.ApiService
import com.bangkit.mystoryapps.data.repositories.StoryRepository
import com.bangkit.mystoryapps.data.repositories.UserRepository
import com.bangkit.mystoryapps.utils.DataDummy
import com.bangkit.mystoryapps.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var storyViewModel: StoryViewModel
    private val dummyStory = DataDummy.generateDummyStoryEntity()
    @Mock private lateinit var storyRepo: StoryRepository
    @Mock private lateinit var storyRemoteMediator: StoryRemoteMediator
    @Mock private lateinit var storyPagingSource: PagingSource<Int, StoryEntity>

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
    fun `when getStoryPaging should not null and return paging data`() = runTest{
        val observer = Observer<PagingData<StoryEntity>>{}
        try {
            @OptIn(ExperimentalPagingApi::class)
            val expectedRes = Pager(
                config = PagingConfig(pageSize = 5),
                remoteMediator = storyRemoteMediator,
                pagingSourceFactory = {
                    storyPagingSource
                }
            ).liveData
            `when`(storyRepo.getStoryPaging()).thenReturn(expectedRes)
            val actualRes = storyViewModel.storyPaging().observeForever(observer)
            Mockito.verify(storyRepo).getStoryPaging()
            Assert.assertNotNull(actualRes)
        }finally {
            storyViewModel.storyPaging().removeObserver(observer)
        }
    }

    @Test
    fun `when getAllStoryList successfully read data from web service`() = runTest {
        val expectedStory =MutableLiveData<Result<List<StoryEntity>>>()
        expectedStory.value = Result.Success(dummyStory)
        `when`(storyRepo.getAllStory()).thenReturn(expectedStory)
        val actualStory = storyViewModel.getAllStoryList().getOrAwaitValue()
        Mockito.verify(storyRepo).getAllStory()
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Success)
        Assert.assertEquals(dummyStory.size, (actualStory as Result.Success).data.size)
        Assert.assertEquals(dummyStory[0], (actualStory as Result.Success).data[0])
    }

    @Test
    fun `when data not found should return error`(){
        val expectedStory = MutableLiveData<Result<List<StoryEntity>>>()
        expectedStory.value = Result.Error("Error")
        `when`(storyRepo.getAllStory()).thenReturn(expectedStory)
        val actualStory = storyViewModel.getAllStoryList().getOrAwaitValue()
        Mockito.verify(storyRepo).getAllStory()
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Error)
    }
}