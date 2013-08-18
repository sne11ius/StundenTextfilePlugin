StundenTextfilePlugin
=====================

Plugin for https://github.com/sne11ius/stunden that reads data from human friendly text files.

Configuration
=============

`readFrom` must point to a file a directory that contains your data. If it is a directory,
_all_ files will be recursively read and must be parsable for this plugin.

Alle files must contain a date in the filename and may only be composed of lines of the following form:

`hh:mm - hh:mm: [Description]`

Example:
  - filename: `my work hours for 2013-03-07 are crazy.txt`
  - content:

    ```
        10:00 - 10:30: Project 1
        10:30 - 11:00: Intern
        11:00 - 12:00: Project 2
        12:00 - 13:00: Essen
        13:00 - 13:45: Project 1
        13:45 - 17:00: Project 2
        17:00 - 20:00: Project 3
    ```

Build
=====
see https://github.com/sne11ius/stunden
