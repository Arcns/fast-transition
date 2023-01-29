# fast-transition
[![](https://jitpack.io/v/com.gitee.arcns/fast-transition.svg)](https://jitpack.io/#com.gitee.arcns/fast-transition)


> 最近小伙伴有个需求，就是实现类似于小红书、Lemon8的共享元素转场效果，查了一圈发现并没有特别合适的Library，于是便做了一个开源Library项目，方便大家集成后，一行代码实现Android仿小红书、Lemon8的共享元素转场效果。

![Lemon8的共享元素转场效果](./image/lemon8.gif)

#### 1.实现思路：
经过分析，如果要实现上图的效果，我们需要解决以下问题：
- （1）实现自定义的共享元素：包括圆角过渡，TextView过渡，不同图片间过渡等
- （2）实现多个连续页面的共享元素过渡（Q及以上系统，3个及以上连续的activity拥有共享元素动画时，会有共享元素动画丢失的BUG）
- （3）实现拖拽退出效果

问题已经分析出来了，接下来我们逐个解决：
- （1）实现自定义的共享元素我们通过自定义`Transition`，在`createAnimator`中返回响应的动画来实现
- （2）实现多页面的共享元素过渡我们通过反射修复BUG（如果你对反射有顾虑或没有该功能场景，则不需要考虑该问题）
- （3）实现拖拽退出效果，这里我通过另外有个开源项目来完整解决该问题[Fast DragExitLayout](https://github.com/Arcns/arc-fast#%E5%8D%81%E4%B8%80fast-dragexitlayout)

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
 implementation 'com.gitee.arcns:fast-transition:latest.release'
```

#### 3.使用方式
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
```
#### 4.支持的动画
所有支持的动画如下：
| 动画名 | 简介 |
| ------ | ------ |
| FastTextViewItem | TextView的共享元素动画，能够实现文字大小、颜色、行高、间距、粗体等属性的过渡动画 |
| FastRoundedItem | 圆角的共享元素动画（需要使用圆角控件[Fast Rounded](https://github.com/Arcns/arc-fast#%E5%85%ABfast-rounded)） |
| FastToggleImageViewItem | 支持根据状态切换图片的共享元素动画，通常用于点赞、收藏、关注等需要根据状态同时切换`开始页`与`目标页`图片的场景 |
| FastSimpleItem | 可实现渐变或缩放的简单共享元素动画，通常用于那些内部不一致的共享元素容器控件，避免内部不同导致过渡时突兀 |
| FastImageItem | 支持切换图片或背景的共享元素动画，通常用于不同图片间的过渡，该动画会通过渐变效果的过渡到另一张图片 |
| FastBackgroundFadeItem | 背景渐变显示或隐藏的共享元素动画，通常用于只有一边有背景而另一边没有的场景 |
| FastBackgroundFadeItem | [FastTextView](https://github.com/Arcns/arc-fast#%E4%B9%9Dfast-textview)渐变消失的共享元素动画，该动画会在目标页面创建相同控件以完成渐变消失动画 |
| FastSystemTransitionItem | 系统自带的共享元素动画，用于实现在本库中使用系统自带的动画 |

#### 5.扩展自定义动画