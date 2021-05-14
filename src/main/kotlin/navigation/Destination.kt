package navigation

import androidx.compose.runtime.*

typealias Content = @Composable () -> Unit

interface Destination {
    fun navigate(onRoute: Router): Content
}