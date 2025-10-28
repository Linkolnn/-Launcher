package com.customlauncher.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.customlauncher.app.R
import com.customlauncher.app.data.model.IconPack

class IconPackAdapter(
    private val onIconPackSelected: (IconPack) -> Unit
) : RecyclerView.Adapter<IconPackAdapter.IconPackViewHolder>() {
    
    private var iconPacks = listOf<IconPack>()
    private var selectedPackage: String? = null
    
    fun setIconPacks(packs: List<IconPack>) {
        iconPacks = packs
        notifyDataSetChanged()
    }
    
    fun setSelectedPack(packageName: String?) {
        selectedPackage = packageName
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconPackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_icon_pack, parent, false)
        return IconPackViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: IconPackViewHolder, position: Int) {
        holder.bind(iconPacks[position])
    }
    
    override fun getItemCount() = iconPacks.size
    
    inner class IconPackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconPackIcon: ImageView = itemView.findViewById(R.id.iconPackIcon)
        private val iconPackName: TextView = itemView.findViewById(R.id.iconPackName)
        private val selectionRing: View = itemView.findViewById(R.id.selectionRing)
        private val checkMarkContainer: FrameLayout = itemView.findViewById(R.id.checkMarkContainer)
        
        fun bind(iconPack: IconPack) {
            iconPackIcon.setImageDrawable(iconPack.icon)
            iconPackName.text = if (iconPack.isSystemDefault) "Системные" else iconPack.name
            
            val isSelected = iconPack.packageName == selectedPackage || 
                            (selectedPackage == null && iconPack.isSystemDefault)
            
            selectionRing.visibility = if (isSelected) View.VISIBLE else View.GONE
            checkMarkContainer.visibility = if (isSelected) View.VISIBLE else View.GONE
            
            itemView.setOnClickListener {
                val oldSelection = selectedPackage
                selectedPackage = if (iconPack.isSystemDefault) null else iconPack.packageName
                
                // Update UI
                iconPacks.forEachIndexed { index, pack ->
                    if (pack.packageName == oldSelection || pack.packageName == selectedPackage ||
                        (oldSelection == null && pack.isSystemDefault) ||
                        (selectedPackage == null && pack.isSystemDefault)) {
                        notifyItemChanged(index)
                    }
                }
                
                onIconPackSelected(iconPack)
            }
        }
    }
}
