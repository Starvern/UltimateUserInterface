# UltimateUserInterface
[![](https://jitpack.io/v/IIStarZ4/UltimateUserInterface.svg)](https://jitpack.io/#IIStarZ4/UltimateUserInterface)
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
UUI handles all GUIs by itself, meaning you can focus on creating logic without the clutter of GUI-management.

For `build.gradle`
```
repositores {
  maven {
    url = 'https://jitpack.io'
  }
}

dependencies {
  compileOnly 'com.github.IIStarZ4:UltimateUserInterface:pre-release'
}
```

The API provides an interface for getting and altering items, for example:

```
GuiManager.getGui("example_menu").getItem("#").getItem().setMaterial(Material.AIR);
```

This code snippet will change all of the '#' item to air.

# Advanced
If you are looking for a more complicated interface which may change on the fly, take a look at the following code snippet:

```
Gui gui = GuiManager.getGui("example_menu");
gui.createInstances("#");

int i = 0;
for (GuiItem item : gui.getAllInstances("#"))
{

  i++;
  item.getItem().setDisplayName(String.valueOf(i)).setMaterial((i > 10) ? Material.AIR : Material.BEACON);
}
```

The `Gui#createInstance` method will split all of the characters found in the pattern into their own items, rather than being uniform.
You can then loop over all of the item instances and alter them as you please.

