package components.utils

import javax.inject.Inject

import play.api.Configuration
import play.api.inject.ApplicationLifecycle


trait CheckFileName {}

class CheckFileNameImpl @Inject()(configuration: Configuration,appLifecycle: ApplicationLifecycle) extends CheckFileName{


  def checkFileNameConf(): Unit = {
    if(configuration.getString("query.file.path").isEmpty || configuration.underlying.getString("query.file.path").isEmpty) {
      println("You must fill the \"query.file.path\" field in /conf/application.conf which must be the path of the file to be read.")


    }
  }
  checkFileNameConf()
}
