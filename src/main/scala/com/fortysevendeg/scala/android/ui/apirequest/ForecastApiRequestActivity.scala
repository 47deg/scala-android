package com.fortysevendeg.scala.android.ui.apirequest

import android.content.Context
import android.location.{Criteria, Location, LocationListener, LocationManager}
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.ActionBarActivity
import com.fortysevendeg.macroid.extras.FragmentExtras._
import com.fortysevendeg.scala.android.R
import macroid.Contexts
import macroid.FullDsl._

class ForecastApiRequestActivity 
  extends ActionBarActivity 
  with Contexts[FragmentActivity] 
  with Layout
  with DefaultLocationListener {

  lazy val locationManager = this.getSystemService(Context.LOCATION_SERVICE).asInstanceOf[LocationManager]

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)

    setContentView(layout)

    toolBar map setSupportActionBar

    getSupportActionBar.setDisplayHomeAsUpEnabled(true)

    if (savedInstanceState == null) {
      runUi(addFragment(f[ForecastFragment], Some(Id.fragment), Some(forecastFragmentName)))
    }
  }

  def loadClientLocation = {
    val criteria = new Criteria()
    criteria.setAccuracy(Criteria.NO_REQUIREMENT)
    
    Option(locationManager.getBestProvider(criteria, true)) map { provider =>
      val last = Option(locationManager.getLastKnownLocation(provider))
      if (last.isDefined) loadForecast(last.get.getLatitude, last.get.getLongitude)
      else locationManager.requestLocationUpdates(provider, 0, 0, this)
    }
  }

  override def onLocationChanged(location: Location) = {
    locationManager.removeUpdates(this)
    loadForecast(location.getLatitude, location.getLongitude)
  }

  override def onProviderDisabled(provider: String) = {
    locationManager.removeUpdates(this)
    showError(R.string.error_message_api_request_location_api)
  }

  private def showError(errorMessage: Int) =
    findFragmentByTag[ForecastFragment](forecastFragmentName) map (_.error(Some(errorMessage)))
  
  private def loadForecast(latitude: Double, longitude: Double) = 
    findFragmentByTag[ForecastFragment](forecastFragmentName) map (_.loadForecast((latitude, longitude)))
}

trait DefaultLocationListener extends LocationListener {
  
  override def onLocationChanged(location: Location) = {}

  override def onProviderEnabled(provider: String) = {}

  override def onStatusChanged(provider: String, status: Int, extras: Bundle) = {}

  override def onProviderDisabled(provider: String) = {}
}
