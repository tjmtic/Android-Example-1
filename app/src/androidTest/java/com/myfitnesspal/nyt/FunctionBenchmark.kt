package com.myfitnesspal.nyt

import org.junit.Rule
import org.junit.Test
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FunctionBenchmark {

    val testData = "article_response.json"

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @Test
    fun benchmarkFullParse() = benchmarkRule.measureRepeated {
        //val articleList = Mapper.streamParse(this::class.java.classLoader?.getResourceAsStream(testData))

    }

    @Test
    fun benchmarkShortParse() = benchmarkRule.measureRepeated {
        //val articleList = Mapper.streamParse(this::class.java.classLoader?.getResourceAsStream(testData))

    }
}