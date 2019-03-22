# SBKing - an online King card game


## About the project

SBKing is a King card game software aimed to provide a rich and intuitive interface and an online experience to King players. It is still in early development and is licensed as GPL.

## Compiling

SBKing uses [Maven](https://maven.apache.org/) to build. The following line should be enough:

```
mvn clean package
```

This should clean your directory, compile, run all tests and package the final .jar at the target/ directory. Use `java -jar target/sbking-<version>.jar` to run it.

## Code quality

After a sucessful package, you can see [JaCoCo](https://www.jacoco.org/jacoco/)'s code coverage report at `target/site/jacoco/index.html`

You can also discover potential bugs with [SpotBugs](https://spotbugs.github.io/) with:

```
mvn spotbugs:spotbugs
```

and see them with

```
mvn spotbugs:gui
```

## Authors and copyright

### Authors:
See file AUTHORS

### Copyright information:
See file COPYRIGHT

### Full license text:
See file COPYING
