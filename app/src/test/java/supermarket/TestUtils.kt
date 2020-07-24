package supermarket

import supermarket.model.Product
import supermarket.model.ProductUnit
import supermarket.model.SupermarketCatalog

object TestUtils {
    val toothbrush = Product("toothbrush", ProductUnit.Each)
    val apples = Product("apples", ProductUnit.Kilo)

    fun setupCatalog(catalog: SupermarketCatalog) {
        catalog.addProduct(toothbrush, 0.99)
        catalog.addProduct(apples, 1.99)
    }
}