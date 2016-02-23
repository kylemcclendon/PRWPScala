name := "PRWPScala"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Spigot Repo" at "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
resolvers += "BungeeCord Repo" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "WorldEdit Repo" at "http://maven.sk89q.com/repo/"

libraryDependencies += "org.spigotmc" % "spigot-api" % "1.8.8-R0.1-SNAPSHOT" % "provided"
libraryDependencies += "org.bukkit" % "bukkit" % "1.8.8-R0.1-SNAPSHOT" % "provided"
libraryDependencies += "com.sk89q.worldedit" % "worldedit-bukkit" % "6.0.0-SNAPSHOT"
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.9"
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-mapping" % "2.1.9"