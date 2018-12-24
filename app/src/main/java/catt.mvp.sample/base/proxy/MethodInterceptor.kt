package catt.mvp.sample.base.proxy

import android.util.Log.e
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class MethodInterceptor : InvocationHandler {
    private var target: Any? = null

    fun bind(target: Any): Any {
        this.target = target
        return Proxy.newProxyInstance(target::class.java.classLoader, target::class.java.interfaces, this@MethodInterceptor)
    }

    @Throws(Exception::class)
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        e("BBB", "事物开始, proxy=$proxy, method=$method, args=$args")
        val invoke: Any? = when (args != null) {
            true -> method?.invoke(target, args)
            false -> method?.invoke(target)
        }
        e("BBB", "事物结束, proxy=$proxy, method=$method, args=$args, invoke=$invoke")
        return invoke
    }
}