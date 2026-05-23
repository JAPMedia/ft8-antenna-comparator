package ch.hb9hit.pskreporter.service;

import ch.hb9hit.pskreporter.model.ReceptionReports;
import ch.hb9hit.pskreporter.model.Spot;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PskReporterServiceTest {

    @Test
    public void testXmlParsing() throws Exception {
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<receptionReports currentSeconds=\"1773483404\">\n" +
                "  <receptionReport receiverCallsign=\"F5LZN\" receiverLocator=\"JN24NE\" senderCallsign=\"UR5LP\" senderLocator=\"KN89JK\" frequency=\"14076588\" flowStartSeconds=\"1773483387\" mode=\"FT8\" isReceiver=\"1\" sNR=\"-6\" />\n" +
                "  <receptionReport receiverCallsign=\"F5LZN\" receiverLocator=\"JN24NE\" senderCallsign=\"II8ICN\" senderLocator=\"JN71DB\" frequency=\"14074782\" flowStartSeconds=\"1773483377\" mode=\"FT8\" isReceiver=\"1\" sNR=\"-25\" />\n" +
                "</receptionReports>";

        XmlMapper xmlMapper = new XmlMapper();
        ReceptionReports reports = xmlMapper.readValue(xml, ReceptionReports.class);

        assertNotNull(reports);
        assertEquals(2, reports.getReceptionReports().size());

        Spot spot1 = reports.getReceptionReports().get(0);
        assertEquals("UR5LP", spot1.getSenderCallsign());
        assertEquals("F5LZN", spot1.getReceiverCallsign());
        assertEquals(-6, spot1.getSnr());
        assertEquals(1773483387L, spot1.getTimestamp());
        assertEquals(14076588L, spot1.getFrequency());
    }

    @Test
    public void testDistanceCalculation() {
        PskReporterService service = new PskReporterService();

        double[] coords1 = service.decodeLocator("JN36ET"); // Approx 46.18, 6.14
        assertNotNull(coords1);
        assertTrue(coords1[0] > 46 && coords1[0] < 47);
        assertTrue(coords1[1] > 6 && coords1[1] < 7);

        double[] coords2 = service.decodeLocator("FN20"); // Approx 40.5, -75.0
        assertNotNull(coords2);
        assertTrue(coords2[0] > 40 && coords2[0] < 41);
        assertTrue(coords2[1] > -76 && coords2[1] < -74);

        double dist = service.calculateDistance(coords1[0], coords1[1], coords2[0], coords2[1]);
        // Distance between Geneva and Philadelphia is roughly 6200km
        assertTrue(dist > 6000 && dist < 6500);
    }
}
