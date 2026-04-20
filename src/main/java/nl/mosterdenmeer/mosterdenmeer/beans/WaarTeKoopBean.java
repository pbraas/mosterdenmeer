package nl.mosterdenmeer.mosterdenmeer.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import nl.mosterdenmeer.mosterdenmeer.model.Seller;

import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class WaarTeKoopBean {

    private List<Seller> sellers;

    @PostConstruct
    public void init() {
        sellers = new ArrayList<>();
        sellers.add(new Seller("aspergeboerderijooms.png",  "Asperge Boerderij Ooms",        "Oud-Heinenoordseweg 9",    "3274 LC Heinenoord",      "www.aspergeooms.nl",                "aspergeooms.nl"));
        sellers.add(new Seller("basbakt.png",               "Bas Bakt",                       "Molenstraat 12",           "3268 BN Goudswaard",       "www.basbakt.nl",                    "basbakt.nl"));
        sellers.add(new Seller("benpietsers.png",            "Ben & Pietsers",                 "Dorpsstraat 45",           "3274 BG Heinenoord",       "www.benpietsers.nl",                "benpietsers.nl"));
        sellers.add(new Seller("BoerderijwinkelJansen.png", "Boerderijwinkel Jansen",         "Zeedijk 3",                "3271 LB Mijnsheerenland",  "www.boerderijwinkeljansen.nl",      "boerderijwinkeljansen.nl"));
        sellers.add(new Seller("boerderijwinkelzevenbergen.png", "Boerderijwinkel Zevenbergen","Zevenbergseweg 1",        "3295 LD 's-Gravendeel",    "www.boerderijwinkelzevenbergen.nl", "boerderijwinkelzevenbergen.nl"));
        sellers.add(new Seller("cavent.png",                "Cavent",                         "Benedenstraat 8",          "3261 AJ Oud-Beijerland",   "www.cavent.nl",                     "cavent.nl"));
        sellers.add(new Seller("debuytenhof.png",           "De Buytenhof",                   "Buytenhofweg 2",           "3281 LX Numansdorp",       "www.debuytenhof.nl",                "debuytenhof.nl"));
        sellers.add(new Seller("dehoogheerlijkheid.png",    "De Hoog Heerlijkheid",           "Heerlijkheidsweg 10",      "3273 XA Westmaas",         "www.dehoogheerlijkheid.nl",         "dehoogheerlijkheid.nl"));
        sellers.add(new Seller("dekleineduiker.png",        "De Kleine Duiker",               "Boomgaardstraat 4",        "3262 AK Oud-Beijerland",   "www.dekleineduiker.nl",             "dekleineduiker.nl"));
        sellers.add(new Seller("foodnerd.png",              "Food Nerd",                      "Markt 16",                 "3261 DR Oud-Beijerland",   "www.foodnerd.nl",                   "foodnerd.nl"));
        sellers.add(new Seller("hoekschlekkers.png",        "Hoeksch Lekkers",                "Dorpsstraat 22",           "3267 AE Goudswaard",       "www.hoekschlekkers.nl",             "hoekschlekkers.nl"));
        sellers.add(new Seller("hoekswaardschelandschap.png","Hoeksche Waard Landschap",      "Sportlaan 6",              "3261 ME Oud-Beijerland",   "www.hoekswaardschelandschap.nl",    "hoekswaardschelandschap.nl"));
        sellers.add(new Seller("HoeveAckerdijk.png",        "Hoeve Ackerdijk",                "Ackerdijk 7",              "3271 RL Mijnsheerenland",  "www.hoeveackerdijk.nl",             "hoeveackerdijk.nl"));
        sellers.add(new Seller("museumhoekschewaard.png",   "Museum Hoeksche Waard",          "Binnenlandse Baan 4",      "3261 CW Oud-Beijerland",   "www.museumhoekschewaard.nl",        "museumhoekschewaard.nl"));
        sellers.add(new Seller("natuurlijkgezond.png",      "Natuurlijk Gezond",              "Kerkweg 18",               "3286 LB Klaaswaal",        "www.natuurlijkgezond.nl",           "natuurlijkgezond.nl"));
        sellers.add(new Seller("natuurmonumententiengemeenten.png", "Staatsbosbeheer Tiengemeten", "Tiengemeten 1",       "3281 NA Numansdorp",       "www.tiengemeten.nl",                "tiengemeten.nl"));
        sellers.add(new Seller("rijsoordsemolen.png",       "Rijsoordse Molen",               "Molenstraat 1",            "2918 AE Rijsoord",         "www.rijsoordsemolen.nl",            "rijsoordsemolen.nl"));
        sellers.add(new Seller("schieveen.png",             "Schieveen",                      "Rotterdamseweg 80",        "2641 KA Pijnacker",        "www.schieveen.nl",                  "schieveen.nl"));
        sellers.add(new Seller("strijkerkorenmolenaars.png","Strijker Korenmolenaars",        "Molenweg 5",               "3271 NB Mijnsheerenland",  "www.strijkerkorenmolenaars.nl",     "strijkerkorenmolenaars.nl"));
    }

    public List<Seller> getSellers() { return sellers; }
}

