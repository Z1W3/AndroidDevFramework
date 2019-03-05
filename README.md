# AndroidDevelopmentFramework
### [LICENSE](https://github.com/LuckyCattZW/AndroidDevelopmentFramework/blob/add-license-1/LICENSE)

### 简介
- 采用代理类的形式实现Activity、Fragment、DialogFragment
- 每个代理类均采用MVP的设计模式
- 每个代理类绑定的Presenter以注解的形式注入代理类中
- 网络通信采用retrofit2
- 集成像素比例适配方案

### 初始化
```kotlin
    import catt.mvp.framework.initializeDevelopmentFrameworks
    import catt.mvp.framework.initializeNetwork

    class GlobalApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            /*
               第一个参数，传入上下文
               第二个参数传入适配的像素
             */
            initializeDevelopmentFrameworks(applicationContext, "1920x1080,2046x1536")
            
            
            initializeNetwork(
                            headerInterceptor = HeaderInterceptor(),
                            loggingInterceptor = LoggingInterceptor()
                        )
        }
    }
```


### 1 委托类
#### 1.1 创建委托类
委托类(Activity、Fragment、DialogFragment)均需分别继承相应的Base类
```kotlin
    //Your Activity
    class MainActivity : catt.mvp.framework.app.BaseActivity()
    
    //Your Fragment
    class MainFragment : catt.mvp.framework.app.BaseFragment()
    
    //Your DialogFragment
    class MainDialogFragment : catt.mvp.framework.app.BaseDialogFragment()
```

#### 1.2 委托类中的方法
委托类(Activity、Fragment、DialogFragment)中需要实现的方法

```kotlin
    //pageLabel() 只有继承了BaseFragment与BaseDialogFragment
    //才会出现，这个是给友盟的页面标注使用的
    override fun pageLabel(): String = "演示DialogFragment"

    override fun injectLayoutId(): Int = R.layout.xxxx_main

    override fun injectProxyImpl(): ILifecycle<MainDialogFragment/*当前委托类*/> {
        return MainImpl()//该委托类的代理类
    }
```

### 2 代理类
#### 2.1 创建代理类
代理类根据委托类的类型分别继承相应的Base类
```kotlin
    //Activity委托代理的实现类需要继承ProxyBaseActivity, 泛型内传入该Activity的实际委托类
    class MainActivityImpl : ProxyBaseActivity<MainActivity>()

    //Fragment委托代理的实现类需要继承ProxyBaseFragment, 泛型内传入该Fragment的实际委托类
    class MainFragmentImpl : ProxyBaseFragment<MainFragment>()

    //DialogFragment委托代理的实现类需要继承ProxyBaseDialogFragment, 泛型内传入该DialogFragment的实际委托类
    class MainDialogFragmentImpl : ProxyBaseDialogFragment<MainDialogFragment>()
```

#### 2.2 代理类MVP
##### 2.2.1 创建V层与P层接口
建立V层与P层接口,并使用注解是声明
```kotlin
interface IMainActivity {
    //需要对View层实现添加@DeclaredViewInterface注解
    //使Presenter能扫描到V层接口
    @DeclaredViewInterface
    interface View {
        fun onExampleMethod(content: String)
        
    }
    
    //需要对Presenter层实现添加@DeclaredPresenterInterface注解
    //使View层能扫描到P层接口
    @DeclaredPresenterInterface
    interface Presenter {
        fun setExampleMethod()
    }
}
```
##### 2.2.2 代理类实现V层接口
代理类实现V层接口
```kotlin
    class MainActivityImpl : catt.mvp.framework.proxy.ProxyBaseActivity<MainActivity>(), IMainActivity.View{
        //Your View Interface Method
        // ....
    }

    //Fragment委托代理的实现类需要继承ProxyBaseFragment, 泛型内传入该Fragment的实际委托类
    class MainFragmentImpl : catt.mvp.framework.proxy.ProxyBaseFragment<MainFragment>(), IMainFragment.View{
        //Your View Interface Method
        // ....
    }

    //DialogFragment委托代理的实现类需要继承ProxyBaseDialogFragment, 泛型内传入该DialogFragment的实际委托类
    class MainDialogFragmentImpl : catt.mvp.framework.proxy.ProxyBaseDialogFragment<MainDialogFragment>(), IMainDialogFragment.View{
        //Your View Interface Method
        // ....
    }
```
##### 2.2.3 创建P层类并实现P层接口
```kotlin
    //每个Presenter均需继承BasePresenter，并实现2.2.1中的P层接口
    class MainActivityPresenter : catt.mvp.framework.presenter.BasePresenter(), IMainActivity.Presenter{
        //Your Presenter Interface Method
        // ....
    }
```
##### 2.2.4 代理类绑定Presenter
```kotlin
    // 对需要绑定Presenter的代理类添加@InjectPresenter注解
    // value = catt.mvp.sample.presenter.MainActivityPresenter
    // 是Presenter的全类名
    @InjectPresenter(value = "catt.mvp.sample.presenter.MainActivityPresenter")
    class MainActivityImpl : ProxyBaseActivity<MainActivity>(), IMainActivity.View
```

#### 2.3 代理类MVP的使用

##### 2.3.1 V层调用P层
代理类内部中调用如下方法
```kotlin
    //sample
    class MainDialogFragmentImpl : ProxyBaseDialogFragment<MainDialogFragment>(), IMainDialogFragment.View{
        //Your View Interface Method
        // ....
        
        override fun onContent(content:String){
            //TODO SOMETHING.
            //example: textView.text = content
        }
        
        override fun onViewCreated(view: View, savedInstanceState: Bundle?){
            view.findViewById<Button>(R.id.click_btn).setOnClickListener{
                //可以调用该方法回调Presenter的实现接口
                //泛型中的类型根据2.2.1中定义的P层接口而决定
                getPresenterInterface<IMainDialogFragment.Presenter>().setContent()               
            }
        }
    }
```

##### 2.3.2 P层调用V层
```kotlin
    class MainDialogPresenter : BasePresenter(), IMainDialogFragment.Presenter {
        //Your Presenter Interface Method
        // ....
            
        override fun setContent() {
            getViewInterface<IMainDialogFragment.View>().onContent("This is message for MainDialogPresenter")
        }
    }
```

#### 2.4 代理类调用或切换Fragment或Dialog
可以直接通过代理类调用
```kotlin
    //动态替换Activity中的FrameLayout
    fragmentTransaction?.commitFragment(R.id.container_layout, MainFragmentImpl::class.java.newInstanceOrigin())
    
    //调用DialogFragment
    MainDialogFragmentImpl::class.java.newInstanceOrigin<DialogFragment>().show(fragmentTransaction!!)
    
```

#### 2.5 代理类中互相通信

##### 2.5.1 AndroidEventBus
可以使用[@AndroidEventBus](https://github.com/LuckyCattZW/AndroidEventBusStudio)进行通信，具体请看链接
```kotlin
    //注册
    EventBus.getDefault().register(this)
    //注销
    EventBus.getDefault().unregister(this)
     
    //接收
    @Subscriber(tag = "YOUR_EVENT_BUS_TAG")
    private fun getUser(user:User){
        //TODO SOMETHING
    }
    
    //发送
    EventBus.getDefault().post(User("LuckyCatt", 30, 110101209900000019), "YOUR_EVENT_BUS_TAG")
```

##### 2.5.2 Fragment之间采用arguments进行消息传递
```kotlin
    //Fragment arguments
    val fragment = MainFragmentImpl::class.java.newInstanceOrigin<Fragment>().apply{
        arguments = Bundle().apply { 
            putParcelable("KEY_USER", User())
        }
    }
    fragmentTransaction?.commitFragment(R.id.container_layout, fragment)
    
    //DialogFragment arguments
    MainDialogFragmentImpl::class.java.newInstanceOrigin<DialogFragment>().apply{
        arguments = Bundle().apply { 
            putParcelable("KEY_USER", User())
        }
    }.show(fragmentTransaction!!)
```

### 3 M层网络通信
-  [@retrofit2](https://github.com/square/retrofit)创建服务接口 
```kotlin
    //创建retrofit2服务接口
    val dggService:IDggStoreService by lazy { OkRft.create(IDggStoreService::class.java) }
    
```
- 访问网络与返回
```kotlin
    dggService.getLotteryTypes().callJsonArrayResponse(result = object : SimpleCallResult<Array<LotteryTypesBean>>(){
        override fun onCheckLocalWifi() {
        }

        override fun onFailure(code: Int, ex: Throwable) {
            super.onFailure(code, ex)
        }

        override fun onResponse(response: Array<LotteryTypesBean>) {
            println("response.size = ${response.size}")
        }
    }, coroutine = this@MainPresenter)


    dggService.getLotteryList("1", 0).callJsonObjectResponse(result = object : SimpleCallResult<LotteryListBean>(){
        override fun onCheckLocalWifi() {
        }
        
        override fun onFailure(code: Int, ex: Throwable) {
            super.onFailure(code, ex)
        }
        
        override fun onResponse(response: LotteryListBean) {
            println("onResponse: $response")
        }
    }, coroutine = this@MainPresenter)
```
- 定义Response Bean
```kotlin
    //定义基础返回类型字段
    //目前root json element共四个字段
    // {
    //      "code": 200,            //返回code
    //      "msg": "成功返回",     //返回结果
    //      "timestamp": "1547891520",    //时间戳
    //      "data": ""                 //Bean数据
    // }
    @JsonCallField 
    class LotteryListBean
    
    @JsonCallField
    
    // 可以指定接收data字段内的子元素层
    // parent field -> data
    // hierarchy -> accountVo/userVo/safezoneInfo
    // 接收的JSON String，会直接截取至safezoneInfo字段
    @JsonCallDataTargetField(hierarchy = "accountVo/userVo/safezoneInfo")
    class LotteryTypesBean
```
PS:使用retrofit2需要注意的地方，bean中的字段不得少于返回的JSON String字段,否则retrofit2会抛出异常

### 4 配置项

#### 4.1 gradle.properties
根目录的gradle.properties
```groovy
###################签名配置###################
JKS_ALIAS=demo
JKS_PASSWORD=123456
JKS_STORE_PASSWORD=123456
JKS_STORE_FILE=../keystore/demo.jks

###################配置Glide#####################
#glide缓存路径
glide_cache_path=.OwnGlideCache
#glide缓存大小 24mb
glide_cache_memory=1024L * 1024L * 24L

###################配置友盟统计Umeng#######################
umeng_secret_key=
umeng_app_identity=
umeng_channel=

###################配置网络通信Base-url#######################
produce_base_url=http://www.xxx.zzz.com/
test_base_url=http://dev.xxx.zzz.com/
```

### 5 其他
#### 5.1 [@像素比适配方案](https://github.com/LuckyCattZW/CompatLayoutAdapter)
#### 5.2 彩色、可快速替换的[@Toast](https://github.com/LuckyCattZW/Toasty)