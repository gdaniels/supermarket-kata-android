package supermarket.model

sealed class Offer {
    abstract fun getDiscount(
        shoppingCart: ShoppingCart,
        catalog: SupermarketCatalog
    ): Discount?
}

abstract class SingleProductOffer(val product: Product): Offer()

class PercentageOffer(product: Product, val percentOff: Double) : SingleProductOffer(product) {
    override fun getDiscount(
        shoppingCart: ShoppingCart,
        catalog: SupermarketCatalog
    ): Discount? {
        val quantityOf = shoppingCart.quantityOf(product) ?: return null

        val unitPrice = catalog.getUnitPrice(product)
        return Discount(product, "$percentOff% off", quantityOf * unitPrice * percentOff / 100.0)
    }
}

class ThreeForTwoOffer(product: Product) : SingleProductOffer(product) {
    override fun getDiscount(
        shoppingCart: ShoppingCart,
        catalog: SupermarketCatalog
    ): Discount? {
        TODO("Not yet implemented")
    }
}

class QuantityForAmountOffer(product: Product, private val discountQuantity: Int, private val price: Double) :
    SingleProductOffer(product) {
    override fun getDiscount(
        shoppingCart: ShoppingCart,
        catalog: SupermarketCatalog
    ): Discount? {
        val productQuantity = shoppingCart.quantityOf(product)
        val quantityInt = productQuantity?.toInt()?.let {
            if (it < discountQuantity) null else it
        } ?: return null

        val unitPrice = catalog.getUnitPrice(product)
        val discounted = price * (quantityInt / discountQuantity) + (quantityInt % discountQuantity) * unitPrice
        val normal = quantityInt * unitPrice
        return Discount(product, "$discountQuantity for \$$price", normal - discounted)
    }
}

fun main() {
    val apples = Product("apples", ProductUnit.Each)
    val offer = QuantityForAmountOffer(apples, 2, 1.0)
    val cart = ShoppingCart()
    cart.addItemQuantity(apples, 3.0)
    val catalog = object : SupermarketCatalog {
        override fun addProduct(product: Product, price: Double) {
            TODO("Not yet implemented")
        }

        override fun getUnitPrice(product: Product): Double = 2.0
    }

    val discount = offer.getDiscount(cart, catalog)
    discount?.let {
        println(discount.description)
        println(discount.discountAmount)
    }
}

/*

** TODO: Make sure we reflect all of the below (formerly from ShoppingCart) in the new Offer structure

for (p in productQuantities().keys) {
    val quantity = productQuantities[p]!!
    if (offers.containsKey(p)) {
        val offer = offers[p]!!
        val unitPrice = catalog.getUnitPrice(p)
        val quantityAsInt = quantity.toInt()
        var x = 1
        if (offer.offerType === SpecialOfferType.ThreeForTwo) {
            x = 3

        } else if (offer.offerType === SpecialOfferType.TwoForAmount) {
            x = 2
            if (quantityAsInt >= 2) {
                val total = offer.argument * (quantityAsInt / x) + quantityAsInt % 2 * unitPrice
                val discountN = unitPrice * quantity - total
                discount = Discount(p, "2 for " + offer.argument, discountN)
            }

        }
        if (offer.offerType === SpecialOfferType.FiveForAmount) {
            x = 5
        }
        val numberOfXs = quantityAsInt / x
        if (offer.offerType === SpecialOfferType.ThreeForTwo && quantityAsInt > 2) {
            val discountAmount =
                quantity * unitPrice - (numberOfXs.toDouble() * 2.0 * unitPrice + quantityAsInt % 3 * unitPrice)
            discount = Discount(p, "3 for 2", discountAmount)
        }
        if (offer.offerType === SpecialOfferType.TenPercentDiscount) {
            discount =
                Discount(p, offer.argument.toString() + "% off", quantity * unitPrice * offer.argument / 100.0)
        }
        if (offer.offerType === SpecialOfferType.FiveForAmount && quantityAsInt >= 5) {
            val discountTotal =
                unitPrice * quantity - (offer.argument * numberOfXs + quantityAsInt % 5 * unitPrice)
            discount = Discount(p, x.toString() + " for " + offer.argument, discountTotal)
        }
        if (discount != null)
            receipt.addDiscount(discount)

    }

}
*/
