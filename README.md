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




## Notes

### How to dockerize wanderland-server

Video tutorial: https://www.jetbrains.com/help/idea/docker.html. Assumes Intellij Ultimate edition


Docker daemon must be running. Normally, the os should do this automatically when it boots (e.g. https://docs.docker.com/config/daemon/)

###  A) Container with Postgres

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


### B) Container with Backend

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


### C) Docker compose with Frontend, Backend and Postgresql

Also useful:
https://www.section.io/engineering-education/running-a-multi-container-springboot-postgresql-application-with-docker-compose/  
https://wkrzywiec.medium.com/how-to-run-database-backend-and-frontend-in-a-single-click-with-docker-compose-4bcda66f6de  
About docker volumes: https://docs.docker.com/storage/volumes/

1) Create docker-compose.yml file for local use:
```
version: '3.8'

services:
  wanderland-server:
    image: ikeller13/wanderland-server:local-1.0
    volumes:
      - ../photo_upload:/src/main/upload/photos
    depends_on:
      - postgres14
    restart: always
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres14:5432/photodb
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=4c6BqbLccYqbMx9rXqDPj7zbCdiK8YYA5QPxf33E

  postgres14:
    image: postgres:14.3
    restart: always
    environment:
      - POSTGRES_PASSWORD=4c6BqbLccYqbMx9rXqDPj7zbCdiK8YYA5QPxf33E
      - POSTGRES_USER=postgres
      - POSTGRES_DB=photodb
    ports:
      - "5432:5432"
    volumes:
      - wanderland-db-postgres:/var/lib/postgresql/data

  wanderland-client:
    image: ikeller13/wanderland-client:local-1.0
    ports:
      - "4200:80"
    depends_on:
      - wanderland-server

volumes:
  wanderland-db-postgres:
```
wanderland-db-postgres is a volume that will be managed by docker but will persist. This is recommended over "bind mounts" where a specific directory in the host file-system is mounted, as explained [here](https://docs.docker.com/storage/volumes/). Apparently, it should be easy to migrate docker volumes to a different host.

To check which volumes exist on your system: `docker volume ls`



2) Start up as `docker-compose up`

Note: If the wanderland-db-postgres volume does not yet exist on your system, it will be created automatically. You may encounter problems, however, if the photodb database does not yet exist. In this case, it may be necessary to open a psql shell within the postgres container as follows:
- In Intellij, navigate to the running container (Services tab) --> Right click on postgres container --> Exec --> Create and run --> `psql postgresql://postgres:4c6BqbLccYqbMx9rXqDPj7zbCdiK8YYA5QPxf33E@localhost:5432`
- Run: `CREATE DATABASE photodb;`

Once the database exists in the volume and the volume is mounted in the docker compose file, everything should work. For example, you can then stop and delete the containers and restart them at a later date, and the database will be reconnected from the volume. 


3) Switch off as `docker-compose down`


#### Push images to Dockerhub
https://docs.docker.com/docker-hub/repos/
1) By re-tag an existing local image:  `docker tag <existing-image> <hub-user>/<repo-name>[:<tag>]`
2) `docker push <hub-user>/<repo-name>:<tag>`
