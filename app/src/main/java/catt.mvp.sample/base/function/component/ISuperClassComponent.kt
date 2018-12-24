package catt.mvp.sample.base.function.component

import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import java.lang.reflect.ParameterizedType

interface ISuperClassComponent {

    fun <F : Fragment> Class<*>.generatedActualTypeObject(index: Int, obj: Class<F>): F {
        return ((this.genericSuperclass as ParameterizedType).actualTypeArguments[index] as Class<*>).newInstance() as F
    }

    fun <A : AppCompatActivity> Class<*>.generatedActualTypeObject(index: Int, obj: Class<A>): A {
        return ((this.genericSuperclass as ParameterizedType).actualTypeArguments[index] as Class<*>).newInstance() as A
    }

    fun <D : DialogFragment> Class<*>.generatedActualTypeObject(index: Int, obj: Class<D>): D {
        return ((this.genericSuperclass as ParameterizedType).actualTypeArguments[index] as Class<*>).newInstance() as D
    }
}