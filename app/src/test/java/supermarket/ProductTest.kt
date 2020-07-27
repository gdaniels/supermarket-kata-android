package supermarket

import org.hamcrest.CoreMatchers.`is` as Is
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.closeTo
import org.junit.Before
import org.junit.Test
import supermarket.TestUtils.apples
import supermarket.TestUtils.toothbrush
import supermarket.TestUtils.toothpaste
import supermarket.model.BundleOffer
import supermarket.model.PercentageOffer
import supermarket.model.ShoppingCart
import supermarket.model.Teller
import supermarket.model.ThreeForTwoOffer

class ProductTest {
    private val catalog = FakeCatalog()
    private val cart = ShoppingCart()
    private val teller = Teller(catalog)

    @Before
    fun setup() {
        TestUtils.setupCatalog(catalog)
    }

    @Test
    fun `something or other`() {
        cart.addItemQuantity(toothbrush, 1.0)

        teller.addSpecialOffer(PercentageOffer(toothbrush, 10.0))

        val receipt = teller.checksOutArticlesFrom(cart)
        val discounts = receipt.getDiscounts()
        assertThat(discounts.size, Is(1))
    }

    @Test
    fun applesDiscount() {
        cart.addItemQuantity(apples, 2.5)
        teller.addSpecialOffer(PercentageOffer(apples, 50.0))
        val receipt = teller.checksOutArticlesFrom(cart)
        val discounts = receipt.getDiscounts()
        assertThat(discounts.size, Is(1))
        assertThat(discounts[0].discountAmount, Is(1.99))
    }

    @Test
    fun bundle() {
        cart.addItemQuantity(toothbrush, 1.0)
        cart.addItemQuantity(toothpaste, 1.0)

        teller.addSpecialOffer(BundleOffer("Tooth bundle", listOf(toothbrush, toothpaste), 3.00))
        val receipt = teller.checksOutArticlesFrom(cart)
        val discounts = receipt.getDiscounts()
        assertThat(discounts.size, Is(1))
        assertThat(discounts[0].discountAmount, Is(closeTo(1.41, 0.00001)))
        println(ReceiptPrinter().printReceipt(receipt))
    }

    @Test
    fun threeForTwo() {
        cart.addItemQuantity(toothbrush, 4.0)
        teller.addSpecialOffer(ThreeForTwoOffer(toothbrush))
        val receipt = teller.checksOutArticlesFrom(cart)
        val discounts = receipt.getDiscounts()
        assertThat(discounts.size, Is(1))
        assertThat(discounts[0].discountAmount, Is(closeTo(0.99, 0.00001)))
        println(ReceiptPrinter().printReceipt(receipt))
    }
}
