package pl.relationsystems.DC

import scala.scalajs.js

trait IDatachart extends IBaseChart[IDatachart] with IAbstractStackableChart[IDatachart] with IAbstractCoordinateGridChart[IDatachart] with IMarginable[IDatachart] with IListener[IDatachart] {
  var size: IGetSet[Double, IDatachart] = js.native
  var columns: IGetSet[js.Array[js.Function1[js.Any, String]], IDatachart] = js.native
  var sortBy: IGetSet[js.Function1[js.Any, js.Any], IDatachart] = js.native
  var order: IGetSet[js.Function2[js.Any, js.Any, scala.Any], IDatachart] = js.native
}
