# Minesweeper-CLI

This is a remake of minesweeper, just in a purely console view.

It's just designed to be a fun project, so no guarantee on functionality.
Let me know via the [issue page](https://github.com/UnknownUser95/MineSweeper-CLI/issues) if you find a bug though.

# Starting a game

For now, there is no good way to do so. You have to execute the SinglePlayer file, found in the package net.unknownuser.mines.execs.

# Commands

## click

syntax: click x,y

This is the standard click. It imitates a click, as the name implies.

## flag

syntax: flag x,y

Places a flag on an unknown field or removes it. Error on every other type.

## debug

syntax: debug <option>

As of now, there are two options:
  - show: displays the board without the "fog"
  - value: shows the value of each board. Mines have a "-"

## exit / done

Ends the game and shows the clear board.
