package navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import ui.scan.ScannerUi
import ui.scan.ScannerViewModel

object ScannerView : Destination {
    private val scannerViewModel by lazy { ScannerViewModel() }

    @OptIn(ExperimentalMaterialApi::class)
    override fun navigate(onRoute: Router): Content = @Composable {
        ScannerUi(scannerViewModel, onRoute)
    }
}