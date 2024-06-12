AtHere 1.0.3
-
AtHere is a small command helper intended for Minecraft server operators.

Features
-
* The "@here" command argument, which executes a command for every player online.
* The "@[number]->[number]" command argument, which counts up from the starting number to the ending number.
* The "@step->[number]" argument modifier, which will affect how fast the number argument will count upwards.

**Usage Examples:**
-
* /tphere @here <- Teleport everyone online to you
* /msg @here hello! <- Message every player "hello!"
* /fill ~ ~ ~ ~ ~@0->200 ~ tnt <- Spawn in towers of TNT until Y=200
* /give @here cooked_beef @1->9 <- Give everyone 45 steak
* /tp @here ~ @300->2000 ~ @step->100 <- Teleport everyone up to 2000 blocks, 100 blocks at a time

**Commands:**
-
* /athere exclude [player] <- Excludes a player or argument from command execution
* /athere include [player] <- Includes a player or argument from command execution
* /athere delay [number] <- Sets the delay in milliseconds between each command's execution
* /athere stop <- Stops every running AtHere task
* /athere status <- Displays AtHere's current statuses

:)