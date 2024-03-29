package com.nxg.app.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.nxg.app.R
import com.nxg.loopscrollanimation.RecyclerViewLoopScrollAnimation
import com.nxg.loopscrollanimation.RecyclerViewScrollAction
import com.nxg.loopscrollanimation.utils.LogUtil
import com.nxg.recyclerview.widget.LooperLinearLayoutManager

/**
 * 双色球适配器
 */
class DoubleColorBallAdapter(
    private val context: Context,
    @LayoutRes var resource: Int,
    private val data: MutableList<List<TextBean>>,
    var fakeResult: MutableList<String> = mutableListOf()
) :
    RecyclerView.Adapter<DoubleColorBallAdapter.DoubleColorBallViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoubleColorBallViewHolder {
        return DoubleColorBallViewHolder(
            LayoutInflater.from(context).inflate(resource, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DoubleColorBallViewHolder, position: Int) {
        if (position < 6) {
            holder.doubleColorBallBg.setBackgroundResource(R.drawable.shape_double_color_ball_red)
            val loopScrollViewTextAdapter = LoopScrollViewTextAdapter(
                context,
                R.layout.loop_scroll_view_item_text_red,
                data[position]
            )
            //LogUtil.i("DoubleColorBallAdapter", "i = $position, before ${data[position]}")
            holder.recyclerView.adapter = loopScrollViewTextAdapter
        } else {
            holder.doubleColorBallBg.setBackgroundResource(R.drawable.shape_double_color_ball_blue)
            val loopScrollViewTextAdapter = LoopScrollViewTextAdapter(
                context,
                R.layout.loop_scroll_view_item_text_blue,
                data[position]
            )
            //LogUtil.i("DoubleColorBallAdapter", "i = $position, before ${data[position]}")
            holder.recyclerView.adapter = loopScrollViewTextAdapter

        }
        val looperLinearLayoutManager =
            LooperLinearLayoutManager(context, RecyclerView.VERTICAL, false)
        holder.recyclerView.layoutManager = looperLinearLayoutManager
        holder.recyclerViewLoopScrollAnimation.setConfiguration(holder.recyclerViewLoopScrollAnimationConfiguration)
        holder.recyclerViewLoopScrollAnimation.attachToRecyclerView(holder.recyclerView)
        holder.recyclerViewLoopScrollAnimation.setRecyclerViewScrollAction(object :
            RecyclerViewScrollAction {
            override fun findFirstVisibleItemPosition(): Int {
                return looperLinearLayoutManager.findFirstVisibleItemPosition()
            }

            override fun findLastVisibleItemPosition(): Int {
                val selectPosition = looperLinearLayoutManager.findLastVisibleItemPosition()
                //指定号码
                if (fakeResult.size > 0) {
                    val adapter = holder.recyclerView.adapter as LoopScrollViewTextAdapter
                    /*LogUtil.i(
                        "DoubleColorBallAdapter",
                        "adapter.dataList[selectPosition].text ${adapter.dataList[selectPosition].text}"
                    )
                    LogUtil.i(
                        "DoubleColorBallAdapter",
                        "fakeResult[holder.adapterPosition].text ${fakeResult[holder.adapterPosition]}"
                    )*/
                    adapter.dataList[selectPosition].text = fakeResult[holder.adapterPosition]
                    adapter.notifyItemChanged(selectPosition)
                }
                return selectPosition
            }
        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class DoubleColorBallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.double_color_ball_recycler_view)
        val doubleColorBallBg: LinearLayout = itemView.findViewById(R.id.double_color_ball_bg)
        val recyclerViewLoopScrollAnimation = RecyclerViewLoopScrollAnimation()
        val recyclerViewLoopScrollAnimationConfiguration = RecyclerViewLoopScrollAnimation.build {
            scrollAnimatorDuration = 4000L
        }
    }
}