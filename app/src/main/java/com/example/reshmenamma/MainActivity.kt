package com.example.reshmenamma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReshmeApp()
        }
    }
}

@Composable
fun ReshmeApp() {

    var temp by remember { mutableStateOf("") }
    var humidity by remember { mutableStateOf("") }
    var selectedInstar by remember { mutableStateOf(0) }

    var result by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.Gray) }

    val instarStages = listOf(
        "Instar 1", "Instar 2", "Instar 3", "Instar 4", "Instar 5"
    )

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = temp,
            onValueChange = { temp = it },
            label = { Text("Temperature (°C)") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = humidity,
            onValueChange = { humidity = it },
            label = { Text("Humidity (%)") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text("Select Instar Stage")

        instarStages.forEachIndexed { index, stage ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedInstar == index,
                    onClick = { selectedInstar = index }
                )
                Text(stage)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {

            val t = temp.toFloatOrNull()
            val h = humidity.toFloatOrNull()

            // Validation
            if (t == null || h == null) {
                result = "Enter valid values"
                color = Color.Gray
                return@Button
            }

            if (t !in 10f..45f) {
                result = "Temp must be 10–45°C"
                color = Color.Red
                return@Button
            }

            if (h !in 0f..100f) {
                result = "Humidity must be 0–100%"
                color = Color.Red
                return@Button
            }

            // Ideal values per instar
            val (tMin, tMax, hMin, hMax) = when (selectedInstar) {
                0 -> listOf(26, 28, 85, 90)
                1 -> listOf(25, 27, 80, 85)
                2 -> listOf(24, 26, 75, 80)
                3 -> listOf(23, 25, 70, 75)
                else -> listOf(22, 24, 65, 70)
            }

            // Logic
            if (t in tMin.toFloat()..tMax.toFloat() &&
                h in hMin.toFloat()..hMax.toFloat()
            ) {
                result = "Ideal Conditions"
                color = Color.Green
            } else if (
                t in (tMin - 2).toFloat()..(tMax + 2).toFloat() ||
                h in (hMin - 5).toFloat()..(hMax + 5).toFloat()
            ) {
                result = "Caution: Adjust slightly"
                color = Color(0xFFFFA500) // Orange
            } else {
                result = "Danger! Take action"
                color = Color.Red
            }

        }) {
            Text("Check Condition")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Climate Dial (circle)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(result)
    }
}