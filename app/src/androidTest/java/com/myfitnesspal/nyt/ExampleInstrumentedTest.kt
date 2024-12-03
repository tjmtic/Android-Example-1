package com.myfitnesspal.nyt

import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.stream.JsonReader
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
class ExampleInstrumentedTest {

    companion object {
        init {
            // Force the test environment to use HiltTestApplication
            System.setProperty("hilt.testing.force.application", "dagger.hilt.android.testing.HiltTestApplication")
        }
    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mockRepo: ArticleRepository

    lateinit var articleViewModel: ArticleViewModel
    var articleList: List<ArticleData>? = null
    val testData = "article_response.json"

    val fakes = TestData.fakeArticles


    @Before
    fun setup(){
        hiltRule.inject()
        //val sample = this::class.java.classLoader?.getResourceAsStream(testData)
        //val data = JsonReader(BufferedReader(InputStreamReader(sample)))
        //val mockApi = mock(ArticleApi::class.java)
       // val moa = mockk<ArticleApi>()
        //val repo = ArticleRepositoryImpl(moa)
        //val mockService:ArticleApi = mock(ArticleApi::class.java)
        //val mockRepo:ArticleRepository = ArticleRepositoryImpl(moa)
       // val mockRepo2 = mockk<ArticleRepositoryImpl>()

        articleViewModel = ArticleViewModel(mockRepo)
        articleList = Mapper.streamParse(this::class.java.classLoader?.getResourceAsStream(testData))
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.myfitnesspal.nyt", appContext.packageName)
    }

    @Test
    fun testMainFunction(){
        val sample = this::class.java.classLoader?.getResourceAsStream(testData)
        val data = JsonReader(BufferedReader(InputStreamReader(sample)))
        val returnData = Mapper.testFun(data)
        assertNotEquals(returnData, null)
    }

    @Test
    fun testUrlMatch(){
        val sample = this::class.java.classLoader?.getResourceAsStream(testData)
        val data = JsonReader(BufferedReader(InputStreamReader(sample)))
        val returnData = articleList//Mapper.testFun(data)
        assertEquals(returnData, fakes)
       // assertEquals(returnData[1].url, fakeUrls[1])
       // assertEquals(returnData[2].url, fakeUrls[2])
    }

    @Test
    fun testUrlMatch2(){
        val sample = this::class.java.classLoader?.getResourceAsStream(testData)
        val sample2 = this::class.java.classLoader?.getResourceAsStream(testData)
        val data = JsonReader(BufferedReader(InputStreamReader(sample)))
        val returnData = Mapper.testFun(data)
        val returnData2 = Mapper.streamParse(sample2)
        assertEquals(returnData, returnData2)
    }

    @Test
    fun testUrlMatch3(){
        val sample = this::class.java.classLoader?.getResourceAsStream(testData)
        val data = JsonReader(BufferedReader(InputStreamReader(sample)))
        val returnData = articleViewModel.testFun(data)
        assertEquals(returnData[2].url, fakes[2])
    }

    @Test
    fun testUrlMatch4(){
        val sample = this::class.java.classLoader?.getResourceAsStream(testData)
        val data = JsonReader(BufferedReader(InputStreamReader(sample)))
        val returnData = articleViewModel.testFun(data)

        val sample2 = this::class.java.classLoader?.getResourceAsStream(testData)
        val returnData2 = Mapper.streamParse(sample2)

        assertEquals(returnData, returnData2)
    }

    @Test
    fun testUrlMatch5(){
        val sample = this::class.java.classLoader?.getResourceAsStream(testData)
        val data = JsonReader(BufferedReader(InputStreamReader(sample)))
        val returnData = articleViewModel.testFun(data)
        val returnData2 = articleViewModel.articles.value
        assertEquals(returnData, returnData2)
    }

    @Test
    fun testUrlMatch6(){
        val sample = this::class.java.classLoader?.getResourceAsStream(testData)
        val data = JsonReader(BufferedReader(InputStreamReader(sample)))
        articleViewModel.testFun(data)
        val returnData = articleViewModel.articles.value
        val returnData2 = articleViewModel.articleList.value
        assertEquals(returnData, returnData2)
    }

    @Test
    fun testEmpty(){
        val sample = null//this::class.java.classLoader?.getResourceAsStream(testData)
        val data = JsonReader(BufferedReader(InputStreamReader(sample)))
        val returnData = Mapper.testFun(data)
        assertEquals(returnData, emptyList<ArticleData>())
    }
}