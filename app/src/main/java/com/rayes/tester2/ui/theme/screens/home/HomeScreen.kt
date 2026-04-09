package com.rayes.tester2.ui.theme.screens.home

import android.R.attr.navigationIcon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rayes.tester2.R
import com.rayes.tester2.navigation.Route_LogIn
import com.rayes.tester2.navigation.Route_Register
import com.rayes.tester2.ui.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homescreen(navController: NavHostController) {
    Column(verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Orange)) {

//    start of topbar
        TopAppBar(
            title = {Text("Home")},
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Home,
                        contentDescription = "Home Icon")
                }
            },

            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Search,
                        contentDescription = "Search")
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Settings,
                        contentDescription = "Settings")
                }
            }

        )
//        end of topbar
        Spacer(modifier = Modifier
            .height(30.dp))
        Text("Manchester United",
        color = Color.White,
        fontSize = 40.sp,
        fontStyle = FontStyle.Italic)
        Spacer(modifier = Modifier
            .height(48.dp))
        Text(
            "Welcome Ray...",
            fontSize = 18.sp,
            color = Orange,
            fontFamily = FontFamily.Cursive)

        Image(painter = painterResource(id = R.drawable.img1),
            contentDescription = "DesktopImage",
            modifier = Modifier
                .width(300.dp)
                .height(400.dp)
        )

        Button(onClick ={navController.navigate(Route_LogIn)},
            colors = ButtonDefaults.buttonColors(Color.Black),
            modifier = Modifier
            .width(300.dp)
        ){
            Text("LogIn", fontSize = 20.sp, fontFamily = FontFamily.SansSerif)
        }

        Button(onClick = {navController.navigate(Route_Register)},
            colors = ButtonDefaults.buttonColors(Color.Green),
            modifier = Modifier
                .width(300.dp)

        ){
            Text("Register")
        }
    }



}

@Preview(showSystemUi = true)
@Composable
private fun Homeprev() {
    Homescreen(rememberNavController())

}