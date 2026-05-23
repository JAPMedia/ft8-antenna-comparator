package ch.hb9hit.pskreporter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Spot {
    @JacksonXmlProperty(isAttribute = true)
    private String senderCallsign;

    @JacksonXmlProperty(isAttribute = true)
    private String receiverCallsign;

    @JacksonXmlProperty(isAttribute = true)
    private String senderLocator;

    @JacksonXmlProperty(isAttribute = true)
    private String receiverLocator;

    @JacksonXmlProperty(isAttribute = true)
    private long frequency;

    @JacksonXmlProperty(isAttribute = true)
    private String mode;

    @JacksonXmlProperty(isAttribute = true, localName = "sNR")
    private int snr;

    @JacksonXmlProperty(isAttribute = true, localName = "flowStartSeconds")
    private long timestamp;

    private String antennaName;
    private double distance;
    private Double senderLatitude;
    private Double senderLongitude;

    // Default constructor for Jackson
    public Spot() {}

    public String getSenderCallsign() { return senderCallsign; }
    public void setSenderCallsign(String senderCallsign) { this.senderCallsign = senderCallsign; }

    public String getReceiverCallsign() { return receiverCallsign; }
    public void setReceiverCallsign(String receiverCallsign) { this.receiverCallsign = receiverCallsign; }

    public String getSenderLocator() { return senderLocator; }
    public void setSenderLocator(String senderLocator) { this.senderLocator = senderLocator; }

    public String getReceiverLocator() { return receiverLocator; }
    public void setReceiverLocator(String receiverLocator) { this.receiverLocator = receiverLocator; }

    public long getFrequency() { return frequency; }
    public void setFrequency(long frequency) { this.frequency = frequency; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public int getSnr() { return snr; }
    public void setSnr(int snr) { this.snr = snr; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getAntennaName() { return antennaName; }
    public void setAntennaName(String antennaName) { this.antennaName = antennaName; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public Double getSenderLatitude() { return senderLatitude; }
    public void setSenderLatitude(Double senderLatitude) { this.senderLatitude = senderLatitude; }

    public Double getSenderLongitude() { return senderLongitude; }
    public void setSenderLongitude(Double senderLongitude) { this.senderLongitude = senderLongitude; }
}
