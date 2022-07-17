# Wanderland Server

The companion Github repository with the front-end code is available [here](https://github.com/marcoavol/wanderland-client)

The app is available [here](https://www.wanderland.dev).


## How to start the app

The Wanderland app can be run either in local mode or on our Hetzner server. 

### Local mode
The postgreSQL database will be persisted in a docker volume. The jpg files of the photos will be saved to a directory called photo_upload located at the same level as the wanderland-server directory.
1) Start the app using `docker-compose -f docker-compose-local.yml up -d` from the app's root directory. 
2) Connect in browser at localhost:4200.
3) Shut down the app: `docker-compose -f docker-compose-local.yml down`

### Deployment mode
The postgreSQL database will be persisted in a docker volume. The jpg files are saved to a directory on our Hetzner volume. We use [Certbot](https://certbot.eff.org/) to obtain an SSL certificate.
1) Login to server: `ssh -o "ServerAliveInterval 60" root@135.181.205.41`
2) `cd /mnt/docker_images`
3) `docker compose -f docker-compose-deploy.yml up -d`
4) Connect to the app in browser at https://www.wanderland.dev or 135.181.205.41:4200
5) Shut down the app: `docker compose -f docker-compose-deploy.yml down`


## Profiles
- deploy: Configuration to run the app on our Hetzner server with a PostgreSQL database
- local: Configuration to run the app locally with a PostgreSQL database
- local-h2: Configuration to run the app locally with an in-memory h2 database


## Helper scripts
- `scp-docker-compose.sh/bat`: Copies the docker compose file for deployment to the Hetzner server
- `update_ssl_certificate.sh`: Updates the SSL certificate. This needs to be done every three months

---





## Notes

The purpose of the following sections is to keep a record of what we learned in the process of containerising and deploying the app. 

### How to dockerize wanderland-server

Video tutorial: https://www.jetbrains.com/help/idea/docker.html. Assumes Intellij Ultimate edition


Docker daemon must be running. Normally, the os should do this automatically when it boots (e.g. https://docs.docker.com/config/daemon/)

#### Container with Postgres

These steps are useful to set up Intellij to work with docker and to see how you can use Intellij to pull an image from Dockerhub and create a container. In principle, they are not necessary. The postgres image will be pulled directly from Dockerhub in the docker compose step below if it doesn't exist locally. 

1) Configure Intellij to be able to work with the local docker installation:
Settings --> Build, Execution, Deployment --> Docker --> +. Leave everything as is. It should then say "Connection successful" --> OK

2) Docker is now visible in Services window --> Click connect

3) Download Postgres image: In Intellij Services window, click on Pull Image icon (Arrow with little square in top right corner). Enter image name --> Ctrl + Enter to download

4) Downloaded image is now visible in Images. Right click --> Create container. In the Create Docker Configuration window, set Name and Container name fields. Click Modify options --> Environment variables. Click on folder icon in Environment variables field --> + --> Add:  
POSTGRES_PASSWORD	4c6BqbLccYqbMx9rXqDPj7zbCdiK8YYA5QPxf33E     
POSTGRES_DATABASE	jdbc:postgresql://localhost:5432/photodb   # Must exist  
Modify options --> Bind ports --> Click on folder icon --> + --> Add  
5432	5432  

Initialise photodb needs to be done when a new container is started where photodb doesn't exist yet. The container can be stopped and restarted and the db will persist. Only when the container is deleted (docker rm <container>), will the db also be removed
5) open a shell to connect to psql client: rightclick on container --> Exec --> Create and run --> psql postgresql://postgres:4c6BqbLccYqbMx9rXqDPj7zbCdiK8YYA5QPxf33E@localhost:5432
6) Run: CREATE DATABASE photodb;


#### Container with Backend

Also using https://www.educative.io/answers/how-do-you-dockerize-a-maven-project

1) In application.properties, specify which profile should be used. See Profiles section above for more info.
2) Create jar file for project: Rightclick on wanderland-server --> Run maven --> package
3) New --> File --> Dockerfile. In Dockerfile enter:
```
FROM openjdk:17
ADD target/wanderland-server-0.0.1-SNAPSHOT.jar wanderland-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","wanderland-server-0.0.1-SNAPSHOT.jar"]
```

4) Create image, e.g. by running Dockerfile in Intellij or from command-line as `docker build -t <image_name>:<tag_name> .`
--> On Mac arm (M1, ...) see https://stackoverflow.com/questions/66920645/exec-format-error-when-running-containers-build-with-apple-m1-chip-arm-based !


#### Docker compose with Frontend, Backend and Postgresql

Useful links:
https://www.section.io/engineering-education/running-a-multi-container-springboot-postgresql-application-with-docker-compose/  
https://wkrzywiec.medium.com/how-to-run-database-backend-and-frontend-in-a-single-click-with-docker-compose-4bcda66f6de  
About docker volumes: https://docs.docker.com/storage/volumes/

Create docker compose yml file: 
wanderland-db-postgres is a volume that will be managed by docker but will persist. This is recommended over "bind mounts" where a specific directory in the host file-system is mounted, as explained [here](https://docs.docker.com/storage/volumes/). Apparently, it should be easy to migrate docker volumes to a different host.

To check which volumes exist on your system: `docker volume ls`


#### Using https

1) Obtain an SSL certificate:
Tutorial: https://mindsers.blog/post/https-using-nginx-certbot-docker/

The docker compose file is modified to also include [certbot](https://certbot.eff.org/). We can then get a certificate using:
```
 docker compose -f docker-compose-deploy.yml run --rm certbot certonly --webroot --webroot-path /var/www/certbot/
```
The certificate is persisted in a folder called `./certbot/conf`, where it is accessible to both the front- and backend.


2) In front-end: Modify the nginx config file to listen to port 443, include link to the ssl certificate and various arguments related to handling the ssl certificate.
3) In front-end: Change apiBaseUrl to port 8443: `https://www.wanderland.dev:8443`
4) Convert the certificate to PKCS12 format: `openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out keystore.p12 -name tomcat -CAfile chain.pem -caname root`. This will be used by the backend.
5) In back-end: Modify application-deploy.properties to use port 8443. To enable HTTPS, add the following:
```
security.require-ssl=true
server.ssl.key-store=/etc/letsencrypt/live/www.wanderland.dev/keystore.p12
server.ssl.key-store-password=springboot
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=tomcat
```


#### Start app using docker compose
1) Start up as `docker-compose -f <compose.yml> up -d` (Windows, Mac) or `docker compose -f <compose.yml> up -d` (Linux)
2) Switch off as `docker-compose -f <compose.yml> down` or `docker compose -f <compose.yml> down`


#### Push images to Dockerhub
https://docs.docker.com/docker-hub/repos/
1) Re-tag an existing local image:  `docker tag <existing-image> <hub-user>/<repo-name>[:<tag>]`
2) `docker push <hub-user>/<repo-name>:<tag>`
