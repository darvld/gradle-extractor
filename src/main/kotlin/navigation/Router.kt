package navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*

typealias Router = (Destination) -> Unit

@Composable
fun Navigator(home: Destination) {
    var current by remember { mutableStateOf(home) }
    val content = current.navigate {
        current = it
    }
    Crossfade(content) {
        it()
    }
}
