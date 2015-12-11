# play-requestcount

RequestCountFilter for Play Framework

[![Build Status](https://travis-ci.org/edvakf/play-requestcount.svg)](https://travis-ci.org/edvakf/play-requestcount)
[![Coverage Status](https://coveralls.io/repos/edvakf/play-requestcount/badge.svg?branch=master&service=github)](https://coveralls.io/github/edvakf/play-requestcount?branch=master)

## Install

Add to your build.sbt;

```sbt
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.edvakf" % "play-requestcount" % "0.0.2"
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
