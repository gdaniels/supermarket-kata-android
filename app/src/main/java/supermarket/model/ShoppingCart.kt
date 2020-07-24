package supermarket.model

import java.util.ArrayList

class ShoppingCart {

    private val items = mutableListOf<ProductQuantity>()
    internal val productQuantities = mutableMapOf<Product, Double>()

    internal fun getItems(): List<ProductQuantity> {
        return ArrayList(items)
    }

    fun addItemQuantity(product: Product, quantity: Double) {
        items.add(ProductQuantity(product, quantity))
        if (productQuantities.containsKey(product)) {
            productQuantities[product] = productQuantities[product]!! + quantity
        } else {
            productQuantities[product] = quantity
        }
    }

    internal fun handleOffers(receipt: Receipt, offers: List<SingleProductOffer>, catalog: SupermarketCatalog) {
        offers.forEach { offer ->
            offer.getDiscount(this, catalog)?.let {
                receipt.addDiscount(it)
            }
        }
    }

    fun quantityOf(product: Product): Double? {
        return productQuantities[product]
    }
}
