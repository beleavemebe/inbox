package io.github.beleavemebe.inbox.tasks.ui.task_details.periodicity

import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.Group

class PeriodicityUi(
    val switchPeriodicity: SwitchCompat,
    val tvPeriodicity: TextView,
    val cvPeriodicity: CardView,
    val groupWeeklyPeriodicityCheckboxes: Group,
    val cbPeriodicityMon: CheckBox,
    val cbPeriodicityTue: CheckBox,
    val cbPeriodicityWed: CheckBox,
    val cbPeriodicityThu: CheckBox,
    val cbPeriodicityFri: CheckBox,
    val cbPeriodicitySat: CheckBox,
    val cbPeriodicitySun: CheckBox,
)
