package cenec.mealvity.mealvity.classes.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import java.lang.ClassCastException

/**
 * Dialog for choosing the address for the home delivery
 * @param appContext Application context
 */
class ChooseAddressDialog(private var appContext: Context): DialogFragment() {
    private lateinit var dialogView: View // Layout of the dialog
    private lateinit var spinnerAddress: Spinner // Spinner for a list of addresses
    private lateinit var radioGroup: RadioGroup // Group of radio buttons
    private lateinit var etAddress: EditText // EditText to insert a new address
    private var dListener: ChooseAddressDialogListener? = null // Listener for the dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogView = LayoutInflater.from(appContext).inflate(R.layout.choose_address_dialog, null)
        spinnerAddress = dialogView.findViewById(R.id.spinner_address_list)
        etAddress = dialogView.findViewById(R.id.editText_address)
        radioGroup = dialogView.findViewById(R.id.radioGroup)
        setupSpinner()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(appContext)
        builder.setView(dialogView)

        builder.setTitle("Choose an address")
        builder.setMessage("Select an exsiting address from your list, or write a new address")

        builder.setPositiveButton("Accept", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when (radioGroup.checkedRadioButtonId) {
                    R.id.radio_button_select_address -> {
                        dListener?.onAcceptClick(spinnerAddress.selectedItem as String)
                        dismiss()
                    }
                    R.id.radio_button_insert_new_address -> {
                        dListener?.onAcceptClick(etAddress.text.toString())
                        dismiss()
                    }
                }
            }

        })

        builder.setNegativeButton("Cancel", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // Do nothing
            }

        })

        return builder.create()
    }

    /**
     * Sets the listener for the dialog
     * @param newListener New listener
     */
    fun setChooseAddressDialogListener (newListener: ChooseAddressDialogListener) {
        dListener = newListener
    }

    /**
     * Sets up a spinner containing a list of addresses saved by the user
     */
    private fun setupSpinner() {
        val currentUser = UserSingleton.getInstance().getCurrentUser()
        val addressNameList = arrayListOf<String>()
        for (address in currentUser.addresses) {
            addressNameList.add("${address.title} - ${address.name}, ${address.number}")
        }

        val arrayAdapter = ArrayAdapter(appContext, android.R.layout.simple_spinner_item, addressNameList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAddress.adapter = arrayAdapter
    }

    /**
     * Interface for the dialog
     */
    interface ChooseAddressDialogListener {
        /**
         * Triggered when the user presses the 'Accept' button. Takes the address selected
         * @param address Address selected
         */
        fun onAcceptClick(address: String)
    }
}