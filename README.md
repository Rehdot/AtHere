AtHere 1.0.3
-
AtHere is a small command helper intended for Minecraft server operators.
Essentially, it's a more configurable "@a" argument that works on any command.

**Usage Examples:**
-
* /tphere @here <- Teleport all to you
* /msg @here hello! <- Message every player "hello!"
* /gamemode creative @here <- Set everyone's gamemode to creative
* /say Hello @here! <- Say hello to everyone online
* /athere exclude @here <- Exclude every online player from command execution

**Commands:**
-
* /athere exclude (player) <- Excludes a player from the command execution
* /athere include (player) <- Includes a player from the command execution
* /athere delay (integer) <- Sets the delay in milliseconds between each command's execution
* /athere stop <- Stops every running AtHere task
* /athere status <- Displays AtHere's current statuses

:)