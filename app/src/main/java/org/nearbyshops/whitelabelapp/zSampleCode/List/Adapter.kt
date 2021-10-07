package org.nearbyshops.whitelabelapp.zSampleCode.List

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.zSampleCode.ItemListDialogFragment

import org.nearbyshops.whitelabelapp.zSampleCode.List.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class Adapter(
    private val values: List<DummyItem>,
    private var fragment: Fragment
) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }

        init {
            view.setOnClickListener {

                ItemListDialogFragment.newInstance(130)
                    .show(fragment.requireActivity().supportFragmentManager, "dialog")
//                UtilityFunctions.showToastMessage(view.context,"helllo!")


            }
        }
    }
}