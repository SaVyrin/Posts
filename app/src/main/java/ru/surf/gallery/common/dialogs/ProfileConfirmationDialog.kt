package ru.surf.gallery.common.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.surf.gallery.R

class ProfileConfirmationDialog(
    val positiveClickListener: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.dialog_logout_text))
            .setPositiveButton(getString(R.string.dialog_positive_button_text)) { _, _ ->
                positiveClickListener()
            }
            .setNegativeButton(getString(R.string.dialog_negative_button_text)) { _, _ -> }
            .create()

    companion object {
        const val TAG = "ProfileConfirmationDialog"
    }
}