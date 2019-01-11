package catt.mvp.framework.proxy

interface IProxy {
    fun injectProxyImpl(): ILifecycle<*>

    val proxy: ILifecycle<*>
}