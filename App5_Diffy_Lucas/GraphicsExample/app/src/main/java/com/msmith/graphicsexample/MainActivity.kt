package com.msmith.graphicsexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.timerTask

class TimerObject : TimerTask()
{
  override fun run()
  {
    var helper = HelperThread()

    MainActivity.getInstance().runOnUiThread(helper)
  }
}

class HelperThread : Runnable
{
  override fun run()
  {
    MainActivity.getInstance().update()
  }
}



class MainActivity : AppCompatActivity() {
  private var speed = 0;
  private var oval_dir = 1;
  private var ball_dir = 1
  private var alpha = 0.0
  private var velocity = 0
  private var startTime = 0.0;
  private var currentTime = 0.0;
  private var shots = 0
  private var score = 0

  private var timer = Timer() //Create threads which thread?

  companion object {
    private var instance: MainActivity? = null
    public fun getInstance(): MainActivity {
      return instance!!
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    supportActionBar?.hide()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main) //MyView going on  Timer runs faster than the MyView Set onsizeChange
    var angleSlider = findViewById<SeekBar>(R.id.angleBar)
    angleSlider.setOnSeekBarChangeListener(seekbarAngleHandler())

    var speedSlider = findViewById<SeekBar>(R.id.velocityBar)
    speedSlider.setOnSeekBarChangeListener(seekbarVelocityHandler())

    var fireButton = findViewById<Button>(R.id.Fire)
    fireButton.setOnClickListener(Handler())

    instance = this

    //var handler = Handler()
    //add1.setOnClickListener(handler)

    var timerTask = TimerObject()

    timer.schedule(timerTask, 0, 25)
  }

  public fun update() {
    //Synchronize with the view getting setup
    if (myView.getWidth() > 1)  //Make sure that the getWidth is not 0
    {
      currentTime = System.currentTimeMillis().toDouble()
      var alpha = angleBar.progress.toDouble() * 0.01745329 // conversion to radians
      var deltaTime = (currentTime - startTime) / 1000
      //println(myView.getWidth())
      //println(myView.getHeight())
      var x = (velocity * (kotlin.math.cos(alpha) * deltaTime)) + 0
      var y = (-velocity * (kotlin.math.sin(alpha) * deltaTime) + 0.5 * 9.8 * deltaTime * deltaTime) + (myView.height * .8)


      if(shots == 0)
      {
       ball.visibility = INVISIBLE
      }


      if (speed != 0) {
        ball.left = x.toInt()
        ball.right = x.toInt() + 100
        ball.top = (y.toInt())
        ball.bottom = y.toInt() + 100
      }
      //Check for edges
      if ((ball.top < 0) || (ball.bottom > myView.height * .9) || (ball.left < 0) || (ball.right >= myView.width)) {

        angleBar.visibility = VISIBLE
        velocityBar.visibility = VISIBLE
        Fire.visibility = VISIBLE
        ball.visibility = INVISIBLE
        println("OUT OF BOUNDS: RESET BALL!!!")
        println("left: " + ball.left + ", bottom: " + ball.bottom + ", top: " + ball.top + ", right: " + ball.right)
        println("DELTA TIME: $deltaTime")
        scoreLabel.text = "Score: " + score

        if (myView.targets.size == 0) {
          myView.reset()
          shots = 0
          score = 0

          shotsLabel.text = "Shots: "
          scoreLabel.text = "Score: "
        }

        startTime = 0.0
        speed = 0
        //myView.setballCoords(5, (myView.height * .8).toInt(), 5 + ball.width, (myView.height * .8).toInt() + ball.height)

        ball.left = 100
        ball.bottom = (myView.height * .89).toInt()
        ball.right = 200
        ball.top = (myView.height * .89).toInt() - 100



        speed = 0
        ball_dir = 1





        myView.setballCoords(ball.left, ball.top, ball.right, ball.bottom)
        myView.invalidate()

      }




      for (i in 0 until myView.targets.size) {
        if (myView.targets[i].bounds.intersects(
            ball!!.left,
            ball!!.top,
            ball!!.right,
            ball!!.bottom

          )
        ) {
          println("______CIRCLE TOUCHED AT " + myView.targets[i].bounds)
          myView.targets.removeAt(i)
//          scoreLabel.text = "Score "
          score++
          myView.invalidate()
          break;

        }
      }

    }
  }

  inner class Handler : View.OnClickListener {

    override fun onClick(v: View?) {
      velocity = getInstance().findViewById<SeekBar>(R.id.velocityBar).progress
     speed = velocity
      myView.invalidate()
      startTime = System.currentTimeMillis().toDouble()


      if(speed !=0)
      {
        shots++
        shotsLabel.text ="Shots: " + shots

        ball.visibility = VISIBLE
        velocityBar.visibility = INVISIBLE
        angleBar.visibility = INVISIBLE
        Fire.visibility = INVISIBLE
      }

    }
  }

    inner class seekbarAngleHandler : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        var currentAngle = getInstance().findViewById<TextView>(R.id.angleText)
        currentAngle.setText(progress.toString())
        alpha = progress * 0.01745329 // conversion to radians


      }

      override fun onStartTrackingTouch(seekBar: SeekBar?) {

      }

      override fun onStopTrackingTouch(seekBar: SeekBar?) {

      }
    }


    inner class seekbarVelocityHandler : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        var currentVelocity = getInstance().findViewById<TextView>(R.id.speedText)
        currentVelocity.setText(progress.toString())
        speed = progress
        velocity = progress


      }

      override fun onStartTrackingTouch(seekBar: SeekBar?) {

      }

      override fun onStopTrackingTouch(seekBar: SeekBar?) {

      }


    }
  }