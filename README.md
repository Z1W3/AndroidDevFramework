# MVP辅助框架

### 前言
如果你的项目是一个纯app商业项目，你更应使用以下两种其一的框架进行开发
1. MVVM框架（Databinding+LiveData +ViewModel）的方式
2. Android Jetpack Compose框架

如果你的项目是一个复杂的SDK项目，那么你可以尝试使用这个MVP框架进行处理


V层与P层之间都只能使用接口进行调用，双方不允许持有对方实现对象

### V层
Activity
```java
@InjectPresenter(values = {MyPresenterImpl1.class, MyPresenterImpl2.class})
public class MyActivity extends AppCompatActivity implements MyViewAPI {
    // 实例MVPHelper
    private final MVPHelper MVPHelper = new MVPHelper();
    // MyPresenter1 为 MyPresenterImpl1 的接口定义
    private MyPresenterAPI1 mPresenter1;
    // MyPresenter2 为 MyPresenterImpl2 的接口定义
    private MyPresenterAPI2 mPresenter2;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        // 将MVPHelper与你当前的Activity进行绑定
        MVPHelper.attach(this);
        // 通过P层接口的类对象进行查找P层实例对象
        mPresenter1 = getPresenterAPI(MyPresenterAPI1.class);
        mPresenter2 = getPresenterAPI(MyPresenterAPI2.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //委托声明周期处理（Presenter中会被触发）
        MVPHelper.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //委托声明周期处理（Presenter中会被触发）
        MVPHelper.detach();
    }
    
    // 提供查询Presenter的方法
    protected <T> T getPresenterAPI(Class<T> cls){
        return MVPHelper.getPresenterAPI(cls);
    }
}
```
Fragment
```java
@InjectPresenter(values = {MyPresenterImpl1.class, MyPresenterImpl2.class})
public class MyFragment extends Fragment implements MyViewAPI {
    private final MVPHelper mvpHelper = new MVPHelper();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mvpHelper.attach(this);
    }

    protected <T> T getPresenterAPI(Class<T> cls) {
        return mvpHelper.getPresenterAPI(cls);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpHelper.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mvpHelper.detach();
    }
}

```

### P层
P层需要继承 z1w3.mvp.support.BasePresenter
```java
// P层实现允许被单例
// 声明单例后，getContext()方法 为 applicationContext
@Singleton
public class MyPresenterImpl1 extends BasePresenter implements MyPresenter {

    private MyViewAPI viewAPI;
    private MyPresenterAPI2 mPresenterAPI2;

    // 如果使用单例则只会触发一次
    @Override
    public void onCreate() {
        super.onCreate();
        viewAPI = getViewAPI();
        mPresenterAPI2 = getOtherPresenterAPI(MyPresenterAPI2.class);
    }
    
    // 如果使用单例则不会触发
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
```

### 注解

V层实现通过注解的形式将P层实现注入
```java
@InjectPresenter(values = {MyPresenterImpl1.class, MyPresenterImpl2.class})
public class MyActivity extends Activity{/*_*/}

@InjectPresenter(values = {MyPresenterImpl1.class, MyPresenterImpl2.class})
public class MyFragment extends Fragment{/*_*/}
```

V层注解声明
```java
@ViewAPI
public interface MyViewAPI{}
```

P层注解声明
```java
@PresenterAPI
public interface MyPresenterAPI{}
```

P层单例声明
```java
@Singleton
public class MyPresenterImpl1 extends BasePresenter implements MyPresenterAPI{/*_*/}
```


### 混淆
```properties
# MVP
-keep class z1w3.mvp.support.**{public *;}
-keep class * extends z1w3.mvp.support.BasePresenter{public *;}
```


