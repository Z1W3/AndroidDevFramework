package com.catt.mvp.delegated;

import android.content.Intent;

/**
 * @author:         支玮
 * @createDate:     2019-07-18 13:03
 * @description:
 * 这个接口定义了V层委托类的一些基础声明周期，
 * 这些声明周期的触发将跟随被代理类（例如：Activity，Fragment）的声明周期触发而触发，
 * 从而使V层委托实现类拥有声明周期。
 *
 * PS：目前使用此方式触发生命周期并不是最优方案，应该使用LifecyclerOwner来进行处理。
 * 这个需要集成android Lifecycler的引用。可以在重构版中使用。
 */
public interface IDelegatedView {

    void onCreate();

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * @author:         支玮
     * @createDate:     2019-07-18 13:27
     * @description:
     *
     * 这个方法一定是在视图已经在设备屏幕上绘制完毕后才会触发的
     *
     * <pre>
     *  //触发的条件演示
     *  android.view.View.post(()->{
     *      if(delegated != null){
     *          delegated.onViewLoadCompleted();
     *      }
     *  });
     * <pre/>
     */
    void onViewLoadCompleted();
}
