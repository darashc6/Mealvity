package cenec.mealvity.mealvity.classes.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import cenec.darash.mealvity.R
import java.lang.ClassCastException

class InsertStreetNumberDialog(private var appContext: Context): DialogFragment() {
    private lateinit var dListener: InsertStreetNumberListener


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(appContext).inflate(R.layout.insert_street_number_dialog, null)
        val dialogBuilder = AlertDialog.Builder(appContext)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setTitle(appContext.getString(R.string.insert_street_number_dialog_title))
        dialogBuilder.setMessage(appContext.getString(R.string.insert_street_number_dialog_message))
        dialogBuilder.setPositiveButton(appContext.getString(R.string.insert_street_number_positive_button), object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val etStreetNumber = dialogView.findViewById<EditText>(R.id.editText_street_number)
                dListener.getStreetNumber(Integer.parseInt(etStreetNumber.text.toString()))
            }
        })
        dialogBuilder.setNegativeButton(appContext.getString(R.string.insert_street_number_negative_button), object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // Do nothing
            }

        })

        return dialogBuilder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            dListener = context as InsertStreetNumberListener
        } catch (ex: ClassCastException) {
            throw ClassCastException("$context must implement cscDialogListener")
        }
    }

    interface InsertStreetNumberListener {
        fun getStreetNumber(number: Int)
    }
}