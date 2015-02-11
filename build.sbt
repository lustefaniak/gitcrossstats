name := "gitcrossstats"

scalaVersion := "2.11.5"

resolvers += Resolver.url("scala-js-releases", url("http://dl.bintray.com/content/scala-js/scala-js-releases"))(Resolver.ivyStylePatterns)

enablePlugins(ScalaJSPlugin)

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
  "be.doeraene" %%% "scalajs-jquery" % "0.8.0",
  "com.lihaoyi" %%% "scalatags" % "0.4.5"
)

workbenchSettings
bootSnippet := "GitCrossStats().main())"
refreshBrowsers <<= refreshBrowsers.triggeredBy(fastOptJS in Compile)




