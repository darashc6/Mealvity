package cenec.mealvity.mealvity.fragments.payment.paymentmethod

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cenec.darash.mealvity.R

/**
 * Paypal payment fragment
 */
class PaypalFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paypal, container, false)
    }

}
