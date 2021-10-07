package org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model.HighlightListItem
import org.nearbyshops.whitelabelapp.databinding.ListItemHighlightBinding


class ViewHolderHighlightListItem(
    itemView: View,
    private val context: Context,
    private val fragment: Fragment,
    private val adapter: RecyclerView.Adapter<*>?
) : RecyclerView.ViewHolder(itemView) {


    private lateinit var highlightListItem: HighlightListItem
    var binding = ListItemHighlightBinding.bind(itemView)




    fun setItem(highlightListItem: HighlightListItem) {

        this.highlightListItem = highlightListItem

        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px)

//        String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/VehicleType/Image/" + "five_hundred_"+ this.highlightItem.getImagePath() + ".jpg";
//        String image_url = PrefGeneral.getServiceURL(context) + "/api/v1/VehicleType/Image/" + this.highlightItem.getImagePath();
        Picasso.get()
            .load(highlightListItem.imageURL)
            .placeholder(drawable!!)
            .into(binding.image)
    }




    fun listItemClick() {

        if (fragment is ListItemClick) {

            fragment.listItemCLick(
                highlightListItem,
                highlightListItem.slideNumber)
        }
    }




    interface ListItemClick {

        fun listItemCLick(item:Any,slideNumber:Int)
    }



    init {
        binding.listItem.setOnClickListener { listItemClick() }
    }



    companion object {
        @JvmStatic
        fun create(
            parent: ViewGroup,
            context: Context,
            fragment: Fragment,
            adapter: RecyclerView.Adapter<*>?
        ): ViewHolderHighlightListItem {


            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_highlight, parent, false)

            return ViewHolderHighlightListItem(view, context, fragment, adapter)
        }
    }


} // ViewHolder Class declaration ends
