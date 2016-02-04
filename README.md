# Java 8 web application using Embedded Tomcat, Weld and RESTEasy

Also as a bonus there is a JSP and a Servlet.

## Introduction

A working example of a [Springboot](https://spring.io/guides/gs/actuator-service/) equivalent for [CDI](http://www.cdi-spec.org/) is neither as easy to find nor construct as one might expect.

Nevertheless, this application demonstrates more or less the minimum needed to have CDI (Weld), Tomcat (embedded) and RESTEasy all running in harmony.

## Build and package

Build and package with the plugin already provided in the POM:

`mvn package`

Then you may run (from the project root directory) with:

`target\bin\webapp.bat`

You can of course run from an IDE. Just locate `launch.Main.java`.

## Resources
Some reasonably essential reading:
- The best example of [getting an embedded Tomcat up and running](https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat) by far are the instructions provided by Heroku. 
- [RESTEasy documentation 3.0.13.Final](https://docs.jboss.org/resteasy/docs/3.0.13.Final/userguide/html/). Pay attention to how to [configuring RESTEasy when not using JBoss](http://docs.jboss.org/resteasy/docs/3.0.13.Final/userguide/html_single/index.html#d4e113).

## Notes
Notes on the experience:
- A good approach is to clone the [example provided courtesy of Heroku on Github](https://github.com/heroku/devcenter-embedded-tomcat). Start with that, get it working and then modify as necessary.
- Painful [Jersey](https://jersey.java.net/) to merely get the correct [dependencies in a CDI environment](https://jersey.java.net/documentation/latest/cdi.support.html). Abandoned and went with RESTEasy.
- Weld is supposed to work with Jetty but documentation and experience seems to be a little thin. Most CDI users are likely to be enterprise customers deploying to JBoss or some other JEE container. Most of the material online will help with these cases. Very little on Jetty but more on Tomcat. Go with Tomcat.
- Example in the [Weld documentation for embedding Tomcat](https://docs.jboss.org/weld/reference/latest/en-US/html/environments.html#_embedded_tomcat) just did not work. `org.jboss.weld.environment.servlet.Listener` is not required at all despite the fact that it states "With embedded Tomcat it is necessary to register Weld's listener programmatically". But then notice how the line pertinent line in the code example is commented. It almost looks like a mistake and therefore tempting to uncomment it. If you copied and pasted blindly you would be fine but in any case it appears not to be required. It was necessary however to have `/WEB-INF/beans.xml` and `/META-INF/context.xml` in place.