package com.felixlin.dcmetroexplorer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.felixlin.dcmetroexplorer.R
import kotlinx.android.synthetic.main.row_station.view.*

class MetroStationListAdapter(private var context: Context, private var MetroData: ArrayList<String>) : RecyclerView.Adapter<MetroStationListAdapter.ViewHolder>(){

    private var TAG: String = "MetroStationAdapter"

    override fun getItemCount() = MetroData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_station, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = MetroData[position]
        holder.itemView.stationName.text = station

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun filterList(newList : ArrayList<String>){
        MetroData = newList
        notifyDataSetChanged()
    }
}