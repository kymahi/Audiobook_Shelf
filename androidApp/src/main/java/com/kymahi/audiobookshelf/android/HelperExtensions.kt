package com.kymahi.audiobookshelf.android

import android.content.res.Resources
import android.util.TypedValue
import android.view.View


val Number.dp get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    toFloat(),
    Resources.getSystem().displayMetrics
).toInt()

fun Resources.get(id: Int) = getDimension(id).toInt()

fun View.gone() { visibility = View.GONE }
fun View.visible() { visibility = View.VISIBLE }
fun View.invisible() { visibility = View.INVISIBLE }