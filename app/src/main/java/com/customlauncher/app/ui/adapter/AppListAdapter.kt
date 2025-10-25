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
        
        fun bind(app: AppInfo) {
            appIcon.setImageDrawable(app.icon)
            appName.text = app.appName
            packageName.text = app.packageName
            
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
