package com.catt.mvp;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.catt.mvp.delegated.BaseActivityDelegated;
import com.catt.mvp.stack.ActivityStack;
import com.catt.mvp.stack.FragmentStack;


/**
 * @author:         支玮
 * @createDate:     2019-07-18 14:25
 * @description:
 *
 * 将Activity的业务逻辑完全委托给委托类进行处理，
 * 委托类可以传递来的Intent进行动态创建。
 *
 * Activity的实现类中可以定制特殊的方法共委托类使用,
 * 也可以在BaseActivity中设置公共方法供委托类使用
 *
 * example:
 * <pre>
 *      public class MyActivity extends BaseActivity {
 *
 *          @Override
 *          BaseActivityDelegated injectDelegated() {
 *              return new MyViewImpl();
 *          }
 *      }
 * <pre/>
 */
public abstract class BaseActivity extends AppCompatActivity {

    private BaseActivityDelegated delegated;

    protected abstract BaseActivityDelegated injectDelegated();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.INSTANCE.push(this);
        delegated = injectDelegated();
        delegated.onCreate();
        setContentView(delegated.getLayoutResID());
        delegated.onInitData(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        delegated.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        delegated.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        delegated.onViewCreated();
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                if (delegated != null) {
                    delegated.onViewLoadCompleted();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        delegated.onActivityResult(requestCode, resultCode, data);
        for (int index = FragmentStack.INSTANCE.getStack().size() - 1; index >= 0; index--) {
            final Fragment fragment = FragmentStack.INSTANCE.getStack().get(index);
            if (fragment != null
                    && !ActivityStack.INSTANCE.isActivityDestroy(fragment.getActivity())) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        delegated.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        delegated.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        delegated.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        delegated.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        delegated.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delegated.onDestroy();
        delegated = null;
        ActivityStack.INSTANCE.remove(this);
    }
}
