package cenec.mealvity.mealvity.classes.config

import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import java.math.BigDecimal

/**
 * Class used for storing Paypal configuration options
 */
class PaypalConfig {
    companion object {

        /**
         * Creates a new Paypal configuration
         */
        fun createPaypalConfiguration(): PayPalConfiguration {
            return PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                .acceptCreditCards(true)
        }

        /**
         * Generates the amount to pay on Paypal
         */
        fun generatePaypalPayment(totalPayment: Float): PayPalPayment {
            return PayPalPayment(BigDecimal(totalPayment.toString()), "EUR", "Mealvity - Food Order", PayPalPayment.PAYMENT_INTENT_SALE)
        }
    }
}