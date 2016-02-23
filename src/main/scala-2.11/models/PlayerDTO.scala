package models

import java.util.UUID

case class PlayerDTO(playerID: UUID, permit: Set[Int], rankId: Int, restrict: Set[Int])