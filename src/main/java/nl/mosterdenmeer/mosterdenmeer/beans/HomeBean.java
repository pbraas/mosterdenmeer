package nl.mosterdenmeer.mosterdenmeer.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.primefaces.model.ResponsiveOption;

import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class HomeBean {

    private List<String> images;
    private List<ResponsiveOption> responsiveOptions;

    @PostConstruct
    public void init() {
        images = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            images.add(i + ".png");
        }

        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("1400px", 5, 1));
        responsiveOptions.add(new ResponsiveOption("1024px", 4, 1));
        responsiveOptions.add(new ResponsiveOption("768px", 3, 1));
        responsiveOptions.add(new ResponsiveOption("560px", 1, 1));
    }

    public List<String> getImages() { return images; }
    public List<ResponsiveOption> getResponsiveOptions() { return responsiveOptions; }
}

