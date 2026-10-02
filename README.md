# PowerMenu Helper (电源菜单助手)

**减少物理电源按键的负担：从默认系统电源菜单关机、重启，以及一键锁屏。**

Reduce the load on your physical power button: open the default system power
menu (power off / restart) and lock the screen with a single tap — no
long-press on the side key, no root required.

> 本项目为原创实现，灵感来自同类工具类应用。代码、界面与图标均为原创，遵循 MIT 协议开源。

## 功能 Features

| 功能 | 说明 |
|---|---|
| 打开电源菜单 | 通过无障碍服务触发系统电源对话框，从中选择关机 / 重启 / 紧急呼叫 |
| 一键锁屏 | 使用系统全局锁屏操作（Android 9+），无需设备管理员 |
| 快捷设置磁贴 | 下拉通知栏点击「电源菜单」磁贴，任意界面一键呼出 |
| 触感反馈 | 触发操作时轻微振动，确认动作已执行 |

## 系统要求

- Android 8.0（API 26）及以上
- 需要开启无障碍服务（仅用于执行「打开电源菜单 / 锁屏」两个全局操作）

## 为什么需要无障碍权限？

「打开系统电源菜单」和「锁屏」是系统级全局操作，Android 仅允许无障碍服务
（或设备管理员等特殊身份）调用。本应用：

- **不读取、不收集、不上传任何屏幕内容**（`canRetrieveWindowContent=false`）
- 不申请网络权限，完全离线运行
- 无广告、无追踪、无任何第三方 SDK

## 构建 Build

```bash
./gradlew assembleDebug
# 产物: app/build/outputs/apk/debug/app-debug.apk
```

无需 Android Studio，任何 JDK 17 + Android SDK 34 环境均可编译。

## 安装使用

1. 下载 APK 并安装（需允许未知来源安装）
2. 打开应用，点击「去开启无障碍服务」
3. 在系统设置中启用「电源菜单助手」
4. 返回应用，点击按钮即可打开电源菜单或锁屏

## 常见问题：提示「受限制的设置」

Android 13（API 33）及以上系统默认**拦截侧载应用开启无障碍服务**——这是系统安全
策略，所有侧载 APK 都会受影响，并非本应用的 bug。解决方法：

1. 打开系统设置 → 应用管理 → **电源菜单助手**（v1.0.1 起可从应用内一键跳转）
2. 点击右上角 **「⋮」菜单**
3. 点击 **「允许受限制的设置」**（部分系统需验证锁屏密码）
4. 返回应用，重新点「去开启无障碍服务」

## 隐私 Privacy

本应用不联网、不收集数据、不包含任何分析 SDK。无障碍服务仅在你主动点击时
执行全局操作，全部代码开源可审计。

## License

[MIT](LICENSE)
