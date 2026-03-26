package nl.mosterdenmeer.mosterdenmeer.model;

public class Seller {

    private String logo;
    private String name;
    private String street;
    private String city;
    private String website;
    private String websiteDisplay;

    public Seller(String logo, String name, String street, String city, String website) {
        this.logo = logo;
        this.name = name;
        this.street = street;
        this.city = city;
        this.website = website;
        this.websiteDisplay = website;
    }

    public Seller(String logo, String name, String street, String city, String website, String websiteDisplay) {
        this(logo, name, street, city, website);
        this.websiteDisplay = websiteDisplay;
    }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getWebsiteDisplay() { return websiteDisplay; }
    public void setWebsiteDisplay(String websiteDisplay) { this.websiteDisplay = websiteDisplay; }
}

