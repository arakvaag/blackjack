# Blackjack - REST-versjon

Du spiller gjennom å kjøre forskjellige HTTP-kall til localhost:8080. Postman kan gjerne brukes som REST-klient.

### Starte nytt spill
POST-kall mot http://localhost:8080/spill <br/>
Body: { "spillersNavn": "<navn på spilleren>" }

### Trekke nytt kort
POST-kall mot http://localhost:8080/spill/trekkKort <br/>
Body: ingen

### Velge å stå
POST-kall mot http://localhost:8080/spill/staa <br/>
Body: ingen

### Hente info om aktivt spill
GET-kall mot http://localhost:8080/spill <br/>
