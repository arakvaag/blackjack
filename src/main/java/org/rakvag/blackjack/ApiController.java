package org.rakvag.blackjack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rakvag.blackjack.domene.Spill;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/spill")
public class ApiController {

    private final ObjectMapper objectMapper;
    private final SpillServer spillServer;

    public ApiController(ObjectMapper objectMapper, SpillServer spillServer) {
        this.objectMapper = objectMapper;
        this.spillServer = spillServer;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> opprettNyttSpill(@RequestBody String requestBody) throws JsonProcessingException {
        @SuppressWarnings("unchecked")
        Map<String, Object> requestParams = (Map<String, Object>) objectMapper.readValue(requestBody, Map.class);

        Spill spill = spillServer.startNyttSpill((String) requestParams.get("spillersNavn"));

        return ResponseEntity.ok(objectMapper.writeValueAsString(spill.lagDTO()));
    }

    @PostMapping(path = "/trekkKort", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> trekkKort() throws JsonProcessingException {
        Spill spill;
        try {
            spill = spillServer.hentAktivtSpill().trekkKort();
        } catch (SpillServer.SpillIkkeStartetException e) {
            return ResponseEntity.badRequest().body("{\"feilmelding\": \"Det er ikke startet et spill enda, og det er derfor ikke mulig å trekke nytt kort.\"}");
        } catch (Spill.SpillerErFullførtException e) {
            return ResponseEntity.badRequest().body("{\"feilmelding\": \"Spillet er fullført, kan ikke trekke flere kort.\"}");
        }
        return ResponseEntity.ok(objectMapper.writeValueAsString(spill.lagDTO()));
    }

    @PostMapping(path = "/staa", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> stå() throws JsonProcessingException {
        Spill spill;
        try {
            spill = spillServer.hentAktivtSpill().stå();
        } catch (SpillServer.SpillIkkeStartetException e) {
            return ResponseEntity.badRequest().body("{\"feilmelding\": \"Det er ikke startet et spill enda, og det er derfor ikke mulig å trekke nytt kort.\"}");
        } catch (Spill.SpillerErFullførtException e) {
            return ResponseEntity.badRequest().body("{\"feilmelding\": \"Spillet er fullført.\"}");
        }
        return ResponseEntity.ok(objectMapper.writeValueAsString(spill.lagDTO()));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> hentSpill() throws JsonProcessingException {
        Spill spill;
        try {
            spill = spillServer.hentAktivtSpill();
        } catch (SpillServer.SpillIkkeStartetException e) {
            return ResponseEntity.badRequest().body("{\"feilmelding\": \"Det er ikke startet et spill enda.\"}");
        }
        return ResponseEntity.ok(objectMapper.writeValueAsString(spill.lagDTO()));
    }

}
