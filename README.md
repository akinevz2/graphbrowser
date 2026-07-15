# README

This project aims to develop a software package that will enable its users to visualise page dependencies without 
having to view the page content.

PagerTS is a command line utility that provides the user with a portable tool for transforming URLs into JSON.

GraphBrowser is the graphical user interface to PagerTS using Quarkus and web technologies.

The output of the CLI represents the set of navigable items within a webpage.
These can be used to represent a hierarchy of web objects linking from page to page within the World Wide Web.

## Dependencies

To use this project, first clone [https://github.com/akinevz2/GraphBrowser](Graphbrowser):

```bash
mkdir graphbrowser && cd graphbrowser
git clone https://github.com/akinevz0/GraphBrowser ./
```

Then you will need to install `pagerts` as a global system dependency using node package manager.

```bash
npm i -g pagerts
```

### Installing PagerTS

To use pagerts as a command line utility, it must first be globally installed using NPM.

You may wish to either clone the repo or install it from NPM.

```bash
git clone https://github.com/akinevz0/pagerts
cd pagerts
npm run build
npm install -g ./
# or alternatively
npm install -g pagerts
```

You will now have a system-wide application under the name of `pagerts`.

```bash
pagerts -h
```

Pagerts comes with a standard CLI arg parsing library.

### PagerTS Output

The output encodes a dependency list of resources. For each input URL, pagerts will produce a object.

Each URL is parsed using the JSDOM library and returned to the user as an object annotated with fields `title`, `url`, `resources`.

The `resources` field maps a human-readable name of the resource to a url.

The name is extracted from the readable text on the page, using a priority list of tag properties.

### Installing GraphBrowser

The GraphBrowser application can be installed using the Quarkus CLI app:

```bash
cd graphbrowser
quarkus build
quarkus run
```

Alternatively, if Quarkus CLI is not installed:

```bash
cd graphbrowser
./gradlew clean quarkusBuild quarkusRun
```

Hint: check whether gradle is installed on the system.

## Configuring GraphBrowser

GraphBrowser configuration is located in `src/main/resources/application.properties`.

Default port can be configured at build time or runtime: please see [quarkus documentation on HTTP](https://quarkus.io/guides/http-reference) and [environment variables](https://quarkus.io/guides/config-reference)

## Running GraphBrowser

Before running the application please ensure that `pagerts` is installed. 

Having `pagerts` installed from NPM registry will result in faster response time, as the CLI is invoked for every webpage.

Current implementation (as of 2025) results in much slower node startup if installed from local folder.

## Bugs and TODOs

Welcome page contributions can be added to the `src/main/webui/src/index.md` file.

## Screenshots

![Usage](https://github.com/akinevz0/graphbrowser/blob/f29c361f14bd7beecffc34f8ad8afd53f8aae49e/doc/Screenshot%202025-05-06%20123055.png)
