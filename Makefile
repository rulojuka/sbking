APP_NAME=sbking
# WIN_NAME=MINGW64_NT-10.0-18363

build:
	docker build -t $(APP_NAME) .

run:
	docker run --rm $(APP_NAME)

clean:
	docker rmi $(APP_NAME)
	rm ./sbking-client.jar
	rm ./sbking-server.jar

kill_server:
	@./kill_sbking_server.sh

client: kill_server
	mvn -f pom-client.xml clean package && cp target/sbking-1.0.0-alpha-jar-with-dependencies.jar ./sbking-client.jar && chmod +x ./sbking-client.jar

server: kill_server
	mvn clean package && cp target/sbking-1.0.0-alpha-jar-with-dependencies.jar ./sbking-server.jar