package catt.mvp.sample

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log.e
import catt.mvp.sample.base.adm.BaseActivityStack
import java.text.SimpleDateFormat
import com.google.gson.GsonBuilder
import com.umeng.analytics.MobclickAgent
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import kotlin.collections.HashMap


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 *
 * Java Usage:
 * <pre>
 *     Application.class
 *     public void onCreate(){
 *          super.onCreate();
 *          GlobalCrashHandler.get().initContext(context);
 *      }
 * </pre>
 *
 * Kotlin Usage:
 * <pre>
 *     Application.kt
 *     override fun void onCreate(){
 *          super.onCreate();
 *          GlobalCrashHandler.get().initContext(context)
 *      }
 * </pre>
 */
class GlobalCrashHandler: Thread.UncaughtExceptionHandler {
    private var context: Context? = null

    private val _TAG by lazy { GlobalCrashHandler::class.java.simpleName }


    private val defaultHandler: Thread.UncaughtExceptionHandler by lazy {
        Thread.setDefaultUncaughtExceptionHandler(this)
        Thread.getDefaultUncaughtExceptionHandler()
    }

    private val exMap:HashMap<String, String> by lazy { HashMap<String, String>() }
    private val formatter:SimpleDateFormat by lazy { SimpleDateFormat("yyyy-MM-dd_HH:mm:ss") }

    private fun seven(): String = GsonBuilder().enableComplexMapKeySerialization().create().toJson(exMap)

    fun initContext(context: Context) {
        this.context = context
    }

    override fun uncaughtException(t: Thread?, ex: Throwable?) {
        ex ?: return
        e(_TAG, "Error->", ex)
        if (!processorException(ex) && defaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            defaultHandler!!.uncaughtException(t, ex)
        } else {
            BaseActivityStack.get().killMyPid()
        }
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private fun processorException(ex: Throwable?): Boolean {
        ex ?: return false
        context?:throw NullPointerException("Please initialize the context.")
        MobclickAgent.reportError(context, ex)
        fetchLocalBaseInfo(context!!.packageName, context!!.packageManager)
        fetchAppThrowable(ex)
//        saveCrashInfo2File()
        return true
    }

    /**
     * 收集设备参数信息
     */
    private fun fetchLocalBaseInfo(packageName:String, pm: PackageManager) {
        try {
            val pckInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            exMap["PACKAGE_NAME"] = packageName
            exMap["VERSION_NAME"] = when (pckInfo.versionName) {
                null -> ""
                else -> pckInfo.versionName
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                exMap["VERSION_CODE"] = pckInfo.longVersionCode.toString()
            }
            else {
                exMap["VERSION_CODE"] = pckInfo.versionCode.toString()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e(_TAG, "an error occured when collect package info", e)
        }

        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                exMap[field.name] = field.get(null).toString()
            } catch (e: Exception) {
                e(_TAG, "an error occured when collect crash info", e)
            }
        }
    }

    private fun fetchAppThrowable(ex: Throwable): String {
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        try {
            ex.printStackTrace(printWriter)
            var cause: Throwable? = ex.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause = cause.cause
            }
        } catch (ex: Exception) {
            e(_TAG, "printWriter", ex)
        } finally {
            printWriter.close()
        }
        exMap["THROWABLE_LOG"] = writer.toString()
        var recordTime = formatter.format(Date())
        exMap["RECORD_TIME"] = recordTime
        return recordTime
    }

    /**
     * 保存错误信息到文件中 *
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfo2File(recordTime: String): String? {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val timestamp = System.currentTimeMillis()
            val fileName = "hcb_dgg_store_crash-$recordTime-$timestamp.log"
            val path = "${Environment.getExternalStorageDirectory()}/crash/"
            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val fos = FileOutputStream(path + fileName)
            try {
                fos.write(seven().toByteArray())
            } catch (ex: Exception) {
                e(_TAG, "FileOutputStream", ex)
            } finally {
                fos.close()
            }
            return fileName
        }
        return null
    }

    companion object {
        private val BASES = arrayListOf("PACKAGE_NAME", "VERSION_NAME", "VERSION_CODE", "TIME",
            "FINGERPRINT", "CPU_ABI", "CPU_ABI2", "ID", "SERIAL", "MANUFACTURER", "BRAND", "TYPE")

        private object Single{
            internal val INSTANCE by lazy { GlobalCrashHandler() }
        }

        @JvmStatic
        fun get(): GlobalCrashHandler = Single.INSTANCE
    }

}