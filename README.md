#SMSSDK
对[mob](http://www.mob.com/#/)的短信验证二次封装。更简洁的调用，maven直接仓库依赖。

#依赖
`compile 'com.jude:smssdk_mob:1.0.2'`

#示例APP下载
[直接下载](http://7xn7nj.com2.z0.glb.qiniucdn.com/smssdk.apk)

#使用方法
在你需要发送验证码的时候直接使用：

    SMSManager.getInstance().sendMessage(this, "86","18888888888");
    
即可收到短信，然后使用

    SMSManager.getInstance().verifyCode(this, "86", "18888888888", "4812", new Callback() {
                @Override
                public void success() {
                    //验证成功
                }
                
                @Override
                public void error(Throwable error) {
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            });
            
2行代码即完成短信验证。

##重发与等待时间
因为短信验证不能过于频繁。所以默认有60秒的等待时间。用下面代码注册可以收到通知并更新UI。

        SMSManager.getInstance().registerTimeListener(new TimeListener() {
            @Override
            public void onLastTimeNotify(int lastSecond) {
                //剩余时间通知
            }

            @Override
            public void onAbleNotify(boolean valuable) {
                //当前能否发短信的通知
            }
        });
        
        //注意解除通知避免内存泄露
        SMSManager.getInstance().unRegisterTimeListener(this);

具体用法可以参考demo。
可以使用 `SMSManager.getInstance().setDefaultDelay(60)`来设置最短时间。

##使用自己的APPKEY
在`AndroidManifest`的`application`节点下增加

        <meta-data
            android:name="SMS_MOB_APPKEY"
            android:value="xxxxxxx"
            />
        <meta-data
            android:name="SMS_MOB_APPSECRET"
            android:value="xxxxxxx"
            />
            
填上自己的在mob创建的应用的数App Key 与 App Secret即可。  
如果是使用服务器验证也没问题。我也推荐使用这种方式验证，更加安全。发送验证码过后就不需其他处理，让服务器去验证。
