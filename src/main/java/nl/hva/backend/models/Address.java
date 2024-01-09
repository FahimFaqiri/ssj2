package nl.hva.backend.models;

public class Address {

    private String country;
    private String state;
    private String city;
    private String postcode;
    private String street;
    private Integer houseNumber;

    public Address(String country, String state, String city, String postcode, String street) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s", country, state, city, postcode, street);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }
}
