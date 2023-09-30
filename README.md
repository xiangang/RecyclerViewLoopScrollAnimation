# 1.RecyclerViewLoopScrollAnimation项目介绍
 > 🍎 `RecyclerViewLoopScrollAnimation` 适用于Android RecyclerView的循环滚动动画的帮助类，可实现类似于老虎机抽奖，数字滚动等效果。

[![](https://jitpack.io/v/xiangang/RecyclerViewLoopScrollAnimation.svg)](https://jitpack.io/#xiangang/RecyclerViewLoopScrollAnimation) [![Android CI](https://github.com/xiangang/RecyclerViewLoopScrollAnimation/actions/workflows/android.yml/badge.svg?branch=main)](https://github.com/xiangang/RecyclerViewLoopScrollAnimation/actions/workflows/android.yml)

# 2. 效果展示

git太大，压缩的厉害，导致帧率太低，建议下载工程运行查看实际效果。

![演示图](https://github.com/xiangang/RecyclerViewLoopScrollAnimation/blob/main/demo.gif)

# 3. 使用方法：

第一步：
在你的根目录中的  build.gradle 文件中，repositories 标签下添加jitpack maven仓库：

Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
    	...
    	maven { url 'https://jitpack.io' }
    }
}

```
若使用Gradle 7.0 ，则在setting.gradle中的dependencyResolutionManagement的repositories标签中添加：
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
		...
        maven { url 'https://jitpack.io' }
        ...
    }
}
...

```

第二步：
添加依赖
Add the dependency

```
dependencies {
	implementation 'com.github.xiangang:RecyclerViewLoopScrollAnimation:v1.0.0-alpha02'
}

```

# 4. 使用示例
使用实例如下：
```kotlin
//创建一个RecyclerViewLoopScrollAnimation实例
val recyclerViewLoopScrollAnimation = RecyclerViewLoopScrollAnimation()
//创建一个Configuration
val recyclerViewLoopScrollAnimationConfiguration = RecyclerViewLoopScrollAnimation.build {
    	scrollAnimatorDuration = 4000L
        }
//设置Configuration
recyclerViewLoopScrollAnimation.setConfiguration(recyclerViewLoopScrollAnimationConfiguration)
//将recyclerViewLoopScrollAnimation关联到RecyclerView
recyclerViewLoopScrollAnimation.attachToRecyclerView(holder.recyclerView)
//配合LooperLinearLayoutManager实现无限滚动
val looperLinearLayoutManager = LooperLinearLayoutManager(context, RecyclerView.VERTICAL, false)
recyclerView.layoutManager = looperLinearLayoutManager
//设置滚动Action，用于滚动时返回当前显示View的Position
recyclerViewLoopScrollAnimation.setRecyclerViewScrollAction(object:RecyclerViewScrollAction {
    override fun findFirstVisibleItemPosition(): Int {
        return looperLinearLayoutManager.findFirstVisibleItemPosition()
    }

    override fun findLastVisibleItemPosition(): Int {
        return looperLinearLayoutManager.findLastVisibleItemPosition()
    }
})
//开始动画
recyclerViewLoopScrollAnimation.start()
```

更多用法可参考工程里的app。

# 5. 其它

RecyclerViewLoopScrollAnimation.Configuration配置类：

```kotlin
/**
* 配置
*/
class Configuration {
    /**
    * 是否禁止Item相应手指滑动
    */
    var disableItemTouchListener = true

    /**
    * 第一阶段的滚动动画时长
    */
    var scrollAnimatorDuration = 3000L

    /**
    * 第一阶段的滚动动画的插值器
    */
    var scrollAnimatorInterpolator = AccelerateDecelerateInterpolator()

    /**
    * 是否开启弹簧动画
    */
    var enableSpringAnimator = true

    /**
    * 第一阶段的滚动动画针对item的滚动步长（scrollBy方法的y参数）
    */
    var scrollAnimatorScrollByYStep = 30

    /**
    * 第二阶段的弹簧动画的SpringForce
     */
    var springAnimatorForce: SpringForce = SpringForce(0f)
    .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
    .setStiffness(SpringForce.STIFFNESS_VERY_LOW)
    .setDampingRatio(0.2f)
    .setStiffness(350f)

    /**
    * 弹簧动画的起始位置
    */
    var springAnimatorStartValue = -50f

}
```

RecyclerViewLoopScrollAnimation.build的用法为[Function literals with receiver](https://kotlinlang.org/docs/lambdas.html#function-literals-with-receiver)：

```kotlin
/**
* 配置
*/
//创建一个RecyclerViewLoopScrollAnimation实例
val recyclerViewLoopScrollAnimation = RecyclerViewLoopScrollAnimation()
//创建一个Configuration
val recyclerViewLoopScrollAnimationConfiguration = RecyclerViewLoopScrollAnimation.build {
    	scrollAnimatorDuration = 4000L
        }
//设置Configuration
recyclerViewLoopScrollAnimation.setConfiguration(recyclerViewLoopScrollAnimationConfiguration)

```

扩展阅读：[你会用Kotlin实现构建者模式吗？](https://zhuanlan.zhihu.com/p/267145868)



其它接口：

```
/**
* RecyclerView滚动过程中需要执行的Action
*/
fun setRecyclerViewScrollAction(recyclerViewScrollAction: RecyclerViewScrollAction) {
	this.recyclerViewScrollAction = recyclerViewScrollAction
}

/**
* 设置滚动动画监听器
*/
fun setOnSpringAnimationEndListener(onScrollAnimatorListener: Animator.AnimatorListener) {
	this.onScrollAnimatorListener = onScrollAnimatorListener
}

/**
* 设置弹簧动画结束监听器
* 可用于动画结束后执行爆炸粒子效果
*/
fun setOnSpringAnimationEndListener(onAnimationEndListener: DynamicAnimation.OnAnimationEndListener) {
	this.onSpringAnimationEndListener = onAnimationEndListener
}
```



# License

```text
Copyright 2022 xiangang

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

