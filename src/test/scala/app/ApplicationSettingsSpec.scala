package app

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.typesafe.config.ConfigFactory
import testing.BaseSpecLike

final class ApplicationSettingsSpec
    extends ScalaTestWithActorTestKit
    with BaseSpecLike {

  private val DefaultActorSystemName = "my-app"

  "ApplicationSettings should have the default values" in {

    val settings = ApplicationSettings(system)
    assert(settings.config === system.settings.config)
    assert(settings.actorSystemName === DefaultActorSystemName)

  }

  "ApplicationSettings should parse settings from the given config" in {

    val config = ConfigFactory.parseString(
      """
        |app {
        |  actor-system-name = "my-actor-system"
        |}
        |""".stripMargin
    )

    val settings = ApplicationSettings(config)
    assert(settings.config === config)

    assert(settings.actorSystemName === "my-actor-system")
    assert(
      settings.actorSystemName !== DefaultActorSystemName,
      "The config should override `actor-system-name`"
    )

  }

}
