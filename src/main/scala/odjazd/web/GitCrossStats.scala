package pl.relationsystems


import org.scalajs.dom._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js._
import scala.scalajs.js.annotation.JSExport
import scala.util.{Random, Failure, Success}

import d3._

@JSExport("GitCrossStats")
object GitCrossStats {

  @JSExport
  def main(args: Seq[String] = Seq()): Unit = {

    console.log("Starting GitCrossStats")
    type Data = (Int, Float)

    val data: js.Array[Data] = new js.Array[(Int, Float)]()
    for (i <- 1 to 100) data.push((i, Random.nextFloat()))


    val ndx = crossfilter.crossfilter[Data](data)

    val indexDimension = ndx.dimension({ (d: Data) => d._1 })

    val speedSumGroup = indexDimension.group().reduceSum({
      (pair:Data) => pair._2
    })

    val chart = DC.barChart("#test");
    chart
      .width(768)
      .height(480)
      .x(d3.d3.scale.linear().domain(js.Array(0,10)))
      //.brushOn(false)
      //.yAxisLabel("This is the Y Axis!")
      .dimension(indexDimension)
      .group(speedSumGroup)

    DC.renderAll()

  }
}
