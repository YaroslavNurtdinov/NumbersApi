package com.example.numbersapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.numbersapi.R
import com.example.numbersapi.data.database.entity.NumbersEntity
import com.example.numbersapi.databinding.NumbersRowLayoutBinding
import com.example.numbersapi.ui.fragments.main.MainFragmentDirections
import com.example.numbersapi.util.NumbersDiffUtil


class MainAdapter:RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    private var numbersEntity = emptyList<NumbersEntity>()

    fun setData(newData : List<NumbersEntity>){
        val numbersDiffUtil = NumbersDiffUtil(numbersEntity,newData)
        val diffUtilResult = DiffUtil.calculateDiff(numbersDiffUtil)
        numbersEntity = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val binding = NumbersRowLayoutBinding.bind(itemView)
        val numberRowLayout = binding.numberRowLayout

        fun bind(result:NumbersEntity)= with(binding){
            titleTextView.text = result.numbers.number.toString()
            descriptionTextView.text = result.numbers.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.numbers_row_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFact = numbersEntity[position]
        holder.bind(currentFact)

        holder.numberRowLayout.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToOverviewFragment(currentFact.numbers)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = numbersEntity.size
}