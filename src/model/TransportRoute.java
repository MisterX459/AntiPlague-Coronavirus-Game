package model;

/**
 * Represents a route between two countries, with a certain transport type.
 */
public class TransportRoute {
    private Country source;
    private Country destination;
    private TransportType transportType;
    private boolean open;  // if true, infection can spread along this route

    public TransportRoute(Country source, Country destination,
                          TransportType transportType, boolean open) {
        this.source = source;
        this.destination = destination;
        this.transportType = transportType;
        this.open = open;
    }

    public Country getSource() {
        return source;
    }

    public Country getDestination() {
        return destination;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}