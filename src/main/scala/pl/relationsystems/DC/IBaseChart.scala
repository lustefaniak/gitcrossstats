package pl.relationsystems.DC

import pl.relationsystems.D3

import scala.scalajs.js

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
  var turnOnControls: IGetSet[Boolean, T] = js.native
}
