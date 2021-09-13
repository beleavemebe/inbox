package io.github.beleavemebe.inbox.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import java.lang.IllegalStateException

internal class Toaster private constructor(private val context: Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE : Toaster ?= null

        fun get(): Toaster = INSTANCE
            ?: throw IllegalStateException("Toaster was not initialized")

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = Toaster(context)
        }
    }

    fun toast(stringRes: Int) {
        Toast.makeText(
            context.applicationContext,
            stringRes,
            Toast.LENGTH_SHORT
        ).show()
    }
}