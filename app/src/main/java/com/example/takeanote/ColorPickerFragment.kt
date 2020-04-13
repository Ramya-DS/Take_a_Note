package com.example.takeanote


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

/**
 * A simple [Fragment] subclass.
 */
class ColorPickerFragment : DialogFragment(), OnColorSelectedListener {


    companion object {
        fun newInstance() = ColorPickerFragment().apply {
            this.arguments = Bundle()
        }

        //TODO Why is this variable nullable ?
        var value: Array<BackgroundColor>? = BackgroundColor.values()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_color_pickerfragment, container, false)

        val colorList = rootView.findViewById<RecyclerView>(R.id.color_recycler)
        colorList.layoutManager = GridLayoutManager(context, 3)
        colorList.setHasFixedSize(true)
        colorList.adapter = ColorAdapter(this,WeakReference(context!!))
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.ColorAnimation
    }



    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onColorSelected(color: Int) {
        targetFragment?.onActivityResult(1, Activity.RESULT_OK, Intent().apply {
            this.putExtra("color",color )
        })
        this.dismiss()
    }
}
