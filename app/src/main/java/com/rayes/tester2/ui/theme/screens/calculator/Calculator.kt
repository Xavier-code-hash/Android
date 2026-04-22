package com.rayes.tester2.ui.theme.screens.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import java.util.Locale
import kotlin.math.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calculator(navController: NavHostController) {
    var display by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scientific Calculator", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            // Display Area
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = expression,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = display,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Keypad
            Column(
                modifier = Modifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Scientific Row 1
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SciButton("sin", Modifier.weight(1f)) { display = String.format(Locale.US, "%.4f", sin(display.toDoubleOrNull() ?: 0.0)) }
                    SciButton("cos", Modifier.weight(1f)) { display = String.format(Locale.US, "%.4f", cos(display.toDoubleOrNull() ?: 0.0)) }
                    SciButton("tan", Modifier.weight(1f)) { display = String.format(Locale.US, "%.4f", tan(display.toDoubleOrNull() ?: 0.0)) }
                    SciButton("log", Modifier.weight(1f)) { display = String.format(Locale.US, "%.4f", log10(display.toDoubleOrNull() ?: 1.0)) }
                }

                // Scientific Row 2
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SciButton("√", Modifier.weight(1f)) { display = String.format(Locale.US, "%.4f", sqrt(display.toDoubleOrNull() ?: 0.0)) }
                    SciButton("x²", Modifier.weight(1f)) { val d = display.toDoubleOrNull() ?: 0.0; display = (d * d).toString() }
                    SciButton("π", Modifier.weight(1f)) { display = PI.toString() }
                    SciButton("e", Modifier.weight(1f)) { display = E.toString() }
                }

                // Standard Keys
                val gridButtons = listOf(
                    listOf("C", "DEL", "%", "/"),
                    listOf("7", "8", "9", "*"),
                    listOf("4", "5", "6", "-"),
                    listOf("1", "2", "3", "+"),
                    listOf("0", ".", "=", "")
                )

                var operand1 by remember { mutableStateOf(0.0) }
                var activeOperator by remember { mutableStateOf("") }
                var isNewNumber by remember { mutableStateOf(true) }

                gridButtons.forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { label ->
                            if (label.isNotEmpty()) {
                                CalcButton(
                                    label = label,
                                    modifier = Modifier.weight(1f),
                                    containerColor = when {
                                        label == "C" -> MaterialTheme.colorScheme.errorContainer
                                        label in listOf("/", "*", "-", "+", "=") -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    contentColor = when {
                                        label == "C" -> MaterialTheme.colorScheme.onErrorContainer
                                        label in listOf("/", "*", "-", "+", "=") -> MaterialTheme.colorScheme.onPrimary
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                ) {
                                    when (label) {
                                        "C" -> {
                                            display = "0"
                                            expression = ""
                                            operand1 = 0.0
                                            activeOperator = ""
                                        }
                                        "DEL" -> {
                                            if (display.length > 1) display = display.dropLast(1)
                                            else display = "0"
                                        }
                                        in "0".."9", "." -> {
                                            if (isNewNumber) {
                                                display = if (label == ".") "0." else label
                                                isNewNumber = false
                                            } else {
                                                if (label == "." && display.contains(".")) return@CalcButton
                                                display += label
                                            }
                                        }
                                        in listOf("+", "-", "*", "/", "%") -> {
                                            operand1 = display.toDoubleOrNull() ?: 0.0
                                            activeOperator = label
                                            expression = "$display $label"
                                            isNewNumber = true
                                        }
                                        "=" -> {
                                            val operand2 = display.toDoubleOrNull() ?: 0.0
                                            val result = when (activeOperator) {
                                                "+" -> operand1 + operand2
                                                "-" -> operand1 - operand2
                                                "*" -> operand1 * operand2
                                                "/" -> if (operand2 != 0.0) operand1 / operand2 else Double.NaN
                                                "%" -> operand1 % operand2
                                                else -> operand2
                                            }
                                            expression = ""
                                            display = if (result.isNaN()) "Error" else result.toString().removeSuffix(".0")
                                            isNewNumber = true
                                        }
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SciButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CalcButton(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = label, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorView() {
    Calculator(navController = rememberNavController())
}
