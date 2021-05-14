import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import navigation.ExtractorView
import navigation.Navigator
import navigation.ScannerView

fun main() = Window(size = IntSize(800, 440), resizable = false) {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            Navigator(ScannerView)
        }
    }
}