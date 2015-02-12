package pl.relationsystems

import scala.scalajs.js
import js.annotation._


package DC {

trait IGetSet[T, V] extends js.Object {
  def apply(): T = js.native

  def apply(t: T): V = js.native
}

trait ILegendwidget extends js.Object {
  var x: IGetSet[Double, Double] = js.native
  var y: IGetSet[Double, Double] = js.native
  var gap: IGetSet[Double, Double] = js.native
  var itemHeight: IGetSet[Double, Double] = js.native
  var horizontal: IGetSet[Boolean, Boolean] = js.native
  var legendWidth: IGetSet[Double, Double] = js.native
  var itemWidth: IGetSet[Double, Double] = js.native
}

trait IBaseChart[T] extends js.Object {
  var width: IGetSet[Double, T] = js.native
  var height: IGetSet[Double, T] = js.native
  var minWidth: IGetSet[Double, T] = js.native
  var minHeight: IGetSet[Double, T] = js.native
  var dimension: IGetSet[js.Any, T] = js.native
  var group: IGetSet[js.Any, T] = js.native
  var transitionDuration: IGetSet[Double, T] = js.native
  var colors: IGetSet[js.Array[String], T] = js.native
  var keyAccessor: IGetSet[js.Function1[js.Any, Double], T] = js.native
  var valueAccessor: IGetSet[js.Function1[js.Any, Double], T] = js.native
  var label: IGetSet[js.Function1[js.Any, String], T] = js.native
  var renderLabel: IGetSet[Boolean, T] = js.native
  var renderlet: js.Function1[js.Function1[T, Unit], T] = js.native
  var title: IGetSet[js.Function1[String, String], T] = js.native
  var filter: IGetSet[js.Any, T] = js.native
  var filterAll: js.Function0[Unit] = js.native
  var expireCache: js.Function0[Unit] = js.native
  var legend: js.Function1[ILegendwidget, T] = js.native
  var chartID: js.Function0[Double] = js.native
  var options: js.Function1[Object, Unit] = js.native
  var select: js.Function1[D3.Selection, D3.Selection] = js.native
  var selectAll: js.Function1[D3.Selection, D3.Selection] = js.native
}

trait IEvents extends js.Object {
  def trigger(fnctn: js.Function0[Unit], delay: Double = ???): Unit = js.native
}

trait IListener[T] extends js.Object {
  var on: js.Function2[String, js.Function1[T, Unit], T] = js.native
}

trait ImarginObj extends js.Object {
  var top: Double = js.native
  var right: Double = js.native
  var bottom: Double = js.native
  var left: Double = js.native
}

trait IMarginable[T] extends js.Object {
  var margins: IGetSet[ImarginObj, T] = js.native
}

trait IAbstractColorChart[T] extends js.Object {
  var colorDomain: IGetSet[js.Array[Double], T] = js.native
}

trait IAbstractStackableChart[T] extends js.Object {
  var stack: js.Function3[IChartGroup, String, js.Function1[Object, Double], T] = js.native
}

trait IAbstractCoordinateGridChart[T] extends js.Object {
  var x: IGetSet[js.Any, T] = js.native
  var y: IGetSet[js.Any, T] = js.native
  var elasticY: IGetSet[Boolean, T] = js.native
  var xAxis: IGetSet[D3.Svg.Axis, T] = js.native
  var yAxis: IGetSet[D3.Svg.Axis, T] = js.native
  var yAxisPadding: IGetSet[Double, T] = js.native
  var xAxisPadding: IGetSet[Double, T] = js.native
  var renderHorizontalGridLines: IGetSet[Boolean, T] = js.native
}

trait IAbstractBubblechart[T] extends js.Object {
  var r: IGetSet[js.Any, T] = js.native
  var radiusValueAccessor: IGetSet[js.Function1[js.Any, Double], T] = js.native
}

trait IBubblechart extends IBaseChart[IBubblechart] with IAbstractColorChart[IBubblechart] with IAbstractBubblechart[IBubblechart] with IAbstractCoordinateGridChart[IBubblechart] with IMarginable[IBubblechart] with IListener[IBubblechart] {
}

trait IPiechart extends IBaseChart[IPiechart] with IAbstractColorChart[IPiechart] with IAbstractBubblechart[IPiechart] with IAbstractCoordinateGridChart[IPiechart] with IMarginable[IPiechart] with IListener[IPiechart] {
  var radius: IGetSet[Double, IPiechart] = js.native
  var minAngleForLabel: IGetSet[Double, IPiechart] = js.native
}

trait IBarchart extends IBaseChart[IBarchart] with IAbstractStackableChart[IBarchart] with IAbstractCoordinateGridChart[IBarchart] with IMarginable[IBarchart] with IListener[IBarchart] {
  var centerBar: js.Function1[Boolean, IBarchart] = js.native
  var gap: js.Function1[Double, IBarchart] = js.native
}

trait ILinechart extends IBaseChart[ILinechart] with IAbstractStackableChart[ILinechart] with IAbstractCoordinateGridChart[ILinechart] with IMarginable[ILinechart] with IListener[ILinechart] {
}

trait IDatachart extends IBaseChart[IDatachart] with IAbstractStackableChart[IDatachart] with IAbstractCoordinateGridChart[IDatachart] with IMarginable[IDatachart] with IListener[IDatachart] {
  var size: IGetSet[Double, IDatachart] = js.native
  var columns: IGetSet[js.Array[js.Function1[js.Any, String]], IDatachart] = js.native
  var sortBy: IGetSet[js.Function1[js.Any, js.Any], IDatachart] = js.native
  var order: IGetSet[js.Function2[scala.Any, scala.Any, scala.Any], IDatachart] = js.native
}

trait IRowchart extends IBaseChart[IRowchart] with IAbstractColorChart[IRowchart] with IAbstractStackableChart[IRowchart] with IAbstractCoordinateGridChart[IRowchart] with IMarginable[IRowchart] with IListener[IRowchart] {
}

trait IChartGroup extends js.Object {
}


@JSName("dc")
object dc extends js.Object {
  var events: IEvents = js.native

  def filterAll(chartGroup: IChartGroup = ???): Unit = js.native

  def renderAll(chartGroup: IChartGroup = ???): Unit = js.native

  def redrawAll(chartGroup: IChartGroup = ???): Unit = js.native

  def bubbleChart(cssSel: String): IBubblechart = js.native

  def pieChart(cssSel: String): IPiechart = js.native

  def barChart(cssSel: String): IBarchart = js.native

  def lineChart(cssSel: String): ILinechart = js.native

  def dataTable(cssSel: String): IDatachart = js.native

  def rowChart(cssSel: String): IRowchart = js.native
}

}
