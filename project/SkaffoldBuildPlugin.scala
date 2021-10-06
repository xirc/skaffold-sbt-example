import com.typesafe.sbt.packager.Keys.packageName
import com.typesafe.sbt.packager.docker.DockerPlugin
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.Docker
import sbt.Keys._
import sbt._

object SkaffoldBuildPlugin extends AutoPlugin {
  override def requires: Plugins = DockerPlugin
  override def trigger: PluginTrigger = allRequirements

  override lazy val projectSettings: Seq[Setting[_]] =
    autoImport.baseSkaffoldSettings

  object autoImport {
    val skaffoldImageNameAndTag =
      settingKey[Option[SkaffoldImageNameAndTag]](
        "Docker image name and tag provided by Skaffold"
      )
    val skaffoldImageName =
      settingKey[Option[String]](
        "Docker image name provided by Skaffold"
      )
    val skaffoldImageTag =
      settingKey[Option[String]](
        "Docker image tag provided by Skaffold"
      )
    val skaffoldPushImage =
      settingKey[Boolean](
        "Push Docker image if set to true. It is provided by Skaffold."
      )
    val skaffoldBuild =
      taskKey[Unit](
        "Build Docker image for Skaffold Build"
      )

    lazy val baseSkaffoldSettings: Seq[Setting[_]] = Seq(
      skaffoldImageNameAndTag := {
        SkaffoldImageNameAndTag.fromEnv()
      },
      skaffoldImageName := {
        skaffoldImageNameAndTag.value.map(_.name)
      },
      skaffoldImageTag := {
        skaffoldImageNameAndTag.value.map(_.tag)
      },
      skaffoldPushImage := {
        SkaffoldPushImage.fromEnv().getOrElse(false)
      },
      skaffoldBuild := {
        if (skaffoldPushImage.value) {
          (Docker / publish).value
        } else {
          (Docker / publishLocal).value
        }
      },
      Docker / packageName := {
        val default = (Docker / packageName).value
        skaffoldImageName.value.getOrElse(default)
      },
      Docker / version := {
        val default = (Docker / version).value
        skaffoldImageTag.value.getOrElse(default)
      }
    )

  }

  final case class SkaffoldImageNameAndTag(name: String, tag: String)
  object SkaffoldImageNameAndTag {
    def fromEnv(): Option[SkaffoldImageNameAndTag] = {
      Option(System.getenv("IMAGE")).map { image =>
        image.split(':').toVector match {
          case Vector(imageName, tagName) =>
            SkaffoldImageNameAndTag(imageName, tagName)
          case _ =>
            throw new IllegalArgumentException(
              s"""
                 |Environment variable `IMAGE=$image` is an invalid format.
                 |The expected format is `image:tag`.
                 |See also https://skaffold.dev/docs/pipeline-stages/builders/custom/#contract-between-skaffold-and-custom-build-script
                 |""".stripMargin
            )
        }
      }
    }
  }

  object SkaffoldPushImage {
    def fromEnv(): Option[Boolean] = {
      Option(System.getenv("PUSH_IMAGE")).map { pushImage =>
        try pushImage.toBoolean
        catch {
          case cause: Throwable =>
            throw new IllegalArgumentException(
              s"""
                 |Environment variable `PUSH_IMAGE=$pushImage` is an invalid format.
                 |The expected value is `ture`.
                 |See also https://skaffold.dev/docs/pipeline-stages/builders/custom/#contract-between-skaffold-and-custom-build-script
                 |""".stripMargin,
              cause
            )
        }
      }
    }
  }

}
