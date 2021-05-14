package model

import MAVEN_PATH
import SAMPLE_ARTIFACT_PATH
import SAMPLE_CACHE_DIR
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.test.assertEquals



internal class ArtifactTest {
    @Test
    fun `create artifact at path`() {
        val artifact = Artifact.at(File(SAMPLE_CACHE_DIR).toPath(), File(SAMPLE_ARTIFACT_PATH).toPath())

        assertEquals("org.jetbrains.compose.animation", artifact.groupId)
        assertEquals("animation", artifact.id)
        assertEquals("0.3.0", artifact.version)
    }

    @OptIn(ExperimentalPathApi::class)
    @Test
    fun `reconstruct directory structure`() {
        val artifact = Artifact.at(File(SAMPLE_CACHE_DIR).toPath(), File(SAMPLE_ARTIFACT_PATH).toPath())

        assertEquals(
            "${MAVEN_PATH}org/jetbrains/compose/animation/animation/0.3.0/animation-0.3.0.pom",
            artifact.reconstructPath(Path(MAVEN_PATH)).toString()
        )
    }
}