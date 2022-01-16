package com.nxg.app.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nxg.loopscrollanimation.utils.LogUtil
import java.util.*
import java.util.stream.Collectors

class MainViewModel : ViewModel() {
    //双色球
    private val _doubleColorBallNum = MutableLiveData<MutableList<List<TextBean>>>()
    val doubleColorBallNum: LiveData<MutableList<List<TextBean>>> = _doubleColorBallNum
    private fun createDoubleColorBallNum(): MutableList<List<TextBean>> {
        val data = mutableListOf<List<TextBean>>()
        for (i in 0..6) {
            val numberList = mutableListOf<TextBean>()
            val bound = if (i < 6) {
                33
            } else {
                16
            }
            for (j in 1..bound) {
                numberList.add(
                    TextBean(
                        if (j < 10) "0$j" else {
                            j.toString()
                        }
                    )
                )
            }
            //LogUtil.i("MainViewModel", "i = $i, before $numberList")
            numberList.shuffle()
            //LogUtil.i("MainViewModel", "i = $i, after $numberList")
            data.add(i, numberList)
        }
        return data
    }

    fun refreshDoubleColorBallNum() {
        _doubleColorBallNum.value = createDoubleColorBallNum()
    }

    /**
     * 生成6个红色双色球号码
     */
    private fun createSixRedDoubleColorBall(): List<String> {
        val redBalls = mutableListOf<String>()
        for (j in 1..33) {
            redBalls.add(
                if (j < 10) "0$j" else {
                    j.toString()
                }
            )
        }
        LogUtil.i("MainViewModel", "createSixRedDoubleColorBall before $redBalls")
        redBalls.shuffle()
        LogUtil.i("MainViewModel", "createSixRedDoubleColorBall shuffle after $redBalls")
        val balls = redBalls.subList(0, 6)
        LogUtil.i("MainViewModel", "createSixRedDoubleColorBall subList after $balls")
        balls.sort()
        LogUtil.i("MainViewModel", "createSixRedDoubleColorBall sort after $balls")
        return balls
    }

    /**
     * 生成1个蓝色双色球号码
     */
    private fun createSingleBlueDoubleColorBall(): String {
        val blueBall = Random().nextInt(16) + 1
        LogUtil.i("MainViewModel", "createSingleBlueDoubleColorBall $blueBall")
        return if (blueBall < 10) "0$blueBall" else {
            blueBall.toString()
        }
    }

    /**
     * 随机生成6个红色双色球号码+1个蓝色双色球号码
     */
    fun createSevenDoubleColorBall(): List<String> {
        val balls = mutableListOf<String>()
        val redBalls = createSixRedDoubleColorBall()
        balls.addAll(redBalls)
        val blueBall = createSingleBlueDoubleColorBall()
        balls.add(blueBall)
        LogUtil.i("MainViewModel", "createSevenDoubleColorBall $balls")
        return balls
    }


}