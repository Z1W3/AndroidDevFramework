package catt.mvp.sample.base.proxy

interface IProxyLifecycle<T> {
    fun injectProxyImpl(): ILifecycle<T>

    val proxy: ILifecycle<T>

//    fun <V> proxy(): ILifecycle<T>
}