# Lorenzo_il_Magnifico
### Software Engineering final test -  Academic Academic Year 2016 - 2017
### Authors: Avanzi Davide, Bocca Nicolò, Carsenzuola Fabrizio

### Project JavaDoc:
#### http://lim.ingsw.davideavanzi.com/javadoc

## Functionalities recap:
- Available connection protocols:
  - Remote Method Invocation (RMI)
  - Socket
- User Interfaces:
  - CLI (colored, with emoji support)
- Full ruleset
- Configurations loaded from files
- Fifth player
- Game persistence
- Users database using SQL

## Features:

### Login & user management
Players can access the game only after performing a login. The player must enter a username and a password:
- If the username is not in the database, the game creates a new player with that password.
- If the username is in the database but the password is wrong, the player is asked to re-enter his login credentials.
- An user can log in from only one client at the same time.

### Persistence
Server handles multiple rooms: when a user logs in he's joined to the last available room.
Disconnected users can re-login and they will be added to the room they were playing before, the game won't be affected.
Disconnected users are considered by the game as idle.
If the server crashes, it's status can be restored to the last completed turn. It will be told by the server admin
(who starts the server) whether to restore previous status or not. If status is restored, connecting players will be put
in rooms they where playing before and the game resumed.

### Fifth Player
We added the possibility to make a match with five players. The games stretches it's structures to make a fair match.
A fifth tower is also added to the game board; it holds a new kind of cards we invented: black cards.
Black cards represent "bad actions" that a user can perform: they are strongly condemned by the vatican, but they
give a lot of useful immediate resources


### Customizable settings & configs:
This software is able to load custom configurations from files, they are stored in src/main/gameData/configs/.
In order to create a new custom configuration, make a new folder in configs/ and put there all configuration files.
These configs are user-customizable at will.
There are also other custom settings stored in a code file. Changing these settings requires that configuration files
provide correct amount of data (such as cards, excommunications). The game will automatically adjust to the new settings.

#### From file:
- Development cards of all ages and colors.
- Excommunications of all ages.
- Bonuses from action spaces of all board (council, towers, market, etc.).
- Default bonuses for production and harvest actions.
- Initial resources for first player (each following will have a coin more than the previous one).

#### In settings file:
- Harvest, Productio sizes and action costs.
- Default action strengths and costs.
- Miscellaneous costs and requirements not required to be loaded from file.