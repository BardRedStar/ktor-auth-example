package com.retroblade.achievo.common.di

abstract class BaseComponentHolder<C : DIComponent> {

    abstract fun set(component: C?)

    abstract fun get(): C
}