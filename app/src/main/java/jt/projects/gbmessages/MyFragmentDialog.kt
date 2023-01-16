package jt.projects.gbmessages

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MyFragmentDialog : DialogFragment() {

    companion object {
        const val TAG = "MyFragmentDialog"
    }

    private var info: IInformative? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        info = context as? IInformative
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("MyFragmentDialog")
            .setMessage("Some message...")
            .setPositiveButton("Ok") { dialog, button ->
                info?.showInfoText("Ok pressed")
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                info?.showInfoText("Cancel pressed")
            }
            .setCancelable(false)
            .create()
    }

    override fun onDestroy() {
        info?.setBlurEffect(false)
        super.onDestroy()
    }
}