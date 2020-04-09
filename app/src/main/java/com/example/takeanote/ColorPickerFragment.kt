package com.example.takeanote


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass.
 */
class ColorPickerFragment : DialogFragment() {


    companion object {
        fun newInstance() = ColorPickerFragment().apply {
            this.arguments = Bundle()
        }

        val value = BackgroundColor.values()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_color_pickerfragment, container, false)

        val colorList = rootView.findViewById<RecyclerView>(R.id.color_recycler)
        colorList.layoutManager = GridLayoutManager(context, 3)
        colorList.setHasFixedSize(true)
        colorList.adapter=ColorAdapter()
        return rootView
    }

    inner class ColorAdapter : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

        inner class ColorViewHolder(val colorView: View) : RecyclerView.ViewHolder(colorView),
            View.OnClickListener {
            init {
                colorView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                targetFragment?.onActivityResult(1, Activity.RESULT_OK,Intent().apply {
                    this.putExtra("color", value[adapterPosition].color)
                })
                this@ColorPickerFragment.dismiss()
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

        override fun getItemCount() = value.size

        override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
            holder.colorView.setBackgroundColor(getColor(context!!,value[position].color))
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations=R.style.ColorAnimation
    }

}
