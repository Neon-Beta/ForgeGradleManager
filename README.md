ForgeGradleManager
==================

A simple program designed to help you with troubles of FG, and offers a version control system.

[Screenshot](http://puu.sh/6uGeb.png)

Most of the program should make sense, but if you should fail to determine the function of a button, or checkbox.
Simply place it inside the folder with the gradlew file.


**Guide**
***
1: The "+" button increments the version number in the field as if it was one big number.  
2: The "-" button decrements the version number in the field as if it was one big number.  
3: The field with version number is a dynamic field, so you can add to it if you wish.
If it has letters in the field and you increment it, it will remove them, however you can add the text before you hit build.  
4: The "Start build" button begins the gradlew "build" command, unless you have a secondary file selected.  
5: The "Install" button begins the gradlew "setupDevWorkspace" command. 
6: The "Setup" button begins the gradlew "eclipse" command.  
7: The "Decompile" buttons begins the graldew "setupDecompWorkspace" command.  
8: The "saveToConfig" button saves the current setup to the config files. Note: That this does not include the version field.
9: The "Secondary" checkbox determines whether or not to use a custom script file.
10: The "Clean build" checkbox determines whether or not to run the extra command "clean" with the build command, Note: Doesn't get executed if you have a secondary script.
11: The "Secondary" button, only visible when the appropriate checkbox is selected, opens a window for you to select your script file.
12: The text next to the "Secondary" button,only visible when the appropriate checkbox is selected, shows what file is currently selected.
