
import com.example.moviefront.Domian.Product
import com.example.moviefront.R

object MockDataSource {
    fun getProducts(): List<Product> {
        return listOf(
            Product(
                id = String(),
                name = "Product 1",
                imageResId = R.drawable.image1,
                price = 15.99,
                description = "A high-quality product perfect for daily use.",

                ),
            Product(
                id = String(),
                name = "Product 2",
                imageResId = R.drawable.image2,
                price = 9.49,
                description = "Affordable and durable, designed for long-lasting performance.",

                ),
            Product(
                id = String(),
                name = "Product 3",
                imageResId = R.drawable.image3,
                price = 25.00,
                description = "Premium quality with modern design aesthetics.",
            ),
            Product(
                id = String(),
                name = "Product 4",
                imageResId = R.drawable.image4,
                price = 12.75,
                description = "Compact and efficient, suitable for a variety of uses.",

                ),
            Product(
                id = String(),
                name = "Product 5",
                imageResId = R.drawable.image5,
                price = 17.49,
                description = "Elegant design combined with outstanding functionality.",

                ),
            Product(
                id = String(),
                name = "Product 6",
                imageResId = R.drawable.image6,
                price = 20.99,
                description = "High-performance product made with top-notch materials.",

                )//cart movie
        )
    }
}
