package com.example.weatherappkotlin.utils

import android.content.Context
import android.location.Geocoder
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.weatherappkotlin.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

fun TextView.leftDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, id, 0, 0)
}
fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()
fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration)
        .setTextColor(ContextCompat.getColor(context, R.color.white))
        .setBackgroundTint(ContextCompat.getColor(context, R.color.green_solid))
        .show()
}
fun ImageView.loadImagesWithGlideExt(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .error(R.drawable.ic_image_placeholder_new)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_image_placeholder_new)
        .into(this)
}
fun Context.getCurrentDate():String{
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    return sdf.format(Date())
}
fun Context.getDateStringToAnotherFormat(dateS:String):String{
    val parser =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    return formatter.format(parser.parse(dateS))
}
fun View.setMargins(
    left: Int = this.marginLeft,
    top: Int = this.marginTop,
    right: Int = this.marginRight,
    bottom: Int = this.marginBottom,
) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        setMargins(left, top, right, bottom)
    }
}
