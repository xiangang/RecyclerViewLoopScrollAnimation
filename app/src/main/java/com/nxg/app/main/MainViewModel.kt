package com.nxg.app.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nxg.loopscrollanimation.utils.LogUtil

class MainViewModel : ViewModel() {
    //双色球
    private val _doubleColorBallNum = MutableLiveData<MutableList<List<TextBean>>>()
    val doubleColorBallNum: LiveData<MutableList<List<TextBean>>> = _doubleColorBallNum
    private fun createDoubleColorBallNum(): MutableList<List<TextBean>> {
        val data = mutableListOf<List<TextBean>>()
        for (i in 0 until 6) {
            val numberList = mutableListOf<TextBean>()
            val until = if (i < 6) {
                33
            } else {
                16
            }
            for (j in 1..until) {
                numberList.add(
                    TextBean(
                        if (j < 10) "0$j" else {
                            j.toString()
                        }
                    )
                )
            }
            LogUtil.i("MainViewModel", "i = $i, before $numberList")
            numberList.shuffle()
            LogUtil.i("MainViewModel", "i = $i, after $numberList")
            data.add(i, numberList)
        }
        return data
    }

    fun refreshDoubleColorBallNum() {
        _doubleColorBallNum.value = createDoubleColorBallNum()
    }
}