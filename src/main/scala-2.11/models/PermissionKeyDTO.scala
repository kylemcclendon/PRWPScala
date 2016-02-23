package models

case class PermissionKeyDTO(id: Int, name: String, childPermissions: Set[Int])