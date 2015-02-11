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
    type Data = (Int, Double)

    val data = new js.Array[Data]()
    for (i <- 1 to 100) data.push((i - 1, Random.nextInt(4)+1))

    val ndx = crossfilter.crossfilter[Data](data.sortBy(_._1))

    val indexDimension = ndx.dimension({ (d: Data) => d._1})
    val frequencyDimension = ndx.dimension({ (d: Data) => d._2})

    val valueGroup = indexDimension.group().reduceSum({
      (pair: Data) => pair._2
    })

    val frequencyGroup = frequencyDimension.group()


    val chart = DC.barChart("#test");
    chart
      .width(768)
      .height(480)
      .x(d3.d3.scale.linear().domain(js.Array(-1, 11)))
      //.brushOn(false)
      .dimension(indexDimension)
      .group(valueGroup)

    val piechart = DC.pieChart("#test2")
    piechart.dimension(frequencyDimension).group(frequencyGroup)


    DC.renderAll()

  }
}
