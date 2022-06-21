package com.xykk.flownetfast

import net.flow.jetpackmvvm.base.BaseApp

class MyApp: BaseApp() {

    companion object{
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            MyApp()
        }
    }

    override fun onCreate() {
        super.onCreate()
    }


}