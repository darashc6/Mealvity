package cenec.mealvity.mealvity.classes.config

import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import java.math.BigDecimal

class PaypalConfig {
    companion object {
        fun createPaypalConfiguration(): PayPalConfiguration {
            return PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                .acceptCreditCards(true)
        }

        fun generatePaypalPayment(totalPayment: Float): PayPalPayment {
            return PayPalPayment(BigDecimal(totalPayment.toString()), "EUR", "Mealvity - Food Order", PayPalPayment.PAYMENT_INTENT_SALE)
        }
    }
}