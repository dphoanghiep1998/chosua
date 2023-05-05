package com.neko.hiepdph.dogtranslatorlofi.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.neko.hiepdph.dogtranslatorlofi.R
import com.neko.hiepdph.dogtranslatorlofi.common.clickWithDebounce
import com.neko.hiepdph.dogtranslatorlofi.data.DogModel
import com.neko.hiepdph.dogtranslatorlofi.databinding.LayoutItemDogBinding

class ActionDogTypeAdapter(val context: Context, private val onClickItem: (DogModel) -> Unit) :
    RecyclerView.Adapter<ActionDogTypeAdapter.ActionDogTypeViewHolder>() {

    private var data = mutableListOf<DogModel>()
    private val scaleAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.scale_anim)


    fun setData(rawData: MutableList<DogModel>) {
        data.clear()
        data.addAll(rawData)
        notifyDataSetChanged()
    }

    inner class ActionDogTypeViewHolder(val binding: LayoutItemDogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionDogTypeViewHolder {
        val binding =
            LayoutItemDogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActionDogTypeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ActionDogTypeViewHolder, position: Int) {
        with(holder) {
            val item = data[adapterPosition]
            binding.imvDog.setImageResource(item.image)
            binding.tvContent.text = itemView.context.getString(item.content)
            binding.root.clickWithDebounce {
                binding.root.startAnimation(scaleAnimation)
                onClickItem(item)
            }
        }
    }
}