
<p align="center"><img src="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.huabanimg.com%2Fc8c0c98749dadff0a55f0dda33433f2bde7040fe12c4e-1GDsbS_fw658&refer=http%3A%2F%2Fhbimg.huabanimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652497061&t=ce7f9f0622ca3361d9cdc68cd731663f" width="300"/>

<p align="center"><strong>基于mvvm，kotlin协程(flow)基础开发库</strong></p>

<p align="center">
<img src="https://img.shields.io/badge/language-kotlin-orange.svg"/>
<img src="https://img.shields.io/badge/license-Apache-blue"/>
</p>

<br>

主要功能

- activity/fragment基类封装
- Kotlin协程
- DSL作用域编程
- mvvm
- 网络请求封装
- loading处理
- liveData数据处理
<br>

## 安装

```groovy
repositories {
    mavenCentral()
}
```

module 的 build.gradle 添加依赖：

```groovy
implementation 'io.github.hanxiaofeng:flownet-flow:1.0.2'
```

## 使用

#### 1.新建apiService

#### 1.基于Activity、fragment使用方式

```
① 创建requestViewModel继承自BaseViewModel
class RequestMainViewModel: BaseViewModel() {

    private val _websiteResult = MutableStateFlow<ResultState<UsuallyWebSites>>(ResultState.onEmpty())
    val websiteResult:StateFlow<ResultState<UsuallyWebSites>> = _websiteResult

    fun postWebSiteRequest(){
         request(scope,{ apiService.website()},_websiteResult,true)
    }

}

② 发起请求
requestMainViewModel.postWebSiteRequest(lifecycleScope)

③ 接收响应
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED){
        requestMainViewModel.websiteResult.collect {
            parseState(it,{ websites ->
                websites.toString().loge()
            },{ error ->
                error.message?.loge()
            })
        }
    }
}

```

#### 2.全局请求（不依赖Activity、fragment）

```
requestGlobal({ apiService.website()},{
    //todo something
}, showLoading = true)
```

## 运行demo

直接clone项目到本地，然后项目根目录创建gradle.properties文件，并添加如下内容即可：

```
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.enableJetifier=true
kotlin.code.style=official
```

## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
