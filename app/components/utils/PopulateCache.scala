package components.utils

import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Provider}

import akka.actor.ActorSystem
import components.Reader
import play.api.{Application, Configuration, Logger}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.io.Source
import scala.util.Try


trait PopulateCache

/**
  * Verifies that a correct file is set and loads it into the cache
  */
class PopulateCacheImpl @Inject()(configuration: Configuration, actorSystem: ActorSystem, applicationProvider: Provider[Application]) extends PopulateCache {

  private val STOP_DELAY = Duration.create(5, TimeUnit.SECONDS)
  val runnable = new Runnable {
    // TODO I think it's really bad but I don't have any other idea
    override def run(): Unit = sys.runtime.exit(0)
  }

  def populateCache(): Unit = {
    val filePath = configuration.underlying.getString("query.file.path")
    if (filePath.isEmpty) {
      Logger.error("You must fill the \"query.file.path\" field in /conf/application.conf which must be the path of the file to be read.")
      actorSystem.scheduler.scheduleOnce(STOP_DELAY, runnable)
    } else if (!filePath.endsWith(".tsv")) {
      Logger.error(s"File $filePath is not a tsv file.")
      actorSystem.scheduler.scheduleOnce(STOP_DELAY, runnable)
    } else if (Try(Source.fromFile(filePath)).isFailure) {
      Logger.error(s"File $filePath not found.")
      actorSystem.scheduler.scheduleOnce(STOP_DELAY, runnable)
    } else {
      val stream = Reader.stream(filePath)
      if(stream.isEmpty){
        Logger.error(s"File $filePath is not readable, verify file format")
        actorSystem.scheduler.scheduleOnce(STOP_DELAY, runnable)
      }
    }
  }

  populateCache()
}
