package pl.relationsystems.DC

import scala.scalajs.js


trait IAbstractBubblechart[T] extends js.Object {
  var r: IGetSet[js.Any, T] = js.native
  var radiusValueAccessor: IGetSet[js.Function1[js.Any, Double], T] = js.native
}
