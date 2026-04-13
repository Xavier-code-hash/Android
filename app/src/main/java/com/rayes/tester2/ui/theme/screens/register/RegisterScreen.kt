package com.rayes.tester2.ui.theme.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rayes.tester2.navigation.Route_LogIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("Home") },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        )

        Text(
            text = "Register Screen",
            fontSize = 24.sp,
            fontFamily = FontFamily.Serif,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon") },
            label = { Text("Enter Email") },
            modifier = Modifier.width(300.dp).padding(4.dp)
        )

        Text("Phone Number",
            color = Color.Red,
            fontStyle = FontStyle.Italic,
        modifier = Modifier
            .padding(4.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Phone,
                    contentDescription = "Phone Icon")
            },
            label = { Text("Enter Phone Number") },
            modifier = Modifier.width(300.dp)
        )

        Text("Password",
            color = Color.Red,
            fontStyle = FontStyle.Italic,
        modifier = Modifier
            .padding(4.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock Feature") },
            label = { Text("Enter Password") },
            modifier = Modifier.width(300.dp)
        )

        Text("Confirm Password",
            color = Color.Red,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(4.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock,
                    contentDescription = "Lock Feature")
            },
            label = { Text("Confirm Password") },
            modifier = Modifier.width(300.dp)
        )

        Button(
            onClick = { navController.navigate(Route_LogIn) },
            modifier = Modifier
                .padding(top = 20.dp)
                .height(50.dp)
                .width(300.dp),
            colors = ButtonDefaults.buttonColors(Color.Green)
        ) {
            Text("Register", fontSize = 18.sp, color = Color.Blue)
        }
       TextButton(onClick = {navController.navigate(Route_LogIn)}) {
           Text("Already have an account, click to LogIn",
               color = Color.Red,
               fontStyle = FontStyle.Italic,
               modifier = Modifier
                   .padding(4.dp))
       }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun RegisterPrev() {
    Register(rememberNavController())
}
