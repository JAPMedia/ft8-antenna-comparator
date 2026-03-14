package ch.hb9hit.pskreporter.service;

import ch.hb9hit.pskreporter.model.ReceptionReports;
import ch.hb9hit.pskreporter.model.Spot;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class PskReporterService {

    private final List<Spot> historicalSpots = new CopyOnWriteArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final XmlMapper xmlMapper = new XmlMapper();

    public void fetchAndStoreSpots(String receiverCallsign, String antennaName) {
        String url = String.format("https://retrieve.pskreporter.info/query?receiverCallsign=%s&flowStartSeconds=-3600", receiverCallsign);

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "HB9HIT-Antenna-Comparator/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getBody() != null) {
                ReceptionReports reports = xmlMapper.readValue(response.getBody(), ReceptionReports.class);
                if (reports.getReceptionReports() != null) {
                    for (Spot spot : reports.getReceptionReports()) {
                        spot.setAntennaName(antennaName);
                        // Avoid duplicates if needed, but for comparison we might want all.
                        // For simplicity, we just add them.
                        historicalSpots.add(spot);
                    }
                }
            }
        } catch (Exception e) {
            // Log error or handle it
            e.printStackTrace();
        }
    }

    public List<Spot> getAllSpots() {
        return Collections.unmodifiableList(historicalSpots);
    }

    public void clearSpots() {
        historicalSpots.clear();
    }
}
