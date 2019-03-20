package catt.mvp.framework.function.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.text.HtmlCompat
import android.support.v7.app.AlertDialog
import android.text.Spanned
import android.util.Log.i
import catt.mvp.framework.R
import catt.mvp.framework.function.component.IPermissionComponent
import catt.mvp.framework.function.component.IPermissionComponent.PH.PERMISSION_REQUEST_CODE
import catt.mvp.framework.function.component.IPermissionComponent.PH.permissionAscriptionGroup
import catt.mvp.framework.function.component.IPermissionComponent.PH.translatePermission
import catt.mvp.framework.function.component.isNeedEnablePermission

class PermissionHelper(private val activity: Activity, private val listener: OnPermissionListener) : IPermissionComponent {
    private val _TAG by lazy { PermissionHelper::class.java.simpleName }
    private val context:Context by lazy { activity.applicationContext }
    private val permissionAlertDialogBuilder:AlertDialog.Builder by lazy { AlertDialog.Builder(activity) }
    private var permissionAlertDialog:AlertDialog? = null

    override fun onPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if(PERMISSION_REQUEST_CODE != requestCode) return
        i(_TAG, "onPermissionsResult: begin")
        val needRationaleList: MutableList<String> = mutableListOf()
        val disablePermissionList:MutableList<String> = mutableListOf()
        for(index in permissions.indices){
            val permission:String = permissions[index]
            val enablePermission:Boolean = grantResults[index] == PackageManager.PERMISSION_GRANTED
            i(_TAG, "onPermissionsResult: permission:{name=$permission, enable=$enablePermission}")
            val needRationale:Boolean = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[index])
            when {
                !enablePermission && needRationale -> /*权限未授权并且未点击不再提醒*/{
                    needRationaleList.add(permission)
                }
                !enablePermission && !needRationale -> /*权限未授权并且点击不再提醒*/{
                    disablePermissionList.add(permission)
                }
            }
        }

        when{
            needRationaleList.size == 0 && disablePermissionList.size == 0-> listener.onGrantedPermissionCompleted()
            needRationaleList.size != 0->{
                needRationaleList.addAll(disablePermissionList)
                ActivityCompat.requestPermissions(activity, needRationaleList.toTypedArray(), PERMISSION_REQUEST_CODE)
            }
            needRationaleList.size == 0 && disablePermissionList.size != 0 && !isShowingPermissionAlertDialog()->
                permissionAlertDialog = configurePermissionAlertDialogBuilder(scanPermissionList(disablePermissionList).toString()).show()
        }
        i(_TAG, "onPermissionsResult: finished")
    }

    override fun onActivityResultForPermissions(requestCode: Int, resultCode: Int, data: Intent?) {
        if(PERMISSION_REQUEST_CODE != requestCode) return
        if (activity.isNeedEnablePermission()) {
            scan()
        } else {
            dismissPermissionAlertDialog()
            listener.onGrantedPermissionCompleted()
        }
    }

    override fun scan() {
        if (activity.isNeedEnablePermission()) activity.requestAllOwnPermissions()
        else listener.onGrantedPermissionCompleted()
    }

    private fun scanPermissionList(disablePermissionList: MutableList<String>): StringBuffer {
        val mutableMap = mutableMapOf<String, String>()
        for (index in disablePermissionList.indices) {
            val permission = disablePermissionList[index]
            mutableMap[permissionAscriptionGroup(permission)] = translatePermission(context, permission)
        }
        val sb = StringBuffer()
        sb.append("<font color='#333333' size=20px>${context.getString(R.string.goto_apply_permission)}</>")
            .append("<br>").append("<br>")
        for (map in mutableMap.entries) {
            sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
            sb.append("<font color='#888888' size=16px>・${map.value}</>")
            sb.append("<br>")
        }
        sb.removeSuffix("<br>")
        return sb
    }

    // 提示用户去应用设置界面手动开启权限
    private fun configurePermissionAlertDialogBuilder(msg:String): AlertDialog.Builder =
        permissionAlertDialogBuilder
            .setTitle(fromHtml(context.getString(R.string.unapply_permission_list_title), "#008577"))
            .setMessage(fromHtml(msg))
            .setPositiveButton(context.getString(R.string.apply_now)) {
                    _, _ -> activity.requestAllOwnPermissionsByAppSettings()
            }
            .setCancelable(false)


    private fun isShowingPermissionAlertDialog(): Boolean = permissionAlertDialog != null && permissionAlertDialog!!.isShowing

    private fun dismissPermissionAlertDialog() {
        if (isShowingPermissionAlertDialog()) {
            permissionAlertDialog!!.dismiss()
        }
    }

    private fun fromHtml(content:String, color:String) : Spanned =
        HtmlCompat.fromHtml("<font color='$color'>$content</>", 0)

    private fun fromHtml(content:String) : Spanned =
        HtmlCompat.fromHtml(content, 0)

    interface OnPermissionListener{
        fun onGrantedPermissionCompleted()
    }
}