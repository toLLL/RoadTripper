package com.usa.dev.drawerlayout.API;

/**
 * Author: Philippe Volpe
 * Date: 16/01/2016
 * Purpose: Define a Client
 */
public class Client {
    String facebookId;
    String publicId;
    String firstName;
    String lastName;
    String name;

    public Client() {
    }

    public String getFacebookId() { return this.facebookId;}
    public String getPublicId() {return this.publicId;}
    public String getFirstName() { return this.firstName;}
    public String getLastName() { return this.lastName;}
    public String getName() { return this.name;}
}
