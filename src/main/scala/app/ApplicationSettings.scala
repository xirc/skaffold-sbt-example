package app

import akka.actor.ClassicActorSystemProvider
import com.typesafe.config.Config

final class ApplicationSettings(
    val config: Config
) {

  /** The name of ActorSystem to use */
  val actorSystemName: String =
    config.getString("app.actor-system-name")

}

object ApplicationSettings {

  /** Creates settings from the config of the given ActorSystem */
  def apply(system: ClassicActorSystemProvider): ApplicationSettings = {
    apply(system.classicSystem.settings.config)
  }

  /** Creates settings from the given config */
  def apply(config: Config): ApplicationSettings = {
    new ApplicationSettings(config)
  }

}
