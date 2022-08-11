package com.xu.commonutils.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager

/**
 * desc:
 **
 * user: xujj
 * time: 2022/8/10 16:37
 **/
object StatusBarUtils {

    /**
     * @param activity
     * @param isLight 状态栏和导航栏图标和文字是否浅色
     */
    fun init(activity: Activity, isLight: Boolean = true) {
        init(activity, isLight, isLight)
    }

    /**
     * @param activity
     * @param isLightStatusBar     状态栏图标和文字是否浅色
     * @param isLightNavigationBar 导航栏图标和文字是否浅色
     */
    fun init(activity: Activity, isLightStatusBar: Boolean, isLightNavigationBar: Boolean) {
        val window = activity.window
        var visibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        if (!isLightStatusBar) { //状态栏图标和文字颜色为暗色
            visibility = visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (!isLightNavigationBar) { //导航栏图标和文字颜色为暗色
            visibility = visibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        window.decorView.systemUiVisibility = visibility
        //透明状态栏-导航栏FLAG_TRANSLUCENT_NAVIGATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //系统栏设置不强调，否则不会完全透明
                window.isNavigationBarContrastEnforced = false
                window.isStatusBarContrastEnforced = false
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) //设置透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) //设置透明导航栏
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        var height = 0
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId)
        }
        return height
    }

    fun getNavigationBarHeight(context: Context): Int {
        var height = 0
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (hasNavBar(context) && resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId)
        }
        return height
    }

    fun hasNavBar(context: Context): Boolean {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val defaultDisplay = windowManager.defaultDisplay
        //获取屏幕高度
        val outMetrics = DisplayMetrics()
        defaultDisplay.getRealMetrics(outMetrics)
        val heightPixels = outMetrics.heightPixels
        val widthPixels = outMetrics.widthPixels

        //获取内容高度
        val outMetrics2 = DisplayMetrics()
        defaultDisplay.getMetrics(outMetrics2)
        val heightPixels2 = outMetrics2.heightPixels
        val widthPixels2 = outMetrics2.widthPixels
        val statusBarHeight = getStatusBarHeight(context)
        return heightPixels > heightPixels2 + statusBarHeight || widthPixels > widthPixels2 + statusBarHeight
    }

    /**
     * 沉浸式状态栏
     */
    fun immerseStatusBar(view: View?) {
        if (view == null || view.context == null) return
        view.setPadding(0, getStatusBarHeight(view.context), 0, 0)
    }

    /**
     * 沉浸式导航栏
     */
    fun immerseNavigationBar(view: View?) {
        if (view == null || view.context == null) return
        view.setPadding(0, 0, 0, getNavigationBarHeight(view.context))
    }

    /**
     * 沉浸式状态栏、导航栏
     */
    fun fitsSystemWindows(view: View?) {
        if (view == null || view.context == null) return
        view.setPadding(
            0,
            getStatusBarHeight(view.context),
            0,
            getNavigationBarHeight(view.context)
        )
    }
}