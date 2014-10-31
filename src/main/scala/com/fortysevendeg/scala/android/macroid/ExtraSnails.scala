package com.fortysevendeg.scala.android.macroid

import android.animation.{Animator, AnimatorListenerAdapter}
import android.view.View._
import android.view.{ViewGroup, View, ViewAnimationUtils}
import com.fortysevendeg.scala.android.ui.components.RippleBackgroundView
import macroid.Snail

import scala.concurrent.{Future, Promise}
import scala.util.Success

object RevealSnails {

  val showCircularReveal = Snail[View] {
    view ⇒
      val animPromise = Promise[Unit]()
      val x = view.getWidth / 2
      val y = view.getHeight / 2
      val radius = SnailsUtils.calculateRadius(x, y, view.getWidth, view.getHeight)
      val anim: Animator = ViewAnimationUtils.createCircularReveal(view, x, y, 0, radius)
      anim.addListener(new AnimatorListenerAdapter {
        override def onAnimationEnd(animation: Animator) {
          super.onAnimationEnd(animation)
          animPromise.complete(Success(()))
        }
      })
      view.setVisibility(VISIBLE)
      anim.start
      animPromise.future
  }

  val hideCircularReveal = Snail[View] {
    view ⇒
      val animPromise = Promise[Unit]()
      val x = view.getWidth / 2
      val y = view.getHeight / 2
      val radius = SnailsUtils.calculateRadius(x, y, view.getWidth, view.getHeight)
      val anim: Animator = ViewAnimationUtils.createCircularReveal(view, x, y, radius, 0)
      anim.addListener(new AnimatorListenerAdapter {
        override def onAnimationEnd(animation: Animator) {
          super.onAnimationEnd(animation)
          view.setVisibility(INVISIBLE)
          animPromise.complete(Success(()))
        }
      })
      anim.start
      animPromise.future
  }

  def move(v: Option[View]) = Snail[View] {
    view ⇒
      val animPromise = Promise[Unit]()

      v.map{
        toView=>
          val finalX: Int = (toView.getX + (toView.getWidth / 2) - (view.getWidth / 2) - view.getX).asInstanceOf[Int]
          val finalY: Int = (toView.getY + (toView.getHeight / 2) - (view.getHeight / 2) - view.getY).asInstanceOf[Int]

          view.animate.translationXBy(finalX).translationYBy(finalY).setListener(new AnimatorListenerAdapter {
            override def onAnimationEnd(animation: Animator) {
              super.onAnimationEnd(animation)
              animPromise.complete(Success(()))
            }
          }).start

      }
      animPromise.future
  }

}

object SnailsUtils {

  def calculateRadius(x : Int = 0, y : Int = 0, width : Int = 0, height : Int = 0) = {
    val catheti1: Int = if (x < width / 2) width - x else x
    val catheti2: Int = if (y < height / 2) height - y else y
    Math.sqrt((catheti1 * catheti1) + (catheti2 * catheti2)).asInstanceOf[Int]
  }

}


