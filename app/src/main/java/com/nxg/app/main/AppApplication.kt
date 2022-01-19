package com.nxg.app.main

import android.app.Application
import com.nxg.recyclerview.utils.LogUtil
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AppApplication : Application() {

    companion object {
        const val TAG = "AppApplication"
        private var instance: AppApplication by NotNullSingleValueVar()
        fun instance() = instance
    }

    //定义一个属性管理类，进行非空和重复赋值的判断
    private class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {
        private var value: T? = null
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return value ?: throw IllegalStateException("application not initialized")
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            this.value = if (this.value == null) value
            else throw IllegalStateException("application already initialized")
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        LogUtil.i(TAG, "onCreate ")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        LogUtil.i(TAG, "onLowMemory ")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        LogUtil.i(TAG, "onTrimMemory levent $level")
    }
}