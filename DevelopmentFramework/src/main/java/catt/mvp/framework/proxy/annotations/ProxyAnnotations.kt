package catt.mvp.framework.proxy.annotations



/**
 * value 必须是全类名，即：包名+类名
 * example: catt.mvp.sample.presenter.XXXXXXPresenter
 */
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class InjectPresenter(val value:String)

/**
 * 声明View层需要实现的接口
 */
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class DeclaredViewInterface

/**
 * 声明Presenter层需要实现的接口
 */
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class DeclaredPresenterInterface