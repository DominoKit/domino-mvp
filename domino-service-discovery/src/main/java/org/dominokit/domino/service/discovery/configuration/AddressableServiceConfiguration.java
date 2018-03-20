package org.dominokit.domino.service.discovery.configuration;

public abstract class AddressableServiceConfiguration extends BaseServiceConfiguration {

    private final String address;

    public AddressableServiceConfiguration(String name, String address) {
        super(name);
        if (!isValid(address))
            throw new InvalidServiceAddressException();
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public static class InvalidServiceAddressException extends RuntimeException {
    }
}
