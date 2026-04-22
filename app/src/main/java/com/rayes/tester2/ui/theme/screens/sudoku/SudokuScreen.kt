package com.rayes.tester2.ui.theme.screens.sudoku

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuScreen(navController: NavController) {
    var board by remember { mutableStateOf(generateSudoku()) }
    var selectedCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var initialBoard by remember { mutableStateOf(board.map { it.toList() }) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sudoku Master", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        board = generateSudoku()
                        initialBoard = board.map { it.toList() }
                        selectedCell = null
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "New Game")
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Sudoku Grid
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
            ) {
                Column {
                    for (row in 0 until 9) {
                        Row {
                            for (col in 0 until 9) {
                                SudokuCell(
                                    value = board[row][col],
                                    isSelected = selectedCell == row to col,
                                    isInitial = initialBoard[row][col] != 0,
                                    row = row,
                                    col = col,
                                    onClick = { selectedCell = row to col }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Number Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (n in 1..9) {
                    NumberButton(
                        number = n,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedCell?.let { (r, c) ->
                                if (initialBoard[r][c] == 0) {
                                    val newBoard = board.map { it.toMutableList() }.toMutableList()
                                    newBoard[r][c] = n
                                    board = newBoard
                                }
                            }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    selectedCell?.let { (r, c) ->
                        if (initialBoard[r][c] == 0) {
                            val newBoard = board.map { it.toMutableList() }.toMutableList()
                            newBoard[r][c] = 0
                            board = newBoard
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Cell")
            }
        }
    }
}

@Composable
fun SudokuCell(value: Int, isSelected: Boolean, isInitial: Boolean, row: Int, col: Int, onClick: () -> Unit) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        (row / 3 + col / 3) % 2 == 0 -> MaterialTheme.colorScheme.surface
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(backgroundColor)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
            .then(
                if (col % 3 == 2 && col < 8) Modifier.padding(end = 2.dp) else Modifier
            )
            .then(
                if (row % 3 == 2 && row < 8) Modifier.padding(bottom = 2.dp) else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                text = "$value",
                fontSize = 18.sp,
                fontWeight = if (isInitial) FontWeight.Bold else FontWeight.Normal,
                color = if (isInitial) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun NumberButton(number: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = "$number", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private fun generateSudoku(): List<List<Int>> {
    val base = listOf(
        listOf(5, 3, 4, 6, 7, 8, 9, 1, 2),
        listOf(6, 7, 2, 1, 9, 5, 3, 4, 8),
        listOf(1, 9, 8, 3, 4, 2, 5, 6, 7),
        listOf(8, 5, 9, 7, 6, 1, 4, 2, 3),
        listOf(4, 2, 6, 8, 5, 3, 7, 9, 1),
        listOf(7, 1, 3, 9, 2, 4, 8, 5, 6),
        listOf(9, 6, 1, 5, 3, 7, 2, 8, 4),
        listOf(2, 8, 7, 4, 1, 9, 6, 3, 5),
        listOf(3, 4, 5, 2, 8, 6, 1, 7, 9)
    )
    
    return base.map { row ->
        row.map { if (Math.random() > 0.4) it else 0 }
    }
}
