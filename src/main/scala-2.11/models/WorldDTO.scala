package models

import java.util.UUID

case class WorldDTO(worldId: UUID, rankId: Int, permit: Set[Int], restrict: Set[Int])