package app

import akka.Done
import akka.actor.typed.scaladsl.adapter.ClassicActorSystemOps
import akka.cluster.Cluster
import akka.http.scaladsl.model.StatusCodes
import testing.BaseScalatestRouteTest

final class HttpManagementSpec extends BaseScalatestRouteTest {

  override def testConfigSource: String =
    """
      |akka.actor.provider = cluster
      |""".stripMargin

  private val httpManagement = new HttpManagement(system.toTyped)

  "/alive should always return OK" in {

    Get("/alive") ~> httpManagement.routes ~> check {
      assert(status === StatusCodes.OK)
    }

  }

  s"/ready should return OK once the required setups are done" in {

    Get("/ready") ~> httpManagement.routes ~> check {
      assert(status === StatusCodes.InternalServerError)
    }

    assert(httpManagement.start().futureValue === Done)
    // Do required setups here
    Cluster(system).join(Cluster(system).selfAddress)

    eventually {
      Get("/ready") ~> httpManagement.routes ~> check {
        assert(status === StatusCodes.OK)
      }
    }

  }

}
