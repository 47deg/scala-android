/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortysevendeg.scala.android.modules.forecast.impl

import com.fortysevendeg.macroid.extras.AppContextProvider
import com.fortysevendeg.scala.android.R
import com.fortysevendeg.scala.android.commons.Service
import com.fortysevendeg.scala.android.modules.forecast.{ForecastResponse, ForecastRequest, ForecastServicesComponent, ForecastServices}
import com.fortysevendeg.scala.android.modules.utils.NetUtils
import com.fortysevendeg.scala.android.ui.apirequest.service.model._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait ApiReads {

  implicit val apiCloudsReads = Json.reads[ApiClouds]
  implicit val apiWindReads = Json.reads[ApiWind]
  implicit val apiWeatherReads = Json.reads[ApiWeather]
  implicit val apiMainReads = Json.reads[ApiMain]
  implicit val apiSysReads = Json.reads[ApiSys]
  implicit val apiCoordReads = Json.reads[ApiCoord]
  implicit val apiModelReads = Json.reads[ApiModel]

}

trait ForecastServicesComponentImpl
    extends ForecastServicesComponent
    with NetUtils {

  self: AppContextProvider =>

  val forecastServices = new ForecastServicesImpl

  class ForecastServicesImpl
      extends ForecastServices
      with Conversions
      with ApiReads {

    override def loadForecast: Service[ForecastRequest, ForecastResponse] = request =>
      Future {
        val jsonUrl = resGetString(R.string.weather_url, request.latitude.toString, request.longitude.toString)
        val header = (resGetString(R.string.weather_key_name), resGetString(R.string.weather_key_value))
        (for {
          json <- getJson(jsonUrl, Seq(header))
          apiModel <- Try(Json.parse(json).as[ApiModel])
        } yield apiModel) match {
          case Success(apiModel) => ForecastResponse(Some(toForecast(apiModel)))
          case Failure(ex) => ForecastResponse(None)
        }
      }

  }

}
