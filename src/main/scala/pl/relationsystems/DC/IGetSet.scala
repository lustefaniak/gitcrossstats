package pl.relationsystems.DC

import scala.scalajs.js

trait IGetSet[T, V] extends js.Object {
  def apply(): T = js.native

  def apply(t: T): V = js.native
}
