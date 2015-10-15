package com.github.edvakf.play.requestcount

import javax.inject.Inject

import play.api.Configuration
import play.api.mvc.{Results, Result, RequestHeader, Filter}
import java.util.concurrent.atomic.AtomicInteger

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RequestCountFilter @Inject() (configuration: Configuration) extends Filter {

  val maybePath = configuration.getString("requestcount.path")

  // number of active requests
  val count = new AtomicInteger(0)

  def apply(nextFilter: (RequestHeader) => Future[Result])
    (requestHeader: RequestHeader): Future[Result] = {

    maybePath match {
      case Some(path) =>
        if (requestHeader.path == path) {

          // return the number of requests if the request path is the configured path
          // this very request is not counted, i.e. the smallest number you can get is zero
          Future.successful(Results.Ok(count.toString))

        } else {

          count.incrementAndGet()

          nextFilter(requestHeader).transform({
            result: Result =>
              count.decrementAndGet()
              result
          }, {
            ex: Throwable =>
              count.decrementAndGet()
              ex
          })
        }
      case None =>
        // if the configuration is not set, do nothing
        nextFilter(requestHeader)
    }
  }
}

