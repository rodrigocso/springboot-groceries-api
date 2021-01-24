<h1 align="center">
  Spring Boot REST API Example
</h1>

<h4 align="center">Structuring and testing a Spring Boot application</h4>

<p align="center">
    <a alt="Java">
        <img src="https://img.shields.io/badge/Java-v11-F46036" />
    </a>
    <a alt="Spring Boot">
        <img src="https://img.shields.io/badge/Spring%20Boot-v2.4.1-6DB33F.svg" />
    </a>
    <a alt="Coverage">
        <img src="https://img.shields.io/badge/coverage->90%25-31D843" />
    </a>
    <a alt="License">
        <img src="https://img.shields.io/badge/license-MIT-63B0CD.svg" />
    </a>
</p>

## Table of Contents ##
1. [Introduction](#Introduction)
2. [Design Philosophy](#Design-Philosophy)
3. [Application](#Application)
4. [License](#License)

## Introduction ##
Learning a new framework nowadays is much more streamlined due to the fact that there is
a plethora of great blogs available with tutorials and examples for absolutely anything.
As good as that is, nothing is perfect: more often than not, information is very fragmented,
forcing the reader to jump back and forth between dozens of articles to be able to see the
big picture.

With that in mind, I took it upon myself to create this repository to serve as a reference to
myself and hopefully others on how to get started building an enterprise grade REST API
project using Spring Boot, to the best of my knowledge, of course.

## Design Philosophy ##
For this project, the following design guidelines were adopted:
* Only battle tested dependencies
* Clean architecture (layer dependencies flow in a single direction)
* Clean code (highly testable, following SOLID and DRY)
* Ample automated testing (> 90% code coverage)

## Application ##
This application serves as the backend for [Groceries Tracker](https://github.com/rodrigocso/groceries-tracker).
At the moment, the entities are:
* **Store** (name and location)
* **Brand** (name)
* **Product** (name and **Brand**)
* **Item** (**Product** and package size)
* **Purchase** (**Store**, **Item**, purchase date, quantity and price)

<img src="https://github.com/rodrigocso/springboot-groceries-api/blob/master/docs/images/relationships.jpg" alt="entity relationship"></a>

You can **Purchase** an **Item** at a **Store**. An **Item** is how you package a
**Product** to be sold. A **Product** may or may not have an associated **Brand**.

### Highlights ###
This project showcases the usage of:
* JPA Repositories with Hibernate
    * A custom repository extending the generated one
    * JPA Criteria API is used with multiple joins
    * JPA Static Metamodel Generator (`get("field")` is not type safe)
* H2 in memory database for automated tests
* MariaDB as the data source
* Ample testing with some interesting techniques
    * Object builders with fluent API `Product p = ProductBuilder.builder().withName("P1").build();`
    * Custom `ResultMatcher` to facilitate repetitive JSON body assertions
* Docker compose to deploy MariaDB instance and the API

## License ##
This project is licensed under the terms of the MIT license.
