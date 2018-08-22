package com.shape

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvOvalCode: TextView = findViewById(R.id.tv_oval_code)
        val color = getColor("#ff4141")
        ShapeInject.inject(tvOvalCode).setShapeType(ShapeInject.TYPE_OVAL).setStroke(2, color).background()

        val btnRoundCode: Button = findViewById(R.id.tv_round_code)
        val roundColor = getColor("#46b942")
        val pressBgColor = getColor("#994897fa")
        val disableBgColor = getColor("#cc999999")
        val normalBgColor = getColor("#3b8fed")
        ShapeInject.inject(btnRoundCode).setShapeType(ShapeInject.TYPE_ROUND).setStroke(2, roundColor)
                .setBackgroundColor(pressBgColor, disableBgColor, normalBgColor).background()

        val tvRoundRectCode: TextView = findViewById(R.id.tv_round_rect_code)
        val roundRectColor = getColor("#4897fa")
        ShapeInject.inject(tvRoundRectCode).setShapeType(ShapeInject.TYPE_ROUND_RECT).setStroke(2, roundRectColor).background()

        val tvSegmentCode: TextView = findViewById(R.id.tv_segment_code)
        val segmentColor = getColor("#ff4141")
        ShapeInject.inject(tvSegmentCode).setShapeType(ShapeInject.TYPE_SEGMENT).setStroke(2, segmentColor).background()

        val tvRadiusCode: TextView = findViewById(R.id.tv_radius_code)
        val radiusColor = getColor("#ff00ff")
        val radius = resources.getDimension(R.dimen.dp_10)
        ShapeInject.inject(tvRadiusCode).setRadius(radius).setStroke(2, radiusColor).background()

        val tvDashGapCode: TextView = findViewById(R.id.tv_dash_gap_code)
        val dashGapPressedColor = getColor("#f7b218")
        val dashGapNormalColor = getColor("#00ffff")
        val dashGapSize = resources.getDimension(R.dimen.dp_5)
        ShapeInject.inject(tvDashGapCode).setRadius(dashGapSize)
                .setStroke(2, dashGapPressedColor, dashGapNormalColor, dashGapSize, dashGapSize)
                .background()

        val llAllCode: LinearLayout = findViewById(R.id.ll_all_code)
        val tvAllCode: TextView = findViewById(R.id.tv_all_code)

        val llNormalBgColor = getColor("#4897fa")
        val llPressedBgColor = getColor("#ff4141")
        val llNormalTextColor = getColor("#fdbc40")
        val llPressedTextColor = getColor("#34c749")
        val llStrokeNormalColor = getColor("#ff4141")
        val llStrokePressColor = getColor("#4897fa")
        val llSize = resources.getDimension(R.dimen.dp_10).toInt()
        val llStrokeWidth = resources.getDimension(R.dimen.dp_2).toInt()
        val radii = floatArrayOf(llSize.toFloat(), llSize.toFloat(), 0f, 0f, llSize.toFloat(), llSize.toFloat(), 0f, 0f)
        ShapeInject.inject(llAllCode)
                .setBackgroundColor(llPressedBgColor, llPressedBgColor, llNormalBgColor)
                .setStroke(llStrokeWidth, llStrokePressColor, llStrokeNormalColor)
                .setTextColor(llPressedTextColor, llPressedTextColor, llNormalTextColor, tvAllCode)
                .setRadii(radii)
                .background()
    }

    private fun getColor(colorStr: String): Int {
        return Color.parseColor(colorStr)
    }
}
