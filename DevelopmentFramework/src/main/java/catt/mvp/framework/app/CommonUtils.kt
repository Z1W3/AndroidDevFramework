package catt.mvp.framework.app

import android.view.View
import android.view.Window

private const val HIDE_NAVIGATION = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        or View.SYSTEM_UI_FLAG_FULLSCREEN)

private val windowMap : HashMap<Int, Window> by lazy { HashMap<Int, Window>() }

fun Window.setFullScreen() {
    windowMap[hashCode()] = this
    decorView.systemUiVisibility = HIDE_NAVIGATION
}

fun Window.enableAutoFullScreen() {
    decorView.setOnSystemUiVisibilityChangeListener {
        if (it != 6) {
            for(window:Window in windowMap.values){
                window.decorView.systemUiVisibility = HIDE_NAVIGATION
            }
        }
    }
}

fun Window.disableAutoFullScreen() {
    decorView.setOnSystemUiVisibilityChangeListener(null)
}