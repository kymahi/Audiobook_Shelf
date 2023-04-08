package com.kymahi.audiobookshelf.android

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.EditText


val Number.dp get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    toFloat(),
    Resources.getSystem().displayMetrics
).toInt()

fun Resources.getDim(id: Int) = getDimension(id).toInt()

fun View.gone() { visibility = View.GONE }
fun View.visible() { visibility = View.VISIBLE }
fun View.invisible() { visibility = View.INVISIBLE }

val EditText.input get() = text.toString()