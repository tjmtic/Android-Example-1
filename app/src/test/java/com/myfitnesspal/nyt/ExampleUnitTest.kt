package com.myfitnesspal.nyt

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    //@get:Rule
   // val instantTaskExecutorRule = InstantTaskExecutorRule()

    //lateinit var mapper: Mapper
    val testData = "article_response.json"

    val fakeUrl = "https://www.nytimes.com/2020/08/31/business/media/kenosha-newspaper-editor-quits.html"

    val fakeUrls = listOf(
        "https://www.nytimes.com/2020/08/31/business/media/kenosha-newspaper-editor-quits.html",
        "https://www.nytimes.com/aponline/2020/08/31/sports/ncaafootball/ap-fbc-virus-outbreak-big-ten.html",
        "https://www.nytimes.com/2020/08/31/briefing/joe-biden-child-infections-us-open.html"
    )

    val fake = mapOf("status" to "OK",
    "copyright" to "Copyright (c) 2020 The New York Times Company. All Rights Reserved.",
    "response" to mapOf(
        "docs" to listOf(
        mapOf(
            "abstract" to "Daniel Thompson, an editor at The Kenosha News, resigned over a headline that highlighted a speaker who made a threat during a peaceful protest.",
            "web_url" to "https://www.nytimes.com/2020/08/31/business/media/kenosha-newspaper-editor-quits.html",
            "snippet" to "Daniel Thompson, an editor at The Kenosha News, resigned over a headline that highlighted a speaker who made a threat during a peaceful protest.",
            "lead_paragraph" to "A journalist resigned on Saturday from his job at The Kenosha News after objecting to the headline of an article that chronicled a rally in support of Jacob Blake, a Black man who was shot seven times in the back by a white Kenosha police officer.",
            "source" to "The New York Times",
    "headline" to mapOf(
        "main" to "Journalist Quits Kenosha Paper in Protest of Its Jacob Blake Rally Coverage",
        "kicker" to null,
        "content_kicker" to null,
        "print_headline" to "Editor in Kenosha Resigns Over Headline",
        "name" to null,
        "seo" to null,
        "sub" to null,
    )))))



    val fakeData = "{\"status\": \"OK\",\n" +
            "  \"copyright\": \"Copyright (c) 2020 The New York Times Company. All Rights Reserved.\",\n" +
            "  \"response\": {\n" +
            "    \"docs\": [\n" +
            "      {\n" +
            "        \"abstract\": \"Daniel Thompson, an editor at The Kenosha News, resigned over a headline that highlighted a speaker who made a threat during a peaceful protest.\",\n" +
            "        \"web_url\": \"https://www.nytimes.com/2020/08/31/business/media/kenosha-newspaper-editor-quits.html\",\n" +
            "        \"snippet\": \"Daniel Thompson, an editor at The Kenosha News, resigned over a headline that highlighted a speaker who made a threat during a peaceful protest.\",\n" +
            "        \"lead_paragraph\": \"A journalist resigned on Saturday from his job at The Kenosha News after objecting to the headline of an article that chronicled a rally in support of Jacob Blake, a Black man who was shot seven times in the back by a white Kenosha police officer.\",\n" +
            "        \"source\": \"The New York Times\"," +
            "\"headline\": {\n" +
            "          \"main\": \"Big Ten Presidents Voted 11-3 to Cancel Fall Football Season\",\n" +
            "          \"kicker\": null,\n" +
            "          \"content_kicker\": null,\n" +
            "          \"print_headline\": \"Big Ten Presidents Voted 11-3 to Cancel Fall Football Season\",\n" +
            "          \"name\": null,\n" +
            "          \"seo\": null,\n" +
            "          \"sub\": null\n" +
            "        }," +
            "}]}}"

    @Before
    fun setup(){
        //val apiService = mockk()
       // articleRepository = ArticleRepository(apiService)

    }


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testArticle() {
        //Hypothesis
        //articleRepository.parseArticle(sampleData)
        val sample = this::class.java.classLoader?.getResourceAsStream(fakeData)
        //val data = JsonReader(BufferedReader(InputStreamReader(sample)))

        //val data = JsonParser.parseString(fakeData)
        //val result = Mapper.parseResponse(data.asJsonObject)

        val result2 = Mapper.streamParse(sample)

        assertEquals(fakeUrls[0], result2?.get(0)?.url)
    }


}