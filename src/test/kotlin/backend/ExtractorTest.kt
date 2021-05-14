package backend

import GRADLE_CACHE_PATH
import MAVEN_PATH
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.test.assertEquals
import kotlin.test.assertTrue
/*
@ExperimentalPathApi
internal class ExtractorTest {

    @Test
    fun `scan cache test`() {
        val extractor = Extractor()

        runBlocking {
            extractor.scan().collect {}
        }
    }

    @Test
    fun `default gradle cache location test`() {
        assertEquals(Path(GRADLE_CACHE_PATH), Extractor.defaultCachePath())
    }

    @Test
    fun `default maven location test`() {
        assertEquals(Path(MAVEN_PATH), Extractor.defaultMavenPath())
    }

    @Test
    fun `extraction test`() {
        val extractor = Extractor()

        runBlocking {
            // Scan for the first 10 artifacts
            val artifacts = extractor.scan().toList()

            // Extract those 10 artifacts
            extractor.extract(artifacts).collect()

            // Verify that the copy occurred
            artifacts.forEach {
                assertTrue(it.reconstructPath(extractor.mavenPath).exists())
            }
        }
    }
}*/