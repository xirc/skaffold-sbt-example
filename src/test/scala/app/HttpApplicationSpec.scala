package app

import akka.actor.typed.scaladsl.adapter.ClassicActorSystemOps
import akka.http.scaladsl.model.StatusCodes
import testing.BaseScalatestRouteTest

final class HttpApplicationSpec extends BaseScalatestRouteTest {

  private val httpApp = new HttpApplication(system.toTyped)

  "/hello" in {

    Get("/hello") ~> httpApp.route ~> check {
      assert(status === StatusCodes.OK)
      assert(responseAs[String] === "hello world!")
    }

  }

}
