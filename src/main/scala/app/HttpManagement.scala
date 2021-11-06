package app

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Route
import akka.management.scaladsl.AkkaManagement

import scala.concurrent.{ExecutionContext, Future}

final class HttpManagement(system: ActorSystem[?]) {

  private val management: AkkaManagement = AkkaManagement(system)

  /** The settings to use */
  val settings: ApplicationHttpSettings =
    ApplicationHttpSettings(system)

  /** The HTTP route this Management HTTP server serves */
  def routes: Route = management.routes

  /** Starts a Management HTTP server
    *
    * Since this also registers the server to
    * [[akka.actor.CoordinatedShutdown]], the server will be terminated
    * gracefully following the manner of the CoordinatedShutdown.
    *
    * If doing something is needed after the server has been started, use a
    * returned `Future` instance that will be completed after the server starts.
    */
  def start(): Future[Done] = {
    implicit val ec: ExecutionContext = system.executionContext
    management
      .start()
      .map { _ =>
        CoordinatedShutdown(system)
          .addTask(
            CoordinatedShutdown.PhaseBeforeServiceUnbind,
            "management-stop"
          ) { () =>
            management.stop()
          }
        Done
      }
  }

}
