<div align="center">
  <h1>UltimateUserInterface</h1>
  <p>An advanced GUI creation system for ease-of-use</p>
  
[![](https://jitpack.io/v/IIStarZ4/UltimateUserInterface.svg)](https://jitpack.io/#IIStarZ4/UltimateUserInterface)
<img src="https://api.codiga.io/project/35489/score/svg" />
<img src="https://api.codiga.io/project/35489/status/svg" />
</div>

<p>Visit the Wiki for more information.</p>

Rules for the Gui's:

    1) Gui's are created inside of a single file.

    2) A Gui can only be altered by copying it.

    3) A Gui is a collection of GuiPages

    4) A GuiPage belongs to a Gui.

    5) A GuiPage represents an inventory with items, 
       which may or may not be GuiItems
    
    6) A GuiItem is a reference to the item as a
       ConfigurationSection, with macros attached.

    7) GuiItems are immovable, but can change in
       appearance.

    8) An item with macros is considered a GuiItem.

    9) A GuiPage's inventory is not directly accessed,
       but can be changed through built-in methods.

    10) A GuiPage can be only be created through a Gui.

    11) An item's macros can be changed, therefore the
        item's status as a GuiItem can also change.