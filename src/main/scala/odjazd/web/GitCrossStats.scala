package pl.relationsystems


import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.annotation.JSExport
import scala.util.{Failure, Success}

@JSExport("GitCrossStats")
object GitCrossStats {

  @JSExport
  def main(args: Seq[String]): Unit = {
    dc.barChart("#somebar")


  }
}
