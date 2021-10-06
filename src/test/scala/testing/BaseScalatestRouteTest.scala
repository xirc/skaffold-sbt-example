package testing

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.typesafe.config.Config

abstract class BaseScalatestRouteTest
    extends BaseSpecLike
    with ScalatestRouteTest {

  override def testConfig: Config = {
    TestConfig.resolveWith(testConfigSource)
  }

}
