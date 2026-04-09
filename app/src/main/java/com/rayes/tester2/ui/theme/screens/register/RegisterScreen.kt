package com.rayes.tester2.ui.theme.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rayes.tester2.navigation.Route_LogIn

@Composable
fun Register(navController: NavHostController) {
    Column(verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)


    ){
        Text("Register Screen",
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif,
            color = Color.White
            )
        OutlinedTextField(value = "",
            onValueChange = {},
            label = {Text("Enter First Name",
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily.Cursive)},
            modifier = Modifier
                .width(300.dp)
            .padding(all = 4.dp))

        Text("Last Name",
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Italic,
            fontSize = 18.sp,
            color = Color.Red)
        OutlinedTextField(value = "",
            onValueChange = {},
            label = {Text("Enter Last Name",
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                color = Color.Black)})
        Text("Phone Number",
        fontFamily = FontFamily.SansSerif,
        fontStyle = FontStyle.Italic,
        fontSize = 18.sp,
        color = Color.Red)
        OutlinedTextField(value = "",
            onValueChange = {},
            label = {Text("Enter Phone Number",
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                color = Color.Black)})
        Text("Password",
        fontFamily = FontFamily.SansSerif,
        fontStyle = FontStyle.Italic,
        fontSize = 18.sp,
        color = Color.Red)
        OutlinedTextField(value = "",
            onValueChange = {},
            label = {Text("Enter Password",
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                color = Color.Black)})
        Text("Password",
        fontFamily = FontFamily.SansSerif,
        fontStyle = FontStyle.Italic,
        fontSize = 18.sp,
        color = Color.Red)
        OutlinedTextField(value = "",
            onValueChange = {},
            label = {Text("Confirm Password",
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                color = Color.Red)})

        Button(onClick = {navController.navigate(Route_LogIn)},
            modifier = Modifier
                .height(50.dp)
                .width(300.dp),
            colors = ButtonDefaults.buttonColors(Color.Green)
        ) {
            Text("Register Here",
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                color = Color.Blue)
        }
    }


}

@Preview(showSystemUi = true)
@Composable
private fun RegisterPrev() {
    Register(rememberNavController())

}