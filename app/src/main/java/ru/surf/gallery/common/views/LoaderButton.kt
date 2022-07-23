package ru.surf.gallery.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import ru.surf.gallery.R
import ru.surf.gallery.databinding.LoaderButtonBinding

class LoaderButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(
    context,
    attributeSet
) {
    private val binding = LoaderButtonBinding.inflate(LayoutInflater.from(context), this)

    var text = ""
        set(value) {
            field = value
            binding.buttonTextTv.text = value
        }

    var isLoading = false
        set(value) {
        field = value
            binding.buttonProgressBar.isVisible = value
            binding .buttonTextTv.isVisible = !value
            binding.root.isEnabled = !value
        }

    init {
        context.withStyledAttributes(attributeSet, R.styleable.LoaderButton) {
            text = getString(R.styleable.LoaderButton_text) ?: ""
            isLoading = getBoolean(R.styleable.LoaderButton_isLoading, false)
        }
    }
}