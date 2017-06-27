# Lorenzo_il_Magnifico
#### Software Engineering project. AA 2016/17

### JavaDoc:
#### http://lim.ingsw.davideavanzi.com/javadoc

## Functionalities:
- Connections:
  - RMI
  - Socket
- User Interfaces:
  - CLI
  - GUI
- Full ruleset
- Configurations loaded from files
- Fifth player

### Customizable settings & configs:
This software is able to load custom configurations from files, they are stored in src/main/gameData/configs/.
In order to create a new custom configuration, make a new folder in configs/ and put there all configuration files.
These configs are user-customizable at will.
There are also other custom settings stored in a code file. Changing these settings requires that configuration files
provide correct amount of data (such as cards, excommunications). The game will automatically adjust to the new settings.

#### From file:
- Development cards of all ages and colors
- Excommunications of all ages
- Bonuses from action spaces of all board (council, towers, market, etc.)
- Default bonuses for production and harvest actions.
- Initial resources for first player (each following will have a coin more than the previous one)

#### In settings file:
- Ages number in one match
- Turns amount in one age
- Default action strengths and costs.