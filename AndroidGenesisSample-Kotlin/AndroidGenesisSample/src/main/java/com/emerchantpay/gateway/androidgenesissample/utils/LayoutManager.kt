package com.emerchantpay.gateway.androidgenesissample.utils

import android.content.Context
import android.text.InputType
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView

object LayoutManager {
    fun createView(context: Context, viewType: ViewType, params: ViewParams? = null): View {
        return when(viewType) {
            ViewType.TEXTABLE -> createTextView(context, params)
            ViewType.EDITABLE -> createEditText(context)
            ViewType.SELECTABLE -> createSpinner(context)
        }
    }

    private fun createTextView(context: Context, params: ViewParams?): TextView {
        val view = TextView(context)

        return (applyViewParams(view) as TextView).apply {
            params?.let { viewParams ->
                with(viewParams) {
                    text = caption
                    textAppearanceRes?.let { setTextAppearance(context, it) }
                    textColorRes?.let { setTextColor(it) }
                    textSize?.let { setTextSize(TypedValue.COMPLEX_UNIT_PX, it) }
                    fontTypeface?.let { typeface = it }
                }
            }
        }
    }

    private fun createEditText(context: Context): EditText {
        val view = EditText(context)

        return (applyViewParams(view) as EditText).apply {
            imeOptions = EditorInfo.IME_ACTION_UNSPECIFIED
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            filters = emptyArray()
        }
    }

    private fun createSpinner(context: Context): Spinner {
        val view = Spinner(context, Spinner.MODE_DROPDOWN)

        return (applyViewParams(view) as Spinner).apply {}
    }

    private fun applyViewParams(view: View): View {
        val viewHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        val viewHorizontalMargin = 0
        val viewTopMargin = 0

        val linearLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewHeight)).apply {
            setMargins(viewHorizontalMargin, viewTopMargin, viewHorizontalMargin, 0)
        }

        return view.apply {
            id = View.generateViewId()
            visibility = View.VISIBLE
            layoutParams = linearLayoutParams
            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        }
    }
}