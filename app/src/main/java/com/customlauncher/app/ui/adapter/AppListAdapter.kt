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

class AppListAdapter(
    private val onAppClick: (AppInfo) -> Unit,
    private val onAppLongClick: (AppInfo) -> Unit
) : ListAdapter<AppInfo, AppListAdapter.AppViewHolder>(AppDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view, onAppClick, onAppLongClick)
    }
    
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    override fun onViewRecycled(holder: AppViewHolder) {
        super.onViewRecycled(holder)
        holder.cancelIconLoad()
    }
    
    class AppViewHolder(
        itemView: View,
        private val onAppClick: (AppInfo) -> Unit,
        private val onAppLongClick: (AppInfo) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        private val appName: TextView = itemView.findViewById(R.id.appName)
        private val packageName: TextView = itemView.findViewById(R.id.packageName)
        private val checkBox: android.widget.FrameLayout = itemView.findViewById(R.id.checkBox)
        private val checkIcon: ImageView = itemView.findViewById(R.id.checkIcon)
        private var iconLoadJob: Job? = null
        
        fun bind(app: AppInfo) {
            appName.text = app.appName
            packageName.text = app.packageName
            
            // Set placeholder immediately
            appIcon.setImageDrawable(app.icon)
            
            // Cancel previous icon load
            iconLoadJob?.cancel()
            
            // Load real icon asynchronously
            iconLoadJob = CoroutineScope(Dispatchers.Main).launch {
                val icon = withContext(Dispatchers.IO) {
                    try {
                        IconCache.loadIcon(
                            itemView.context,
                            app.packageName,
                            itemView.context.packageManager
                        )
                    } catch (e: Exception) {
                        app.icon // Use placeholder on error
                    }
                }
                appIcon.setImageDrawable(icon)
            }
            
            // Update checkbox state
            checkIcon.visibility = if (app.isSelected) View.VISIBLE else View.GONE
            
            // Set background based on selection
            itemView.alpha = if (app.isSelected) 0.9f else 1.0f
            
            // Set click listener for the whole item
            itemView.setOnClickListener {
                onAppClick(app)
            }
            
            // Also handle checkbox clicks
            checkBox.setOnClickListener {
                onAppClick(app)
            }
            
            itemView.setOnLongClickListener {
                onAppLongClick(app)
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
