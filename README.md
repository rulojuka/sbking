# SBKing - an online King card game


## About the project

SBKing is a King card game software aimed to provide a rich and intuitive interface and an online experience to King players. It is still in early development and is licensed as GPL.

## Compiling

SBKing uses Makefile and [Maven](https://maven.apache.org/) to build. The following line should be enough:

```
make server
```

This should clean your directory, compile, run all tests, package the final .jar at the `target/` directory and copy it into `./sbking-server.jar`. Use `java -jar ./sbking-server.jar` to run it.

To compile the client in the same fashion (`./sbking-client.jar`):
```
make client
```

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

## Using Docker

If you prefer to user Docker, you can
```
make build
```
to build the server image and
```
make run
```
to run the server on the default port.

You can also pull the image directly from the [Docker Hub](https://hub.docker.com/r/rulojuka/sbking) if you want to just start the server on the default port
```
docker run rulojuka/sbking
```

### On DigitalOcean
Create a [docker based droplet](https://marketplace.digitalocean.com/apps/docker) and
```
docker run -p 60000:60000 rulojuka/sbking
```
or follow the `digital-ocean-ubuntu-18-04.sh` script if the docker version is not enough.


### Pushing to DockerHub
```
# docker ps -a # To discover unused containers
# docker rm CONTAINER-ID # To remove them
# docker images # To see images
# docker rmi IMAGE-ID # To remove them
make build
docker login
docker images # To discover the image id
docker tag IMAGE-ID rulojuka/sbking
docker push rulojuka/sbking
```

## Authors and copyright

### Authors:
See file AUTHORS

### Copyright information:
See file COPYRIGHT

### Full license text:
See file COPYING
