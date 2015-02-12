package pl.relationsystems.DC

import scala.scalajs.js

trait IBarchart extends IBaseChart[IBarchart] with IAbstractStackableChart[IBarchart] with IAbstractCoordinateGridChart[IBarchart] with IMarginable[IBarchart] with IListener[IBarchart] {
  var centerBar: js.Function1[Boolean, IBarchart] = js.native
  var gap: js.Function1[Double, IBarchart] = js.native
}
