package app

import akka.Done
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route

import scala.concurrent.{ExecutionContext, Future}

final class HttpApplication(
    system: ActorSystem[?]
) {

  /** The setting to use  */
  val settings: ApplicationHttpSettings =
    ApplicationHttpSettings(system)

  /** The HTTP route this HTTP server serves */
  def route: Route =
    path("hello") {
      get {
        complete("hello world!")
      }
    }

  /** Starts an HTTP server
    *
    * Since this also registers the server to [[akka.actor.CoordinatedShutdown]],
    * the server will be terminated gracefully following the manner of the CoordinatedShutdown.
    *
    * If doing something is needed after the server has been started,
    * use a returned `Future` instance that will be completed after the server starts.
    *
    */
  def start(): Future[Done] = {
    implicit val sys: ActorSystem[?] = system
    implicit val ec: ExecutionContext = system.executionContext
    Http()
      .newServerAt(settings.hostname, settings.port)
      .bind(route)
      .map { binding =>
        binding.addToCoordinatedShutdown(settings.terminationHardDeadline)
        Done
      }
  }

}
