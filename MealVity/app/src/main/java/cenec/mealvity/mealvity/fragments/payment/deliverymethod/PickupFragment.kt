package cenec.mealvity.mealvity.fragments.payment.deliverymethod

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.FragmentPickupBinding
import cenec.mealvity.mealvity.classes.singleton.OrderSingleton
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pickup option fragment
 */
class PickupFragment : Fragment() {
    private var _binding: FragmentPickupBinding? = null // View binding for the fragment
    private val binding get() = _binding // Non-nullable version of the binding variable above

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPickupBinding.inflate(inflater, container, false)
        setupViews()
        return binding!!.root
    }

    /**
     * Sets up the fragment's initial views
     */
    private fun setupViews() {
        binding!!.textViewTime.text = setPickupTime()

        binding!!.textViewTime.setOnClickListener {
            showTimePickerDialog()
        }
    }

    /**
     * Converts a Calendar time to String
     * @return Formatted time
     */
    private fun setPickupTime(): String {
        val time = Calendar.getInstance()
        time.add(Calendar.MINUTE, 30)
        val timeDate = time.time
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())

        return format.format(timeDate)
    }

    /**
     * Shows a Time picker dialog
     */
    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(context!!, R.style.DateTimePickerDialog, object: TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                val newDate = Calendar.getInstance()
                newDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), hourOfDay, minute)
                val timeToVerify = newDate.time
                if (verifyTime(timeToVerify)) {
                    val sTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeToVerify)
                    binding!!.textViewTime.text = sTime
                    OrderSingleton.getInstance().getOrder().expectedDeliveryTime = sTime
                } else {
                    Toast.makeText(context, "Please select a correct time. Select a time that is at least 30 minutes from the current time.", Toast.LENGTH_LONG).show()
                }
            }

        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false)
        timePickerDialog.show()
    }

    /**
     * Verifies the time selected via the TimePicker
     * @return True if difference between time selected and current time is at least 30 minutes, false if otherwise
     */
    private fun verifyTime(timeToVerify: Date): Boolean {
        val currentTime = Calendar.getInstance()
        currentTime.add(Calendar.MINUTE, 30)
        val timeDate = currentTime.time

        return timeToVerify.after(timeDate)
    }
}
