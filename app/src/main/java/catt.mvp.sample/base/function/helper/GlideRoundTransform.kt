package catt.mvp.sample.base.function.helper

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import android.graphics.RectF
import android.graphics.BitmapShader
import android.graphics.Bitmap



/**
 * Glide 圆角 Transform
 */
internal class GlideRoundTransform(val radius :Float = 4F): BitmapTransformation() {
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap =
        roundCrop(pool, toTransform)

    private fun roundCrop(pool: BitmapPool, source: Bitmap): Bitmap {
        var result: Bitmap? = pool.get(source.width, source.height, Bitmap.Config.ARGB_8888)
        if (result == null) result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result!!)
        val paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
        val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint)
        return result
    }
}