package pl.relationsystems.DC

import scala.scalajs.js

trait IEvents extends js.Object {
  def trigger(fnctn: js.Function0[Unit], delay: Double = ???): Unit = js.native
}
