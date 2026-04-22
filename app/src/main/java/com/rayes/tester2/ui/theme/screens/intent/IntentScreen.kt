package com.rayes.tester2.ui.theme.screens.intent

import android.app.Activity
import android.content.Intent as AndroidIntent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatedIntentScreen(navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("System Intents", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Device Interactions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Use system services directly from the app",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            IntentGrid(navController)
        }
    }
}

@Composable
fun IntentGrid(navController: NavController) {
    val context = LocalContext.current
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IntentCard(
                "Camera", Icons.Default.PhotoCamera, Modifier.weight(1f),
                onClick = {
                    val cameraIntent = AndroidIntent(MediaStore.ACTION_IMAGE_CAPTURE)
                    (context as? Activity)?.startActivityForResult(cameraIntent, 1)
                }
            )
            IntentCard(
                "Call", Icons.Default.Call, Modifier.weight(1f),
                onClick = {
                    val callIntent = AndroidIntent(AndroidIntent.ACTION_CALL, "tel:+254712345678".toUri())
                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.CALL_PHONE), 1)
                    } else {
                        context.startActivity(callIntent)
                    }
                }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IntentCard(
                "SMS", Icons.Default.Sms, Modifier.weight(1f),
                onClick = {
                    val smsIntent = AndroidIntent(AndroidIntent.ACTION_SENDTO, "smsto:0712345678".toUri())
                    smsIntent.putExtra("sms_body", "Hello from Tester App!")
                    context.startActivity(smsIntent)
                }
            )
            IntentCard(
                "Share", Icons.Default.Share, Modifier.weight(1f),
                onClick = {
                    val shareIntent = AndroidIntent(AndroidIntent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(AndroidIntent.EXTRA_TEXT, "Check out this awesome app!")
                    }
                    context.startActivity(AndroidIntent.createChooser(shareIntent, "Share via"))
                }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IntentCard(
                "Email", Icons.Default.Email, Modifier.weight(1f),
                onClick = {
                    val emailIntent = AndroidIntent(AndroidIntent.ACTION_SENDTO).apply {
                        data = "mailto:".toUri()
                        putExtra(AndroidIntent.EXTRA_EMAIL, arrayOf("support@tester.com"))
                        putExtra(AndroidIntent.EXTRA_SUBJECT, "App Feedback")
                    }
                    context.startActivity(emailIntent)
                }
            )
            IntentCard(
                "Dial", Icons.Default.Dialpad, Modifier.weight(1f),
                onClick = {
                    val dialIntent = AndroidIntent(AndroidIntent.ACTION_DIAL, "tel:+254700000000".toUri())
                    context.startActivity(dialIntent)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntentCard(title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Intentpre() {
    CalculatedIntentScreen(rememberNavController())
}
