package cenec.mealvity.mealvity.classes.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import cenec.darash.mealvity.R
import java.lang.ClassCastException

class CancelSavedChangesDialog(private var appContext: Context): DialogFragment() {
    var saveChanges: Boolean = false
    lateinit var listener: CscDialogListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder=AlertDialog.Builder(appContext)

        builder.setTitle(getString(R.string.cancel_changes_dialog_title))
        builder.setMessage(getString(R.string.cancel_changes_dialog_message))

        builder.setPositiveButton(getString(R.string.cancel_changes_dialog_positive_button), object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                listener.cancelChanges()
            }
        })

        builder.setNegativeButton(getString(R.string.cancel_changes_dialog_negative_button), object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // Do nothing
            }
        })

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as CscDialogListener
        } catch (castException: ClassCastException) {
            throw ClassCastException("$context must implement cscDialogListener")
        }
    }


    interface CscDialogListener {
        fun cancelChanges()
    }
}