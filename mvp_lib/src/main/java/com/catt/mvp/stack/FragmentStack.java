package com.catt.mvp.stack;

import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.lang.reflect.Method;
import java.util.Stack;

/**
 * @author: 支玮
 * @createDate: 2019-07-18 14:23
 * @description: 用于Fragment的堆栈记录器
 */
public enum FragmentStack {
    INSTANCE;

    private volatile Stack<Fragment> stack = new Stack<>();

    public Stack<Fragment> getStack() {
        return stack;
    }

    public void push(Fragment item) {
        synchronized (INSTANCE) {
            stack.remove(item);
            stack.push(item);
        }
    }

    public Fragment peek() {
        return stack.peek();
    }

    public void popHiddenFragment(FragmentManager var) {
        synchronized (INSTANCE) {
            final Fragment pop = stack.pop();
            if (pop != null) {
                var.beginTransaction().hide(pop).commit();
            }
        }
    }

    /**
     * 有可能出现全类名相同，但是对象不通的Fragment
     * 该方法只会找最接近栈顶的那个Fragment
     */
    public Fragment peekFragment(Class<?> clz) {
        synchronized (INSTANCE) {
            for (int index = stack.size() - 1; index >= 0; index--) {
                final Fragment o = stack.get(index);
                if (o != null &&
                        TextUtils.equals(o.getClass().getName(), clz.getName())) {
                    return o;
                }
            }
        }
        return null;
    }

    public void showFragment(FragmentManager var, Class<?> clz) {
        final Fragment fragment = peekFragment(clz);
        if (fragment != null) {
            var.beginTransaction().show(fragment).commit();
        }
    }

    public void hiddenFragment(FragmentManager var, Class<?> clz) {
        final Fragment fragment = peekFragment(clz);
        if (fragment != null) {
            var.beginTransaction().hide(fragment).commit();
        }
    }

    public void remove(Fragment item) {
        synchronized (INSTANCE) {
            stack.remove(item);
        }
    }

    public boolean isVisibleFragment(Fragment fragment) throws Exception {
        final Method method = fragment.getClass().getMethod("isVisibleToUser");
        method.setAccessible(true);
        final Object invoke = method.invoke(fragment);
        return (boolean) invoke;
    }

}
