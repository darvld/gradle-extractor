package navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.core.Preferences
import backend.Extractor
import backend.Scanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private class SettingsViewModel {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _cachePath = MutableStateFlow("")
    val cachePath: StateFlow<String> = _cachePath

    private val _mavenPath = MutableStateFlow("")
    val mavenPath: StateFlow<String> = _mavenPath

    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy

    fun loadSettings() = scope.launch {
        _busy.emit(true)

        _busy.emit(false)
    }

    fun saveSettings() = scope.launch {
        _busy.emit(true)



        _busy.emit(false)
    }
}

object SettingsView : Destination {
    override fun navigate(onRoute: Router): Content = @Composable {
        SettingsUi(onRoute)
    }
}

@Composable
fun SettingsUi(router: Router) {
    var cacheInputState by remember {
        mutableStateOf(TextFieldValue(Scanner.defaultCachePath().toString()))
    }
    var mavenInputState by remember {
        mutableStateOf(TextFieldValue(Extractor.defaultMavenPath().toString()))
    }
    var overwriteFiles by remember { mutableStateOf(false) }

    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            cacheInputState,
            label = { Text("Gradle cache path") },
            onValueChange = { cacheInputState = it },
            modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 1.dp),
            singleLine = true
        )
        OutlinedTextField(
            mavenInputState,
            label = { Text("Maven local repository path") },
            onValueChange = { mavenInputState = it },
            modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 1.dp),
            singleLine = true
        )

        Row(Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Checkbox(
                overwriteFiles,
                onCheckedChange = { overwriteFiles = it },
                Modifier.align(Alignment.CenterVertically).padding(end = 8.dp)
            )
            Text(
                "Overwrite files",
                Modifier.align(Alignment.CenterVertically),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Start
            )
        }

        Row(Modifier.fillMaxSize(), Arrangement.spacedBy(8.dp, Alignment.End)) {
            TextButton(onClick = { router(ScannerView) }, Modifier.align(Alignment.Bottom)) {
                Text("Cancel")
            }
            OutlinedButton(onClick = {
                saveSettings(cacheInputState.text, mavenInputState.text, overwriteFiles)
                router(ScannerView)
            }, Modifier.align(Alignment.Bottom)) {
                Text("Save")
            }
        }
    }
}

private fun saveSettings(gradlePath: String, mavenPath: String, overwriteFiles: Boolean) {

}