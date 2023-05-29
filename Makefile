APP_NAME=rulojuka/sbking

all: package

clean:
	mvn clean
	rm -f ./sbking-client.jar ./sbking-server.jar
	docker rmi $(APP_NAME); true

package: server client

server: kill_server package_server copy_server

kill_server:
	@./kill_sbking_server.sh

package_server:
	mvn -f pom-server.xml clean package

copy_server:
	cp target/sbking-server-1.0.0-alpha.jar ./sbking-server.jar

client: kill_server package_client copy_client

package_client:
	mvn -f pom-client.xml clean package

copy_client:
	cp target/sbking-client-1.0.0-alpha-jar-with-dependencies.jar ./sbking-client.jar && chmod +x ./sbking-client.jar

build:
	docker build -t $(APP_NAME) .

deploy: build
	docker login
	docker push rulojuka/sbking

run:
	docker run -p 60000:60000 -p 8080:8080 --rm $(APP_NAME)
