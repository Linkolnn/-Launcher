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

class AppGridAdapter(
    private val onAppClick: (AppInfo) -> Unit
) : ListAdapter<AppInfo, AppGridAdapter.ViewHolder>(AppDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_grid, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.cancelIconLoad()
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
            
            itemView.setOnClickListener {
                onAppClick(app)
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
