# Alfred Pennyworth (_aka Batman's Butler_)
## About
This will be a bot in slack driven on java code.

## Technologies
* TomCat
* Java
* Maven

## Dependencies
* Gson
* Javax servlet-api

## Deployment (Still in progress!)
1. Clone this repository `git clone https://gitlab.com/BredeFK/alfred.git`
2. Open `Dockerfile` and add the two environment variables `SLACK-BOT-TOKEN` and `CHANNEL-ID-GENERAL`
3. Run maven in root directory `mvn clean install`
4. Build Docker image `sudo docker image build -t alfred .`
5. Deploy app `screen sudo docker container run -p 80:8080 -it alfred`

## Managing deployment
* To check logs on running deployment `screen -r`
* To check status and ID on running container `docker container ls`
* To stop deployment `docker container stop <CONTAINER ID>` 