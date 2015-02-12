package pl.relationsystems.DC

import scala.scalajs.js

trait IPiechart extends IBaseChart[IPiechart] with IAbstractColorChart[IPiechart] with IAbstractBubblechart[IPiechart] with IAbstractCoordinateGridChart[IPiechart] with IMarginable[IPiechart] with IListener[IPiechart] {
  var radius: IGetSet[Double, IPiechart] = js.native
  var innerRadius: IGetSet[Double, IPiechart] = js.native
  var minAngleForLabel: IGetSet[Double, IPiechart] = js.native
}
