# Minesweeper-CLI

This is a remake of minesweeper, just in a purely console view.

It's just designed to be a fun project, so no guarantee on functionality.
Let me know via the [issue page](https://github.com/UnknownUser95/MineSweeper-CLI/issues) if you find a bug though.

# Starting a game

Download the singleplayer.jar on the [release page](https://github.com/UnknownUser95/Minesweeper-CLI/releases). Open the download location, open a console and run "java -jar minesweeper-cli.jar". You will need Java 17.0.3 to run the game. Downloads are available on [Oracles Website](https://www.oracle.com/java/technologies/downloads/).

# Commands

### click

syntax: click x,y

This is the standard click. It imitates a click, as the name implies.

### flag

syntax: flag x,y

Places a flag on an unknown field or removes it. Error on every other type.

### debug

syntax: debug <option>

As of now, there are two options:
  - show: displays the board without the "fog"
  - value: shows the value of each board. Mines have a "-"

### exit / done

Ends the game and shows the clear board.

# Contribution
  
You should just be able to push a new branch and create a pull request. However I'm quite new to open repositories, so if I did anything wrong and you can't push anything let me know and I'll try to fix it.
