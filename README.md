# Alfred Pennyworth (_aka Batman's Butler_)
## About
This bot is only written in Java (with Docker and MakeFile to deploy) and is a bot in slack
where the user can get a daily quote or checkup a GitHub user and then check her/his repositories one at the time.

This project uses api's from _https://quotes.rest/qod_ for quotes, _https://api.github.com/users/_ for getting github users and repositories, 
_https://www.harmannenfalt.no/api_ and _https://www.hardovrefalt.no/api_ for getting info about the mountains Mannen and Dovre have fallen or not.

## Functionality
* Daily quote will be sent every day at 09:15

* `/quote` returns a daily quote

![](https://i.imgur.com/J8nQb1K.png)

* `/mannen` responds with a message if the mountain Mannen has fallen or not

![](https://i.imgur.com/xZze14R.png)

* `/dovre` responds with a message if the mountain Dovre has fallen or not

![](https://i.imgur.com/gbR2xlm.png)

* `/github <Username>` returns the user if they exist

![](https://i.imgur.com/qZyvGVc.png)
    
    
* `Select a repository` and information will be returned about it.

![](https://i.imgur.com/uUqkhjK.png)

(I used [this](https://api.slack.com/tools/block-kit-builder) website to check how the JSON's should look before posting them to slacks api)

## Templates
See [Templates](Templates.md) for better understanding of all `toJson()` functions.

## Technologies
* TomCat
* Java
* Maven
* Docker

## Frameworks
* Spring Boot
* Hibernate

## You will need
* Docker
* maven
* make
* git

## Deployment
1. Clone this repository `git clone https://gitlab.com/BredeFK/alfred.git`
1. Open `Dockerfile` and add the two environment variables `SLACK-BOT-TOKEN` and `CHANNEL-ID-GENERAL`
1. Run `make build`

## SonarQube
1. [Download and start SonarQube server](https://docs.sonarqube.org/latest/setup/get-started-2-minutes/)
1. run `mvn clean verify sonar:sonar`
1. or run `mvn sonar:sonar` if project is unchanged

### Makefile
* make build
* make clean
* make run
* make status

## Managing deployment
* To check logs on running deployment `screen -r`
* To check status and ID on running container `docker container ls`
* To stop deployment `docker container stop <CONTAINER ID>` 