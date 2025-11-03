package com.customlauncher.app.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.customlauncher.app.R
import com.customlauncher.app.data.model.HomeItemModel

/**
 * ViewHolder for home screen items to improve performance
 */
sealed class HomeScreenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    abstract fun bind(item: HomeItemModel)
    abstract fun unbind()
    
    /**
     * ViewHolder for app shortcuts
     */
    class AppViewHolder(itemView: View) : HomeScreenViewHolder(itemView) {
        val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        val appLabel: TextView = itemView.findViewById(R.id.appLabel)
        
        override fun bind(item: HomeItemModel) {
            // Binding will be done in adapter
        }
        
        override fun unbind() {
            // Clear references to avoid memory leaks
            appIcon.setImageDrawable(null)
            appLabel.text = ""
        }
    }
    
    /**
     * ViewHolder for widgets
     */
    class WidgetViewHolder(itemView: View) : HomeScreenViewHolder(itemView) {
        // Widget views are dynamic, so we just hold the container
        
        override fun bind(item: HomeItemModel) {
            // Widget binding will be done in adapter
        }
        
        override fun unbind() {
            // Widget cleanup will be done in adapter
        }
    }
}
