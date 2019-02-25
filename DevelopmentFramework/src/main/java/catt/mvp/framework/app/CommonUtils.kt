package catt.mvp.framework.app

import android.view.View
import android.view.Window

fun hideSystemUI(window: Window) {
    val decorView = window.decorView

//    val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//            or View.SYSTEM_UI_FLAG_FULLSCREEN
//            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    val flags = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_FULLSCREEN)
    decorView.systemUiVisibility = flags
}