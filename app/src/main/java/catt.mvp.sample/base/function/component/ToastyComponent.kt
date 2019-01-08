package catt.mvp.sample.base.function.component

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty


fun Context.toastNormal(strId: Int, duration: Int = Toast.LENGTH_LONG) =
    Toasty.normal(applicationContext, getString(strId), duration).show()

fun Context.toastNormal(message: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    Toasty.normal(applicationContext, message, duration).show()

fun Context.toastInfo(strId: Int, duration: Int = Toast.LENGTH_LONG) =
    Toasty.info(applicationContext, getString(strId), duration, true).show()

fun Context.toastInfo(message: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    Toasty.info(applicationContext, message, duration, true).show()

fun Context.toastSuccess(strId: Int, duration: Int = Toast.LENGTH_LONG) =
    Toasty.success(applicationContext, getString(strId), duration, true).show()

fun Context.toastSuccess(message: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    Toasty.success(applicationContext, message, duration, true).show()

fun Context.toastWarning(strId: Int, duration: Int = Toast.LENGTH_LONG) =
    Toasty.warning(applicationContext, getString(strId), duration, true).show()

fun Context.toastWarning(message: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    Toasty.warning(applicationContext, message, duration, true).show()

fun Context.toastError(strId: Int, duration: Int = Toast.LENGTH_LONG) =
    Toasty.error(applicationContext, getString(strId), duration, true).show()

fun Context.toastError(message: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    Toasty.error(applicationContext, message, duration, true).show()