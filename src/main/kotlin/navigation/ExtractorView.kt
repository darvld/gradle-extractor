package navigation

import App
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import model.Artifact
import navigation.ExtractorState.*
import ui.scan.collectValueAsState
import ui.scan.thinBorder
import kotlin.math.roundToInt

enum class ExtractorState { Ready, Working, Finished }

class ExtractorView(artifacts: Array<Artifact>) : Destination {
    private val viewModel by lazy {
        ExtractorViewModel(artifacts)
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun navigate(onRoute: Router): Content = @Composable {
        Crossfade(viewModel.state.collectValueAsState()) {
            when (it) {
                Ready -> {
                    confirmationScreen(onRoute)
                }
                Working -> {
                    extractionProgress(onRoute)
                }
                Finished -> {
                    finishScreen(onRoute)
                }
            }
        }

    }

    @Composable
    private fun confirmationScreen(router: Router) {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier.padding(24.dp).thinBorder().align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val amount = remember { viewModel.artifacts.size }
                val destination = remember { App.extractor.mavenPath.toString() }
                Text(
                    "Extract $amount artifacts into $destination?",
                    modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp)
                )

                Row(Modifier.padding(24.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { router(SettingsView) }) {
                        Text("Change extraction settings")
                    }

                    OutlinedButton(onClick = { router(ScannerView) }) {
                        Text("Cancel")
                    }

                    Button(onClick = { viewModel.extract() }) {
                        Text("Extract")
                    }
                }
            }
        }
    }

    @Composable
    private fun finishScreen(router: Router) {
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        ) {

            Icon(
                Icons.Rounded.CheckCircle,
                null,
                Modifier.size(92.dp),
                tint = MaterialTheme.colors.primary
            )
            Text("Extraction Complete")

            OutlinedButton(onClick = { router(ScannerView) }) {
                Text("Finish")
            }
        }
    }

    @Composable
    private fun extractionProgress(onRoute: Router) {
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
        ) {

            val progress = viewModel.extractionProgress.collectValueAsState()
            val progressString by mutableStateOf("${(progress * 100).roundToInt()}%")

            Box(Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp)) {
                CircularProgressIndicator(progress, Modifier.size(96.dp))
                Text(progressString, Modifier.align(Alignment.Center))
            }
            Text(
                "Extracting artifacts",
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                viewModel.message.collectValueAsState(),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.alpha(0.5f)
            )

            Row(
                Modifier.height(128.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = { viewModel.cancelExtraction(); onRoute(ScannerView) }) {
                    Text("Cancel")
                }
            }
        }
    }
}