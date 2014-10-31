package com.fortysevendeg.scala.android.ui.ripplebackground

import android.animation.{Animator, AnimatorListenerAdapter}
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.ActionBarActivity
import android.view.MenuItem
import com.fortysevendeg.scala.android.ui.components.{RippleSnailData, CircleView}
import macroid.FullDsl._
import macroid.Contexts
import com.fortysevendeg.scala.android.macroid.RevealSnails._
import com.fortysevendeg.scala.android.macroid.ViewTweaks._
import macroid.util.Ui

import scala.concurrent.ExecutionContext.Implicits.global
import com.fortysevendeg.scala.android.ui.components.RippleBackgroundSnails._

class RippleBackgroundActivity extends ActionBarActivity with Contexts[ActionBarActivity] with Layout {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)

    setContentView(layout)

    val rippleSnailData = RippleSnailData.fromPlace(rippleBackground.get)

    rippleBackground.map(_.setBackgroundColor(Color.RED))

    circle1.map(_.setColor(Color.RED))
    runUi(circle1 <~ On.click {
      anim(circle1, Color.RED)
    })

    circle2.map(_.setColor(Color.BLUE))
    runUi(circle2 <~ On.click {
      anim(circle2, Color.BLUE)
    })

    circle3.map(_.setColor(Color.GREEN))
    runUi(circle3 <~ On.click {
      anim(circle3, Color.GREEN)
    })

    toolBar map setSupportActionBar

    getSupportActionBar().setDisplayHomeAsUpEnabled(true)

  }
  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case android.R.id.home => {
        finish()
        false
      }
    }
    super.onOptionsItemSelected(item)
  }

  def anim(circleView: Option[CircleView], color: Int) = {

    val rippleData = RippleSnailData.fromPlace(rippleBackground.get).
        copy(
          resColor = color,
          listener = Some(new AnimatorListenerAdapter {
            override def onAnimationStart(animation: Animator): Unit = {
              runUi(Ui{ circleView <~ vInvisible })
            }
            override def onAnimationEnd(animation: Animator): Unit = {
              runUi(Ui{ circleView <~ vVisible <~ vTransformation()})
            }
          })
        )
    (circleView <~~ move(rippleBackground)) ~ (rippleBackground <~~ ripple(rippleData))

  }

}
