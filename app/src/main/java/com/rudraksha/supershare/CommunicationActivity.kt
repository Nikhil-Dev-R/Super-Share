package com.rudraksha.supershare

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudraksha.supershare.core.viewmodel.CommunicationViewModel
import com.rudraksha.supershare.ui.theme.SuperShareTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class CommunicationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperShareTheme {
                CommunicationScreen()
            }
        }
    }
}

@Composable
fun CommunicationScreen(viewModel: CommunicationViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Display Server status
        Text(text = "Server Status: ${viewModel.serverStatus.value}", color = Color.Gray)

        // Display Client status
        Text(text = "Client Status: ${viewModel.clientStatus.value}", color = Color.Gray)

        // Display received message
        Text(text = "Received: ${viewModel.messageReceived.value}", color = Color.Gray)

        // Start Server Button
        Button(
            onClick = { viewModel.startServer() },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Start Server")
        }

        // Connect as Client
        var serverIp = remember { TextFieldValue() }
        BasicTextField(
            value = serverIp,
            onValueChange = { serverIp = it },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Button(
            onClick = { viewModel.connectToServer(serverIp.text) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Connect as Client")
        }

        // Send Message Button
        var message = remember { TextFieldValue() }
        BasicTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Button(
            onClick = {
                viewModel.sendMessage(message.text)
                message = TextFieldValue("") // Clear the input after sending
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Send Message")
        }

        // Stop Server Button
        Button(
            onClick = { viewModel.stopServer() },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Stop Server")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommunicationScreen() {
    SuperShareTheme {
        CommunicationScreen()
    }
}
