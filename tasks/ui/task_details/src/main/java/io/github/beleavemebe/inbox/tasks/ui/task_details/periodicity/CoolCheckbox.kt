package io.github.beleavemebe.inbox.tasks.ui.task_details.periodicity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import io.github.beleavemebe.inbox.core.utils.toast
import io.github.beleavemebe.inbox.tasks.ui.task_details.databinding.CoolCheckboxBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach

class CoolCheckbox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val binding: CoolCheckboxBinding
    private val _isChecked = MutableStateFlow(false)
    private val isChecked = _isChecked.asStateFlow()

    init {
        binding = CoolCheckboxBinding.inflate(LayoutInflater.from(context), this)
        binding.root.setOnClickListener {
            _isChecked.value = !_isChecked.value
        }
        isChecked.onEach {
            context.toast("$it")
        }
    }

}
