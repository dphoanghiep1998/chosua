package com.neko.hiepdph.dogtranslatorlofi.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neko.hiepdph.dogtranslatorlofi.common.clickWithDebounce
import com.neko.hiepdph.dogtranslatorlofi.common.hide
import com.neko.hiepdph.dogtranslatorlofi.common.invisible
import com.neko.hiepdph.dogtranslatorlofi.data.DogModel
import com.neko.hiepdph.dogtranslatorlofi.databinding.LayoutItemDogBinding

class RoarDogTypeAdapter(private val onClickItem: (DogModel) -> Unit) :
    RecyclerView.Adapter<RoarDogTypeAdapter.ActionDogTypeViewHolder>() {
    private var data = mutableListOf<DogModel>()

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
            binding.tvContent.invisible()
            binding.root.clickWithDebounce {
                onClickItem(item)
            }
        }
    }
}