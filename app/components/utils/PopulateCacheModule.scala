package components.utils

import com.google.inject.AbstractModule

/**
  * It is the module that allows to read the file and to populate the cache
  */
class PopulateCacheModule extends AbstractModule {

  override def configure() = {
    bind(classOf[PopulateCache]).to(classOf[PopulateCacheImpl]).asEagerSingleton()
  }
}