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

    public void fetchAndStoreSpots(String receiverCallsign, String antennaName, int minutes) {
        int seconds = minutes * 60;
        String url = String.format("https://retrieve.pskreporter.info/query?receiverCallsign=%s&flowStartSeconds=-%d", receiverCallsign, seconds);

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "HB9HIT-Antenna-Comparator/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getBody() != null) {
                ReceptionReports reports = xmlMapper.readValue(response.getBody(), ReceptionReports.class);
                if (reports.getReceptionReports() != null) {
                    for (Spot spot : reports.getReceptionReports()) {
                        if (isDuplicate(spot)) {
                            continue;
                        }

                        spot.setAntennaName(antennaName);
                        calculateAndSetDistance(spot);
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

    private boolean isDuplicate(Spot newSpot) {
        return historicalSpots.stream().anyMatch(s ->
                s.getSenderCallsign().equals(newSpot.getSenderCallsign()) &&
                s.getTimestamp() == newSpot.getTimestamp() &&
                s.getFrequency() == newSpot.getFrequency() &&
                s.getMode().equals(newSpot.getMode())
        );
    }

    private void calculateAndSetDistance(Spot spot) {
        double[] receiverCoords = decodeLocator(spot.getReceiverLocator());
        double[] senderCoords = decodeLocator(spot.getSenderLocator());

        if (receiverCoords != null && senderCoords != null) {
            double distance = calculateDistance(receiverCoords[0], receiverCoords[1], senderCoords[0], senderCoords[1]);
            spot.setDistance(distance);
            spot.setSenderLatitude(senderCoords[0]);
            spot.setSenderLongitude(senderCoords[1]);
        }
    }

    public double[] decodeLocator(String loc) {
        if (loc == null || loc.length() < 4) return null;
        loc = loc.toUpperCase();
        double lon = (loc.charAt(0) - 'A') * 20.0 - 180.0;
        double lat = (loc.charAt(1) - 'A') * 10.0 - 90.0;
        lon += (loc.charAt(2) - '0') * 2.0;
        lat += (loc.charAt(3) - '0') * 1.0;
        if (loc.length() >= 6) {
            lon += (loc.charAt(4) - 'A') * (5.0 / 60.0);
            lat += (loc.charAt(5) - 'A') * (2.5 / 60.0);
            // Center of the small square
            lon += 2.5 / 60.0;
            lat += 1.25 / 60.0;
        } else {
            // Center of the large square
            lon += 1.0;
            lat += 0.5;
        }
        return new double[]{lat, lon};
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
