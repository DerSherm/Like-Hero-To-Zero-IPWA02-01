package lektion6;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("emissionAdmin")
@ViewScoped
public class EmissionAdminBean implements Serializable {

    @Inject
    private EmissionDAO emissionDAO;

    private List<EmissionRecord> emissions;
    private List<Country> countries;

    private Long newCountryId;
    private EmissionRecord newEmission = new EmissionRecord();

    @PostConstruct
    public void init() {
        countries = emissionDAO.findAllCountries();
        emissions = emissionDAO.findAllEmissions();
    }

    public void reload() {
        emissions = emissionDAO.findAllEmissions();
        countries = emissionDAO.findAllCountries();
    }

    public void addNew() {
        if (newCountryId == null ||
                newEmission.getYear() == null ||
                newEmission.getCo2Kilotons() == null) {
            return;
        }
        Country c = countries.stream()
                .filter(x -> x.getId().equals(newCountryId))
                .findFirst().orElse(null);
        if (c == null) return;

        newEmission.setCountry(c);
        if (newEmission.getSource() == null || newEmission.getSource().isBlank()) {
            newEmission.setSource("Manuell erfasst");
        }
        emissionDAO.saveEmission(newEmission);

        newEmission = new EmissionRecord();
        newCountryId = null;
        reload();
    }

    public void delete(EmissionRecord record) {
        if (record.getId() != null) {
            emissionDAO.deleteEmission(record.getId());
            reload();
        }
    }

    public List<EmissionRecord> getEmissions() { return emissions; }
    public List<Country> getCountries() { return countries; }

    public Long getNewCountryId() { return newCountryId; }
    public void setNewCountryId(Long newCountryId) { this.newCountryId = newCountryId; }

    public EmissionRecord getNewEmission() { return newEmission; }
    public void setNewEmission(EmissionRecord newEmission) { this.newEmission = newEmission; }
}
