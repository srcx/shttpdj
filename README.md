# shttpdj

**Warning: unmaintained since 2003!**

Motto: 's' as stupid.

## Running

Requirements:

*  J2RE 1.3 or higher
*  third-party libraries (see below)

Test example run:
```
  java -cp shttpdj.jar:lib/log4j-1.2.8.jar:lib/xercesImpl.jar:lib/xmlParserAPIs.jar \
    cz.srnet.shttpdj.impl.Main test/config.xml
```
## Compilation

Requirements:

*  J2SDK 1.3 or higher
*  third-party libraries (see below)
*  Ant 1.4 or higher

There is build.xml for Ant, which does compilation and packaging, or
.project for Eclipse IDE series 2.x.

## Used third-party libraries

* Log4j series 1.2.x
* JAXP 1.1 compatible XML parser
