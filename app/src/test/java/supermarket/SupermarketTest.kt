package supermarket

import org.junit.Test
import supermarket.TestUtils.apples
import supermarket.TestUtils.toothbrush
import supermarket.model.ShoppingCart
import supermarket.model.SpecialOfferType
import supermarket.model.Teller

class SupermarketTest {

    @Test
    fun testSomething() {
        val catalog = FakeCatalog()
        TestUtils.setupCatalog(catalog)

        val cart = ShoppingCart()
        cart.addItemQuantity(apples, 2.5)
        cart.addItemQuantity(toothbrush, 1.0)

        val teller = Teller(catalog)
        teller.addSpecialOffer(SpecialOfferType.TenPercentDiscount, toothbrush, 10.0)

        val receipt = teller.checksOutArticlesFrom(cart)

        // TODO: This just prints a receipt to give you an idea how the code works.
        println(ReceiptPrinter().printReceipt(receipt))
    }
}
