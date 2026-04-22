package com.rayes.tester2.ui.theme.screens.mickeyadventures

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MickeyAdventuresScreen(navController: NavController) {
    var score by remember { mutableIntStateOf(0) }
    var gameActive by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(30) }
    
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.toFloat()
    val screenHeight = configuration.screenHeightDp.toFloat()
    
    var mickeyPosition by remember { mutableStateOf(Offset(screenWidth / 2, screenHeight / 2)) }

    // Game Loop
    LaunchedEffect(gameActive) {
        if (gameActive) {
            score = 0
            timeLeft = 30
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            gameActive = false
        }
    }

    // Mickey Movement
    LaunchedEffect(gameActive) {
        if (gameActive) {
            while (gameActive) {
                val nextX = Random.nextFloat() * (screenWidth - 100f)
                val nextY = Random.nextFloat() * (screenHeight - 250f)
                mickeyPosition = Offset(nextX, nextY)
                delay(900)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mickey's Adventure", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(listOf(Color(0xFFB3E5FC), Color(0xFFE1F5FE))))
        ) {
            if (!gameActive) {
                // Start/Game Over Screen
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Mickey's Adventure",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFD32F2F)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Catch Mickey to score points!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    if (timeLeft == 0) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Game Over!", fontWeight = FontWeight.Bold)
                                Text("Final Score: $score", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        onClick = { gameActive = true },
                        modifier = Modifier.height(64.dp).width(240.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text(if (timeLeft == 30) "START MISSION" else "PLAY AGAIN", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // Game HUD
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("SCORE", style = MaterialTheme.typography.labelSmall)
                        Text("$score", fontSize = 28.sp, fontWeight = FontWeight.Black)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("TIME LEFT", style = MaterialTheme.typography.labelSmall)
                        Text("${timeLeft}s", fontSize = 28.sp, fontWeight = FontWeight.Black, color = if (timeLeft < 10) Color.Red else Color.Black)
                    }
                }

                // Mickey Character
                MickeyCharacter(
                    position = mickeyPosition,
                    onClick = {
                        score += 10
                        // Immediate jump to new position on click
                        val nextX = Random.nextFloat() * (screenWidth - 100f)
                        val nextY = Random.nextFloat() * (screenHeight - 250f)
                        mickeyPosition = Offset(nextX, nextY)
                    }
                )
            }
        }
    }
}

@Composable
fun MickeyCharacter(position: Offset, onClick: () -> Unit) {
    val animatedX by animateFloatAsState(targetValue = position.x, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    val animatedY by animateFloatAsState(targetValue = position.y, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))

    Canvas(
        modifier = Modifier
            .size(100.dp)
            .offset(x = animatedX.dp, y = animatedY.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onClick() })
            }
    ) {
        val radius = size.minDimension / 3.5f
        val center = Offset(size.width / 2, size.height / 2 + 10f)

        // Ears
        drawCircle(
            color = Color.Black,
            radius = radius * 0.75f,
            center = Offset(center.x - radius * 1.1f, center.y - radius * 1.1f)
        )
        drawCircle(
            color = Color.Black,
            radius = radius * 0.75f,
            center = Offset(center.x + radius * 1.1f, center.y - radius * 1.1f)
        )
        
        // Head
        drawCircle(color = Color.Black, radius = radius, center = center)

        // Face Detail (Skin color)
        drawCircle(
            color = Color(0xFFFFCCBC),
            radius = radius * 0.85f,
            center = Offset(center.x, center.y + 5f)
        )
        
        // Eyes
        drawCircle(color = Color.White, radius = radius * 0.2f, center = Offset(center.x - radius * 0.3f, center.y - radius * 0.1f))
        drawCircle(color = Color.White, radius = radius * 0.2f, center = Offset(center.x + radius * 0.3f, center.y - radius * 0.1f))
        
        // Pupils
        drawCircle(color = Color.Black, radius = radius * 0.08f, center = Offset(center.x - radius * 0.3f, center.y - radius * 0.1f))
        drawCircle(color = Color.Black, radius = radius * 0.08f, center = Offset(center.x + radius * 0.3f, center.y - radius * 0.1f))
        
        // Nose
        drawCircle(color = Color.Black, radius = radius * 0.12f, center = Offset(center.x, center.y + radius * 0.2f))
    }
}
