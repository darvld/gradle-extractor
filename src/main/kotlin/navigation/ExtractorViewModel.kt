package navigation

import App
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import model.Artifact

class ExtractorViewModel(val artifacts: Array<Artifact>) {
    private val extractor = App.extractor
    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    private val _progress = MutableStateFlow(0f)
    val extractionProgress: StateFlow<Float> = _progress

    private val _message = MutableStateFlow("Artifact name")
    val message: StateFlow<String> = _message

    private var extractionJob: Job? = null

    private val _state = MutableStateFlow(ExtractorState.Ready)
    val state: StateFlow<ExtractorState> = _state

    fun cancelExtraction() {
        extractionJob?.cancel()
        extractionJob = null
    }

    fun extract() {
        extractionJob = viewModelScope.launch {
            _state.emit(ExtractorState.Working)
            var extracted = 0f
            val total = artifacts.size

            extractor.extract(artifacts).collect {
                extracted++
                _progress.emit(extracted / total)
                _message.emit(it.fullId)
            }

            _state.emit(ExtractorState.Finished)
            withContext(Dispatchers.Main) {
                extractionJob = null
            }
        }
    }
}