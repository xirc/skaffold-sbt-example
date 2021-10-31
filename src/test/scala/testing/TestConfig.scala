package testing

import com.typesafe.config.{Config, ConfigFactory}

object TestConfig {

  /** Resolves a [[Config]] from the given string and config sources
    *
    * This respects an upper config source than a lower one.
    *  - The given string
    *  - Environment Variables
    *  - System Properties
    *  - `application-test.conf`
    *  - `reference.conf`
    *
    * This resolves all variable references at last.
    * The given string can override the variables defined in configuration files.
    */
  def resolveWith(inlineConfigSource: String): Config = {
    val inlineConfig =
      ConfigFactory.parseString(inlineConfigSource)
    val applicationTestConfig =
      ConfigFactory.parseResourcesAnySyntax("application-test")
    inlineConfig
      .withFallback(ConfigFactory.defaultOverrides())
      .withFallback(applicationTestConfig)
      .withFallback(ConfigFactory.defaultReferenceUnresolved())
      .resolve()
  }

}
