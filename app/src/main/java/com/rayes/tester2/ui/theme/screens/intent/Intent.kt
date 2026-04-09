package com.rayes.tester2.ui.theme.screens.intent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rayes.tester2.navigation.Route_Home



@Composable
fun Intent(navController: NavHostController) {
    Column(verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)) {

        Text("Rationale de la Corizon",
            color = Color.White,
            fontSize = 40.sp,
            fontStyle = FontStyle.Italic)


        Button(onClick ={navController.navigate(Route_Home)},
            colors = ButtonDefaults.buttonColors(Color.Black),
            modifier = Modifier
                .width(300.dp)
        ){
            Text("Return Home",
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif)
        }


    }



}

@Preview(showSystemUi = true)
@Composable
fun Intentprev() {
    Intent(rememberNavController())

}