package nl.mosterdenmeer.mosterdenmeer.model;

import java.util.List;

public class Event {

    private String logo;
    private String title;
    private String description;
    private List<String> details;
    private String address;
    private String url;

    public Event(String logo, String title, String description, List<String> details, String address, String url) {
        this.logo = logo;
        this.title = title;
        this.description = description;
        this.details = details;
        this.address = address;
        this.url = url;
    }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}

