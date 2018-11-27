package catt.mvp.sample.base.function.component

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import catt.mvp.sample.base.function.module.GlideApp
import catt.mvp.sample.base.function.module.GlideRequest
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.io.File
import java.lang.ref.Reference
import java.lang.ref.WeakReference

interface IGlideComponent {

    fun Activity.glideResumeRequests() =
        GlideApp.with(this).resumeRequests()

    fun Activity.glidePauseRequests() =
        GlideApp.with(this).pauseRequests()

    fun Fragment.glideResumeRequests() =
        GlideApp.with(this).resumeRequests()

    fun Fragment.glidePauseRequests() =
        GlideApp.with(this).pauseRequests()

    fun Context.glideResumeRequests() =
        GlideApp.with(this).resumeRequests()

    fun Context.glidePauseRequests() =
        GlideApp.with(this).pauseRequests()

    fun View.glideResumeRequests() =
        GlideApp.with(this).resumeRequests()

    fun View.glidePauseRequests() =
        GlideApp.with(this).pauseRequests()

    fun View.glideClear() {
        GlideApp.with(this).clear(this)
    }

    fun ImageView.setImageSourceByGlide(
        any: Any,
        placeholder: Int, fallback: Int = placeholder, error: Int = placeholder,
        transform: BitmapTransformation? = null,
        cache: DiskCacheStrategy = DiskCacheStrategy.ALL) {
        val reference: Reference<ImageView> = WeakReference<ImageView>(this)
        reference.get()?.apply {
            GlideApp.with(this@apply).load(any).glideRequest(transform, cache, placeholder, fallback, error)
                .into(this@apply)
        }
    }

    fun ImageView.setImageSourceByGlide(
        url: String,
        placeholder: Int, fallback: Int = placeholder, error: Int = placeholder,
        transform: BitmapTransformation? = null,
        cache: DiskCacheStrategy = DiskCacheStrategy.ALL) {
        val reference: Reference<ImageView> = WeakReference<ImageView>(this)
        reference.get()?.apply {
            GlideApp.with(this@apply).load(url).glideRequest(transform, cache, placeholder, fallback, error)
                .into(this@apply)
        }
    }

    fun ImageView.setImageSourceByGlide(
        id: Int,
        placeholder: Int, fallback: Int = placeholder, error: Int = placeholder,
        transform: BitmapTransformation? = null,
        cache: DiskCacheStrategy = DiskCacheStrategy.ALL) {
        val reference: Reference<ImageView> = WeakReference<ImageView>(this)
        reference.get()?.apply {
            GlideApp.with(this@apply).load(id).glideRequest(transform, cache, placeholder, fallback, error)
                .into(this@apply)
        }
    }

    fun ImageView.setImageSourceByGlide(
        bitmap: Bitmap,
        placeholder: Int, fallback: Int = placeholder, error: Int = placeholder,
        transform: BitmapTransformation? = null,
        cache: DiskCacheStrategy = DiskCacheStrategy.ALL) {
        val reference: Reference<ImageView> = WeakReference<ImageView>(this)
        reference.get()?.apply {
            GlideApp.with(this@apply).load(bitmap).glideRequest(transform, cache, placeholder, fallback, error)
                .into(this@apply)
        }
    }

    fun ImageView.setImageSourceByGlide(
        drawable: Drawable,
        placeholder: Int, fallback: Int = placeholder, error: Int = placeholder,
        transform: BitmapTransformation? = null,
        cache: DiskCacheStrategy = DiskCacheStrategy.ALL) {
        val reference: Reference<ImageView> = WeakReference<ImageView>(this)
        reference.get()?.apply {
            GlideApp.with(this@apply).load(drawable).glideRequest(transform, cache, placeholder, fallback, error)
                .into(this@apply)
        }
    }

    fun ImageView.setImageSourceByGlide(
        uri: Uri,
        placeholder: Int, fallback: Int = placeholder, error: Int = placeholder,
        transform: BitmapTransformation? = null,
        cache: DiskCacheStrategy = DiskCacheStrategy.ALL) {
        val reference: Reference<ImageView> = WeakReference<ImageView>(this)
        reference.get()?.apply {
            GlideApp.with(this@apply).load(uri).glideRequest(transform, cache, placeholder, fallback, error)
                .into(this@apply)
        }
    }

    fun ImageView.setImageSourceByGlide(
        file: File,
        placeholder: Int, fallback: Int = placeholder, error: Int = placeholder,
        transform: BitmapTransformation? = null,
        cache: DiskCacheStrategy = DiskCacheStrategy.ALL) {
        val reference: Reference<ImageView> = WeakReference<ImageView>(this)
        reference.get()?.apply {
            GlideApp.with(this@apply).load(file).glideRequest(transform, cache, placeholder, fallback, error)
                .into(this@apply)
        }
    }

    fun ImageView.setImageSourceByGlide(
        bytes: ByteArray,
        placeholder: Int, fallback: Int = placeholder, error: Int = placeholder,
        transform: BitmapTransformation? = null,
        cache: DiskCacheStrategy = DiskCacheStrategy.ALL) {
        val reference: Reference<ImageView> = WeakReference<ImageView>(this)
        reference.get()?.apply {
            GlideApp.with(this@apply).load(bytes).glideRequest(transform, cache, placeholder, fallback, error)
                .into(this@apply)
        }
    }

    private fun GlideRequest<Drawable>.glideRequest(
        transform: BitmapTransformation?, cache: DiskCacheStrategy, placeholder: Int, fallback: Int, error: Int)
            : GlideRequest<Drawable> =
        when {
            transform != null ->
                diskCacheStrategy(cache)
                    .transform(transform)
                    .placeholder(placeholder)
                    .fallback(fallback)
                    .error(error)
                    .dontAnimate()
                    .dontTransform()
            else ->
                diskCacheStrategy(cache)
                    .placeholder(placeholder)
                    .fallback(fallback)
                    .error(error)
                    .dontAnimate()
                    .dontTransform()
        }
}