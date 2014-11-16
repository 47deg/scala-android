package com.fortysevendeg.scala.android.ui.main

import android.os.{Build, Bundle}
import android.support.v7.app.ActionBarActivity
import android.support.v7.widget.{GridLayoutManager, LinearLayoutManager}
import com.fortysevendeg.scala.android.R
import com.fortysevendeg.scala.android.macroid.ExtraActions._
import com.fortysevendeg.scala.android.ui.circularreveal.CircularRevealActivity
import com.fortysevendeg.scala.android.ui.pathmorphing.PathMorphingActivity
import com.fortysevendeg.scala.android.ui.ripplebackground.RippleBackgroundActivity
import com.fortysevendeg.scala.android.ui.textstyles.TextStylesActivity
import macroid.Contexts
import macroid.FullDsl._
import com.fortysevendeg.scala.android.macroid.DevicesQueries._

class MainActivity
    extends ActionBarActivity
    with Contexts[ActionBarActivity]
    with Layout {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(layout)

    val adapter = new ListDemosAdapter(new RecyclerClickListener {
      override def onClick(info: DemoInfo): Unit = {
        if (Build.VERSION.SDK_INT >= info.minApi) {
          // TODO that's a mamarracho. I try use Class.from() and it was impossible
          info match {
            case DemoInfo(_, _, "RippleBackgroundActivity", _, _) => aStartActivity[RippleBackgroundActivity]
            case DemoInfo(_, _, "TextStylesActivity", _, _) => aStartActivity[TextStylesActivity]
            case DemoInfo(_, _, "CircularRevealActivity", _, _) => aStartActivity[CircularRevealActivity]
            case DemoInfo(_, _, "PathMorphingActivity", _, _) => aStartActivity[PathMorphingActivity]
          }
        } else {
          aShortToast(getString(R.string.min_api_not_available))
        }
      }
    })

    val layoutManager =
      landscapeTablet ?
          new GridLayoutManager(this, 4) |
          tablet ?
              new GridLayoutManager(this, 3) | new LinearLayoutManager(this)

    recyclerView.map(view => {
      view.setLayoutManager(layoutManager)
      view.setAdapter(adapter)
      view.addItemDecoration(new DividerItemDecorator)
    })

    toolBar map setSupportActionBar

  }

}
