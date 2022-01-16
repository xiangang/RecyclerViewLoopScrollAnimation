package com.nxg.app.main

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nxg.app.R
import com.nxg.app.databinding.MainFragmentBinding
import com.nxg.loopscrollanimation.RecyclerViewLoopScrollAnimation
import com.nxg.loopscrollanimation.RecyclerViewScrollAction
import com.nxg.loopscrollanimation.utils.LogUtil

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var _binding: MainFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val doubleColorBallDataA = mutableListOf<List<TextBean>>()
    private val doubleColorBallDataB = mutableListOf<List<TextBean>>()
    private val doubleColorBallDataC = mutableListOf<List<TextBean>>()
    private val doubleColorBallDataD = mutableListOf<List<TextBean>>()
    private val doubleColorBallDataE = mutableListOf<List<TextBean>>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val doubleColorBallRecyclerViewA = binding.doubleColorBallRecyclerViewA
        val doubleColorBallRecyclerViewB = binding.doubleColorBallRecyclerViewB
        val doubleColorBallRecyclerViewC = binding.doubleColorBallRecyclerViewC
        val doubleColorBallRecyclerViewD = binding.doubleColorBallRecyclerViewD
        val doubleColorBallRecyclerViewE = binding.doubleColorBallRecyclerViewE
        val doubleColorBallAdapterA =
            DoubleColorBallAdapter(
                requireContext(),
                R.layout.double_color_ball_item,
                doubleColorBallDataA
            )
        val doubleColorBallAdapterB =
            DoubleColorBallAdapter(
                requireContext(),
                R.layout.double_color_ball_item,
                doubleColorBallDataB
            )
        val doubleColorBallAdapterC =
            DoubleColorBallAdapter(
                requireContext(),
                R.layout.double_color_ball_item,
                doubleColorBallDataC
            )
        val doubleColorBallAdapterD =
            DoubleColorBallAdapter(
                requireContext(),
                R.layout.double_color_ball_item,
                doubleColorBallDataD
            )
        val doubleColorBallAdapterE =
            DoubleColorBallAdapter(
                requireContext(),
                R.layout.double_color_ball_item,
                doubleColorBallDataE
            )
        doubleColorBallRecyclerViewA.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        doubleColorBallRecyclerViewA.adapter = doubleColorBallAdapterA
        doubleColorBallRecyclerViewA.addItemDecoration(DoubleColorBallItemDecoration(20))

        doubleColorBallRecyclerViewB.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        doubleColorBallRecyclerViewB.adapter = doubleColorBallAdapterB
        doubleColorBallRecyclerViewB.addItemDecoration(DoubleColorBallItemDecoration(20))

        doubleColorBallRecyclerViewC.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        doubleColorBallRecyclerViewC.adapter = doubleColorBallAdapterC
        doubleColorBallRecyclerViewC.addItemDecoration(DoubleColorBallItemDecoration(20))

        doubleColorBallRecyclerViewD.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        doubleColorBallRecyclerViewD.adapter = doubleColorBallAdapterD
        doubleColorBallRecyclerViewD.addItemDecoration(DoubleColorBallItemDecoration(20))

        doubleColorBallRecyclerViewE.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        doubleColorBallRecyclerViewE.adapter = doubleColorBallAdapterE
        doubleColorBallRecyclerViewE.addItemDecoration(DoubleColorBallItemDecoration(20))
        viewModel.doubleColorBallNum.observe(viewLifecycleOwner, {
            doubleColorBallDataA.clear()
            doubleColorBallDataA.addAll(it)
            doubleColorBallDataA.shuffle()
            doubleColorBallDataB.clear()
            doubleColorBallDataB.addAll(it)
            doubleColorBallDataB.shuffle()
            doubleColorBallDataC.clear()
            doubleColorBallDataC.addAll(it)
            doubleColorBallDataC.shuffle()
            doubleColorBallDataD.clear()
            doubleColorBallDataD.addAll(it)
            doubleColorBallDataD.shuffle()
            doubleColorBallDataE.clear()
            doubleColorBallDataE.addAll(it)
            doubleColorBallDataE.shuffle()
            doubleColorBallAdapterA.notifyDataSetChanged()
            doubleColorBallAdapterB.notifyDataSetChanged()
            doubleColorBallAdapterC.notifyDataSetChanged()
            doubleColorBallAdapterD.notifyDataSetChanged()
            doubleColorBallAdapterE.notifyDataSetChanged()
        })
        binding.doubleColorBallLottery.setOnClickListener {
            //开始摇号,遍历每一个RecyclerView调用RecyclerViewLoopScrollAnimation.start()
            val ballsA = viewModel.createSevenDoubleColorBall()
            LogUtil.i("MainFragment", "ballsA $ballsA")
            doubleColorBallAdapterA.fakeResult.clear()
            doubleColorBallAdapterA.fakeResult.addAll(ballsA)
            for ((index, _) in doubleColorBallDataA.withIndex()) {
                val viewHolder =
                    doubleColorBallRecyclerViewA.findViewHolderForLayoutPosition(index) as DoubleColorBallAdapter.DoubleColorBallViewHolder
                viewHolder.recyclerViewLoopScrollAnimation.start(0L)
            }

            val ballsB = viewModel.createSevenDoubleColorBall()
            LogUtil.i("MainFragment", "ballsB $ballsB")
            doubleColorBallAdapterB.fakeResult.clear()
            doubleColorBallAdapterB.fakeResult.addAll(ballsB)
            for ((index, _) in doubleColorBallDataB.withIndex()) {
                val viewHolder =
                    doubleColorBallRecyclerViewB.findViewHolderForLayoutPosition(index) as DoubleColorBallAdapter.DoubleColorBallViewHolder
                viewHolder.recyclerViewLoopScrollAnimation.start(100L)
            }

            val ballsC = viewModel.createSevenDoubleColorBall()
            LogUtil.i("MainFragment", "ballsC $ballsC")
            doubleColorBallAdapterC.fakeResult.clear()
            doubleColorBallAdapterC.fakeResult.addAll(ballsC)
            for ((index, _) in doubleColorBallDataC.withIndex()) {
                val viewHolder =
                    doubleColorBallRecyclerViewC.findViewHolderForLayoutPosition(index) as DoubleColorBallAdapter.DoubleColorBallViewHolder
                viewHolder.recyclerViewLoopScrollAnimation.start(200L)
            }

            val ballsD = viewModel.createSevenDoubleColorBall()
            LogUtil.i("MainFragment", "ballsD $ballsD")
            doubleColorBallAdapterD.fakeResult.clear()
            doubleColorBallAdapterD.fakeResult.addAll(ballsD)
            for ((index, _) in doubleColorBallDataD.withIndex()) {
                val viewHolder =
                    doubleColorBallRecyclerViewD.findViewHolderForLayoutPosition(index) as DoubleColorBallAdapter.DoubleColorBallViewHolder
                viewHolder.recyclerViewLoopScrollAnimation.start(300L)
            }

            val ballsE = viewModel.createSevenDoubleColorBall()
            LogUtil.i("MainFragment", "ballsE $ballsE")
            doubleColorBallAdapterE.fakeResult.clear()
            doubleColorBallAdapterE.fakeResult.addAll(ballsE)
            for ((index, _) in doubleColorBallDataE.withIndex()) {
                val viewHolder =
                    doubleColorBallRecyclerViewE.findViewHolderForLayoutPosition(index) as DoubleColorBallAdapter.DoubleColorBallViewHolder
                viewHolder.recyclerViewLoopScrollAnimation.start(400L)
            }

        }
        viewModel.refreshDoubleColorBallNum()
        return root
    }


}