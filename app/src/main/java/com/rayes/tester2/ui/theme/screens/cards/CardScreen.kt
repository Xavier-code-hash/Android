package com.rayes.tester2.ui.theme.screens.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rayes.tester2.R

data class Product(val name: String, val price: String, val imageRes: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreen() {
    val products = listOf(
        Product("Laptop", "Ksh 50,000", R.drawable.img1),
        Product("Smartphone", "Ksh 30,000", R.drawable.img1),
        Product("Headphones", "Ksh 5,000", R.drawable.img1),
        Product("Watch", "Ksh 10,000", R.drawable.img1),
        Product("Camera", "Ksh 45,000", R.drawable.img1),
        Product("Tablet", "Ksh 25,000", R.drawable.img1),
        Product("Monitor", "Ksh 15,000", R.drawable.img1)
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Product Catalog") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Section 1: Horizontal List (LazyRow)
            item {
                Text(
                    text = "Featured Products",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(products) { product ->
                        FeaturedProductItem(product)
                    }
                }
            }

            // Section 2: Vertical List (LazyColumn items)
            item {
                Text(
                    text = "All Products",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(products) { product ->
                ProductListItem(product)
            }
        }
    }
}

@Composable
fun FeaturedProductItem(product: Product) {
    Card(
        modifier = Modifier.size(150.dp, 200.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = product.name, fontWeight = FontWeight.Bold)
                Text(text = product.price, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ProductListItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = product.name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(text = product.price, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardView() {
    CardScreen()
}
