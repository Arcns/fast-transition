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
- （1）
```

```