resolvers += Classpaths.sbtPluginReleases

// Scoverage
addSbtPlugin("org.scoverage" %% "sbt-scoverage" % "1.0.4")

// Send Scoverage results to coveralls
addSbtPlugin("org.scoverage" %% "sbt-coveralls" % "1.0.0")
