<div align="center">
  <h1>UltimateUserInterface</h1>
  <p>An advanced GUI creation system for ease-of-use</p>
  
[![](https://jitpack.io/v/IIStarZ4/UltimateUserInterface.svg)](https://jitpack.io/#IIStarZ4/UltimateUserInterface)
</div>

<h2>Information</h2>
<p>This is a Minecraft server plugin designed to work with Spigot server software. It provides user interface functionality through the usage of in-game containers.</p>

<h3>How does it work?</h3>
<p>Each user interface is accessible by server administrators in ".../plugins/UltimateUserInterface/gui/". Each file represents their own GUI and has many attributes which allow it to work.</p>

<h4>Title</h4>
<p>This is the title of the menu, as shown at the top in-game.</p>

<h4>Tick</h4>
<p>Tick is how many ticks to wait before refreshing the GUI. A value of `-1` will disable it.</p>

<h4>Patterns</h4>
<p>Patterns are the structure of each GUI. A single pattern is listed under a name and contains a list of strings (which are a list of symbols) which the plugin interprets into a GUI. Each row in the list represents a row in the GUI, and each symbol in the pattern represents an individual item.</p>

<h4>Actions</h4>
<p>Actions dictate how GUIs operate. Actions are a list of macros which are executed by a specific event. An action may listen to five different events: click, drag, close, open, and tick. Their are also custom events which server administrators may create and utilize at any time.</p>

<h4>Macros</h4>
<p>Macros are specific pieces of logic which may take in arguments and are executed on specific events. The plugin comes with a variety of useful macros built-in; however, other developers may create and register their own macros within their own plugins.</p>

<h4>Properties</h4>
<p>Properties are temporary storage for information. They can be defined in the GUI file and also dynamically by macros. They operate under a key-value system and may be stored either within a GUI or an item. Properties stored in the GUI are accessible by that page and all items within, but properties stored in items are only accessible by that item.</p>
