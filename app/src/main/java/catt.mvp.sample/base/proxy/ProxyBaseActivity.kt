package catt.mvp.sample.base.proxy

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import catt.mvp.sample.base.adm.BaseActivityStack
import catt.mvp.sample.base.app.BaseActivity
import catt.mvp.sample.base.function.component.*
import catt.mvp.sample.base.function.helper.PermissionHelper
import catt.mvp.sample.base.presenter.BasePresenter
import catt.mvp.sample.base.proxy.annotations.DeclaredPresenterInterface
import catt.mvp.sample.base.proxy.annotations.DeclaredViewInterface
import catt.mvp.sample.base.proxy.annotations.InjectPresenter
import catt.mvp.sample.base.proxy.throwables.ProxyArgumentException
import java.lang.ClassCastException
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 *
 * 泛型T，需要被代理的Activity
 */
abstract class ProxyBaseActivity<T : BaseActivity> : ILifecycle<T>, PermissionHelper.OnPermissionListener, IDialogComponent {

    private val injectPresenter:InjectPresenter by lazy {
        val annotation = this@ProxyBaseActivity::class.java.getAnnotation(InjectPresenter::class.java)
        annotation?: throw ProxyArgumentException("Must be declaration annotation class InjectPresenter")
        annotation
    }

    private val presenterInstance by lazy { Class.forName(injectPresenter.value).newInstance() as BasePresenter }

    private val presenterClazz by lazy {
        presenterInstance::class.java.interfaces.forEach {
            it.getAnnotation(DeclaredPresenterInterface::class.java)?.apply {
                return@lazy presenterInstance::class.java.asSubclass(it)
            }
        }
        throw ProxyArgumentException("Must be declaration annotation class DeclaredViewInterface")
    }

    private val castPresenter by lazy {
        val cast = presenterClazz.cast(presenterInstance)
        cast ?: throw ClassCastException("View convert error.")
        cast!!
    }

    fun <T> getPresenterInterface(): T = castPresenter as T

    private val viewClass: Class<out Any> by lazy {
        this@ProxyBaseActivity::class.java.interfaces.forEach {
            it.getAnnotation(DeclaredViewInterface::class.java)?.apply {
                return@lazy this@ProxyBaseActivity::class.java.asSubclass(it)
            }
        }
        throw ProxyArgumentException("Must be declaration annotation class DeclaredViewInterface")
    }

    private var lifecycleState: Lifecycle.State = Lifecycle.State.INITIALIZED

    override val currentLifecycleState: Lifecycle.State
        get() = lifecycleState

    private val declaredClazz: Array<Type>
        get() {
            val genType = javaClass.genericSuperclass
            return (genType as ParameterizedType).actualTypeArguments
        }

    private val activity : T?
        get() = BaseActivityStack.get().search(declaredClazz[0] as Class<T>)

    override val reference: Reference<T>? by lazy {
        WeakReference<T>(activity)
    }

    override val target: T?
        get() = reference?.get()

    override val context: Context?
        get() = reference?.get()?.applicationContext

    internal val fragmentTransaction:FragmentTransaction?
        get() = target?.supportFragmentManager?.beginTransaction()

    override fun onCreate() {
        presenterInstance.onAttach(viewClass, this@ProxyBaseActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    override fun onViewLoadCompleted() {
    }

    open fun onRestart(){}

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onAny(owner: LifecycleOwner) {
        lifecycleState = owner.lifecycle.currentState
    }


    override fun onDestroy() {
        presenterInstance.onDetach()
    }
}
