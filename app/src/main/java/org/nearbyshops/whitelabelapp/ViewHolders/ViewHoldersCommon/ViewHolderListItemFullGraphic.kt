package org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.nearbyshops.whitelabelapp.aaListUI.Interfaces.ListItemClickGeneral
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.databinding.ListItemFullGraphicBinding


class ViewHolderListItemFullGraphic(
    itemView: View,
    private val context: Context?,
    private val fragment: Fragment,
    private val adapter: RecyclerView.Adapter<*>
) : RecyclerView.ViewHolder(itemView){


    private lateinit var order: Order

    var binding = ListItemFullGraphicBinding.bind(itemView)



    fun listItemClick() {

        if (fragment is ListItemClickGeneral) {
            fragment.listItemCLick(order, layoutPosition)
        }
    }





    fun setItem(graphicData: ListItemFullGraphicData) {

        Picasso.get()
            .load(graphicData.graphicResourceID)
            .into(binding.imageGraphic)

//        binding.imageGraphic.scaleType = ImageView.ScaleType.FIT_CENTER
//        binding.imageGraphic.setBackgroundResource(graphicData.graphicResourceID)
    }





    private fun showToastMessage(message: String) {
        UtilityFunctions.showToastMessage(context, message)
    }





    init {

        // setonClickListeners
    }





    companion object {

//        : ViewType()


        data class ListItemFullGraphicData(val graphicResourceID:Int)
//            init {
//                viewType= AdapterKotlin.VIEW_TYPE_ORDER_BILL
//            }



        fun create(
            parent: ViewGroup, context: Context?, fragment: Fragment,
            adapter: RecyclerView.Adapter<*>
        ): ViewHolderListItemFullGraphic {


            val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_full_graphic, parent, false)

            return ViewHolderListItemFullGraphic(view, context, fragment, adapter)
        }
    }


}