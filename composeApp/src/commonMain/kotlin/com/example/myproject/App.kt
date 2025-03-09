package com.example.myproject


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import my_project.composeapp.generated.resources.Res
import my_project.composeapp.generated.resources.compose_multiplatform

expect fun showUpdateInfo(): Boolean

@Composable
fun UpdateInfoDisplay(currentVersion: String, remoteVersion: String, updateAvailable: Boolean) {
    if (showUpdateInfo()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Current version: $currentVersion")
            Text("Latest version: $remoteVersion")
            if (updateAvailable) {
                Text("Update available!", color = MaterialTheme.colors.primary)
            }
        }
    }
}

@Composable
@Preview
fun App(
    currentVersion: String = "Unknown",
    remoteVersion: String = "Unknown",
    updateAvailable: Boolean = false
) {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            UpdateInfoDisplay(currentVersion, remoteVersion, updateAvailable)

            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}
