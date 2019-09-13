# Alfred Pennyworth (_aka Batman's Butler_)
## About
This bot is only written in Java (with Docker and MakeFile to deploy) and is a bot in slack
where the user can get a daily quote or checkup a GitHub user and then check her/his repositories one at the time.

This project uses api's from _https://quotes.rest/_ for quotes and _https://api.github.com/_ for getting github users and repositories.

## Functionality
* `/quote` returns a daily quote

![](https://i.imgur.com/J8nQb1K.png)

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

## You will need
* Docker
* maven
* make
* git

## Deployment (Still in progress!) 
1. Clone this repository `git clone https://gitlab.com/BredeFK/alfred.git`
2. Open `Dockerfile` and add the two environment variables `SLACK-BOT-TOKEN` and `CHANNEL-ID-GENERAL`
3. Run `make build`

### Makefile
* make build
* make clean
* make run
* make status

## Managing deployment
* To check logs on running deployment `screen -r`
* To check status and ID on running container `docker container ls`
* To stop deployment `docker container stop <CONTAINER ID>` 