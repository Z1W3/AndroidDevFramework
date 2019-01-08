package catt.mvp.sample.base.proxy

interface IProxy<T> {
    fun injectProxyImpl(): ILifecycle<T>

    val proxy: ILifecycle<T>
}