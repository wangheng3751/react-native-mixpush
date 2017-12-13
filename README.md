# react-native-mixpush

集成小米推送、华为推送、魅族推送及个推推送的React-Native版本!

由于使用任何一种Android推送都很难在APP进程被杀死后收到推送，只有集成各厂商提供的系统级别推送才能完成此任务,故考虑小米、华为、魅族手机使用官方推送，其他手机使用个推推送！

# 安装：

    npm install --save react-native-mixpush-android

# 使用：

    ## 1、android/settings.gradle

    ...
    include ':react-native-mixpush-android'
    project(':react-native-mixpush-android').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-mixpush-android')

    ## 2、app/build.gradle

    manifestPlaceholders = [
            PACKAGE_NAME : "你的包名",
            //测试环境
            GETUI_APP_ID : "个推APPID",
            GETUI_APP_KEY : "个推APPKEY",
            GETUI_APP_SECRET : "个推APPSECRE"
    ]
    dependencies {
        ...
        compile project(":react-native-mixpush-android")
    }

     ## 3、android/build.gradle
     allprojects {
         repositories {
             mavenLocal()
             jcenter()
             ...
             //个推
             maven {
                 url "http://mvn.gt.igexin.com/nexus/content/repositories/releases/"
             }
             //华为推送
             maven {url 'http://developer.huawei.com/repo/'}
         }
     }

     ## 4、AndroidManifest.xml
     ### manifest节点下添加：
        <!--小米推送-->
         <permission android:name="${PACKAGE_NAME}.permission.MIPUSH_RECEIVE" android:protectionLevel="signature" />
         <uses-permission android:name="${PACKAGE_NAME}.permission.MIPUSH_RECEIVE" />
         <!--小米推送END-->

         <!--魅族推送-->
         <!-- 兼容flyme5.0以下版本，魅族内部集成pushSDK必填，不然无法收到消息-->
         <uses-permission android:name="com.meizu.flyme.push.permission.RECEIVE"></uses-permission>
         <permission android:name="${PACKAGE_NAME}.push.permission.MESSAGE" android:protectionLevel="signature"/>
         <uses-permission android:name="${PACKAGE_NAME}.push.permission.MESSAGE"></uses-permission>
         <!--  兼容flyme3.0配置权限-->
         <uses-permission android:name="com.meizu.c2dm.permission.RECEIVE" />
         <permission android:name="${PACKAGE_NAME}.permission.C2D_MESSAGE"
             android:protectionLevel="signature"></permission>
         <uses-permission android:name="${PACKAGE_NAME}.permission.C2D_MESSAGE"/>
         <!--魅族推送END-->

     ### application节点下添加：
        <!--华为推送配置begin-->
        <meta-data   android:name="com.huawei.hms.client.appid"  android:value="你的APPID"/>

     ## 5、注册推送

        ### MainApplication中引用组件：

        import com.duanglink.rnmixpush.MixPushReactPackage;

        protected List<ReactPackage> getPackages() {
              return Arrays.<ReactPackage>asList(
                  new MainReactPackage(),
                  ...
                  new MixPushReactPackage()
              );
            }
          };

          ### MainActivity中注册推送：

          import com.duanglink.huaweipush.HuaweiPushActivity;

          public class MainActivity extends HuaweiPushActivity {
                @Override
                public void onCreate(Bundle savedInstanceState) {
                    savedInstanceState.putString("meizuAppId","魅族AppId");
                    savedInstanceState.putString("meizuAppKey","魅族AppKey");
                    savedInstanceState.putString("xiaomiAppId","小米AppId");
                    savedInstanceState.putString("xiaomiAppKey","小米AppKey");
                    super.onCreate(savedInstanceState);
                }
                ...
          }

        ## 6.React-Native客户端接收事件：

        var { NativeAppEventEmitter } = require('react-native');
        this.receiveRemoteNotificationSub = NativeAppEventEmitter.addListener(
                'receiveRemoteNotification',
                (notification) => {
                        Alert.alert('消息通知',JSON.stringify(notification));
                        break;
                    }
        );




