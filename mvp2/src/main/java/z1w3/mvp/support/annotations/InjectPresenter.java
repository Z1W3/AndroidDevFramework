package z1w3.mvp.support.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import z1w3.mvp.support.BasePresenter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InjectPresenter {
    Class<? extends BasePresenter>[] values();
}
