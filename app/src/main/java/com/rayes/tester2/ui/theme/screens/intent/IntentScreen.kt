package com.rayes.tester2.ui.theme.screens.intent

import android.app.Activity
import android.content.Intent as AndroidIntent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

class IntentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatedIntentScreen()
        }
    }
}

@Composable
fun CalculatedIntentScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val backgroundColor = Color(0xFF0F172A)
    val cardColor = Color(0xFF1E293B)
    val accentColor = Color(0xFF38BDF8)
    val textColor = Color.White

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "CalcIntent App",
            color = accentColor,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        val buttonModifier = Modifier
            .fillMaxWidth()
            .height(56.dp)

        OutlinedButton(
            onClick = {
                val cameraIntent = AndroidIntent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(context as Activity, cameraIntent, 1, null)
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = cardColor),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = buttonModifier
        ) {
            Text(text = "Camera", fontSize = 18.sp, color = textColor, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                val callIntent = AndroidIntent(AndroidIntent.ACTION_CALL, "tel:+254712345678".toUri())
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.CALL_PHONE), 1)
                } else {
                    context.startActivity(callIntent)
                }
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = cardColor),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = buttonModifier
        ) {
            Text(text = "Call", fontSize = 18.sp, color = textColor, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                val smsIntent = AndroidIntent(AndroidIntent.ACTION_SENDTO, "smsto:0712345678".toUri())
                smsIntent.putExtra("sms_body", "How is today's weather?")
                context.startActivity(smsIntent)
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = cardColor),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = buttonModifier
        ) {
            Text(text = "Sms", fontSize = 18.sp, color = textColor, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                val stkIntent = context.packageManager.getLaunchIntentForPackage("com.android.stk")
                stkIntent?.let { context.startActivity(it) }
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = cardColor),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = buttonModifier
        ) {
            Text(text = "Stk", fontSize = 18.sp, color = textColor, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                val shareIntent = AndroidIntent(AndroidIntent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(AndroidIntent.EXTRA_TEXT, "Hey, download this app!")
                context.startActivity(AndroidIntent.createChooser(shareIntent, "Share via"))
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = cardColor),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = buttonModifier
        ) {
            Text(text = "Share", fontSize = 18.sp, color = textColor, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                val emailIntent = AndroidIntent(AndroidIntent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(AndroidIntent.EXTRA_EMAIL, arrayOf("recipient@example.com"))
                    putExtra(AndroidIntent.EXTRA_SUBJECT, "App Feedback")
                }
                context.startActivity(emailIntent)
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = cardColor),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = buttonModifier
        ) {
            Text(text = "Email", fontSize = 18.sp, color = textColor, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                val dialIntent = AndroidIntent(AndroidIntent.ACTION_DIAL, "tel:+254700000000".toUri())
                context.startActivity(dialIntent)
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = cardColor),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = buttonModifier
        ) {
            Text(text = "Dial", fontSize = 18.sp, color = textColor, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Intentpre() {
    CalculatedIntentScreen()
}
