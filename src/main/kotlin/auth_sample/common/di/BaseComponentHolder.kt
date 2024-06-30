package auth_sample.common.di

abstract class BaseComponentHolder<C : DIComponent> {

    abstract fun set(component: C?)

    abstract fun get(): C
}