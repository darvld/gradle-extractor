package ui.scan

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.flow.StateFlow
import model.Artifact
import navigation.ExtractorView
import navigation.Router
import navigation.SettingsView

@Composable
inline fun <reified T> StateFlow<T>.collectValueAsState(): T = collectAsState().value

@Composable
fun Modifier.thinBorder(): Modifier {
    return border(Dp.Hairline, MaterialTheme.colors.onSurface.copy(0.1f), RoundedCornerShape(5.dp))
}

@OptIn(ExperimentalAnimationApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ScannerUi(viewModel: ScannerViewModel, router: Router) {
    val artifacts = viewModel.artifacts.collectValueAsState()
    val scanning = viewModel.scanning.collectValueAsState()

    // Root
    Column(Modifier.padding(24.dp)) {
        // App name and icon
        Row(Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.End) {
            TitleBanner()
        }

        // Artifacts list
        Box(Modifier.padding(vertical = 16.dp).thinBorder().height(240.dp).fillMaxWidth()) {
            // Switch between empty initial screen and the actual list
            Crossfade(artifacts.isEmpty()) { noArtifacts ->
                if (noArtifacts) {
                    // Empty screen hint
                    ScanHint(viewModel, scanning, router)
                    return@Crossfade
                }

                // Artifacts list
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(artifacts, key = { artifact -> artifact.path }) {
                        ArtifactListItem(it)
                    }
                }
            }

            // Scan progress card (hides automatically)
            ScanProgress(
                viewModel.scanning.collectValueAsState(),
                artifacts.size,
                Modifier.align(Alignment.TopEnd).zIndex(1f).offset((-8).dp, 8.dp)
            )
        }

        // Scan and Extract buttons, hidden if no artifacts are found
        Crossfade(artifacts.isEmpty(), Modifier.align(Alignment.End)) { noArtifacts ->
            if (noArtifacts) return@Crossfade
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)) {
                // Scan
                OutlinedButton(
                    onClick = { viewModel.triggerScan() },
                    enabled = !scanning,
                    modifier = Modifier.width(48.dp)
                ) {
                    Icon(Icons.Default.Refresh, null, Modifier.size(16.dp))
                }

                // Settings
                OutlinedButton(
                    onClick = { router(SettingsView) },
                    enabled = !scanning,
                ) {
                    Text("Settings")
                }

                // Extract
                OutlinedButton(
                    onClick = { router(ExtractorView(viewModel.artifacts.value)) },
                    enabled = !scanning,
                ) {
                    Text("Extract")
                }
            }
        }
    }
}

@Composable
private fun ScanHint(viewModel: ScannerViewModel, scanning: Boolean, router: Router) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Click \"Scan\" to search for artifacts",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.alpha(0.6f)
        )
        Text(
            "You can change the gradle cache directory in the preferences",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.alpha(0.3f)
        )
        Row(Modifier.padding(top = 8.dp), Arrangement.spacedBy(8.dp)) {

            OutlinedButton(
                onClick = { router(SettingsView) },
                enabled = !scanning,
            ) {
                Text("Settings")
            }

            OutlinedButton(
                onClick = { viewModel.triggerScan() },
                enabled = !scanning,
            ) {
                Text("Scan")
            }
        }
    }
}

@Composable
fun ArtifactListItem(artifact: Artifact) = Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
    Box(
        Modifier.thinBorder()
    ) {
        Text(
            artifact.groupId,
            Modifier.padding(8.dp),
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Light
        )
    }
    Box(
        Modifier.thinBorder()
    ) {
        Text(
            artifact.id,
            Modifier.padding(8.dp),
            style = MaterialTheme.typography.subtitle2,
        )
    }
    Box(
        Modifier.thinBorder().align(Alignment.CenterVertically)
    ) {
        Text(
            artifact.version,
            Modifier.padding(8.dp),
            style = MaterialTheme.typography.overline,
        )
    }
}

@Composable
fun ScanProgress(busy: Boolean, artifactsFound: Int, modifier: Modifier = Modifier) {
    Crossfade(
        modifier = modifier
            .wrapContentWidth()
            .thinBorder()
            .background(MaterialTheme.colors.surface, RoundedCornerShape(10)),
        targetState = busy
    ) {
        if (!it) {
            return@Crossfade
        }

        Row(
            Modifier.padding(8.dp)
        ) {
            Column(
                Modifier.align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    "Scanning",
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.End,
                )
                Text(
                    "$artifactsFound artifacts found",
                    style = MaterialTheme.typography.overline,
                    modifier = Modifier.alpha(0.4f),
                    textAlign = TextAlign.End
                )
            }

            CircularProgressIndicator(
                Modifier.size(28.dp)
                    .padding(start = 6.dp, top = 2.dp)
                    .align(Alignment.CenterVertically),
                strokeWidth = 3.dp
            )
        }

    }
}

@Composable
fun RowScope.TitleBanner() = Column(Modifier.fillMaxWidth().weight(2f)) {
    Text("Gradle Extractor", style = MaterialTheme.typography.h6, modifier = Modifier.padding(bottom = 4.dp))
    Text("v0.1", style = MaterialTheme.typography.subtitle2, modifier = Modifier.alpha(0.5f))
}