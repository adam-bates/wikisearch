# Adam Bates' Wiki Search Application

### Table of Contents

- [Description](#description)
- [How to run](#how-to-run)
    - [Requirements](#requirements)
    - [Fetching the project](#fetching-the-project)
    - [Building the project](#building-the-project)
    - [Running the application](#running-the-application)
- [How to use](#how-to-use)
    - [API Definition](#api-definition)
        - [Wiki Pages](#wiki-pages)
        - [Wiki Terms](#wiki-terms)
- [How it works](#how-it-works)


## Description

This application will start up a server when run locally, pull 10 random pages from Wikipedia, and index them to be searched. The server can then be hit with REST requests over HTTP to search against the indexed data.


## How to run

#### Requirements

- [JDK SE 1.8 (Higher should be fine but has not been tested)](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
- [Maven](https://maven.apache.org/install.html) (or simply `brew install maven`)
- [Git](https://git-scm.com/downloads)

#### Fetching the project

In the command line, cd into the parent directory that you want the project in, then run the following command:
```
git clone https://github.com/adam-bates/wikisearch.git
```

#### Building the project

Now that we have the project locally, let's go in and build it:
```
cd wikisearch && mvn clean package
```

#### Running the application

And to run the application, assuming we're in the project's root directory, we'll run:
```
mvn spring-boot:run
```


*Note: If the above command isn't working, try:*
```
java -jar target/wikisearch-0.0.1-SNAPSHOT.jar
```


## How to use

So we know *what* the application is, and we're able to run it locally, but we still need to know how to *use* it.

I've defined the API spec below for what is currently implemented, although I'm not formally versioning the project, and there could be updates in the future.


### API Definition
###### (REST over HTTP using JSON)

#### Wiki Pages
```
GET /wiki/pages
```
##### Description
Returns the list of wiki pages including the search score from the query.

##### Request Params
| Param | Type | Required | Default Value | Description |
|:----- |:-----|:-------- |:------------- |:------------|
| query | string | false | null | If exists, acts as a search query to filter results. Uses [Apache's Lucene query syntax](https://lucene.apache.org/core/2_9_4/queryparsersyntax.html).
| field | enum | false | content | Data field to search on. Must be "content", "title", or "id"
| limit | integer | false | 10 | Positive integer used to limit results

##### Response

```
{
  pagesReturned: integer,
  pages: [
    {
      id: integer,
      score: float,
      wikiPageId: integer,
      wikiPageLink: string,
      title: string,
      content: string
    }
  ]
}
```

```
Example Request: GET http://localhost:8080/wiki/pages?query=funny&limit=2
```
```
Example Response:
{
  totalTermsIndexed: 1234,
  pages: [
    {
      id: 0,
      score: 0.121,
      wikiPageId: 111111,
      wikiPageLink: "https://en.wikipedia.org/wiki/Example_Page_1",
      title: "Example Page 1,
      content: "Some example page content."
    },
    {
      id: 1,
      score: 0.089,
      wikiPageId: 222222,
      wikiPageLink: "https://en.wikipedia.org/wiki/Example_Page_2",
      title: "Example Page 2,
      content: "Some example page content."
    }
  ]
}
```

#### Wiki Page
```
GET /wiki/pages/{id}
```
##### Description
Returns the wiki page specified by the id.

##### Path Variables
| Variable | Type | Description |
|:----- |:-----|:--------
| id | integer | ID of the expected wiki page result.

##### Response

```
{
  id: integer,
  score: float,
  wikiPageId: integer,
  wikiPageLink: string,
  title: string,
  content: string
}
```

```
Example Request: GET http://localhost:8080/wiki/pages/4
```
```
Example Response:
{
  id: 4,
  score: 1,
  wikiPageId: 111111,
  wikiPageLink: "https://en.wikipedia.org/wiki/Example_Page_1",
  title: "Example Page 1,
  content: "Some example page content."
}
```

#### Wiki Terms
```
GET /wiki/terms
```
##### Description
Returns a breakdown of indexed terms, including the total frequency in the index, as well as the number of indexed wiki pages the term is included in.

##### Request Params
| Param | Type | Required | Default Value | Description |
|:----- |:-----|:-------- |:------------- |:------------|
| limit | integer | false | 10 | Positive integer used to limit results

##### Response

```
{
  totalTermsIndexed: long,
  terms: [
    {
      totalFrequency: long,
      wikiPageFrequency: integer,
      term: string
    }
  ]
}
```

```
Example Request: GET /wiki/terms?limit=2
```
```
Example Response:
{
  totalTermsIndexed: 1234,
  terms: [
    {
      totalFrequency: 150,
      wikiPageFrequency: 10,
      term: "the"
    },
    {
      totalFrequency: 120,
      wikiPageFrequency: 9,
      term: "of"
    }
  ]
}
```


## How it works

The application starts a local server (Usually at [http://localhost:8080](http://localhost:8080)) using [Java's Spring](https://spring.io/), and was bootstrapped using [Spring Boot](https://spring.io/projects/spring-boot). 

On startup of the server, [MediWiki's API](https://www.mediawiki.org/wiki/API:Main_page) is used to pull 10 random pages from Wikipedia and extract their content. It then uses [Apache's Lucene](https://lucene.apache.org/) to index and store the content of the pages with the title and page ID.

On API requests (see above API definition) it responds with indexed data based on the specific search.
