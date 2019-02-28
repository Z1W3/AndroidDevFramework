package catt.mvp.framework.function.component

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.content.pm.PermissionInfoCompat

/**
 * 查询是否有需要激活的权限
 * @return if `true`, Need to apply for activation permission. if `false`, has been fully activated permission.
 */
fun Activity.isNeedEnablePermission(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        for (permission in requestedPermissions()) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(applicationContext, permission)) {
                return true
            }
        }
    }
    return false
}

fun Context.requestedPermissions() : Array<String> {
    val list = mutableListOf<String>()
    val permissionArray:Array<String> = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions
    for(index in permissionArray.indices){
        try {
            val permission = permissionArray[index]
            val permissionInfo = packageManager.getPermissionInfo(permission, PackageManager.GET_META_DATA)
            if(PermissionInfo.PROTECTION_DANGEROUS == PermissionInfoCompat.getProtection(permissionInfo))
                list.add(permission)
        }
        catch (ex : PackageManager.NameNotFoundException){
            ex.printStackTrace()
        }
    }
    return list.toTypedArray()
}