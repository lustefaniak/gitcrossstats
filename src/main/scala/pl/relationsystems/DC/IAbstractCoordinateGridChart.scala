package pl.relationsystems.DC

import pl.relationsystems.D3

import scala.scalajs.js

trait IAbstractCoordinateGridChart[T] extends js.Object {
  var x: IGetSet[js.Any, T] = js.native
  var y: IGetSet[js.Any, T] = js.native
  var elasticX: IGetSet[Boolean, T] = js.native
  var elasticY: IGetSet[Boolean, T] = js.native
  var xAxis: IGetSet[D3.Svg.Axis, T] = js.native
  var yAxis: IGetSet[D3.Svg.Axis, T] = js.native
  var xAxisLabel: IGetSet[String, T] = js.native
  var yAxisLabel: IGetSet[String, T] = js.native
  var yAxisPadding: IGetSet[Double, T] = js.native
  var xAxisPadding: IGetSet[Double, T] = js.native
  var renderHorizontalGridLines: IGetSet[Boolean, T] = js.native
}
