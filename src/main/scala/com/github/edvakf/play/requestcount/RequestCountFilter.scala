package com.github.edvakf.play.requestcount

import javax.inject.Inject

import play.api.Configuration
import play.api.mvc.{Filter, RequestHeader, Result, Results}
import java.util.concurrent.atomic.AtomicInteger

import scala.concurrent.{ExecutionContext, Future}
import akka.stream.Materializer

class RequestCountFilter @Inject() (configuration: Configuration)(implicit val mat: Materializer, executionContext: ExecutionContext) extends Filter {

  val maybePath = configuration.getOptional[String]("requestcount.path")

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

