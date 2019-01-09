package catt.mvp.sample.base.proxy.annotations


@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class InjectPresenter(val value:String)

@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class DeclaredViewInterface

@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class DeclaredPresenterInterface