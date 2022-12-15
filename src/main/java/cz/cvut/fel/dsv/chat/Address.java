package cz.cvut.fel.dsv.chat;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class Address implements Serializable {
    private final String name;
    private final String hostname;
    private final int port;

    private final int id;

    public Address(String name, String hostname, int port) throws InvalidAddressException{

        if(Objects.isNull(name)) throw  new InvalidAddressException("None or blank address name");

        this.name = name;
        this.hostname = hostname;
        this.port = port;
        this.id = generateId(hostname, port);
    }


    private int generateId(String hostname, int port) throws InvalidAddressException{
        int id = 0;
        int bigPrime = 	15485863;
        if(port < 0 || port > 65536) throw new InvalidAddressException("Invalid port number");
        if(Objects.isNull(hostname)) throw  new InvalidAddressException("None or blank hostname");

        id += hostname.hashCode() * 10000000;
        id += port * 1000000;

        id %= bigPrime;

        return id;
    }

    @Override
    public String toString() {
        return "Address{" +
                "name='" + name + '\'' +
                ", hostname='" + hostname + '\'' +
                ", port=" + port +
                ", id=" + id +
                '}';
    }
}
