package com.androidstudy.view.errorView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.androidstudy.databinding.ViewErrorBinding

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewErrorBinding.inflate(LayoutInflater.from(context), this, true)

    fun setOnRetryListener(listener: () -> Unit) {
        binding.btnRetry.setOnClickListener { listener() }
    }

}