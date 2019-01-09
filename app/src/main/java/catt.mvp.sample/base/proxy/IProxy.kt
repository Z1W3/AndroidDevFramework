package catt.mvp.sample.base.proxy

interface IProxy {
    fun injectProxyImpl(): ILifecycle<*>

//    fun <T> getProxy() : ILifecycle<T>

    val proxy: ILifecycle<*>
}