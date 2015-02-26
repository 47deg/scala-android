package com.fortysevendeg.scala.android.ui.akkasimon

import android.view.View
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.{AppContext, Tweak}

import scala.language.postfixOps

object Styles {

  def simonButton(color: Int, alpha: Float = 0.3f)(implicit appCtx: AppContext) =
    vBackgroundColor(color) +
      vAlpha(alpha)

  // TODO Add to macroid-extras
  def vAlpha(alpha: Float) = Tweak[View](_.setAlpha(alpha))

  val rootStyle = llVertical

  val buttonsStyle =
    llWrapWeightHorizontal +
      tvMaxLines(1)

  val horizontalLinearLayoutStyle =
    vMatchWidth +
      llHorizontal

  val colorsContainerStyle =
    llMatchWeightVertical +
      llHorizontal

  val columnColorsStyle =
    llMatchWeightHorizontal +
      llVertical

  val fragmentStyle = llMatchWeightVertical

}
