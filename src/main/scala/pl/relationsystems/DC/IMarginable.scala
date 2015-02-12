package pl.relationsystems.DC

import scala.scalajs.js

trait IMarginable[T] extends js.Object {
  var margins: IGetSet[IMarginObj, T] = js.native
}
