package lektion6;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("emissions")
@ViewScoped
public class EmissionView implements Serializable {

    @Inject
    private EmissionDAO emissionDAO;

    private List<Country> countries;
    private Long selectedCountryId;

    private EmissionRecord latest;
    private List<EmissionRecord> allForCountry;

    @PostConstruct
    public void init() {
        emissionDAO.initDemoDataIfEmpty();
        countries = emissionDAO.findAllCountries();
    }

    public void loadEmissions() {
        if (selectedCountryId == null) {
            latest = null;
            allForCountry = null;
            return;
        }

        latest = emissionDAO.findLatestForCountry(selectedCountryId);
        allForCountry = emissionDAO.findByCountry(selectedCountryId);
    }

    public List<Country> getCountries() { return countries; }

    public Long getSelectedCountryId() { return selectedCountryId; }
    public void setSelectedCountryId(Long selectedCountryId) { this.selectedCountryId = selectedCountryId; }

    public EmissionRecord getLatest() { return latest; }
    public List<EmissionRecord> getAllForCountry() { return allForCountry; }
}
