# UltimateUserInterface
An advanced GUI creation system for ease-of-use

# Creating a GUI
Creating a GUI is really simple. Create a `name.yml` file, where the name is the id for your interface.

The file structure for the file looks like this:
```
title: "Example Menu"

pattern:
  - '#########'
  - '#0001000#'
  - '#########'

'1':
  name: '{#f00}Ban Player'
  material: BARRIER
  enchanted: true
  lore:
    - '&7Remove a player from'
    - '&7the server {#f00}permanently'

'#':
  name: ' '
  material: BLACK_STAINED_GLASS_PANE
  enchanted: false
  lore: []
```

The pattern represents how the GUI will look in-game. Each row in the pattern corresponds to a row on the GUI, and each character to a slot in the GUI.
By default, all of the GUI's will prevent the user from adding / removing items.

# For developers

Visit the wiki

