resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

logLevel := Level.Warn

resolvers += Classpaths.sbtPluginReleases

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.5")