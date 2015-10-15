# play-requestcount

RequestCountFilter for Play Framework

## Install

Add to your build.sbt;

```sbt
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.edvakf" % "play-requestcount" % "0.0.1"
```

## Usage

Set the RequestCountFilter;

```scala
import com.github.edvakf.play.requestcount.RequestCountFilter

class Filters @Inject() (requestCountFilter: RequestCountFilter) extends HttpFilters {
  val filters = Seq(requestCountFilter)
}
```

And add this line to a config file;

```
requestcount.path = /requestcount
```

This adds an endpoint to your Play application, which returns the number of currently active connections;

```
$ curl -v http://localhost:9000/requestcount
0
```
