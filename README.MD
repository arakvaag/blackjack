# Blackjack - REST-versjon

Du spiller gjennom å kjøre forskjellige HTTP-kall til localhost:8080. Postman kan gjerne brukes som REST-klient.

### Starte nytt spill
POST-kall mot http://localhost:8080/spill <br/>
Body: { "spillersNavn": "<navn på spilleren>" }

### Trekke nytt kort
POST-kall mot http://localhost:8080/spill/<spillId>/trekk <br/>
Body: ingen

### Velge å stå
POST-kall mot http://localhost:8080/spill/<spillId>/staa <br/>
Body: ingen

### Hente info om aktivt spill
GET-kall mot http://localhost:8080/spill/<spillId> <br/>


### Sette opp database
docker pull postgres
docker run --name blackjack_db -e POSTGRES_PASSWORD=<passord> -p 5432:5432 -d postgres
