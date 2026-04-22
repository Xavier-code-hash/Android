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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
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
data class Enemy(var x: Float, var y: Float, val speed: Float, var health: Int, val type: EnemyType, var angle: Float = 0f)
enum class EnemyType { SCOUT, BOMBER, INTERCEPTOR }
data class Particle(var x: Float, var y: Float, var vx: Float, var vy: Float, var life: Float, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaceShooterScreen(navController: NavController) {
    var isPlaying by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var playerX by remember { mutableFloatStateOf(500f) }
    var playerY by remember { mutableFloatStateOf(1600f) }
    val bullets = remember { mutableStateListOf<Bullet>() }
    val enemies = remember { mutableStateListOf<Enemy>() }
    val particles = remember { mutableStateListOf<Particle>() }
    var gameOver by remember { mutableStateOf(false) }
    var level by remember { mutableIntStateOf(1) }
    var screenShake by remember { mutableFloatStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "SpaceBackground")
    val starOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing), RepeatMode.Restart),
        label = "StarLayer1"
    )
    val starOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
        label = "StarLayer2"
    )

    // Game Loop
    LaunchedEffect(isPlaying) {
        if (!isPlaying) return@LaunchedEffect
        while (!gameOver) {
            delay(16)
            
            if (screenShake > 0) screenShake *= 0.9f

            // Move Bullets
            bullets.forEach { it.y -= 30f }
            bullets.removeAll { it.y < -100f }

            // Move Enemies
            enemies.forEach { enemy ->
                enemy.y += enemy.speed
                when (enemy.type) {
                    EnemyType.BOMBER -> {
                        enemy.x += sin(enemy.y / 60f) * 8f
                    }
                    EnemyType.INTERCEPTOR -> {
                        val dx = playerX - enemy.x
                        enemy.x += dx * 0.02f
                    }
                    else -> {}
                }
            }
            
            // Move Particles
            particles.forEach { 
                it.x += it.vx
                it.y += it.vy
                it.life -= 0.02f
            }
            particles.removeAll { it.life <= 0 }

            // Spawn Enemies
            if (Random.nextFloat() < (0.04f + level * 0.005f)) {
                val type = when {
                    Random.nextFloat() < 0.1f -> EnemyType.BOMBER
                    Random.nextFloat() < 0.2f -> EnemyType.INTERCEPTOR
                    else -> EnemyType.SCOUT
                }
                enemies.add(Enemy(
                    x = Random.nextFloat() * 1000f, 
                    y = -100f, 
                    speed = 5f + Random.nextFloat() * 3f + (level * 0.5f),
                    health = when(type) {
                        EnemyType.BOMBER -> 4
                        EnemyType.INTERCEPTOR -> 2
                        else -> 1
                    },
                    type = type
                ))
            }

            // Collision Detection
            val bulletsToRemove = mutableListOf<Bullet>()
            val enemiesToRemove = mutableListOf<Enemy>()

            enemies.forEach { enemy ->
                // Player collision
                if (abs(enemy.x - playerX) < 80f && abs(enemy.y - playerY) < 80f) {
                    gameOver = true
                    createExplosion(playerX, playerY, Color.Cyan, particles)
                }
                
                // Bullet collision
                bullets.forEach { bullet ->
                    if (abs(enemy.x - bullet.x) < 70f && abs(enemy.y - bullet.y) < 70f) {
                        enemy.health--
                        bulletsToRemove.add(bullet)
                        if (enemy.health <= 0) {
                            enemiesToRemove.add(enemy)
                            score += when(enemy.type) {
                                EnemyType.BOMBER -> 800
                                EnemyType.INTERCEPTOR -> 400
                                else -> 150
                            }
                            createExplosion(enemy.x, enemy.y, Color(0xFFFF9800), particles)
                            screenShake = 0.5f
                        }
                    }
                }
                
                if (enemy.y > 2500f) enemiesToRemove.add(enemy)
            }

            enemies.removeAll(enemiesToRemove)
            bullets.removeAll(bulletsToRemove)

            if (score > level * 8000) level++

            // Auto-fire
            if (System.currentTimeMillis() % 120 < 20) {
                bullets.add(Bullet(playerX - 30f, playerY - 50f))
                bullets.add(Bullet(playerX + 30f, playerY - 50f))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GALAXY COMMAND", fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.3f),
                    titleContentColor = Color.Cyan
                )
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
                        playerY = (playerY + dragAmount.y).coerceIn(size.height * 0.2f, size.height.toFloat())
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val shakeX = (Random.nextFloat() - 0.5f) * screenShake * 30
                val shakeY = (Random.nextFloat() - 0.5f) * screenShake * 30

                withTransform({ translate(shakeX, shakeY) }) {
                    // Deep Space Background (Stars Layer 1 - Slow)
                    repeat(60) { i ->
                        val y = (starOffset1 + i * 150) % size.height
                        drawCircle(Color.White.copy(alpha = 0.2f), radius = 1.5f, center = Offset(Random(i).nextFloat() * size.width, y))
                    }

                    // Near Space Background (Stars Layer 2 - Fast)
                    repeat(30) { i ->
                        val y = (starOffset2 + i * 250) % size.height
                        drawCircle(Color.White.copy(alpha = 0.5f), radius = 2.5f, center = Offset(Random(i + 100).nextFloat() * size.width, y))
                    }

                    // Draw Particles (Explosions)
                    particles.forEach { p ->
                        drawCircle(p.color.copy(alpha = p.life), radius = 4f * p.life, center = Offset(p.x, p.y))
                    }

                    // Draw Bullets (Plasma Bolts)
                    bullets.forEach { bullet ->
                        drawRoundRect(
                            brush = Brush.verticalGradient(listOf(Color.White, Color.Cyan, Color.Transparent)),
                            topLeft = Offset(bullet.x - 4f, bullet.y),
                            size = Size(8f, 50f),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    }

                    // Draw Enemies
                    enemies.forEach { enemy ->
                        drawEnemyShip(enemy)
                    }

                    // Draw Player Ship
                    drawPlayerShip(playerX, playerY)
                }
            }

            // Futuristic HUD
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(20.dp)) {
                Column(modifier = Modifier.align(Alignment.TopStart)) {
                    Text("SYSTEM STATUS: OPTIMAL", color = Color.Green.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("SCORE", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                    Text(score.toString().padStart(6, '0'), color = Color.Cyan, fontSize = 28.sp, fontWeight = FontWeight.Black)
                }
                
                Column(modifier = Modifier.align(Alignment.TopEnd), horizontalAlignment = Alignment.End) {
                    Text("SECTOR", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                    Text(level.toString(), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                }

                // Decorative Corner Accents
                Box(modifier = Modifier.size(40.dp).align(Alignment.BottomStart).alpha(0.2f).background(Brush.linearGradient(listOf(Color.Cyan, Color.Transparent)), shape = RoundedCornerShape(topEnd = 40.dp)))
                Box(modifier = Modifier.size(40.dp).align(Alignment.BottomEnd).alpha(0.2f).background(Brush.linearGradient(listOf(Color.Transparent, Color.Cyan)), shape = RoundedCornerShape(topStart = 40.dp)))
            }

            if (!isPlaying || gameOver) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.9f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            if (gameOver) "MISSION ABORTED" else "GALAXY COMMAND",
                            color = if (gameOver) Color(0xFFF43F5E) else Color.Cyan,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        
                        if (gameOver) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("COMMUNICATIONS LOST", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                            Text("SCORE: $score", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                        } else {
                            Text("READY FOR DEPLOYMENT", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(60.dp))
                        
                        Button(
                            onClick = {
                                score = 0; level = 1
                                bullets.clear(); enemies.clear(); particles.clear()
                                gameOver = false; isPlaying = true
                            },
                            modifier = Modifier.fillMaxWidth().height(64.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan, contentColor = Color.Black)
                        ) {
                            Text(if (gameOver) "REDEPLOY" else "INITIALIZE DRIVE", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        }
                        
                        if (gameOver) {
                            TextButton(onClick = { isPlaying = false; gameOver = false }) {
                                Text("RETURN TO BASE", color = Color.White.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }
    }
}

fun createExplosion(x: Float, y: Float, color: Color, particles: MutableList<Particle>) {
    repeat(15) {
        val angle = Random.nextFloat() * 2 * PI.toFloat()
        val speed = Random.nextFloat() * 10f + 5f
        particles.add(Particle(
            x = x, y = y,
            vx = cos(angle) * speed,
            vy = sin(angle) * speed,
            life = 1f,
            color = color
        ))
    }
}

fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPlayerShip(x: Float, y: Float) {
    withTransform({ translate(x, y) }) {
        // Engine Glow (Pulsing)
        drawCircle(
            brush = Brush.radialGradient(listOf(Color.Cyan.copy(alpha = 0.6f), Color.Transparent)),
            radius = 50f,
            center = Offset(0f, 45f)
        )
        
        // Ship Body (Metallic Gradient)
        val bodyBrush = Brush.verticalGradient(listOf(Color.White, Color(0xFF94A3B8)))
        
        // Main Wings
        val wingPath = Path().apply {
            moveTo(-70f, 30f)
            lineTo(0f, -20f)
            lineTo(70f, 30f)
            lineTo(0f, 10f)
            close()
        }
        drawPath(wingPath, bodyBrush)
        
        // Fuselage
        val shipPath = Path().apply {
            moveTo(0f, -70f)
            lineTo(-25f, 40f)
            lineTo(0f, 25f)
            lineTo(25f, 40f)
            close()
        }
        drawPath(shipPath, Color.White)
        
        // Cockpit
        drawOval(
            color = Color(0xFF0EA5E9),
            topLeft = Offset(-10f, -25f),
            size = Size(20f, 40f)
        )
        
        // Laser Cannons
        drawRect(Color.LightGray, Offset(-35f, -10f), Size(8f, 30f))
        drawRect(Color.LightGray, Offset(27f, -10f), Size(8f, 30f))
    }
}

fun androidx.compose.ui.graphics.drawscope.DrawScope.drawEnemyShip(enemy: Enemy) {
    withTransform({ translate(enemy.x, enemy.y) }) {
        val color = when(enemy.type) {
            EnemyType.BOMBER -> Color(0xFFF43F5E)
            EnemyType.INTERCEPTOR -> Color(0xFF8B5CF6)
            else -> Color(0xFFFB923C)
        }
        
        // Thruster Glow
        drawCircle(
            brush = Brush.radialGradient(listOf(color.copy(alpha = 0.4f), Color.Transparent)),
            radius = 40f,
            center = Offset(0f, -30f)
        )

        val shipPath = Path().apply {
            when (enemy.type) {
                EnemyType.BOMBER -> {
                    moveTo(0f, 50f)
                    lineTo(-50f, -20f)
                    lineTo(-20f, -40f)
                    lineTo(20f, -40f)
                    lineTo(50f, -20f)
                    close()
                }
                EnemyType.INTERCEPTOR -> {
                    moveTo(0f, 60f)
                    lineTo(-30f, 0f)
                    lineTo(0f, -30f)
                    lineTo(30f, 0f)
                    close()
                }
                else -> {
                    moveTo(0f, 40f)
                    lineTo(-35f, -20f)
                    lineTo(35f, -20f)
                    close()
                }
            }
        }
        drawPath(shipPath, color)
        
        // Core Glow
        drawCircle(Color.White.copy(alpha = 0.7f), radius = 8f, center = Offset(0f, 0f))
        
        // Health Bar (for tougher enemies)
        if (enemy.health > 1) {
            val maxHealth = if (enemy.type == EnemyType.BOMBER) 4f else 2f
            val healthWidth = (enemy.health / maxHealth) * 60f
            drawRect(Color.Gray.copy(alpha = 0.5f), Offset(-30f, 60f), Size(60f, 4f))
            drawRect(Color.Green, Offset(-30f, 60f), Size(healthWidth, 4f))
        }
    }
}
