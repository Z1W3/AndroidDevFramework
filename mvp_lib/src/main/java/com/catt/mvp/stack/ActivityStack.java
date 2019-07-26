package com.catt.mvp.stack;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import java.util.Stack;

/**
 * @author:         支玮
 * @createDate:     2019-07-18 14:23
 * @description:
 * 用于Activity的堆栈记录器
 */
public enum ActivityStack {
    INSTANCE;
    private Stack<Activity> stack = new Stack<>();

    public void push(Activity item) {
        synchronized (INSTANCE) {
            stack.remove(item);
            stack.push(item);
        }
    }

    public Activity peek() {
        return stack.peek();
    }

    public void popFinishActivity() {
        synchronized (INSTANCE) {
            final Activity pop = stack.pop();
            if (pop != null) {
                pop.finish();
            }
        }
    }

    public Activity searchActivity(Class<?> clz) {
        synchronized (INSTANCE) {
            for (int index = stack.size() - 1; index >= 0; index--) {
                final Activity o = stack.get(index);
                if (o != null &&
                        TextUtils.equals(o.getClass().getName(), clz.getName())) {
                    return o;
                }
            }
        }
        return null;
    }

    public void remove(Activity item){
        synchronized (INSTANCE){
            stack.remove(item);
        }
    }

    public void finishActivity(Class<?> clz) {
        final Activity o = searchActivity(clz);
        if (o != null) {
            o.finish();
        }
    }

    public void finishActivity(Class<?> clz, int resultCode) {
        final Activity o = searchActivity(clz);
        if (o != null) {
            o.setResult(resultCode, null);
            o.finish();
        }
    }

    public void finishActivity(Class<?> clz, int resultCode, Intent data) {
        final Activity o = searchActivity(clz);
        if (o != null) {
            o.setResult(resultCode, data);
            o.finish();
        }
    }

    public void finishAllActivity() {
        synchronized (INSTANCE) {
            for (int index = stack.size() - 1; index >= 0; index--) {
                final Activity o = stack.remove(index);
                if (o != null) {
                    o.finish();
                }
            }
        }
    }


    public boolean isActivityDestroy(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return activity == null || activity.isFinishing() || activity.isDestroyed();
        }
        return activity == null || activity.isFinishing();
    }

}
