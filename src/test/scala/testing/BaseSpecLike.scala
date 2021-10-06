package testing

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.wordspec.AnyWordSpecLike

trait BaseSpecLike
    extends AnyWordSpecLike
    with TypeCheckedTripleEquals
    with ScalaFutures
    with Eventually
