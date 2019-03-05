package catt.mvp.framework.proxy.component

import catt.mvp.framework.presenter.BasePresenter
import catt.mvp.framework.proxy.annotations.DeclaredPresenterInterface
import catt.mvp.framework.proxy.annotations.DeclaredViewInterface
import catt.mvp.framework.proxy.annotations.InjectPresenter
import catt.mvp.framework.proxy.throwables.ProxyArgumentException
import java.lang.ClassCastException

interface ProxyAnalyticalComponent {

    fun Class<*>.getInjectPresenter():InjectPresenter{
        val annotation = getAnnotation(InjectPresenter::class.java)
        annotation?: throw ProxyArgumentException("Must be declaration annotation class InjectPresenter")
        return annotation
    }

    fun newInstancePresenter(inject: InjectPresenter): BasePresenter = Class.forName(inject.value).newInstance() as BasePresenter


    fun Class<*>.getDeclaredPresenterClass(): Class<out Any>{
        interfaces.forEach {
            it.getAnnotation(DeclaredPresenterInterface::class.java)?.apply {
                return asSubclass(it)
            }
        }
        throw ProxyArgumentException("Must be declaration annotation class DeclaredViewInterface")
    }

    fun Class<*>.castPresenter(presenter: BasePresenter): Any{
        val cast = cast(presenter)
        cast ?: throw ClassCastException("View convert error.")
        return cast
    }

    fun <T> getPresenterInterface(): T


    fun Class<*>.getDeclaredViewClass(): Class<out Any>{
        interfaces.forEach {
            it.getAnnotation(DeclaredViewInterface::class.java)?.apply {
                return asSubclass(it)
            }
        }
        throw ProxyArgumentException("Must be declaration annotation class DeclaredViewInterface")
    }
}