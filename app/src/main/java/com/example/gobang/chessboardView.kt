package com.example.gobang

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeResource
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View


class chessboardView : View {
    // 棋盘的宽度，也是长度
    private val mViewWidth = 0

    // 棋盘每格的长度
    private val maxLineHeight = 0f
    private val paint: Paint = Paint()

    // 定义黑白棋子的Bitmap
    private var mwhitePiece: Bitmap? = null
    // 定义黑白棋子的Bitmap
    private var mblackPiece: Bitmap? = null
    private val ratioPieceOfLineHeight = 3 * 1.0f / 4

    // 判断当前落下的棋子是否是白色的
    private val mIsWhite = true

    // 记录黑白棋子位置的列表
    private val mwhiteArray: ArrayList<Point> = ArrayList()
    private val mblackArray: ArrayList<Point> = ArrayList()

    // 游戏是否结束
    private val mIsGameOver = false

    // 游戏结束，是否是白色方胜利
    private val mIsWhiteWinner = false

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }



    private fun init() {
        paint.color = -0x78000000
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        mwhitePiece = decodeResource(resources, R.mipmap.white)
        mblackPiece = decodeResource(resources, R.mipmap.black)
    }

}


