package catt.mvp.sample.base.adm

interface IStack<S> {
    fun push(target: S)

    fun pop()

    fun remove(target: S)

    fun peek() : S?

    fun empty():Boolean

    fun search(target: S) : S?
}