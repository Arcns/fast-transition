# fast-transition
[![](https://jitpack.io/v/com.gitee.arcns/arc-fast.svg)](https://jitpack.io/#com.gitee.arcns/arc-fast)

## 一、介绍
快速实现Android共享元素转场

## 二、集成方式：
```
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```
```
 implementation 'com.gitee.arcns.arc-fast:core:latest.release'
```

## 三、使用方式：
- 简单获取权限
```
 FastPermissionUtil.request(
            activity, // or fragment
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            ... 
        ) { allGranted: Boolean, result: Map<String, FastPermissionResult> ->
            // allGranted：是否全部权限获取成功
            // result：各个权限的获取结果，key为permission，value为获取结果（Granted:同意；Denied:拒绝；DeniedAndDonTAskAgain:拒绝且不再询问）
             if (allGranted) {
                // 全部权限获取成功
            }
        }
```
- 获取权限，并在必要时弹出权限解释说明
```
 FastPermissionUtil.request(
             fragment = this,
            FastPermissionRequest(Manifest.permission.CAMERA,"应用需要相机权限用于扫描"),
            FastPermissionRequest(Manifest.permission.READ_EXTERNAL_STORAGE,"应用需要储存权限用于选择扫描图片"),
        ) { allGranted, result ->
             if (allGranted) {
                // 全部权限获取成功
            }
        }
```
- 使用自定义的弹出权限解释说明弹窗
```
 FastPermissionUtil.showAlertDialog = {activity,message,positiveButton,onPositiveButton,negativeButton,onNegativeButton ->
           // 弹出自定义弹窗
          MyAlertDialogBuilder(activity)
                    .setMessage(message)
                    .setNegativeButton(negativeButton) { _, _ -> onNegativeButton.invoke() }
                    .setPositiveButton(positiveButton) { _, _ -> onPositiveButton.invoke() }
                    .show()
}
```