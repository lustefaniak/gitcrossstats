package pl.relationsystems

import scala.scalajs.js
import js.annotation._

package CrossFilter {

trait Selector[T] extends js.Object {
  def apply(value: T): js.Dynamic = js.native
}

trait CrossFilterStatic extends js.Object {
  def apply[T](data: js.Array[T]): CrossFilter[T] = js.native
  var version: String = js.native
  def permute[T](array: js.Array[T], index: js.Array[Double]): js.Array[T] = js.native
  var bisect: js.Any = js.native
  var heap: js.Any = js.native
  var heapselect: js.Any = js.native
  var insertionsort: js.Any = js.native
  var quicksort: js.Any = js.native
}

trait Bisection[T] extends js.Object {
  def apply(array: js.Array[T], value: T, lo: Double, hi: Double): Double = js.native
}

trait Bisector[T] extends Bisection[T] {
  var left: Bisection[T] = js.native
  var right: Bisection[T] = js.native
}

trait Heap[T] extends js.Object {
  def apply(array: js.Array[T], lo: Double, hi: Double): js.Array[T] = js.native
  def sort(array: js.Array[T], lo: Double, hi: Double): js.Array[T] = js.native
}

trait HeapSelect[T] extends js.Object {
  def apply(array: js.Array[T], lo: Double, hi: Double, k: Double): js.Array[T] = js.native
}

trait Sort[T] extends js.Object {
  def apply(array: js.Array[T], lo: Double, hi: Double): js.Array[T] = js.native
}

trait GroupAll[T] extends js.Object {
  def reduce[TValue](add: js.Function2[TValue, T, TValue], remove: js.Function2[TValue, T, TValue], initial: js.Function0[TValue]): GroupAll[T] = js.native
  def reduceCount(): GroupAll[T] = js.native
  def reduceSum(value: Selector[T]): GroupAll[T] = js.native
  def dispose(): GroupAll[T] = js.native
  def value(): T = js.native
}

trait Grouping[TKey, TValue] extends js.Object {
  var key: TKey = js.native
  var value: TValue = js.native
}

trait Group[T, TKey, TValue] extends js.Object {
  def top(k: Double): js.Array[Grouping[TKey, TValue]] = js.native
  def all(): js.Array[Grouping[TKey, TValue]] = js.native
  def reduce[TGroup](add: js.Function2[TGroup, T, TGroup], remove: js.Function2[TGroup, T, TGroup], initial: js.Function0[TGroup]): Group[T, TKey, TGroup] = js.native
  def reduceCount(): Group[T, TKey, Double] = js.native
  def reduceSum[TGroup](value: js.Function1[T, TGroup]): Group[T, TKey, TGroup] = js.native
  def order(value: Selector[TValue] = ???): Group[T, TKey, TValue] = js.native
  def orderNatural(): Group[T, TKey, TValue] = js.native
  def size(): Double = js.native
  def dispose(): Group[T, TKey, TValue] = js.native
}

trait CrossFilter[T] extends js.Object {
  def add(records: js.Array[T]): CrossFilter[T] = js.native
  def remove(): CrossFilter[T] = js.native
  def size(): Double = js.native
  def groupAll(): GroupAll[T] = js.native
  def dimension[TDimension](value: js.Function1[T, TDimension]): Dimension[T, TDimension] = js.native
}

trait Dimension[T, TDimension] extends js.Object {
  def filter(value: js.Array[TDimension]): Dimension[T, TDimension] = js.native
  def filterExact(value: TDimension): Dimension[T, TDimension] = js.native
  def filterRange(value: js.Array[TDimension]): Dimension[T, TDimension] = js.native
  def filterFunction(value: Selector[TDimension]): Dimension[T, TDimension] = js.native
  def filterAll(): Dimension[T, TDimension] = js.native
  def top(k: Double): js.Array[T] = js.native
  def bottom(k: Double): js.Array[T] = js.native
  def dispose(): Unit = js.native
  def group(): Group[T, TDimension, TDimension] = js.native
  def group[TGroup](groupValue: js.Function1[TDimension, TGroup]): Group[T, TDimension, TGroup] = js.native
  def groupAll(): GroupAll[T] = js.native
}

}

object CrossFilterScope extends js.GlobalScope {
  var crossfilter: CrossFilter.CrossFilterStatic = js.native
}
