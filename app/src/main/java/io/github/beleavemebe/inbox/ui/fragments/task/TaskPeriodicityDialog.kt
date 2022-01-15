package io.github.beleavemebe.inbox.ui.fragments.task

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.checkbox.MaterialCheckBox
import io.github.beleavemebe.inbox.R
import io.github.beleavemebe.inbox.databinding.DialogTaskPeriodicityBinding
import io.github.beleavemebe.inbox.util.log
import java.time.DayOfWeek

@RequiresApi(26)
class TaskPeriodicityDialog(
    private val onPeriodicitySet: (Array<out DayOfWeek>) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogTaskPeriodicityBinding
    private val result = mutableSetOf<DayOfWeek>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DialogTaskPeriodicityBinding.inflate(inflater, container, false).let {
            binding = it
            binding.doneBtn.setOnClickListener {
                dismiss()
                log("dismiss")
                onPeriodicitySet(result.toTypedArray())
            }

            val map = DayOfWeek.values().associateWith { dayOfWeek ->
                when (dayOfWeek) {
                    DayOfWeek.MONDAY -> binding.cbMonday
                    DayOfWeek.TUESDAY -> binding.cbTuesday
                    DayOfWeek.WEDNESDAY -> binding.cbWednesday
                    DayOfWeek.THURSDAY -> binding.cbThursday
                    DayOfWeek.FRIDAY -> binding.cbFriday
                    DayOfWeek.SATURDAY -> binding.cbSaturday
                    DayOfWeek.SUNDAY -> binding.cbSunday
                }
            }

            map.forEach { (day, checkbox) ->
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        result += day
                    } else {
                        result -= day
                    }
                }
            }
            it.root
        }
    }
}
