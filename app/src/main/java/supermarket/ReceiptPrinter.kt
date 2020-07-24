package supermarket

import supermarket.model.Discount
import supermarket.model.ProductUnit
import supermarket.model.Receipt
import supermarket.model.ReceiptItem
import java.util.*

class ReceiptPrinter @JvmOverloads constructor(private val columns: Int = 40) {

    private fun Discount.descriptionString() = "$description (${product.name})"
    private fun Discount.priceString() = priceAsString(-discountAmount)

    fun printReceipt(receipt: Receipt): String {
        val result = StringBuilder()
        for (item in receipt.getItems()) {
            result.append(getLineForItem(item))
        }
        for (discount in receipt.getDiscounts()) {
            result.append(getLRString(discount.descriptionString(), discount.priceString()))
        }
        result.append("\n")
        result.append(getLRString("Total: ", priceAsString(receipt.totalPrice)))
        return result.toString()
    }

    private fun getLineForItem(item: ReceiptItem): String {
        var line = getLRString(item.product.name, priceAsString(item.totalPrice))

        if (item.quantity != 1.0) {
            line += "  ${priceAsString(item.price)} * ${presentQuantity(item)}\n"
        }
        return line
    }

    private fun priceAsString(price: Double) = String.format(Locale.UK, "%.2f", price)

    private fun getLRString(left: String, right: String): String {
        val whitespaceSize = this.columns - left.length - right.length
        return left + " ".repeat(whitespaceSize) + right + "\n"
    }

    private fun presentQuantity(item: ReceiptItem): String {
        return if (ProductUnit.Each == item.product.unit)
            String.format("%x", item.quantity.toInt())
        else
            String.format(Locale.UK, "%.3f", item.quantity)
    }
}
