package com.example.gobang.com.example.gobang

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeResource
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.gobang.AI
import com.example.gobang.MAX_LINE
import com.example.gobang.R
import com.example.gobang.checkWinner


class AIChessboardView :
    View {
    // 棋盘的宽度，也是长度
    private var mViewWidth = 0

    // 棋盘每格的长度
    private var maxLineHeight = 0f
    private val paint: Paint = Paint()

    // 定义黑白棋子的Bitmap
    private var mwhitePiece: Bitmap? = null

    // 定义黑白棋子的Bitmap
    private var mblackPiece: Bitmap? = null
    private val ratioPieceOfLineHeight = 3 * 1.0f / 4

    // 判断当前落下的棋子是否是白色的
    private var mIsWhite = true

    // 记录黑白棋子位置的列表
    private val mwhiteArray: ArrayList<Point> = ArrayList()
    private val mblackArray: ArrayList<Point> = ArrayList()

    // 游戏是否结束
    private var mIsGameOver = false

    // 游戏结束，是否是白色方胜利
    private var mIsWhiteWinner = false


    constructor(
        context: Context?,
        attrs: AttributeSet?
    ) : super(context, attrs) {

        init()
    }


    private fun init() {
        paint.color = -0x78000000
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        mwhitePiece = decodeResource(
            resources,
            R.mipmap.white
        )
        mblackPiece = decodeResource(
            resources,
            R.mipmap.black
        )
    }

    //获取自定义的长宽
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthModel = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightModel = MeasureSpec.getMode(heightMeasureSpec)
        var width = Math.min(widthSize, heightSize)
        if (widthModel == MeasureSpec.UNSPECIFIED) {
            width = heightSize
        } else if (heightModel == MeasureSpec.UNSPECIFIED) {
            width = widthSize
        }
        setMeasuredDimension(width, width)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // 绘制棋盘的网格
        drawBoard(canvas!!)

        // 绘制棋盘的黑白棋子
        drawPieces(canvas!!)

        // 检查游戏是否结束
        checkGameOver()
    }

    // 根据黑白棋子的数组绘制棋子
    private fun drawPieces(canvas: Canvas) {
        run {
            var i = 0
            val n = mwhiteArray.size
            while (i < n) {
                val whitePoint = mwhiteArray[i]
                val left =
                    (whitePoint.x + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight
                val top =
                    (whitePoint.y + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight
                canvas.drawBitmap(mwhitePiece!!, left, top, null)
                i++
            }
        }
        var i = 0
        val n = mblackArray.size
        while (i < n) {
            val blackPoint = mblackArray[i]
            val left =
                (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight
            val top =
                (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight
            canvas.drawBitmap(mblackPiece!!, left, top, null)
            i++
        }
    }

    // 绘制棋盘的网线
    private fun drawBoard(canvas: Canvas) {
        val w = mViewWidth
        val lineHeight = maxLineHeight
        for (i in 0 until MAX_LINE) {
            val startX = (lineHeight / 2).toInt()
            val endX = (w - lineHeight / 2).toInt()
            val y = ((0.5 + i) * lineHeight).toInt()
            canvas.drawLine(startX.toFloat(), y.toFloat(), endX.toFloat(), y.toFloat(), paint)
            canvas.drawLine(y.toFloat(), startX.toFloat(), y.toFloat(), endX.toFloat(), paint)
        }
    }

    // 检查游戏是否结束
    private fun checkGameOver() {
        val checkWinner = checkWinner()
        val whiteWin: Boolean = checkWinner.checkFiveInLineWinner(mwhiteArray)
        val blackWin: Boolean = checkWinner.checkFiveInLineWinner(mblackArray)
        if (whiteWin || blackWin) {
            mIsGameOver = true
            mIsWhiteWinner = whiteWin
            val text = if (mIsWhiteWinner) "White WIN!" else "Black WIN!"
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
        else{
            if(mwhiteArray.size == 113 || mblackArray.size == 113 || (mwhiteArray.size + mblackArray.size) == 265){
                mIsGameOver = true
                mIsWhiteWinner = whiteWin
                val text = "Even!!!"
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
        }
    }

    //根据棋盘大小绘制棋子
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        maxLineHeight = mViewWidth * 1.0f / MAX_LINE
        val pieceWidth = (maxLineHeight * ratioPieceOfLineHeight).toInt()
        mwhitePiece = Bitmap.createScaledBitmap(mwhitePiece!!, pieceWidth, pieceWidth, false)
        mblackPiece = Bitmap.createScaledBitmap(mblackPiece!!, pieceWidth, pieceWidth, false)
    }

    //落子
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mIsGameOver) {
            return false
        }
        val action = event.action
        if (action == MotionEvent.ACTION_UP) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val point = getValidPoint(x, y)

            if (mwhiteArray.contains(point) || mblackArray.contains(point)) {
                return false
            }
            if (mIsWhite) {
                mwhiteArray.add(point)
                mIsWhite = !mIsWhite

                //after user pick point, AI pick a point
                val ai = AI(mwhiteArray, mblackArray)
                mblackArray.add(ai.getNextPoint())
                mIsWhite = !mIsWhite
            }

            invalidate()
        }
        checkGameOver()
        return true
    }

    //help method
    private fun getValidPoint(x: Int, y: Int): Point {
        val validX = (x / maxLineHeight).toInt()
        val validY = (y / maxLineHeight).toInt()
        return Point(validX, validY)
    }

    // restart
    fun playAgain() {
        mwhiteArray.clear()
        mblackArray.clear()
        mIsGameOver = false
        mIsWhiteWinner = false
        mIsWhite = true
        invalidate()
        Toast.makeText(context, "in A.I. Mode", Toast.LENGTH_SHORT).show()
    }


}


