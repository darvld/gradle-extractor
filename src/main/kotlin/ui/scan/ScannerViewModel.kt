package ui.scan

import App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import model.Artifact

class ScannerViewModel {
    private val scanner = App.scanner
    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    private val _artifacts = MutableStateFlow(emptyArray<Artifact>())
    val artifacts: StateFlow<Array<Artifact>> = _artifacts

    private val _scanning = MutableStateFlow(false)
    val scanning: StateFlow<Boolean> = _scanning

    fun triggerScan() = viewModelScope.launch {
        _scanning.emit(true)
        val cache = mutableListOf<Artifact>()

        scanner.scan().collect {
            cache.add(it)
            _artifacts.value = cache.toTypedArray()
        }

        _scanning.emit(false)
    }
}