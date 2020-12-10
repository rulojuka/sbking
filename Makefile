APP_NAME=sbking

build:
	docker build -t $(APP_NAME) .

run:
	docker run --rm $(APP_NAME)

clean:
	docker rmi $(APP_NAME)
