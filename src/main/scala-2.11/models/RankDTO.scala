package models

case class RankDTO(rankId: Int, rankName: String, isModerator: Boolean, permit: Set[Int], restrict: Set[Int])
case class RankDTOMin(rankId: Int, rankName: String, isModerator: Boolean)