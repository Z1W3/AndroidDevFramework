package com.catt.mvp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.catt.mvp.delegated.BaseFragmentDelegated;

import java.lang.reflect.ParameterizedType;

public class MVPUtils {

    public static <T extends BaseFragmentDelegated> Fragment newInstanceFragment(Class<T> clz) throws IllegalAccessException, InstantiationException {
        ParameterizedType type = (ParameterizedType) clz.getGenericSuperclass();
        return (Fragment) ((Class) type.getActualTypeArguments()[0]).newInstance();
    }

    public static <T extends BaseFragmentDelegated> Fragment newInstanceFragment(Class<T> clz, Bundle args) throws IllegalAccessException, InstantiationException {
        ParameterizedType type = (ParameterizedType) clz.getGenericSuperclass();
        final Fragment fragment = (Fragment) ((Class) type.getActualTypeArguments()[0]).newInstance();
        fragment.setArguments(args);
        return fragment;
    }

    public static <T extends Fragment> void startActivity(Context context, Class<T> clz) {
        ParameterizedType type = (ParameterizedType) clz.getGenericSuperclass();
        final Class targetClz = (Class) type.getActualTypeArguments()[0];
        final Intent intent = new Intent(context, targetClz);
        context.startActivity(intent);
    }

    public static <T extends Fragment> void startActivityForResult(Activity activity, Class<T> clz, int requestCode) {
        startActivityForResult(activity, clz, requestCode, null);
    }

    public static <T extends Fragment> void startActivityForResult(Activity activity, Class<T> clz, int requestCode, Bundle options) {
        ParameterizedType type = (ParameterizedType) clz.getGenericSuperclass();
        final Class targetClz = (Class) type.getActualTypeArguments()[0];
        final Intent intent = new Intent(activity, targetClz);
        activity.startActivityForResult(intent, requestCode, options);
    }


    public static <T extends BaseFragmentDelegated> void replaceFragment(
            FragmentManager fm,
            FrameLayout layout, Class<T> clz) {
        try {
            fm.beginTransaction()
                    .replace(layout.getId(), MVPUtils.newInstanceFragment(clz))
                    .commit();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static <T extends BaseFragmentDelegated> void replaceFragment(
            FragmentManager fm,
            FrameLayout layout, Class<T> clz, Bundle args) {
        try {
            fm.beginTransaction()
                    .replace(layout.getId(), MVPUtils.newInstanceFragment(clz, args))
                    .commit();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
