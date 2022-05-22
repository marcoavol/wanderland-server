## Wanderland Server


* PhotoController gibt im Moment nur einen String in Home Route aus (localhost:8080)


### Dev Profil
* DevConfiguration ist im Prinzip eine Helferklasse, die zu Testzwecken ein "Photo" erzeugt und in die H2 Datenbank speichert. Die Tabelle kann dann via H2 console angeschaut werden.


### Prod Profil
Im Moment muss PostgreSQL lokal installiert sein und eine Datenbank mit Namen photodb existieren

Meine Einstellungen:
* Server: localhost
* Port: 5432
* Username: postgres
* Passwort für Benutzer postgres: postgres
Entspricht application-prod.properties

Datenbank erstellen:
```
# In SQL Shell (psql (14.3))
 CREATE DATABASE photodb;
 # Volle Permissions geben:
 GRANT ALL PRIVILEGES ON DATABASE "photodb" TO postgres;
```

Lokal kann man die Photo-Tabelle in der db wie folgt anschauen:
```
# In SQL Shell (psql (14.3))
# Mit db verbinden:
\c photodb;
# Liste der Relationen anzeigen:
\d
# Einträge in der Tabelle photo anzeigen:
SELECT * FROM photo;
```
