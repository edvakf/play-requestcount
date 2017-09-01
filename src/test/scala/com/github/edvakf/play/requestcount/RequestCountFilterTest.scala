package com.github.edvakf.play.requestcount

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.{BeforeAndAfter, FunSuite}
import play.api.Configuration
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Promise

class TestController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action { Ok("ok") }
  def indexP(promise: Promise[Result]) = Action.async {
    promise.future
  }
}

class RequestCountFilterTest extends FunSuite with BeforeAndAfter {

  implicit val actorSystem = ActorSystem()
  implicit val executionContext = actorSystem.dispatcher
  implicit val materialize = ActorMaterializer()

  after {
    actorSystem.terminate()
  }

  val app = new GuiceApplicationBuilder().build()
  val controller = app.injector.instanceOf[TestController]

  test("returns zero by default") {
    val config = Configuration("requestcount.path" -> "/requestcount")
    val filter = new RequestCountFilter(config)

    val action = controller.index

    val res = filter(action)(FakeRequest(GET, "/requestcount")).run

    assert(status(res) == 200)
    assert(contentAsString(res) == "0")
  }

  test("increment and decrement") {
    val config = Configuration("requestcount.path" -> "/requestcount")
    val filter = new RequestCountFilter(config)

    val promise = Promise[Result]()
    val action = controller.indexP(promise)

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
    val config = Configuration()
    val filter = new RequestCountFilter(config)

    val action = controller.index

    val res = filter(action)(FakeRequest(GET, "/foo")).run

    assert(status(res) == 200)
    assert(contentAsString(res) == "ok")
  }
}
