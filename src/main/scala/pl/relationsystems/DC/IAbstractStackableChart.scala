package pl.relationsystems.DC

import scala.scalajs.js

trait IAbstractStackableChart[T] extends js.Object {
  var stack: js.Function3[IChartGroup, String, js.Function1[Object, Double], T] = js.native
}
