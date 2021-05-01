# Blackjack - REST-versjon

Du spiller gjennom å kjøre forskjellige HTTP-kall til localhost:8080. Postman kan gjerne brukes som REST-klient.

### Starte nytt spill
POST-kall mot http://localhost:8008/spill
Body: { "spillersNavn": "<navn på spilleren>" }

### Trekke nytt kort
POST-kall mot http://localhost:8008/spill/trekkKort
Ingen body

### Velge å stå
POST-kall mot http://localhost:8008/spill/staa
Ingen body

### Hente info om aktivt spill
GET-kall mot http://localhost:8008/spill

