@file:Suppress("MemberVisibilityCanBePrivate")

package model

import java.io.File
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.extension
import kotlin.io.path.relativeTo

class Artifact private constructor(
    val path: Path,
    val groupId: String,
    val id: String,
    val version: String,
    val kind: Kind
) {
    val fullId: String get() = "$groupId:$id:$version"

    enum class Kind {
        JAR, POM, XML, AAR, KLIB, MODULE, OTHER;

        companion object {
            fun fromExtension(extension: String): Kind {
                val normalized = extension.toUpperCase()
                return values().firstOrNull { it.name == normalized } ?: OTHER
            }
        }
    }

    fun asFile(): File = path.toFile()
    fun reconstructPath(relativeTo: Path): Path {
        return relativeTo
            .resolve(groupId.replace('.', '/'))
            .resolve("$id/$version/${path.fileName}")
    }

    companion object {
        @OptIn(ExperimentalPathApi::class)
        fun at(cacheRoot: Path, path: Path): Artifact {
            //Last two are the file and the hash-named directory
            val fullId = path.relativeTo(cacheRoot).toMutableList()

            val kind = Kind.fromExtension(fullId.removeLast().extension)

            fullId.removeLast()
            val version = fullId.last().toString()
            val id = fullId[fullId.lastIndex - 1].toString()
            val groupId = fullId[0].toString()

            return Artifact(path, groupId, id, version, kind)
        }
    }

    override fun toString(): String {
        return "Artifact '$groupId:$id:$version' ($kind) at $path"
    }
}