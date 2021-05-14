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
import kotlin.io.path.listDirectoryEntries

@OptIn(ExperimentalPathApi::class)
class Scanner(var cachePath: Path = defaultCachePath() ?: Path("./extracted/")) {
    fun scan(): Flow<Artifact> = flow {
        val subCaches = cachePath.listDirectoryEntries("modules*").flatMap {
            it.listDirectoryEntries("files*")
        }

        for (cache in subCaches) {
            for (file in cache.toFile().walkBottomUp()) {
                if (!file.isFile) continue

                emit(Artifact.at(cache, file.toPath()))
            }
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        fun defaultCachePath(): Path? {
            val home = System.getProperty("user.home")?.let { Path(it) } ?: return null
            return home.resolve(".gradle/caches/")
        }
    }
}