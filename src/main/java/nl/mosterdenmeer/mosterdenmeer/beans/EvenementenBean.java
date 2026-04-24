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
                "krimpenerwaard.png",
                "Oogst- en Streekmarkt, 23 mei 2026",
                "Proef de Krimpenerwaard",
                Arrays.asList("Lokale streekproducten", "Ambachtelijke markt", "Gratis toegang"),
                "Streekmuseum Krimpenerwaard, IJsseldijk 312, 2922BM Krimpen a/d IJssel",
                "https://www.indekrimpenerwaard.nl/proef-de-krimpenerwaard/proef-krimpen"
        ));

        events.add(new Event(
                "natuurlijkhw.png",
                "Kookvideo's Lokaal Proeven",
                "Kijk vooral naar aflevering 6. Lokaal proeven met Mosterd en Meer.",
                Arrays.asList("Online video's"),
                "online",
                "https://www.visithw.nl/nl/ontdek/videos-recepten"
        ));

        events.add(new Event(
                "rijsoordsemolen.png",
                "Oogstmarkt Rijsoordse Molen",
                "Een sfeervolle oogstmarkt bij de historische Rijsoordse Molen. Kom genieten van lokale streekproducten, ambachtelijk eten en drinken.",
                Arrays.asList("Lokale streekproducten", "Ambachtelijke markt", "Gratis toegang"),
                "Molenstraat 1, 2918 AE Rijsoord",
                "https://www.rijsoordsemolen.nl"
        ));

        events.add(new Event(
                "hoekswaardschelandschap.png",
                "Hoeksche Waard Streekmarkt",
                "De jaarlijkse streekmarkt in het hart van de Hoeksche Waard. Mosterd &amp; Meer is aanwezig met een ruim assortiment mosterdsoorten, jams en chutneys.",
                Arrays.asList("Mosterd proeven", "Vers uit de regio", "Kookdemonstraties"),
                "Sportlaan 6, 3261 ME Oud-Beijerland",
                "https://www.hwl.nl"
        ));

        events.add(new Event(
                "museumhoekschewaard.png",
                "Erfgoeddag bij Museum Hoeksche Waard",
                "Tijdens de Erfgoeddag is Mosterd &amp; Meer aanwezig in het museum met een stand over de ambachtelijke mosterdproductie.",
                Arrays.asList("Historische mosterdproductie", "Rondleidingen", "Proeverij"),
                "Binnenlandse Baan 4, 3261 CW Oud-Beijerland",
                "https://www.museumhw.nl/"
        ));

        events.add(new Event(
                "natuurmonumententiengemeenten.png",
                "Tiengemeten Plukdag",
                "Samen met de beheerders van Tiengemeten organiseren wij een plukdag waarbij bezoekers kunnen leren over het oogsten van kruiden en bloemen.",
                Arrays.asList("Kruiden plukken", "Rondleiding eiland", "Natuurbeleving"),
                "Tiengemeten 1, 3281 NA Numansdorp",
                "https://www.natuurmonumenten.nl/natuurgebieden/tiengemeten"
        ));

    }

    public List<Event> getEvents() {
        return events;
    }
}

