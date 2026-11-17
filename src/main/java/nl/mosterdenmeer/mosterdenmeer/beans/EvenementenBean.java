package nl.mosterdenmeer.mosterdenmeer.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import nl.mosterdenmeer.mosterdenmeer.model.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@ApplicationScoped
public class EvenementenBean {

    private List<Event> events;

    @PostConstruct
    public void init() {
        events = new ArrayList<>();


        events.add(new Event(
                "natuurlijkhw.png",
                "Kookvideo's Lokaal Proeven",
                "Kijk vooral naar aflevering 6. Lokaal proeven met Mosterd en Meer.",
                Arrays.asList("Online video's"),
                "online",
                "https://www.visithw.nl/nl/ontdek/videos-recepten"
        ));

    }

    public List<Event> getEvents() { return events; }
}

