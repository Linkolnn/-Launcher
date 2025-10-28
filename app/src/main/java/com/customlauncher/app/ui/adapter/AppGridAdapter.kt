package com.customlauncher.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.customlauncher.app.R
import com.customlauncher.app.data.model.AppInfo
import com.customlauncher.app.utils.IconCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import android.util.Log
import android.widget.PopupWindow
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.graphics.Color
import android.os.Build

class AppGridAdapter(
    private val onAppClick: (AppInfo) -> Unit,
    private val onAppLongClick: ((AppInfo, View) -> Unit)? = null
) : ListAdapter<AppInfo, AppGridAdapter.ViewHolder>(AppDiffCallback()) {
    
    // Single scope for all icon loading tasks with supervisor job
    private val adapterScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_grid, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.bind(getItem(position))
        } catch (e: Exception) {
            Log.e("AppGridAdapter", "Error binding item at position $position", e)
        }
    }
    
    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.cancelIconLoad()
    }
    
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        // Cancel all coroutines when adapter is detached
        adapterScope.cancel()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        private val appName: TextView = itemView.findViewById(R.id.appName)
        private var iconLoadJob: Job? = null
        
        fun bind(app: AppInfo) {
            appName.text = app.appName
            
            // Set placeholder immediately
            appIcon.setImageDrawable(app.icon)
            
            // Cancel previous icon load
            cancelIconLoad()
            
            // Load real icon asynchronously using adapter scope
            iconLoadJob = adapterScope.launch {
                try {
                    val icon = withContext(Dispatchers.IO) {
                        // Create ComponentName for icon pack support
                        val componentName = itemView.context.packageManager
                            .getLaunchIntentForPackage(app.packageName)?.component
                        
                        IconCache.loadIcon(
                            itemView.context,
                            app.packageName,
                            itemView.context.packageManager,
                            componentName
                        )
                    }
                    // Check if job is still active before updating UI
                    if (iconLoadJob?.isActive == true) {
                        appIcon.setImageDrawable(icon)
                    }
                } catch (e: Exception) {
                    Log.d("AppGridAdapter", "Error loading icon for ${app.packageName}")
                    // Use placeholder on error - already set
                }
            }
            
            itemView.setOnClickListener {
                onAppClick(app)
            }
            
            // Handle long click for context menu
            itemView.setOnLongClickListener { view ->
                onAppLongClick?.invoke(app, view)
                true
            }
        }
        
        fun cancelIconLoad() {
            iconLoadJob?.cancel()
        }
    }
    
    private class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }
        
        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }
}
