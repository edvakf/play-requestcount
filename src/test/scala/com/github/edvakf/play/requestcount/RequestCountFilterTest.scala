package com.github.edvakf.play.requestcount

import org.scalatest.FunSuite
import play.api.Configuration
import play.api.mvc.{Action, Result, Results}
import play.api.test.{FakeApplication, FakeRequest}
import play.api.test.Helpers._

import scala.concurrent.Promise

class RequestCountFilterTest extends FunSuite {

  test("returns zero by default") {
    implicit val materializer = FakeApplication().materializer
    val config = Configuration("requestcount.path" -> "/requestcount")
    val filter = new RequestCountFilter(config)

    val action = Action(Results.Ok("ok"))

    val res = filter(action)(FakeRequest(GET, "/requestcount")).run

    assert(status(res) == 200)
    assert(contentAsString(res) == "0")
  }

  test("increment and decrement") {
    implicit val materializer = FakeApplication().materializer
    val config = Configuration("requestcount.path" -> "/requestcount")
    val filter = new RequestCountFilter(config)

    val promise = Promise[Result]()
    val action = Action.async(promise.future)

    // this request blocks until promise.success is called
    val res0 = filter(action)(FakeRequest(GET, "/foo")).run

    // while the first request is blocking, request count must be 1
    val res1 = filter(action)(FakeRequest(GET, "/requestcount")).run
    assert(status(res1) == 200)
    assert(contentAsString(res1) == "1")

    promise.success(Results.Ok("ok"))
    assert(status(res0) == 200)

    // request count must be 0 after the request
    val res2 = filter(action)(FakeRequest(GET, "/requestcount")).run
    assert(status(res2) == 200)
    assert(contentAsString(res2) == "0")
  }

  test("nothing happens if config is not set") {
    implicit val materializer = FakeApplication().materializer
    val config = Configuration()
    val filter = new RequestCountFilter(config)

    val action = Action(Results.Ok("ok"))

    val res = filter(action)(FakeRequest(GET, "/foo")).run

    assert(status(res) == 200)
    assert(contentAsString(res) == "ok")
  }
}
