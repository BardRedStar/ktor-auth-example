package com.retroblade.achievo.common.di

interface DIComponent

abstract class FeatureComponentHolder<Component: DIComponent>: BaseComponentHolder<Component>() {
    private var component: Component? = null

    override fun get(): Component {
        return component ?: build()
    }

    override fun set(component: Component?) {
        this.component = component
    }

    protected abstract fun build(): Component
}
