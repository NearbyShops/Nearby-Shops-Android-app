package org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.nearbyshops.whitelabelapp.R


class ViewHolderFullScreenProgressBar(var item_view: View,
                                      private val context: Context?,
                                      private val fragment: Fragment): RecyclerView.ViewHolder(item_view)
{


    private var item: FullScreenProgressBarData? = null


    fun setItem(data: FullScreenProgressBarData?) {
        item = data
    }





    private fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }



    init {

    }



    companion object {

        fun create(
            parent: ViewGroup,
            context: Context?,
            fragment: Fragment
        ): ViewHolderFullScreenProgressBar {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_empty_screen_full_screen_progress_circle, parent, false)
            return ViewHolderFullScreenProgressBar(view, context, fragment)
        }


        class FullScreenProgressBarData
    }


}