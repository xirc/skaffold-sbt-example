package app

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.typesafe.config.ConfigFactory
import testing.BaseSpecLike

import scala.concurrent.duration.DurationInt

final class ApplicationHttpSettingsSpec
    extends ScalaTestWithActorTestKit
    with BaseSpecLike {

  private val DefaultHostname = "127.0.0.1"
  private val DefaultPort = 8080
  private val DefaultTerminationHardDeadline = 3.seconds

  "ApplicationHttpSettings should have the default values" in {

    val settings = ApplicationHttpSettings(system)
    assert(settings.config === system.settings.config)
    assert(settings.hostname === DefaultHostname)
    assert(settings.port === DefaultPort)
    assert(settings.terminationHardDeadline === DefaultTerminationHardDeadline)

  }

  "ApplicationHttpSettings should parse settings from the given config" in {

    val config = ConfigFactory.parseString(
      """
        |app.http {
        |  hostname = "0.0.0.0"
        |  port = 80
        |  termination-hard-deadline = 3 minutes
        |}
        |""".stripMargin
    )

    val settings = ApplicationHttpSettings(config)
    assert(settings.config === config)

    assert(settings.hostname === "0.0.0.0")
    assert(
      settings.hostname !== DefaultHostname,
      "The config should override `hostname`"
    )

    assert(settings.port === 80)
    assert(
      settings.port !== DefaultPort,
      "The config should override `port`"
    )

    assert(settings.terminationHardDeadline === 3.minutes)
    assert(
      settings.terminationHardDeadline !== DefaultTerminationHardDeadline,
      "The config should override `termination-hard-deadline`"
    )

  }

}
