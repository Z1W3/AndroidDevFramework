package catt.mvp.framework.app

import android.view.View
import android.view.Window
import java.lang.ref.SoftReference

private const val HIDE_NAVIGATION = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        or View.SYSTEM_UI_FLAG_FULLSCREEN)

private val windowMap : HashMap<Int, SoftReference<Window>> by lazy { HashMap<Int, SoftReference<Window>>() }

fun Window.setFullScreen() {
    addWindowCache()
    setSystemUiVisibility(HIDE_NAVIGATION)
}

private fun Window.setSystemUiVisibility(flag : Int){
    if(decorView.systemUiVisibility != flag){
        decorView.systemUiVisibility = flag
    }
}

private fun Window.addWindowCache(){
    windowMap[hashCode()] = SoftReference(this)
}

fun Window.removeWindowCache(){
    windowMap.remove(hashCode())
}

fun Window.enableAutoFullScreen() {
    decorView.setOnSystemUiVisibilityChangeListener { visible ->
        if (visible != 6) {
            windowMap.values.forEach { soft ->
                if(soft.get() == null){
                    this.setSystemUiVisibility(HIDE_NAVIGATION)
                }
                else{
                    soft.get()?.setSystemUiVisibility(HIDE_NAVIGATION)
                }
            }
        }
    }
}

fun Window.disableAutoFullScreen() {
    decorView.setOnSystemUiVisibilityChangeListener(null)
    removeWindowCache()
}