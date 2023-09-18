package com.example.andersenfragments2.Presenter

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CatDecoration(
    context: Context,
    resId: Int
) : RecyclerView.ItemDecoration() {

    private var myBorder: Drawable = ContextCompat.getDrawable(context, resId)!!

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val dividerLeft: Int = PADDING
        val dividerRight: Int = parent.width - PADDING

        for (i in 0 until parent.childCount) {

            if (i != parent.childCount - ONE) {
                val child: View = parent.getChildAt(i)

                val params = child.layoutParams as RecyclerView.LayoutParams
                val dividerTop: Int = child.bottom + params.bottomMargin
                val dividerBottom: Int = dividerTop + myBorder.intrinsicHeight

                myBorder.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                myBorder.draw(c)
            }
        }
    }
}

const val PADDING = 32
const val ONE = 1