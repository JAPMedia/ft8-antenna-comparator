package ch.hb9hit.pskreporter.controller;

import ch.hb9hit.pskreporter.model.Spot;
import ch.hb9hit.pskreporter.service.PskReporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
public class PskReporterController {

    private final PskReporterService pskReporterService;

    @Autowired
    public PskReporterController(PskReporterService pskReporterService) {
        this.pskReporterService = pskReporterService;
    }

    @PostMapping("/fetch")
    public void fetchSpots(@RequestParam String callsign, @RequestParam String antenna) {
        pskReporterService.fetchAndStoreSpots(callsign, antenna);
    }

    @GetMapping
    public List<Spot> getSpots() {
        return pskReporterService.getAllSpots();
    }

    @DeleteMapping
    public void clearSpots() {
        pskReporterService.clearSpots();
    }
}
