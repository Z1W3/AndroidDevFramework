package catt.mvp.framework.function.module

import android.content.Context
import android.util.Log
import catt.mvp.framework.BuildConfig
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

import java.io.File
import java.io.InputStream

@GlideModule
class GlideConfigure : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setLogLevel(Log.DEBUG)
        builder.setMemoryCache(LruResourceCache(BuildConfig.GLIDE_CACHE_MEMORY))
        builder.setBitmapPool(LruBitmapPool(3))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context,
            getDiskFileString(context, BuildConfig.GLIDE_CACHE_PATH), BuildConfig.GLIDE_CACHE_MEMORY))
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_RGB_565).disallowHardwareConfig())
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory())
    }

    //设置缓存目录
    private fun getDiskFileString(context: Context, str: String): String {
        val dirFile = File(context.cacheDir.path + str)
        val tempFile = File(dirFile, "bitmaps")
        if (!tempFile.parentFile.exists()) {
            tempFile.parentFile.mkdirs()
        }
        return tempFile.path.toString()
    }
}