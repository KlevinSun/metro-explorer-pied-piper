package com.felixlin.dcmetroexplorer.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.felixlin.dcmetroexplorer.R
import com.felixlin.dcmetroexplorer.R.drawable.*
import com.felixlin.dcmetroexplorer.model.Landmark
import com.felixlin.dcmetroexplorer.ui.LandmarkDetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class LandmarksListAdapter(var mContext: Context, val landmarks: List<Landmark>): RecyclerView.Adapter<LandmarksListAdapter.LandmarksViewHolder>()
{
    private fun getContext(): Context
    {
        return mContext
    }

    inner class LandmarksViewHolder(val card: View) : RecyclerView.ViewHolder(card), View.OnClickListener
    {

        init {
            card.setOnClickListener(this)
        }

        override fun onClick(v: View)
        {
            var landmark = landmarks[adapterPosition]

            var intent = Intent(getContext(), LandmarkDetailActivity::class.java)
            intent.putExtra("LANDMARK", landmark)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            getContext().startActivity(intent)
        }
    }

    override fun onBindViewHolder(holder: LandmarksViewHolder?, index: Int)
    {
        if (holder != null)
        {
            val landmark = landmarks[index]
            holder.card.tv_landmark.text = landmark.name
            holder.card.tv_rating.text = landmark.rating.toString()

            if(landmark.imageUrl.isEmpty())
            {
                holder.card.iv_landmark.setImageDrawable(jefferson_memorial)
            }
            else
            {
                Picasso.with(getContext()).load(landmark.imageUrl).into(holder.card.iv_landmark)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandmarksViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return LandmarksViewHolder(view)
    }

    override fun getItemCount() = landmarks.size
}

private fun ImageView.setImageDrawable(jefferson_memorial: Int) {}
