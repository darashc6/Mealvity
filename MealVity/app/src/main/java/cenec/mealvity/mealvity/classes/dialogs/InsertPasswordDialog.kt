package cenec.mealvity.mealvity.classes.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import cenec.darash.mealvity.R
import java.lang.ClassCastException

class InsertPasswordDialog(private var appContext: Context): DialogFragment() {
    private lateinit var dialogView: View
    private lateinit var etPassword: TextView
    private lateinit var listener: InsertPasswordDialogListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogView = LayoutInflater.from(appContext).inflate(R.layout.insert_password_dialog, null)
        etPassword = dialogView.findViewById(R.id.editText_password)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(appContext)
        builder.setView(dialogView)

        builder.setTitle(getString(R.string.insert_password_dialog_title))
        builder.setMessage(getString(R.string.insert_password_dialog_message))
        builder.setPositiveButton(getString(R.string.insert_password_dialog_positive_button), object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                listener.getInputPassword(etPassword.text.toString())
            }
        })
        builder.setNegativeButton(getString(R.string.insert_password_dialog_negative_button), object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // Do nothing
            }

        })

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as InsertPasswordDialogListener
        } catch (castException: ClassCastException) {
            throw ClassCastException("$context must implement cscDialogListener")
        }
    }

    interface InsertPasswordDialogListener{
        fun getInputPassword(password: String)
    }
}