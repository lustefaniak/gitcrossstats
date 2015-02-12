package pl.relationsystems.DC

import scala.scalajs.js
import scala.scalajs.js.annotation._

@JSName("dc")
object dc extends js.Object {
  var events: IEvents = js.native

  def filterAll(chartGroup: IChartGroup = js.native): Unit = js.native

  def renderAll(chartGroup: IChartGroup = js.native): Unit = js.native

  def redrawAll(chartGroup: IChartGroup = js.native): Unit = js.native

  def bubbleChart(cssSel: String): IBubblechart = js.native

  def pieChart(cssSel: String): IPiechart = js.native

  def barChart(cssSel: String): IBarchart = js.native

  def lineChart(cssSel: String): ILinechart = js.native

  def dataTable(cssSel: String): IDatachart = js.native

  def rowChart(cssSel: String): IRowchart = js.native
}
