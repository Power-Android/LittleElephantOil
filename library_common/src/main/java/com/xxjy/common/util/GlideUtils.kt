package com.xxjy.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.xxjy.common.R
import jp.wasabeef.glide.transformations.*
import jp.wasabeef.glide.transformations.CropTransformation.CropType
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType

object GlideUtils : AppGlideModule() {
    /**
     * @param obj       这里obj 只加载 url bitmap  drawable
     * @param imageView 需要加载的图片
     * @describe 加载正常图片
     * @note 这里并没有加载错图，在有错图的时候设置 error()
     */
    fun loadImage(obj: Any?, imageView: ImageView) {
        if (obj is String) {
            Glide.with(imageView.context).load(obj).apply(initOptions())
                .skipMemoryCache(isSkipMemoryCache).error(errorImage)
                .fallback(errorImage).placeholder(placeholder).into(imageView)
        }
        if (obj is Bitmap) {
            Glide.with(imageView.context).load(obj).apply(initOptions())
                .skipMemoryCache(isSkipMemoryCache).error(errorImage)
                .fallback(errorImage).placeholder(placeholder).into(imageView)
        }
        if (obj is Drawable) {
            Glide.with(imageView.context).load(obj).apply(initOptions())
                .skipMemoryCache(isSkipMemoryCache).error(errorImage)
                .fallback(errorImage).placeholder(placeholder).into(imageView)
        }
    }
    /**
     * @param context   当前Activity的上下文对象
     * @param obj
     * @param imageView
     * @describe 与没有context的方法相比 不易导致 内存泄漏问题，原因 activity销毁的时候 imageView 的上下文对象自然不存在
     */
    //    public static void loadImage(Context context, Object obj, ImageView imageView) {
    //        if (obj instanceof String) {
    //            Glide.with(context).load(obj).apply(initOptions())
    //                    .skipMemoryCache(isSkipMemoryCache()).error(getErrorImage())
    //                    .fallback(getErrorImage()).placeholder(getPlaceholder()).into(imageView);
    //        }
    //        if (obj instanceof Bitmap) {
    //            Glide.with(context).load(obj).apply(initOptions())
    //                    .skipMemoryCache(isSkipMemoryCache()).error(getErrorImage())
    //                    .fallback(getErrorImage()).placeholder(getPlaceholder()).into(imageView);
    //        }
    //        if (obj instanceof Drawable) {
    //            Glide.with(context).load(obj).apply(initOptions())
    //                    .skipMemoryCache(isSkipMemoryCache()).error(getErrorImage())
    //                    .fallback(getErrorImage()).placeholder(getPlaceholder()).into(imageView);
    //        }
    //    }
    /**
     * @describe 加载圆形图
     * @param context   当前Activity的上下文对象
     * @param imageView
     */
    fun loadCircleImage(context: Context?, url: String?, imageView: ImageView?) {
//        Glide.with(context).load(url).apply(initOptions())
//                .skipMemoryCache(isSkipMemoryCache()).error(getErrorImage()).placeholder(getPlaceholder())
//                .fallback(getErrorImage()).circleCrop().into(imageView);
        Glide.with(context!!).load(url) //                .skipMemoryCache(true)
            //                .diskCacheStrategy(DiskCacheStrategy.NONE)
            //                .signature(new ObjectKey(System.currentTimeMillis()))
            .apply(RequestOptions().error(R.drawable.default_img_bg))
            .circleCrop().into(imageView!!)
    }

    /**
     * @describe 加载图片
     * @param context   当前Activity的上下文对象
     * @param imageView
     */
    fun loadImage(context: Context, url: String?, imageView: ImageView?) {
        Glide.with(context).load(url)
            .error(R.drawable.default_img_bg)
            .apply(RequestOptions().error(R.drawable.default_img_bg))
            .into(imageView!!)
    }

    /**
     * @describe 加载图片
     * @param context   当前Activity的上下文对象
     * @param imageView
     */
    fun loadImage(context: Context?, url: String?, imageView: ImageView?, defaultImageRes: Int) {
        Glide.with(context!!).load(url)
            .error(defaultImageRes)
            .apply(RequestOptions().error(defaultImageRes))
            .into(imageView!!)
    }

    /**
     * @describe 加载正方形图片
     * @param context   当前Activity的上下文对象
     * @param imageView
     */
    fun loadSquareImage(context: Context?, url: String?, imageView: ImageView?) {
        Glide.with(context!!).load(url)
            .apply(
                RequestOptions().transform(CropSquareTransformation())
                    .error(R.drawable.default_img_bg)
            ) //                .skipMemoryCache(isSkipMemoryCache())
            //                .error(getErrorImage())
            //                .placeholder(getPlaceholder())
            .fallback(errorImage).into(imageView!!)
    }

    /**
     * @describe 加载圆角正方形图片
     * @param context   当前Activity的上下文对象
     * @param imageView
     */
    fun loadRoundSquareImage(context: Context?, url: String?, imageView: ImageView?) {
        Glide.with(context!!).load(url)
            .apply(
                RequestOptions().transform(CropSquareTransformation())
                    .error(R.drawable.default_img_bg)
            )
            .apply(
                RequestOptions().transform(
                    RoundedCornersTransformation(
                        QMUIDisplayHelper.dpToPx(6),
                        0
                    )
                ).error(
                    R.drawable.default_img_bg
                )
            ) //                .skipMemoryCache(isSkipMemoryCache())
            //                .error(getErrorImage())
            //                .placeholder(getPlaceholder())
            .fallback(errorImage).into(imageView!!)
    }

    /**
     * @describe 加载黑白图片
     * @param context   当前Activity的上下文对象
     * @param imageView
     */
    fun loadGrayscaleImage(context: Context?, url: String?, imageView: ImageView?) {
        Glide.with(context!!).load(url).apply(initOptions(GrayscaleTransformation()))
            .skipMemoryCache(isSkipMemoryCache).error(errorImage).placeholder(placeholder)
            .fallback(errorImage).circleCrop().into(imageView!!)
    }

    /**
     * @describe 加载圆角图片  默认所有圆角
     * @param context   当前Activity的上下文对象
     * @param imageView
     * @param radius 圆角
     */
    fun loadGrayscaleImage(context: Context?, url: String?, imageView: ImageView?, radius: Int) {
        Glide.with(context!!).load(url)
            .apply(initOptions(RoundedCornersTransformation(radius, 0, CornerType.ALL)))
            .skipMemoryCache(isSkipMemoryCache).error(errorImage).placeholder(placeholder)
            .fallback(errorImage).circleCrop().into(imageView!!)
    }

    /**
     * @describe 加载圆角图片
     * @param context   当前Activity的上下文对象
     * @param imageView
     * @param radius 圆角
     * @param cornerType 圆角类型
     */
    fun loadGrayscaleImage(
        context: Context?,
        url: String?,
        imageView: ImageView?,
        radius: Int,
        cornerType: CornerType?
    ) {
        Glide.with(context!!).load(url)
            .apply(initOptions(RoundedCornersTransformation(radius, 0, cornerType)))
            .skipMemoryCache(isSkipMemoryCache).error(errorImage).placeholder(placeholder)
            .fallback(errorImage).circleCrop().into(imageView!!)
    }

    /**
     * @describe 自定义裁剪
     * @param context   当前Activity的上下文对象
     * @param imageView
     * @param width,height 圆角宽高
     * @param cropType 裁剪位置
     */
    fun loadCropTransformationImage(
        context: Context?,
        url: String?,
        imageView: ImageView?,
        width: Int,
        height: Int,
        cropType: CropType?
    ) {
        Glide.with(context!!).load(url)
            .apply(initOptions(CropTransformation(width, height, cropType)))
            .skipMemoryCache(isSkipMemoryCache).error(errorImage).placeholder(placeholder)
            .fallback(errorImage).circleCrop().into(imageView!!)
    }

    /**
     * @describe 自定义裁剪 默认居中裁剪
     * @param context   当前Activity的上下文对象
     * @param imageView
     * @param width,height 圆角宽高
     */
    fun loadCropTransformationImage(
        context: Context?,
        url: String?,
        imageView: ImageView?,
        width: Int,
        height: Int
    ) {
        Glide.with(context!!).load(url)
            .apply(initOptions(CropTransformation(width, height, CropType.CENTER)))
            .skipMemoryCache(isSkipMemoryCache).error(errorImage).placeholder(placeholder)
            .fallback(errorImage).circleCrop().into(imageView!!)
    }

    /**
     * @describe 加载动图gif
     * @param context
     * @param url
     * @param imageView
     */
    fun loadGifImage(context: Context?, url: String?, imageView: ImageView?) {
        Glide.with(context!!).asGif().apply(initOptions())
            .skipMemoryCache(isSkipMemoryCache).load(url).error(errorImage).placeholder(placeholder)
            .fallback(errorImage).circleCrop().into(imageView!!)
    }

    /**
     * @describe 加载动图gif
     * @param url
     * @param imageView
     */
    fun loadGifImage(url: String?, imageView: ImageView) {
        Glide.with(imageView.context).asGif().apply(initOptions())
            .skipMemoryCache(isSkipMemoryCache).load(url).error(errorImage)
            .placeholder(placeholder).fallback(errorImage).circleCrop().into(imageView)
    }

    /**
     * @describe 加载高斯模糊大图
     * @param ambiguity 模糊度  eg ：80
     */
    fun loadTransformImage(url: String?, imageView: ImageView, ambiguity: Int) {
        Glide.with(imageView.context).load(url).skipMemoryCache(isSkipMemoryCache)
            .fallback(errorImage).placeholder(placeholder).error(errorImage)
            .apply(initOptions(BlurTransformation(ambiguity)))
            .into(imageView)
    }

    /**
     * @describe 加载缩略图
     * @param sizeMultiplier 如设置0.2f缩略
     */
    fun loadThumbnailImage(url: String?, imageView: ImageView, sizeMultiplier: Float) {
        Glide.with(imageView.context).load(url)
            .skipMemoryCache(isSkipMemoryCache)
            .thumbnail(sizeMultiplier) //缩略的参数
            .apply(initOptions())
            .into(imageView)
    }

    /**
     * @return  设置全局的错误图片 防止更改时多地方修改
     * @describe 当图片加载失败的时候显示
     */
    @get:DrawableRes
    private val errorImage: Int
        private get() = R.drawable.default_img_bg

    /**
     * @return 设置全局的占位图 防止更改时多地方修改
     * @describe 当图片没有加载出来的时候显示
     */
    @get:DrawableRes
    private val placeholder: Int
        private get() = R.drawable.default_img_bg

    /**
     * @return 返回当前石头 跳过内存缓存
     * true 不缓存 false 缓存
     */
    private val isSkipMemoryCache: Boolean
        private get() = false

    /**
     * @describe 设置缓存
     * Glide有两种缓存机制，一个是内存缓存，一个是硬盘缓存。
     * 内存缓存的主要作用是防止应用重复将图片数据读取到内存当中，
     * 而硬盘缓存的主要作用是防止应用重复从网络或其他地方重复下载和读取数据
     * @diskCacheStrategy参数
     * DiskCacheStrategy.NONE： 表示不缓存任何内容
     * DiskCacheStrategy.DATA： 表示只缓存原始图片
     * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片
     * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片
     * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）
     * @return 这里默认设置全部为禁止缓存
     */
    private fun initOptions(transformation: BitmapTransformation): RequestOptions {
        return RequestOptions()
            .transform(transformation)
            .skipMemoryCache(isSkipMemoryCache) //是否允许内存缓存
            .onlyRetrieveFromCache(true) //是否只从缓存加载图片
            .diskCacheStrategy(DiskCacheStrategy.NONE) //禁止磁盘缓存
    }

    private fun initOptions(): RequestOptions {
        return RequestOptions()
            .skipMemoryCache(isSkipMemoryCache) //是否允许内存缓存
            .onlyRetrieveFromCache(true) //是否只从缓存加载图片
            .diskCacheStrategy(DiskCacheStrategy.NONE) //禁止磁盘缓存
    }

    /**
     * @describe 清楚内容缓存
     */
    fun clearMemory(context: Context?) {
        Glide.get(context!!).clearMemory()
    }

    /**
     * @describe 清除磁盘缓存
     */
    fun clearDiskCache(context: Context?) {
        Glide.get(context!!).clearDiskCache()
    }

    /**
     * @describe 设置加载的效果
     * @param transformation
     * @return
     */
    private fun bitmapTransform(transformation: BitmapTransformation): RequestOptions {
        return RequestOptions()
    }
}