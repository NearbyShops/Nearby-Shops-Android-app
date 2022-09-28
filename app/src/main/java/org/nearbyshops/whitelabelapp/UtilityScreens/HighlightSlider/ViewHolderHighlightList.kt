package org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model.HighlightList
import org.nearbyshops.whitelabelapp.databinding.ListItemHighlightListBinding


class ViewHolderHighlightList(
    itemView: View,
    private val context: Context,
    private val fragment: Fragment
) : RecyclerView.ViewHolder(itemView) {


    var binding = ListItemHighlightListBinding.bind(itemView)
    private lateinit var highlightList:HighlightList;


    fun setItem(adapter: RecyclerView.Adapter<*>?, highlightList: HighlightList) {

        this.highlightList = highlightList;

        if (highlightList.listTitle == null) {
            binding.listTitle.visibility = View.GONE
        } else {
            binding.listTitle.visibility = View.VISIBLE
            binding.listTitle.text = highlightList.listTitle
        }


        if(!highlightList.isShowTutorial)
        {
            binding.listTitle.visibility = View.GONE
        }


        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = layoutManager
    }

    fun scrollToPosition(scrollToPosition: Int) {
        if (scrollToPosition > 0) {
            binding.recyclerView.scrollToPosition(scrollToPosition)
        }
    }

    fun setTextSize(size: Float) {
        binding.listTitle.textSize = size
    }


    init {

    }



    companion object {
        fun create(
            parent: ViewGroup,
            context: Context,
            fragment: Fragment
        ): ViewHolderHighlightList {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_highlight_list, parent, false)
            return ViewHolderHighlightList(view, context, fragment)
        }
    }

}