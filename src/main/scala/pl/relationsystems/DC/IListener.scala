package pl.relationsystems.DC

import scala.scalajs.js

trait IListener[T] extends js.Object {
  var on: js.Function2[String, js.Function1[T, Unit], T] = js.native
}
