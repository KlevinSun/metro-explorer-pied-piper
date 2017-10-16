package com.felixlin.dcmetroexplorer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.felixlin.dcmetroexplorer.R
import com.felixlin.dcmetroexplorer.model.Landmark
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class FavoriteListAdapter(var mContext: Context, val favorits: List<Landmark>): RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder>()
{

    class FavoriteViewHolder(val card: View) : RecyclerView.ViewHolder(card)

    override fun onBindViewHolder(holder: FavoriteViewHolder?, position: Int) {
        if (holder != null)
        {
            val favorite = favorits[position]
            holder.card.tv_landmark.text = favorite.name
            holder.card.tv_rating.text = favorite.rating.toString()

            if(favorite.imageUrl.isEmpty())
            {
                holder.card.iv_landmark.setImageDrawable(R.drawable.jefferson_memorial)
            }
            else{
                Picasso.with(getContext()).load(favorite.imageUrl).into(holder.card.iv_landmark)
            }

        }
    }

    override fun getItemCount() = favorits.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return FavoriteViewHolder(view)
    }

    private fun getContext(): Context
    {
        return mContext
    }
}

private fun ImageView.setImageDrawable(jefferson_memorial: Int) {}
