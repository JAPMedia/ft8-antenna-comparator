
package ch.hb9hit.pskreporter.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/spots")
public class PskReporterController {

    @GetMapping
    public List<String> getSpots(@RequestParam String antenna) {
        // Stub de réponse
        return List.of("Spot 1 - " + antenna, "Spot 2 - " + antenna);
    }
}
