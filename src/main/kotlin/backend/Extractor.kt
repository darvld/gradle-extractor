@file:Suppress("MemberVisibilityCanBePrivate")

package backend

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import model.Artifact
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path

@OptIn(ExperimentalPathApi::class)
class Extractor(
    var mavenPath: Path = defaultMavenPath() ?: Path("./extracted"),
) {
    fun extract(artifacts: Array<Artifact>, overwrite: Boolean = true): Flow<Artifact> = flow {
        artifacts.forEach { artifact ->
            try {
                artifact.asFile().copyTo(artifact.reconstructPath(mavenPath).toFile(), overwrite)
            } catch (e: FileAlreadyExistsException) {
            }

            emit(artifact)
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        fun defaultMavenPath(): Path? {
            val home = System.getProperty("user.home")?.let { Path(it) } ?: return null
            return home.resolve(".m2/repository/")
        }

    }
}