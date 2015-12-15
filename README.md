# spike-clojure-standalone

The purpose of this spike is to join the functionality of standalone execution (``uberjar``) and CLI options using ``clojure/cli.tools``

## Standalone execution

```bash
> lein uberjar
> java -jar target/uberjar/fat-jar-0.1.0-SNAPSHOT-standalone.jar
# Hello, World!
```

## REPL

working on the main:

import the private function ``main`` (copied from [here](http://christophermaier.name/blog/2011/04/30/not-so-private-clojure-functions))

```lisp
fat-jar.core=> (require 'fat-jar.core :reload)
; nil
fat-jar.core=> (#'fat-jar.core/-main "a")
  -o, --output DIRECTORY  Folder where to write the file
  -t, --type TYPE         type of input
  -h, --help
; nil
```