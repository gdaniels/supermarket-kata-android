package supermarket

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import supermarket.TestUtils.apples
import supermarket.TestUtils.toothbrush
import supermarket.model.PercentageOffer
import supermarket.model.ShoppingCart
import supermarket.model.Teller

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
        assertThat(discounts.size, `is`(3))
    }

    @Test
    fun applesDiscount() {
        cart.addItemQuantity(apples, 2.5)
        teller.addSpecialOffer(PercentageOffer(apples, 50.0))
        val receipt = teller.checksOutArticlesFrom(cart)
        val discounts = receipt.getDiscounts()
        assertThat(discounts.size, `is`(1))
        assertThat(discounts[0].discountAmount, `is`(2.0))
    }
}
