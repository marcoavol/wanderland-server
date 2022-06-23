## Wanderland Server


* localhost:8080/api-docs.html: Dokumentation des APIs. 


# Dockerize

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

1) Create jar file for project: Rightclick on wanderland-server --> Run maven --> package

2) New --> File --> Dockerfile. In Dockerfile enter:
```
FROM openjdk:17
ADD target/wanderland-server-0.0.1-SNAPSHOT.jar wanderland-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","wanderland-server-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
```

3) Create image, e.g. by running Dockerfile in Intellij or from command-line (analogous to front-end)


### C) Docker compose with Frontend, Backend and Postgresql

Also useful:
https://www.section.io/engineering-education/running-a-multi-container-springboot-postgresql-application-with-docker-compose/  
https://wkrzywiec.medium.com/how-to-run-database-backend-and-frontend-in-a-single-click-with-docker-compose-4bcda66f6de  
About docker volumes: https://docs.docker.com/storage/volumes/

1) Create docker-compose.yml file:
```
version: '1.0'

services:
  wanderland-server:
    image: wanderland-backend:latest
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
      - my-db-postgres:/var/lib/postgresql/data

  wanderland-frontend:
    image: wanderland-client-image:2.0
    ports:
      - "4200:80"
    depends_on:
      - wanderland-server

volumes:
  my-db-postgres:
```
my-db-postgres is a volume that will be managed by docker but will persist. This is recommended over "bind mounts" where a specific directory in the host file-system is mounted, as explained [here](https://docs.docker.com/storage/volumes/). Apparently, it should be easy to migrate docker volumes to a different host.

To check which volumes exist on your system: `docker volume ls`



2) Start up as `docker-compose up`

Note: If the my-db-postgres volume does not yet exist on your system, it will be created automatically. You may encounter problems, however, if the photodb database does not yet exist. In this case, it may be necessary to open a psql shell within the postgres container as follows:
- In Intellij, navigate to the running container (Services tab) --> Right click on postgres container --> Exec --> Create and run --> `psql postgresql://postgres:4c6BqbLccYqbMx9rXqDPj7zbCdiK8YYA5QPxf33E@localhost:5432`
- Run: `CREATE DATABASE photodb;`

Once the database exists in the volume and the volume is mounted in the docker compose file, everything should work. For example, you can then stop and delete the containers and restart them at a later date, and the database will be reconnected from the volume. 


3) Switch off as `docker-compose down`