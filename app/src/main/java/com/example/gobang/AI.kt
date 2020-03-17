package com.example.gobang

import android.graphics.Point
import kotlin.random.Random

class AI {
    private var aiArray: ArrayList<Point> = ArrayList()
    private var userArray: ArrayList<Point> = ArrayList()

    constructor(userlist: ArrayList<Point>, ailist: ArrayList<Point>) {
        userArray = userlist
        aiArray = ailist
    }

    // chessboard view can call this function to find the best point to play
    fun getNextPoint(): Point {

        var res = Point(-1, -1)

        //check if I have 5 connected points
        res = checkPosiblePoint(aiArray, 5)
        if (isValid(res)) {
            return res
        }

        //check if human has 5 connected points
        res = checkPosiblePoint(userArray, 5)
        if (isValid(res)) {
            return res
        }


        //check if human has 4 connected points
        res = checkPosiblePoint(userArray, 4)
        if (isValid(res)) {
            return res
        }

        //check if I have 4 connected points
        res = checkPosiblePoint(aiArray, 4)
        if (isValid(res)) {
            return res
        }

        //check if human has 3 connected points
        res = checkPosiblePoint(userArray, 3)
        if (isValid(res)) {
            return res
        }

        //check if I have 3 connected points
        res = checkPosiblePoint(aiArray, 3)
        if (isValid(res)) {
            return res
        }
        //check if human has 2 connected points
        res = checkPosiblePoint(userArray, 2)
        if (isValid(res)) {
            return res
        }
        //check if I have 2 connected points
        res = checkPosiblePoint(aiArray, 2)
        if (isValid(res)) {
            return res
        }

        //check if human has 1 point, add to next
        res = checkPosiblePoint(userArray, 1)
        if (isValid(res)) {
            return res
        }

        //check if I have 1 point and add to next
        res = checkPosiblePoint(aiArray, 1)
        if (isValid(res)) {
            return res
        } else {
            return findRandomAvaliable()
        }

    }

    // randomly select a point in case non of the requirement meet
    private fun findRandomAvaliable(): Point {
        var resPoint = Point(7, 7)
        while (!isValid(resPoint)) {
            resPoint.x = Random.nextInt(0, 14)
            resPoint.y = Random.nextInt(0, 14)
        }
        return resPoint
    }

    private fun checkPosiblePoint(points: List<Point>, numberInLine: Int): Point {
        var res = Point(-1, -1)
        for (point in points) {
            val x = point.x
            val y = point.y

            res = checkHorizontal(x, y, points, numberInLine)
            if (isValid(res)) {
                return res
            }
            res = checkVertical(x, y, points, numberInLine)
            if (isValid(res)) {
                return res
            }
            res = checkLeftDiagonal(x, y, points, numberInLine)
            if (isValid(res)) {
                return res
            }
            res = checkRighttDiagonal(x, y, points, numberInLine)
            if (isValid(res)) {
                return res
            }
        }
        return res
    }

    // 横
    private fun checkHorizontal(
        x: Int,
        y: Int,
        points: List<Point>,
        numberInLine: Int
    ): Point {
        var point1: Point? = null
        var point2: Point? = null
        var count = 1
        var xsmall = 16
        var ysmall = 16
        var xbig = -2
        var ybig = -2
        for (i in 0 until numberInLine) {
            point1 = Point(x - i, y)
            if (points.contains(point1)) {
                if (x - i < xsmall) {
                    xsmall = x - i
                }
                count++
            } else {
                break
            }
        }
        for (i in 0 until numberInLine) {
            point2 = Point(x + i, y)
            if (points.contains(point2)) {
                if (x + i > xbig) {
                    xbig = x + i
                }
                count++
            } else {
                break
            }
        }
        xsmall--
        xbig++
        if (count >= numberInLine) {
            if (isValid(Point(xsmall, y))) {
                return Point(xsmall, y)
            } else {
                return Point(xbig, y)
            }
        } else {
            return Point(-1, -1)
        }

    }

    // 竖
    private fun checkVertical(
        x: Int,
        y: Int,
        points: List<Point>,
        numberInLine: Int
    ): Point {
        var point1: Point? = null
        var point2: Point? = null
        var count = 1
        var xsmall = 16
        var ysmall = 16
        var xbig = -2
        var ybig = -2
        for (i in 0 until numberInLine) {
            point1 = Point(x, y - i)
            if (points.contains(point1)) {
                if (y - i < ysmall) {
                    ysmall = y - i
                }
                count++
            } else {
                break
            }
        }
        for (i in 0 until numberInLine) {
            point2 = Point(x, y + i)
            if (points.contains(point2)) {
                if (y + i > ybig) {
                    ybig = y + i
                }
                count++
            } else {
                break
            }
        }
        ysmall--
        ybig++

        if (count >= numberInLine) {
            if (isValid(Point(x, ysmall))) {
                return Point(x, ysmall)
            } else {
                return Point(x, ybig)
            }
        } else {
            return Point(-1, -1)
        }
    }

    // 左斜
    private fun checkLeftDiagonal(
        x: Int,
        y: Int,
        points: List<Point>,
        numberInLine: Int
    ): Point {
        var point1: Point? = null
        var point2: Point? = null
        var count = 1
        var xsmall = 16
        var ysmall = 16
        var xbig = -2
        var ybig = -2
        for (i in 0 until numberInLine) {
            point1 = Point(x - i, y + i)
            if (points.contains(point1)) {
                if (x - i < xsmall) {
                    xsmall = x - i
                }
                if (y + i > ybig) {
                    ybig = y + i
                }
                count++
            } else {
                break
            }
        }
        for (i in 0 until numberInLine) {
            point2 = Point(x + i, y - i)
            if (points.contains(point2)) {
                if (x + i > xbig) {
                    xbig = x + i
                }
                if (y - i < ysmall) {
                    ysmall = y - i
                }
                count++
            } else {
                break
            }
        }
        xsmall--
        ysmall--
        xbig++
        ybig++

        if (count >= numberInLine) {
            if (isValid(Point(xsmall, ybig))) {
                return Point(xsmall, ybig)
            } else {
                return Point(xbig, ysmall)
            }
        } else {
            return Point(-1, -1)
        }
    }

    // 右斜
    private fun checkRighttDiagonal(
        x: Int,
        y: Int,
        points: List<Point>,
        numberInLine: Int
    ): Point {
        var point1: Point? = null
        var point2: Point? = null
        var count = 1
        var xsmall = 16
        var ysmall = 16
        var xbig = -2
        var ybig = -2
        for (i in 0 until numberInLine) {
            point1 = Point(x + i, y + i)
            if (points.contains(point1)) {
                if (x + i > xbig) {
                    xbig = x + i
                }
                if (y + i > ybig) {
                    ybig = y + i
                }
                count++
            } else {
                break
            }
        }
        for (i in 0 until numberInLine) {
            point2 = Point(x - i, y - i)
            if (points.contains(point2)) {
                if (x - i < xsmall) {
                    xsmall = x - i
                }
                if (y - i < ysmall) {
                    ysmall = y - i
                }
                count++
            } else {
                break
            }
        }
        xsmall--
        ysmall--
        xbig++
        ybig++

        if (count >= numberInLine) {
            if (isValid(Point(xsmall, ysmall))) {
                return Point(xsmall, ysmall)
            } else {
                return Point(xbig, ybig)
            }
        } else {
            return Point(-1, -1)
        }
    }

    private fun isValid(check: Point): Boolean {
        if (check.x < 0 || check.y < 0 || check.x > 14 || check.y > 14) {
            return false
        }
        if (aiArray.contains(check) || userArray.contains(check)) {
            return false
        }
        return true
    }
}