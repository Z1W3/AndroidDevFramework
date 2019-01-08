package catt.mvp.sample.base.proxy

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import catt.mvp.sample.base.adm.BaseActivityStack
import catt.mvp.sample.base.function.component.*
import catt.mvp.sample.base.function.helper.PermissionHelper
import catt.mvp.sample.base.presenter.BasePresenter
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * 泛型注释
 * type T, 需要代理实现的Activity
 * type V, View Interface
 * type P, Presenter类,
 *
 * 功能：
 * 获取代理Activity类的对象
 * 对代理Activity类进行弱引用处理
 * 获取Presenter类的对象
 */
abstract class ProxyBaseActivity<T : AppCompatActivity, V, P: BasePresenter<V>>
    : ILifecycle<T>, PermissionHelper.OnPermissionListener, IDialogComponent {

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


    val presenter : P by lazy { (declaredClazz[declaredClazz.size - 1]as Class<P>).getConstructor().newInstance() }

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
        presenter.onAttach(this as V)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

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
        presenter.onDetach()
    }
}
