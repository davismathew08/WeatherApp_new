package com.example.weatherappkotlin.ui.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappkotlin.R
import com.example.weatherappkotlin.model.location_details.AddLocationDetails
import kotlinx.android.synthetic.main.item_weather_selected_locations.view.*
import java.util.ArrayList


class SelectedLocationsAdapter(private var locationDetailsList: ArrayList<AddLocationDetails>,
                               val onItemDeleteClicked: (Int) -> Unit, val onItemSelected:(Int)->Unit,
val onItemBooked:(Int)->Unit,val onItemRemove:(Int)->Unit) : RecyclerView.Adapter<SelectedLocationsAdapter.ViewHold>()
{
    private var selectedPosition : Int = 0
    private var context: Context? = null
    private var row_index = -1

    class ViewHold(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold
    {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather_selected_locations, parent, false)
        return ViewHold(
            view
        )
    }

    override fun getItemCount(): Int
    {
        return locationDetailsList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int)
    {
        if(locationDetailsList.size>0){
            holder.itemView.tvSelectedLocationName.text=locationDetailsList[position].location_name
            if(locationDetailsList[position].bookedmarked){
                holder.itemView.ivBookmarked.visibility=View.VISIBLE
            }
        }
        holder.itemView.ivDelete.setOnClickListener {
            onItemDeleteClicked.invoke(position)
        }
        holder.itemView.ivOutlinedBookmark.setOnClickListener {
            locationDetailsList[position].bookedmarked=true
            holder.itemView.ivOutlinedBookmark.visibility=View.GONE
            holder.itemView.ivBookmarked.visibility=View.VISIBLE
            onItemBooked.invoke(position)
        }
        holder.itemView.ivBookmarked.setOnClickListener {
            locationDetailsList[position].bookedmarked=false
            holder.itemView.ivOutlinedBookmark.visibility=View.VISIBLE
            holder.itemView.ivBookmarked.visibility=View.GONE
            onItemRemove.invoke(position)
        }
        holder.itemView.setOnClickListener {
            if(locationDetailsList[position].bookedmarked){
                onItemSelected.invoke(position)
            }
            else{
                Toast.makeText(context,"Please Bookmark the location",Toast.LENGTH_SHORT).show()
            }
        }

       /* holder.itemView.radioDialog.isChecked = selectedPosition == position

        holder.itemView.radioDialog.setOnClickListener {
            if(holder.itemView.radioDialog.isChecked)
            {
                row_index = position
                notifyDataSetChanged()
                funSelectedGov.invoke(position)
            }
        }
        holder.itemView.radioDialog.isChecked = row_index==position

        holder.itemView.tvDialogAddress.text = governorsList[position].name*/


    }
}