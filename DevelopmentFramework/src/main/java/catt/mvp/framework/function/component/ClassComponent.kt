package catt.mvp.framework.function.component

import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

fun <T> Class<*>.newInstanceOrigin(indexType: Int = 0): T =
    ((this.genericSuperclass as ParameterizedType).actualTypeArguments[indexType] as Class<*>).newInstance() as T


fun <T> Class<*>.generatedTypeClass(): Class<T> =
    when (isInterface) {
        true -> generatedType() as Class<T>
        false -> generatedType() as Class<T>
    }

fun <T> Class<*>.generatedArrayTypeClass(): Class<T> =
    (generatedType() as GenericArrayType).genericComponentType as Class<T>

fun Class<*>.generatedType(): Type =
    when (isInterface) {
        true -> (genericInterfaces[0] as ParameterizedType).actualTypeArguments[0]
        false -> (genericSuperclass as ParameterizedType).actualTypeArguments[0]
    }