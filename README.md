# ServerListMOTD - Set an MOTD as well as your playercount message!

**ServerListMOTD Latest Version:** - [![version](https://img.shields.io/badge/version-v1.2.4-blue)](https://github.com/Bradydawg/ServerListMotd/releases/tag/1.2.4)

## ServerListMOTD:
ServerListMOTD was originally created by [(KennyTv)](https://github.com/KennyTV) for 
use on Spigot and Bungeecord Servers. The plugin allowed users to set a custom MOTD
and server-icon easily. KennyTv eventually moved on to greater things, so as of now,
I (Bradydawg) have taken over this project, in hopes to provide the spigot community 
with a fully supported version of the plugin.

## ServerListMOTD Dependencies:
ServerListMOTD was built with weight in mind, therefore, the dependencies are quite easy:
ProtocolLib, and PlaceholderAPI. Generally speaking, PlaceholderAPI is not a true 
dependency, but rather the plugin does provide support for it. ProtocolLib is only 
required if you are using the spigot version of the plugin.

## ServerListMOTD Issues
When creating an issue, please include the following information.
- ServerListMotd version
- Whether you are using the plugin on a Spigot or a BungeeCord server
- Explanation of how to reproduce the issue
- The entire error message (!) if present

Issues can be created here: https://github.com/KennyTV/ServerListMotd/issues

## "What features are you planning for ServerListMOTD?"
Nothing at the moment! Stay tuned.

## ServerListMOTD Commands:
```
commands:
  motd reload:
    description: Reloads the config files and the server-icon
  motd set <index> <1/2> <motd>:
    description: Sets a MOTD
  motd remove <index>:
    description: Removes a MOTD
  motd list:
    description: Lists the currently saved MOTDs
  motd update:
    description: Remotely downloads the newest version of the plugin onto your server
```

## ServerListMOTD Permissions
```
permissions:
  serverlistmotd.command:
    description: Use the motd commands
  serverlistmotd.reload:
    description: Use the "/motd reload" command
  serverlistmotd.update:
    description: use the "/motd update" command
  serverlistmotd.setmotd:
    description: use the "/motd set" and "/motd remove" commands
  serverlistmotd.list:
    description: use the "/motd list" command
```