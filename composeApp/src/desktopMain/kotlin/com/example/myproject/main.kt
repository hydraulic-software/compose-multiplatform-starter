package com.example.myproject

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState

import dev.hydraulic.conveyor.control.SoftwareUpdateController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.inputStream

val updateController: SoftwareUpdateController? = SoftwareUpdateController.getInstance()
val canDoOnlineUpdates get() = updateController?.canTriggerUpdateCheckUI() == SoftwareUpdateController.Availability.AVAILABLE

fun main(args: Array<String>) = application {
    // app.dir is set when packaged to point at the inputs collected by Conveyor.
    val appIcon = remember {
        System.getProperty("app.dir")
            ?.let { Paths.get(it, "icon-512.png") }
            ?.takeIf { it.exists() }
            ?.inputStream()
            ?.buffered()
            ?.use { BitmapPainter(loadImageBitmap(it)) }
    }

    val windowState = remember { WindowState(size = DpSize(800.dp, 900.dp)) }
    val currentVersion by remember { mutableStateOf(updateController?.currentVersion?.version ?: "Unknown") }
    Window(onCloseRequest = ::exitApplication, icon = appIcon, title = "Software Update Demo $currentVersion", state = windowState) {
        var remoteVersion by remember { mutableStateOf("Checking...") }
        var updateAvailable by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val remoteVersionObj: SoftwareUpdateController.Version? = updateController?.currentVersionFromRepository
                    remoteVersion = remoteVersionObj?.version ?: "Unknown"
                    updateAvailable = (remoteVersionObj?.compareTo(updateController.currentVersion) ?: 0) > 0
                } catch (e: Exception) {
                    remoteVersion = "Error: ${e.message}"
                }
                println("Remote app version is $remoteVersion" + if (updateAvailable) " (update available)" else "")
            }
        }

        App(
            currentVersion = currentVersion,
            remoteVersion = remoteVersion,
            updateAvailable = updateAvailable
        )
    }
}
