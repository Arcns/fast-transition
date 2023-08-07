# fast-transition

[![](https://jitpack.io/v/Arcns/fast-transition.svg)](https://jitpack.io/#Arcns/fast-transition)


> 最近小伙伴有个需求，就是实现类似于小红书、Lemon8的共享元素转场效果，查了一圈发现并没有特别合适的Library，于是便做了一个开源Library项目，方便大家集成后，一行代码实现Android仿小红书、Lemon8的共享元素转场效果。

![Lemon8的共享元素转场效果](./image/lemon8.gif)

#### 1.实现思路：

经过分析，如果要实现上图的效果，我们需要解决以下问题：

- （1）实现自定义的共享元素：包括圆角过渡，TextView过渡，不同图片间过渡等
- （2）实现多个连续页面的共享元素过渡（Q及以上系统，3个及以上连续的activity拥有共享元素动画时，会有共享元素动画丢失的BUG）
- （3）实现拖拽退出效果

问题已经分析出来了，接下来我们逐个解决：

- （1）实现自定义的共享元素我们通过自定义`Transition`，在`createAnimator`中返回响应的动画来实现
- （2）实现多页面的共享元素过渡我们通过反射修复BUG（如果你对反射有顾虑或没有该功能场景，则不需关注该方式）
- （3）实现拖拽退出效果，这里我通过另外一个开源项目来更加完整的解决该问题：[FastDragExitLayout](https://github.com/Arcns/arc-fast#%E5%8D%81%E4%B8%80fast-dragexitlayout)

#### 2.集成方式：

```
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```

```
 // 注意：本Library基于androidx
 implementation 'com.github.Arcns:fast-transition:latest.release'
 // 可选：如果你需要使用FastRoundedItem（圆角的共享元素动画），那么你项目中需要引入fast rounded
 implementation 'com.github.Arcns.arc-fast:rounded:yourversion'
 // 可选：如果你需要使用FastDisposableFastTextViewItem（渐变消失的图片文本视图），那么你项目中需要引入fast textview
 implementation 'com.github.Arcns.arc-fast:text-view:yourversion'
```

#### 3.使用方式

本Library对共享元素转场的配置进行了简化，减少了使用的复杂度，在简单场景集成时仅需两步：

- （1）在转场`开始页`跳转到`目标页`时，使用`FastTransitionViewManager`配置共享元素动画和启动`目标页`

```
// 在开始页 StartActivity.kt
// 跳转到目标页
fun goTarget(){
     // 1、添加需要参与转场的共享元素并配置所需动画
    val fastTransitionViewManager = FastTransitionViewManager()
    fastTransitionViewManager.addView(
        "IMAGE", // 共享元素的key
        ivImage, // 共享元素view
        FastRoundedItem(FastRoundedValue(12f.dpToPx)),//共享元素动画：圆角动画
        FastSystemTransitionItem(FastSystemTransitionType.ChangeImageTransform),//共享元素动画：图片切换动画
        ... // 可以配置更多动画
    )
    fastTransitionViewManager.addView(...)
    fastTransitionViewManager.addView(...)
    fastTransitionViewManager.addView(...) // 可以添加更多需要参与转场的共享元素
    // 2、通过startActivity启动目标页
    fastTransitionViewManager.startActivity(
        activity = this, // 当前页activity
        targetActivityCLass = TargetActivity::class.java, // 目标页Class
        targetDataID = "1", // 可选：目标页对应的数据ID，默认为null
        applyIntent = { intent-> 
            // 可选：intent回调，你可以在这里为intent添加更多数据
        }
    )
}
```

- （2）在到`目标页`的onCreate中，使用`FastTransitionTargetManager`配置与`开始页`对应的共享元素并应用转场动画

```
// 在目标页 TargetActivity.kt
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val transitionTargetManager = FastTransitionTargetManager.getManager(this)
    // 1、配置与`开始页`对应的共享元素
    transitionTargetManager?.setTransitionView(
         "IMAGE", // 与`开始页`对应的共享元素的key
         ivImage // 与`开始页`对应的共享元素view
    )
    transitionTargetManager?.setTransitionView(...)
    // 2、应用转场动画
    transitionTargetManager?.applyTransitionEnterAndReturnConfig(
        duration = 150, // 可选：转场动画时长，默认为150,
        postponeEnterTransition = false, // 可选：是否暂停转场动画，直到用户调用startTransitionEnter再开始转场动画，默认为false
        postponeEnterTransitionTimeout = 500, // 可选：如果暂停转场动画，那么达到该超时时间仍未调用startTransitionEnter时，管理器将自动开始转场动画
        pageCurrentScale = { 1f }, // 可选：返回当前页面的缩放比例，该方法一般用于与拖拽退出结合使用，默认为null
        onTransitionEnd = {
            // 可选：转场动画结束的回调，默认为null
        }
,
    )
}
// 可选：修复Q及以上系统，activity调用onStop后共享元素动画丢失的BUG
override fun onStop() {
    transitionTargetManager?.onStop()
    super.onStop()
}
```

#### 4.修复多个连续页面的共享元素过渡时，共享元素动画丢失的BUG

注意：该BUG需要使用反射进行修复，截至到目前最新的API 33，该方法仍然能够有效修复该BUG，但如果你对反射有顾虑或没有该功能场景，则不要使用以下方法。

```
// 在自定义Application MyApplication.kt
class MyApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // 启用多个连续页面的共享元素过渡功能
        FastTransitionUtils.enableMultipleActivityTransition(this)
    }
}
```

```
// 在目标页 TargetActivity.kt
// 修复Q及以上系统，3个及以上连续的activity拥有共享元素动画时，共享元素动画丢失的BUG（使用反射）
override fun finishAfterTransition() {
    transitionTargetManager?.finishAfterTransition()
    super.finishAfterTransition()
}
```

#### 5.支持的动画

本Library在原有系统自带的共享元素动画基础上，扩展了一些常用的动画效果，所有内置动画如下： 
| 动画名 | 简介 | 
| ------ | ------ | 
| FastTextViewItem | TextView的共享元素动画，能够实现文字大小、颜色、行高、间距、粗体等属性的过渡动画 | 
| FastRoundedItem | 圆角的共享元素动画（需要使用圆角控件[FastRounded](https://github.com/Arcns/arc-fast#%E5%85%ABfast-rounded)） | 
| FastToggleImageViewItem | 支持根据状态切换图片的共享元素动画，通常用于点赞、收藏、关注等需要根据状态同时切换`开始页`与`目标页`图片的场景 | 
| FastSimpleItem | 可实现渐变或缩放的简单共享元素动画，通常用于那些内部不一致的共享元素容器控件，避免内部不同导致过渡时突兀 | 
| FastImageItem | 支持切换图片或背景的共享元素动画，通常用于不同图片间的过渡，该动画会通过渐变效果的过渡到另一张图片 | 
| FastBackgroundFadeItem | 背景渐变显示或隐藏的共享元素动画，通常用于只有一边有背景而另一边没有的场景 | 
| FastDisposableFastTextViewItem | [FastTextView](https://github.com/Arcns/arc-fast#%E4%B9%9Dfast-textview)渐变消失的共享元素动画，该动画会在目标页面创建相同控件以完成渐变消失动画 |
| FastSystemTransitionItem | 系统自带的共享元素动画，用于实现在本库中使用系统自带的动画 |

#### 6.扩展自定义的动画

如果内置的动画不符合你的需求场景，或者你需要让你的其他控件也参与共享元素动画，那么你可以扩展自定义的动画.
本Library对扩展自定义的动画也进行了简化，通常情况下你只需两步即可实现扩展自定义的动画：

- （1）创建自定义的动画计算器

```
// 1.1 继承FastBaseCalculator<计算器的数据类型，控件的类型>
class CustomCalculator(
    _first: Float,// 动画起始数据，演示用法，你可以按需替换为你自己的构造参数
    _last: Float, // 动画结束数据，演示用法，你可以按需替换为你自己的构造参数
) : FastBaseCalculator<Float, View>( // 此处<Float, View>仅为演示用法，你可以按需替换为你自己的数据类型与控件类型
    viewClass = View::class,
    first = _first,
    last = _last
) {
    // 1.2 返回动画起始数据与结束数据的差额
    override val differ: Float by lazy { 
         last - first // 此处仅为演示用法，你可以按需替换为你自己的差额计算方式
    }

    // 1.3 返回某个进度下的动画数据（progress的区间为0至1）
    override fun getValue(progress: Float): Float =
        calculatorFloatValue(first, last, differ, progress) // 此处仅为演示用法，你可以按需替换为你自己的进度数据计算方式

    // 1.4 把某个进度下的动画数据设置到你的控件中
    override fun setView(view: View, progress: Float, value: Float) {
        // 此处仅为演示用法，你可以按需替换为你自己的控件设置方式
        view.alpha = value
    }
}
```

- （2）创建自定义的动画Item，并返回上一步的动画计算器

```
@Parcelize
data class CustomItem(
    var start: Float,
    var end: Float,
) : FastTransitionItem() {

    // 返回动画计算器
    override fun getCalculator(
        isEnter: Boolean,
        pageCurrentScale: Float?
    ): FastSimpleCalculator {
        // 此处仅为演示用法，你可以按需替换为你自己的计算器构建及返回方式
        return if (isEnter) CustomCalculator(start, end)
        else CustomCalculator(end, start)
    }
    
    // 可选：校验动画Item当前是否可用，如果不可用将不调用getCalculator
    override val enable: Boolean get() = start != end && start >= 0f && end >= 0f // 此处仅为演示用法，你可以按需替换为你自己的校验方法
    
    // 可选：视图动画准备（下一步将创建计算器）的回调，您可以在此处进行视图相关的初始化，例如根据视图准备目标页对应的共享元素数据
    override fun onViewAnimReady(isEnter: Boolean, view: View, pageCurrentScale: Float?) {
        // 此处仅为演示用法，你可以按需替换为你自己的初始化方法
        end = view.alpha
    }
    
    // 可选：执行进入动画前的回调，您可以在此进行进入动画前的初始化工作
    override fun onEnterBefore(activity: Activity, transitionConfig: FastTransitionConfig) {
    }

    // 可选：执行离开动画前的回调，您可以在此进行离开动画前的初始化工作
    override fun onReturnBefore(view: View, pageCurrentScale: Float?) {
    }
}
```