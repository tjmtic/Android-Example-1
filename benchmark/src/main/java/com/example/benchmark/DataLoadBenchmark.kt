package com.example.benchmark

// macrobenchmark/src/androidTest/java/com/example/macrobenchmark/DataLoadBenchmark.kt


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.*
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataLoadBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun dataLoading() = benchmarkRule.measureRepeated(
        packageName = "com.myfitnesspal.nyt",
        metrics = listOf(FrameTimingMetric(), MemoryUsageMetric(
            mode = MemoryUsageMetric.Mode.Max,
            subMetrics = emptyList()
        ), PowerMetric(
            type = PowerMetric.Type.Battery()
        )),
        iterations = 5,
        setupBlock = {
            // Prepare your app for the test
            pressHome()
            startActivityAndWait()
        }
    ) {
        // Perform the actions you want to benchmark
        // For example, click a button to load data
        device.findObject(By.text("Load Data")).click()

        // Wait for data to load (you may need to adjust the criteria)
        device.wait(Until.hasObject(By.text("Load Data")), 10_000)
    }
}
