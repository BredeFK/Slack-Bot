default: build

build:
	git pull
	mvn clean install
	sudo docker image build -t alfred .
	screen sudo docker container run -p 80:8080 -it alfred

run:
	screen sudo docker container run -p 80:8080 -it alfred

clean:
	git pull
	mvn clean install
