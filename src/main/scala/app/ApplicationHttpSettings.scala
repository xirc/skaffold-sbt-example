package app

import akka.actor.ClassicActorSystemProvider
import com.typesafe.config.Config

import scala.concurrent.duration.FiniteDuration
import scala.jdk.DurationConverters.JavaDurationOps

final class ApplicationHttpSettings(
    val config: Config
) {

  /** The hostname to bind as an HTTP endpoint */
  val hostname: String =
    config.getString("app.http.hostname")

  /** The port number to bind as an HTTP endpoint */
  val port: Int =
    config.getInt("app.http.port")

  /** The deadline(timeout) to wait for all in-flight HTTP requests completed.
    */
  val terminationHardDeadline: FiniteDuration =
    config.getDuration("app.http.termination-hard-deadline").toScala

}

object ApplicationHttpSettings {

  /** Creates settings from the config of the given ActorSystem */
  def apply(system: ClassicActorSystemProvider): ApplicationHttpSettings = {
    apply(system.classicSystem.settings.config)
  }

  /** Creates settings from the given config */
  def apply(config: Config): ApplicationHttpSettings = {
    new ApplicationHttpSettings(config)
  }

}
