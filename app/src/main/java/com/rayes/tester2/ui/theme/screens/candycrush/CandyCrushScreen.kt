package com.rayes.tester2.ui.theme.screens.candycrush

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class CandyType(val color: Color) {
    RED(Color(0xFFFF5252)),
    BLUE(Color(0xFF448AFF)),
    GREEN(Color(0xFF69F0AE)),
    YELLOW(Color(0xFFFFE57F)),
    PURPLE(Color(0xFFE040FB)),
    ORANGE(Color(0xFFFFAB40))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandyCrushScreen(navController: NavController) {
    var score by remember { mutableStateOf(0) }
    val gridSize = 6
    var grid by remember { mutableStateOf(List(gridSize) { List(gridSize) { CandyType.values().random() } }) }
    var selectedPosition by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    // Logic to check and remove matches
    LaunchedEffect(grid) {
        if (isProcessing) return@LaunchedEffect
        val matches = findMatches(grid)
        if (matches.isNotEmpty()) {
            isProcessing = true
            delay(500)
            score += matches.size * 10
            grid = removeAndRefill(grid, matches)
            isProcessing = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Candy Crush Saga", fontWeight = FontWeight.Bold) },
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
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            // Score Board
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Score", style = MaterialTheme.typography.labelLarge)
                    Text("$score", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Game Grid
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    grid.forEachIndexed { rowIndex, row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            row.forEachIndexed { colIndex, candy ->
                                CandyItem(
                                    candy = candy,
                                    isSelected = selectedPosition == rowIndex to colIndex,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        if (isProcessing) return@CandyItem
                                        if (selectedPosition == null) {
                                            selectedPosition = rowIndex to colIndex
                                        } else {
                                            val (r1, c1) = selectedPosition!!
                                            if (isAdjacent(r1, c1, rowIndex, colIndex)) {
                                                grid = swap(grid, r1, c1, rowIndex, colIndex)
                                                selectedPosition = null
                                            } else {
                                                selectedPosition = rowIndex to colIndex
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            Text("Swap adjacent candies to match 3 or more!", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun CandyItem(candy: CandyType, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val scale by animateFloatAsState(if (isSelected) 1.15f else 1f)
    
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(candy.color.copy(alpha = 0.8f), candy.color)
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Candy Shine effect
        Box(
            modifier = Modifier
                .size(12.dp)
                .align(Alignment.TopStart)
                .offset(4.dp, 4.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.4f))
        )
    }
}

private fun isAdjacent(r1: Int, c1: Int, r2: Int, c2: Int): Boolean {
    return (Math.abs(r1 - r2) == 1 && c1 == c2) || (Math.abs(c1 - c2) == 1 && r1 == r2)
}

private fun swap(grid: List<List<CandyType>>, r1: Int, c1: Int, r2: Int, c2: Int): List<List<CandyType>> {
    val newGrid = grid.map { it.toMutableList() }.toMutableList()
    val temp = newGrid[r1][c1]
    newGrid[r1][c1] = newGrid[r2][c2]
    newGrid[r2][c2] = temp
    return newGrid
}

private fun findMatches(grid: List<List<CandyType>>): Set<Pair<Int, Int>> {
    val matches = mutableSetOf<Pair<Int, Int>>()
    val size = grid.size

    // Horizontal
    for (r in 0 until size) {
        var count = 1
        for (c in 1 until size) {
            if (grid[r][c] == grid[r][c - 1]) {
                count++
            } else {
                if (count >= 3) {
                    for (i in 0 until count) matches.add(r to c - 1 - i)
                }
                count = 1
            }
        }
        if (count >= 3) {
            for (i in 0 until count) matches.add(r to size - 1 - i)
        }
    }

    // Vertical
    for (c in 0 until size) {
        var count = 1
        for (r in 1 until size) {
            if (grid[r][c] == grid[r - 1][c]) {
                count++
            } else {
                if (count >= 3) {
                    for (i in 0 until count) matches.add(r - 1 - i to c)
                }
                count = 1
            }
        }
        if (count >= 3) {
            for (i in 0 until count) matches.add(size - 1 - i to c)
        }
    }
    return matches
}

private fun removeAndRefill(grid: List<List<CandyType>>, matches: Set<Pair<Int, Int>>): List<List<CandyType>> {
    val size = grid.size
    val newGrid = grid.map { it.toMutableList() }.toMutableList()

    for (c in 0 until size) {
        var emptyCount = 0
        for (r in size - 1 downTo 0) {
            if (matches.contains(r to c)) {
                emptyCount++
            } else if (emptyCount > 0) {
                newGrid[r + emptyCount][c] = newGrid[r][c]
            }
        }
        for (r in 0 until emptyCount) {
            newGrid[r][c] = CandyType.values().random()
        }
    }
    return newGrid
}
