package com.rayes.tester2.ui.theme.screens.spaceshooter

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random

data class Bullet(var x: Float, var y: Float, val type: BulletType = BulletType.NORMAL)
enum class BulletType { NORMAL, POWERED }
data class Enemy(var x: Float, var y: Float, val speed: Float, var health: Int, val type: EnemyType)
enum class EnemyType { SCOUT, BOMBER, BOSS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaceShooterScreen(navController: NavController) {
    var isPlaying by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var playerX by remember { mutableStateOf(500f) }
    var playerY by remember { mutableStateOf(1600f) }
    val bullets = remember { mutableStateListOf<Bullet>() }
    val enemies = remember { mutableStateListOf<Enemy>() }
    var gameOver by remember { mutableStateOf(false) }
    var level by remember { mutableStateOf(1) }

    val infiniteTransition = rememberInfiniteTransition()
    val starOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing), RepeatMode.Restart)
    )

    // Game Loop
    LaunchedEffect(isPlaying) {
        if (!isPlaying) return@LaunchedEffect
        while (!gameOver) {
            delay(16)
            
            // Move Bullets
            bullets.forEach { it.y -= 25f }
            bullets.removeAll { it.y < -100f }

            // Move Enemies
            enemies.forEach { 
                it.y += it.speed 
                if (it.type == EnemyType.BOMBER) {
                    it.x += sin(it.y / 50f) * 5f
                }
            }
            
            // Spawn Enemies
            if (Random.nextFloat() < (0.03f + level * 0.005f)) {
                val type = when {
                    Random.nextFloat() < 0.1f -> EnemyType.BOMBER
                    else -> EnemyType.SCOUT
                }
                enemies.add(Enemy(
                    x = Random.nextFloat() * 1000f, 
                    y = -100f, 
                    speed = 4f + Random.nextFloat() * 4f + level,
                    health = if (type == EnemyType.BOMBER) 3 else 1,
                    type = type
                ))
            }

            // Collision Detection
            val bulletsToRemove = mutableListOf<Bullet>()
            val enemiesToRemove = mutableListOf<Enemy>()

            enemies.forEach { enemy ->
                if (Math.abs(enemy.x - playerX) < 70f && Math.abs(enemy.y - playerY) < 70f) {
                    gameOver = true
                }
                
                bullets.forEach { bullet ->
                    if (Math.abs(enemy.x - bullet.x) < 60f && Math.abs(enemy.y - bullet.y) < 60f) {
                        enemy.health--
                        bulletsToRemove.add(bullet)
                        if (enemy.health <= 0) {
                            enemiesToRemove.add(enemy)
                            score += if (enemy.type == EnemyType.BOMBER) 500 else 100
                        }
                    }
                }
                
                if (enemy.y > 2200f) enemiesToRemove.add(enemy)
            }

            enemies.removeAll(enemiesToRemove)
            bullets.removeAll(bulletsToRemove)

            if (score > level * 5000) level++

            // Rapid Fire (Assault Style)
            if (System.currentTimeMillis() % 150 < 20) {
                bullets.add(Bullet(playerX - 25f, playerY - 40f))
                bullets.add(Bullet(playerX + 25f, playerY - 40f))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thunder Assault", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF020617))
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        playerX = (playerX + dragAmount.x).coerceIn(0f, size.width.toFloat())
                        playerY = (playerY + dragAmount.y).coerceIn(size.height * 0.5f, size.height.toFloat())
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Moving Background Stars
                repeat(40) { i ->
                    val y = (starOffset + i * 100) % size.height
                    drawCircle(Color.White.copy(alpha = 0.4f), radius = 2f, center = Offset(Random(i).nextFloat() * size.width, y))
                }

                // Draw Bullets (Glowing Lasers)
                bullets.forEach { bullet ->
                    drawRect(
                        brush = Brush.verticalGradient(listOf(Color.Cyan, Color.Transparent)),
                        topLeft = Offset(bullet.x - 3f, bullet.y),
                        size = Size(6f, 40f)
                    )
                }

                // Draw Enemies
                enemies.forEach { enemy ->
                    val color = if (enemy.type == EnemyType.BOMBER) Color(0xFFF43F5E) else Color(0xFFFB923C)
                    withTransform({ translate(enemy.x, enemy.y) }) {
                        val path = Path().apply {
                            moveTo(0f, 30f)
                            lineTo(-40f, -20f)
                            lineTo(40f, -20f)
                            close()
                        }
                        drawPath(path, color)
                        drawCircle(Color.White.copy(alpha = 0.5f), 10f, Offset(0f, 0f))
                    }
                }

                // Draw Player (Thunder Assault Fighter)
                withTransform({ translate(playerX, playerY) }) {
                    // Engine Glow
                    drawCircle(
                        Brush.radialGradient(listOf(Color.Cyan, Color.Transparent)),
                        radius = 40f,
                        center = Offset(0f, 40f)
                    )
                    // Wings
                    drawRect(Color(0xFF60A5FA), Offset(-50f, 10f), Size(100f, 15f))
                    drawRect(Color(0xFF2563EB), Offset(-60f, 20f), Size(20f, 30f))
                    drawRect(Color(0xFF2563EB), Offset(40f, 20f), Size(20f, 30f))
                    // Fuselage
                    val shipPath = Path().apply {
                        moveTo(0f, -60f)
                        lineTo(-20f, 40f)
                        lineTo(20f, 40f)
                        close()
                    }
                    drawPath(shipPath, Color.White)
                    drawRect(Color(0xFF93C5FD), Offset(-10f, -20f), Size(20f, 40f))
                }
            }

            // High-End HUD
            Column(modifier = Modifier.padding(20.dp).padding(paddingValues)) {
                Text("SCORE: $score", color = Color.Cyan, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text("LEVEL: $level", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            }

            if (!isPlaying || gameOver) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.85f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            if (gameOver) "MISSION FAILED" else "THUNDER ASSAULT",
                            color = if (gameOver) Color.Red else Color.Cyan,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                        if (gameOver) {
                            Text("Final Score: $score", color = Color.White, modifier = Modifier.padding(8.dp))
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                        Button(
                            onClick = {
                                score = 0; level = 1
                                bullets.clear(); enemies.clear()
                                gameOver = false; isPlaying = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan, contentColor = Color.Black)
                        ) {
                            Text("ENGAGE ENEMY", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
