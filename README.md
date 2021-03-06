ErrorLinkyThing
===============

This is a crude Eclipse plugin that inserts hyperlinks to files into the console.  It is written with my own current setup (CDT + GCC + MinGW + addr2line) in mind, but it could easily be adapted for other purposes.  The plugin simply finds text in the console that matches a regex, parses that text as file names and line numbers, and adds the hyperlinks.  It is very simple and was written for casual use on my own local computer, so there are probably situations where it fails.  It is offered here for other users who might otherwise want to make such a plugin for their own casual, local use, and for reference.

This plugin is written for use with Eclipse Luna, version 4.4.0, and is not guaranteed to work with other versions of Eclipse (or even with that one, actually).

For those unfamiliar with Eclipse plugin development, here is one way to use the plugin:

1) Get the files and open them in Eclipse as a project.<br />
2) Find the regex that the plugin uses to identify line-in-file references.  This regex is stored in plugin.xml.  You can find it by poking around in the project in Eclipse, or by simply opening the aforementioned XML file in a text editor.<br />
3) Replace this regex with one appropriate for your purpose.  Note that the separation of file name from line number is done later; this regex should simply match ALL the text that is to become a single hyperlink.  (Also note that two kinds of escaping are applied here:  first regex escaping for characters like the backslash, then XML escaping for characters like the less-than sign.  You may have to deal with both of these types of escaping, or with just one of them, depending on what editor you use to alter plugin.xml; because of this, a plain text editor may display the regex differently than Eclipse's plugin editor UI will.)<br />
4) Open the .java file and, as appropriate, change how the file path, line number, and whatever other information you require are read from the text that the regex matches.<br />
(If you need to import any classes that aren't available in the JARs the project is already linked to, you might have to add dependencies to the project in order to link additional JARs.  Do this by opening the MANIFEST.MF file in Eclipse, moving to the Dependencies tab, and clicking the Add button.)<br />
5) Install the plugin locally by exporting it:  Do File->Export; then choose "Deployable plug-ins and fragments"; then choose "Install into host."  (Or you can export to a JAR and then separately instruct Eclipse to install a new plugin from that JAR.)<br />
Now when text is printed to the console, the plugin should automatically be invoked.<br />
