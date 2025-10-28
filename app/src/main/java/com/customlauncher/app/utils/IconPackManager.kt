package com.customlauncher.app.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import com.customlauncher.app.data.model.IconPack
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class IconPackManager(private val context: Context) {
    
    companion object {
        private const val TAG = "IconPackManager"
        private const val NOVA_LAUNCHER = "com.teslacoilsw.launcher"
        private const val GO_LAUNCHER = "com.gau.go.launcherex"
        private const val APEX_LAUNCHER = "com.anddoes.launcher"
        private const val ADW_LAUNCHER = "org.adw.launcher"
        private const val ACTION_LAUNCHER = "com.actionlauncher.playstore"
    }
    
    fun getAvailableIconPacks(): List<IconPack> {
        val iconPacks = mutableListOf<IconPack>()
        val packageManager = context.packageManager
        
        // Add system default first
        try {
            val systemIcon = context.packageManager.defaultActivityIcon
            iconPacks.add(
                IconPack(
                    packageName = "",
                    name = "System",
                    icon = systemIcon,
                    isSystemDefault = true
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error getting system icon", e)
        }
        
        // Find installed icon packs by checking for launcher themes
        val iconPackIntents = listOf(
            Intent("com.teslacoilsw.launcher.THEME"),
            Intent("com.gau.go.launcherex.theme"),
            Intent("com.anddoes.launcher.THEME"),
            Intent("org.adw.launcher.THEMES"),
            Intent("com.actionlauncher.THEME"),
            Intent("android.intent.action.MAIN").apply {
                addCategory("com.anddoes.launcher.THEME")
            }
        )
        
        val processedPackages = mutableSetOf<String>()
        
        for (intent in iconPackIntents) {
            try {
                val resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.GET_META_DATA)
                for (info in resolveInfos) {
                    val packageName = info.activityInfo.packageName
                    if (packageName !in processedPackages) {
                        processedPackages.add(packageName)
                        try {
                            val appInfo = packageManager.getApplicationInfo(packageName, 0)
                            val icon = packageManager.getApplicationIcon(appInfo)
                            val name = packageManager.getApplicationLabel(appInfo).toString()
                            
                            iconPacks.add(
                                IconPack(
                                    packageName = packageName,
                                    name = name,
                                    icon = icon,
                                    isSystemDefault = false
                                )
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "Error loading icon pack: $packageName", e)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error querying icon packs for intent: $intent", e)
            }
        }
        
        return iconPacks
    }
    
    fun getIconFromPack(packageName: String?, componentName: ComponentName): Drawable? {
        if (packageName == null) return null
        
        try {
            val iconPackResources = context.packageManager.getResourcesForApplication(packageName)
            val iconResId = getIconResId(iconPackResources, packageName, componentName)
            
            if (iconResId != 0) {
                return iconPackResources.getDrawable(iconResId, null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting icon from pack: $packageName for $componentName", e)
        }
        
        return null
    }
    
    private fun getIconResId(
        iconPackResources: Resources, 
        iconPackPackage: String, 
        componentName: ComponentName
    ): Int {
        // Try different naming conventions
        val componentKey = componentName.flattenToString().replace("/", ".")
        val shortComponentKey = componentName.className
        val packageKey = componentName.packageName
        
        val possibleNames = listOf(
            componentKey,
            componentKey.lowercase(),
            shortComponentKey,
            shortComponentKey.lowercase(),
            packageKey,
            "${componentName.packageName}.${componentName.className}",
            componentName.className.substringAfterLast(".")
        )
        
        for (name in possibleNames) {
            try {
                val resId = iconPackResources.getIdentifier(name, "drawable", iconPackPackage)
                if (resId != 0) {
                    return resId
                }
            } catch (e: Exception) {
                // Try next name
            }
        }
        
        // Try to find from appfilter.xml if exists
        return getIconFromAppFilter(iconPackResources, iconPackPackage, componentName)
    }
    
    private fun getIconFromAppFilter(
        iconPackResources: Resources,
        iconPackPackage: String,
        componentName: ComponentName
    ): Int {
        try {
            val appFilterId = iconPackResources.getIdentifier("appfilter", "xml", iconPackPackage)
            if (appFilterId == 0) return 0
            
            val parser = iconPackResources.getXml(appFilterId)
            val componentString = componentName.flattenToString()
            
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.name == "item") {
                    val component = parser.getAttributeValue(null, "component")
                    if (component != null && component.contains(componentString)) {
                        val drawable = parser.getAttributeValue(null, "drawable")
                        if (drawable != null) {
                            return iconPackResources.getIdentifier(drawable, "drawable", iconPackPackage)
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing appfilter.xml", e)
        }
        
        return 0
    }
}
