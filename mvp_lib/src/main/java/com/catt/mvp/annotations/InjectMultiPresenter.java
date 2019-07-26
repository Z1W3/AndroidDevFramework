package com.catt.mvp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)

@Target(ElementType.TYPE)

/**
 * @author:         支玮
 * @createDate:     2019-07-26 16:10
 * @description:    描述
 *
 * 这个注解需要在V层的委托实现类上进行标注，标注的目的是为了将V层的委托实现类与P层的实现类进行绑定
 */
public @interface InjectMultiPresenter {

    /**
     * @author:         支玮
     * @createDate:     2019-07-26 16:10
     * @description:    描述
     * 填写P层实现类的全类名,
     * example:
     *      {
     *          com.a.b.presenter.MyFirstPresenterImpl,
     *          com.a.b.presenter.MySecondPresenterImpl,
     *      }
     */
    String[] values();
}
