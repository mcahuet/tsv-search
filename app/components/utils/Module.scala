package components.utils

import com.google.inject.AbstractModule

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[CheckFileName]).to(classOf[CheckFileNameImpl]).asEagerSingleton()
  }
}