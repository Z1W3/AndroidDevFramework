package catt.mvp.framework.function.component

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.StringDef
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.pm.PermissionInfoCompat
import catt.mvp.framework.R

interface IPermissionComponent {

    /**
     * 扫描需要授权的权限
     */
    fun scan()
    fun onActivityResultForPermissions(requestCode: Int, resultCode: Int, data: Intent?)
    fun onPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)

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

    /**
     * 是否重新请求授权
     */
    fun Activity.shouldShowRequestPermissionRationale(): Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in requestedPermissions()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    return true
                }
            }
        }
        return false
    }

    fun Activity.OnRequestPermissionsResultCallback(function: (Int, Array<out String>, IntArray) -> Unit) =
        ActivityCompat.OnRequestPermissionsResultCallback(function)

    /**
     * 通过主动弹窗激活权限
     */
    fun Activity.requestAllOwnPermissions() {
        ActivityCompat.requestPermissions(this, requestedPermissions(), PERMISSION_REQUEST_CODE)
    }

    /**
     * 通过手动跳转APP设置页面激活权限
     */
    fun Activity.requestAllOwnPermissionsByAppSettings() {
        val it = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        }
        ActivityCompat.startActivityForResult(this, it, PERMISSION_REQUEST_CODE, null)
    }

    private fun Context.requestedPermissions() : Array<String> {
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


    companion object PH{
        const val PERMISSION_REQUEST_CODE :Int = 520
        private const val PERMISSION_GROUP_OTHER = "PERMISSION_GROUP_OTHER"

        @StringDef(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        @Retention(AnnotationRetention.SOURCE)
        private annotation class DangerousPermission

        @StringDef(
            Manifest.permission_group.CALENDAR,
            Manifest.permission_group.CAMERA,
            Manifest.permission_group.CONTACTS,
            Manifest.permission_group.LOCATION,
            Manifest.permission_group.MICROPHONE,
            Manifest.permission_group.PHONE,
            Manifest.permission_group.SENSORS,
            Manifest.permission_group.SMS,
            Manifest.permission_group.STORAGE
        )
        @Retention(AnnotationRetention.SOURCE)
        private annotation class DangerousPermissionGroup

        fun translatePermission(context: Context, @DangerousPermission permission: String): String {
            val i = translatePermissionGroupMap[permissionAscriptionGroup(permission)]
            i ?: return context.getString(R.string.no_dangerous_permission)
            return context.getString(i)
        }

        fun permissionAscriptionGroup(@DangerousPermission permission:String) : String{
            for(map in permissionGroupMap.entries){
                for(index in map.value.indices){
                    if(permission == map.value[index]){
                        return map.key
                    }
                }
            }
            return PERMISSION_GROUP_OTHER
        }

        private val permissionGroupMap:HashMap<String, Array<String>> by lazy {
            hashMapOf(
                Manifest.permission_group.CALENDAR to arrayOf(
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
                ),
                Manifest.permission_group.CAMERA to arrayOf(
                    Manifest.permission.CAMERA
                ),
                Manifest.permission_group.CONTACTS to arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS
                ),

                Manifest.permission_group.LOCATION to arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),

                Manifest.permission_group.MICROPHONE to arrayOf(
                    Manifest.permission.RECORD_AUDIO
                ),
                Manifest.permission_group.PHONE to arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.ADD_VOICEMAIL,
                    Manifest.permission.USE_SIP,
                    Manifest.permission.PROCESS_OUTGOING_CALLS
                ),

                Manifest.permission_group.SENSORS to arrayOf(
                    Manifest.permission.BODY_SENSORS
                ),

                Manifest.permission_group.SMS to arrayOf(
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS
                ),

                Manifest.permission_group.STORAGE to arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

        private val translatePermissionGroupMap:HashMap<String, Int> by lazy {
            hashMapOf(
                Manifest.permission_group.CALENDAR to R.string.dangerous_permission_group_calendar,
                Manifest.permission_group.CAMERA to R.string.dangerous_permission_group_camera,
                Manifest.permission_group.CONTACTS to R.string.dangerous_permission_group_contacts,
                Manifest.permission_group.LOCATION to R.string.dangerous_permission_group_location,
                Manifest.permission_group.MICROPHONE to R.string.dangerous_permission_group_microphone,
                Manifest.permission_group.PHONE to R.string.dangerous_permission_group_phone,
                Manifest.permission_group.SENSORS to R.string.dangerous_permission_group_sensors,
                Manifest.permission_group.SMS to R.string.dangerous_permission_group_sms,
                Manifest.permission_group.STORAGE to R.string.dangerous_permission_group_storage
            )
        }
    }
}