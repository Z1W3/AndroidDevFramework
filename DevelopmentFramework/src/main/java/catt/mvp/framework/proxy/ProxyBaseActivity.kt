package catt.mvp.framework.proxy

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentTransaction
import catt.mvp.framework.adm.ActivityStack
import catt.mvp.framework.app.BaseActivity
import catt.mvp.framework.function.component.*
import catt.mvp.framework.function.helper.PermissionHelper
import catt.mvp.framework.proxy.annotations.InjectPresenter
import catt.mvp.framework.proxy.component.ProxyAnalyticalComponent
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 *
 * 泛型T，需要被代理的Activity
 */
abstract class ProxyBaseActivity<T : BaseActivity> : ILifecycle<T>, ProxyAnalyticalComponent, PermissionHelper.OnPermissionListener, IDialogComponent {

    private val injectPresenter: InjectPresenter by lazy {
        this@ProxyBaseActivity::class.java.getInjectPresenter()
    }

    private val presenterInstance by lazy {
        newInstancePresenter(injectPresenter) }

    private val presenterClazz by lazy {
        presenterInstance::class.java.getDeclaredPresenterClass()
    }

    private val castPresenter by lazy {
        presenterClazz.castPresenter(presenterInstance)
    }

    override fun <P> getPresenterInterface(): P = castPresenter as P

    private val viewClass: Class<out Any> by lazy {
        this@ProxyBaseActivity::class.java.getDeclaredViewClass()
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
        get() = ActivityStack.get().search(declaredClazz[0] as Class<T>)

    override val reference: Reference<T>? by lazy {
        WeakReference<T>(activity)
    }

    override val target: T?
        get() = reference?.get()

    override val context: Context?
        get() = reference?.get()?.applicationContext

    val fragmentTransaction:FragmentTransaction?
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
