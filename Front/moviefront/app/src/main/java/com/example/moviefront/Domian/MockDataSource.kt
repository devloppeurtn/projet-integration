package com.example.moviefront.Domian

import com.example.moviefront.R


object MockDataSource {
    fun getProducts(): List<Product> {
        return listOf(
            Product("Product 1", R.drawable.image1),
            Product("Product 2", R.drawable.image2),
            Product("Product 3", R.drawable.image3) ,
            Product("Product 4", R.drawable.image4),
            Product("Product 5", R.drawable.image5),
            Product("Product 6", R.drawable.image6)
            // Add more mock products
        )
    }
}
