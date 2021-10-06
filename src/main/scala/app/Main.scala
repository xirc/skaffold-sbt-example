package app

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.management.cluster.bootstrap.ClusterBootstrap
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Main extends App {

  /** The shutdown was initiated by an bootstrap failure */
  final case class BootstrapFailureReason(cause: Throwable)
      extends CoordinatedShutdown.Reason

  private val logger = LoggerFactory.getLogger(getClass)
  private val config = ConfigFactory.load()
  private val settings = ApplicationSettings(config)
  private val system: ActorSystem[Nothing] =
    ActorSystem[Nothing](
      Behaviors.empty,
      settings.actorSystemName,
      settings.config
    )
  private implicit val executionContext: ExecutionContext =
    system.executionContext

  private val management = new HttpManagement(system)
  private val app = new HttpApplication(system)

  val bootstrap = for {
    _ <- app.start()
    _ <- management.start()
    _ <- Future { ClusterBootstrap(system).start() }
  } yield Done
  bootstrap.onComplete {
    case Success(_) =>
      logger.info("Bootstrap succeeded")
    case Failure(cause) =>
      logger.error("Bootstrap failed", cause)
      CoordinatedShutdown(system).run(BootstrapFailureReason(cause))
  }

}
