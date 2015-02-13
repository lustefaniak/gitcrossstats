# gitcrossstats

This is tiny GitStats clone written in Scala by Lukas Stefaniak to learn [Scala.js](http://www.scala-js.org/)

## Example

Is deployed on github pages: [HERE](http://lustefaniak.github.io/gitcrossstats/)

## How to use

It uses dc-js, crossfilter, d3 and bootstrap on the frontend.

To use it, generate your own `data/log.json` file using `/bin/git-log2json.sh` script. Just go to the directory of your 
git repository checkout and redirect output of the script to file: `bin/git-log2json.sh > data/log.json`

You can also point it to commits endpoint of [restfulgit](https://github.com/hulu/restfulgit).

Format of the commit log used should be also compatible with Gihub API, but I didn't check that.

## Fork and code

If you want to play with some code just launch `sbt` in main directory.
 * In interactive shell type `~fastOptJS`, see the scala.js magic happen.
 * After a while you should get file `gitcrossstats/target/scala-2.11/gitcrossstats-fastopt.js`
 * Point your browser to 
[http://localhost:12345/target/scala-2.11/classes/index-dev.html](http://localhost:12345/target/scala-2.11/classes/index-dev.html)
 * Do some changes in the code
 * Enjoy auto refresh on every successfull compile
