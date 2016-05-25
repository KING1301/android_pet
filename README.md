# android_pet android桌面宠物
##`主要功能`
* 能够`悬浮于桌面`的小宠物，能够`自由拖动具有多种表情动效`，位于桌面不同位置具有不同表情效果，能够`切换宠物模型和自动贴边`，一段时间不触摸可以自动显示文字提示
* `闹钟提醒功能`，能够定时提醒支持多个闹钟和重复提醒以及提醒备注闹钟铃声选择等功能，数据保存采用数据库方式
* 能够`获取微信消息通知`并显示在宠物界面，点击宠物界面的消息提示可以跳转到相应的消息页面
* 扩展功能（待实现）通过蓝牙或网络实现宠物配对  

##实现思路
* 通过android 4.4新增的`NotificationListenerService的onNotificationPosted`实现对系统通知栏消息的监听
* 闹钟提醒使用`AlarmManager`实现
* 桌面宠物使用`WindowManager添加view并处理Ontouch事件`实现，根据触摸坐标和所处状态实现不同的表情变化  


##下载
[apk](android_pet/app/app-release.apk)

