package com.example.takeanote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ColorAdapter(val mOnColorSelectedListener: OnColorSelectedListener) :
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    inner class ColorViewHolder(val colorView: View) : RecyclerView.ViewHolder(colorView),
        View.OnClickListener {
        init {
            colorView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mOnColorSelectedListener.onColorSelected(ColorPickerFragment.value[adapterPosition].color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder =
        ColorViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.color_box,
                parent,
                false
            )
        )

    override fun getItemCount() = ColorPickerFragment.value.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.colorView.setBackgroundColor(
            ContextCompat.getColor(
                holder.colorView.context,
                ColorPickerFragment.value[position].color
            )
        )
    }


}