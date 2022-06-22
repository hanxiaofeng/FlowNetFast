package com.xykk.flownetfast

import net.flow.jetpackmvvm.base.BaseApp
import kotlin.properties.Delegates

class MyApp: BaseApp() {

    //单例化的第二种方式：利用系统自带的Delegates生成委托属性
    companion object {
        private var instance: MyApp by Delegates.notNull()
        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}