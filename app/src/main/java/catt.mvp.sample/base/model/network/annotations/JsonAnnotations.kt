package catt.mvp.sample.base.model.network.annotations


@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class JsonCallField(
    val code: String = "code",
    val msg: String = "msg",
    val timestamp: String = "timestamp",
    val data:String = "data"
)

@Target(AnnotationTarget.CLASS)
@MustBeDocumented
/**
 *
 * example:
 * <pre>
 *      hierarchy = "accountVo/userVo/name"
 *
 *      // all hierarchy -> data/accountVo/userVo/nameVo
 *      // 追中获取字段nameVo
 * </pre>
 */
annotation class JsonCallDataTargetField(val hierarchy: String = "")
