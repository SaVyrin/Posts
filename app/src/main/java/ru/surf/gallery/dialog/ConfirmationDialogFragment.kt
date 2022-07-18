package ru.surf.gallery.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.surf.gallery.R

class ConfirmationDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.dialog_delete_from_featured_text))
            .setPositiveButton(getString(R.string.dialog_positive_button_text)) { _, _ -> }
            .setNegativeButton(getString(R.string.dialog_negative_button_text)) { _, _ -> }
            .create()

    companion object {
        const val TAG = "ConfirmationDialog"
    }
}