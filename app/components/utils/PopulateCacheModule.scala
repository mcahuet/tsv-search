package components.utils

import com.google.inject.AbstractModule

class PopulateCacheModule extends AbstractModule {

  override def configure() = {
    bind(classOf[PopulateCache]).to(classOf[PopulateCacheImpl]).asEagerSingleton()
  }
}