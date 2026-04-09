package com.rayes.tester2.ui.theme.screens.login

//noinspection SuspiciousImport
import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rayes.tester2.navigation.Route_Home

@Composable
fun LogIn(navController: NavHostController) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)) {
        Text("LogIn Screen",
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Cursive,
            fontSize = 28.sp,
            fontWeight = FontWeight(600))
        OutlinedTextField(value ="", onValueChange = {}, label = {
            Text(
                "Enter email",
                fontSize = 18.sp,
                fontFamily = FontFamily.Cursive,
                color = Color.White,
                modifier = Modifier
                    .width(300.dp)
            )
        })
        Spacer(modifier = Modifier
            .height(20.dp))
        OutlinedTextField(value ="", onValueChange = {}, label = {
            Text("Enter Password",
                fontSize = 18.sp,
                fontWeight = FontWeight(600),
                fontFamily = FontFamily.Cursive,
                color = Color.White,
                modifier = Modifier
                    .width(300.dp)

            )
        })
Spacer(modifier = Modifier
    .height(20.dp))
        Button(onClick = {navController.navigate(Route_Home)}, Modifier
            .defaultMinSize(150.dp, 40.dp),
        colors = ButtonDefaults.buttonColors(Color.Green)) {
            Text("Login",
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(5.dp)
                )
        }

    }

}

@Preview(showSystemUi = true)
@Composable
private fun LogInPreview() {
    LogIn(rememberNavController())

}