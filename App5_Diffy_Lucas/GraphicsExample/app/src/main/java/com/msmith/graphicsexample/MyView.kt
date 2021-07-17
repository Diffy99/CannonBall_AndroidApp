package com.msmith.graphicsexample

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MyView  : View
{

    companion object
    {
        private var instance : MyView? = null
        public fun getInstance() : MyView
        {
            return instance!!
        }
    }
    private var x1 : Float = 100.0f
    private var y1 : Float = 100.0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var ball : ImageView? = null //Any widgets inside the view cannot Be accessed at this time
     var targets = ArrayList<Drawable>() //Targets
    private var start : Boolean = true          ///Display the ball properly

    private var boundaryCoords: Rect = Rect(0,0,0,0)
    private var ballCoords : Rect = Rect(0,0,0,0)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        boundaryCoords.set(0, (height*.1).toInt(),width,0)
        instance = this
    }

    public fun setBoundaryCoords(ux: Int, uy: Int, lx: Int, ly: Int)
    {
        this.boundaryCoords.set(ux, uy.toInt(),lx, ly.toInt())
    }


    public fun setballCoords(ux : Int, uy : Int, lx : Int, ly : Int)
    {
        this.ballCoords.set(ux,uy,lx,ly)
    }

    public fun getBallCoords() : Rect
    {
        return ballCoords
    }

    public fun setCoords(x1 : Float, y1 : Float)
    {
        this.x1 = x1
        this.y1 = y1
    }


    public fun getX1() : Float
    {
        return x1
    }
    public fun getY1() : Float
    {
        return y1
    }

    override fun onDraw(canvas: Canvas?)
    {
      super.onDraw(canvas)

        /*ball!!.left = 100
        ball!!.bottom = (height*.89).toInt()

        ball!!.right = 200
        ball!!.top = (height*.89).toInt() -100
*/

         if (canvas != null) {
            setBoundaryCoords(0,(height *.9).toInt(), width,height )
        }
        paint.setColor(Color.LTGRAY)

//Draw a black rectangle
        canvas?.drawRect(boundaryCoords,paint)  //Move the black Rectangle
        paint.setColor(Color.BLUE)
  //      canvas?.drawOval(circleCoords,paint) //Move the oval


        //Draw the targets normally –
        for (i in 0 until targets.size)
        {
            var drawable = targets.get(i)
            drawable.draw(canvas!!)
        }
        /*for( i in 0 until targets.size) {
            if (targets[i].bounds.intersects(
                    ball!!.left,
                    ball!!.top,
                    ball!!.right,
                    ball!!.bottom
                )
            ) {
                println("______ball TOUCHED AT " + targets[i].bounds)
                targets.removeAt(i)
                break;

            }
        }*/


    }

    public fun reset()
    {
        for (ii in 0..6) {
            for (i in 0..6) {

                //setballCoords(25, (height * .8).toInt(), 25+ ball?.width!!, (height * .8).toInt() + ball?.height!!  )

                var x = (.5 * width).toInt()
                var y = (.1 * height).toInt()
                var imageView = ImageView(MainActivity.getInstance())
                imageView.setImageResource(R.drawable.untouched)
                var drawable = imageView.getDrawable()
                drawable.setBounds(
                    x + (100 * i),
                    y + (100 * ii),
                    x + 100 + (i * 100),
                    y + 100 + (ii * 100)
                ) //Sets the dimensions

                targets.add(drawable)  //stores away the image


            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var width = this.getWidth()
        var height = this.getHeight()
        //Run only once per class
        for (ii in 0..6) {
            for (i in 0..6) {


                var x = (.5 * width).toInt()
                var y = (.1 * height).toInt()
                var imageView = ImageView(MainActivity.getInstance())
                imageView.setImageResource(R.drawable.untouched)
                var drawable = imageView.getDrawable()
                drawable.setBounds(
                    x + (100 * i),
                    y + (100 * ii),
                    x + 100 + (i * 100),
                    y + 100 + (ii * 100)
                ) //Sets the dimensions

                targets.add(drawable)  //stores away the image


            }
        }

        ball = MainActivity.getInstance().findViewById<ImageView>(R.id.ball)
        //setballCoords(100, (height * .89).toInt() , 200, (height * .89).toInt() -100)



    }


   }
