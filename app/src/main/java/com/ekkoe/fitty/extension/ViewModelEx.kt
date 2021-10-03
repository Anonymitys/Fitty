package com.ekkoe.fitty.extension

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModelsEx(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): ReadOnlyProperty<Any?, VM> =
    createViewModelProperty(VM::class, { ownerProducer().viewModelStore }, factoryProducer)

@MainThread
fun <VM : ViewModel> Fragment.createViewModelProperty(
    viewModelClass: KClass<VM>,
    storeProducer: () -> ViewModelStore,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): ReadOnlyProperty<Any?, VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelProperty(viewModelClass, storeProducer, factoryPromise)
}

class ViewModelProperty<VM : ViewModel>(
    private val viewModelClass: KClass<VM>,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory
) : ReadOnlyProperty<Any?, VM> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): VM {
        val factory = factoryProducer()
        val store = storeProducer()
        return ViewModelProvider(store, factory).get(viewModelClass.java)
    }
}