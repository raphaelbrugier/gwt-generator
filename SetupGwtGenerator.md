# Prerequisite #

We assume you have Eclipse installed with the latest Google Eclipse plugin.

Install a svn plugin for Eclipse, like subversive is also preferred.


# Checkout the projects #
Check out the Gwt-Uml-Api project at :
https://gwtuml.googlecode.com/svn/trunk/GWTUMLAPI

Check out the Generator-Api project at :
https://gwt-generator.googlecode.com/svn/trunk/GeneratorApi

Check out the Gwt-Generator-Project at :
https://gwt-generator.googlecode.com/svn/trunk/GwtGenerator


# Fix the missing libraries #
For the Gwt-Generator project, you will have compile errors because of missing libraries.
- appengine-api-labs.jar
- appengine-api-labs-stubs.jar
- appengine-testing.jar

These libraries come with the appengine-sdk, usually you have the sdk installed with the google eclipse plugin. The libraries is locating in your eclipse directory, in the path :
($eclipseDirectory)/plugins/com.google.appegine.eclipse.sdkbundle.($sdk\_version)\lib

Change the location of the missing libraries to your sdk’s location :
Properties of the project -> Java build Path -> Libraries


# Add the source folders #
You now still have compile errors in the Gwt-Generator project because of missing sources. This is because the Gwt-Generator project use the Gwt-Uml-Api and Generator-Api projects by linking to their sources and not using them as a compiled library. This simplified the development process with Gwt in development mode.

Change the path for the missing sources :
Properties of the project -> Java build path -> Source

- Change the folder generatorAPI-src location to ($workspace)\GeneratorApi\src

- Change the folder generatorAPI-tst location to ($workspace)\GeneratorApi\tst

- Change the folder gwtUmlAPi location to ($workspace)\gwtUmlAPi\src


# Run ! #
Now you can run the application !
run as -> Web application.