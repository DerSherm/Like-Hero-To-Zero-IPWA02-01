package lektion6;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "country")
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iso_code", length = 3, nullable = false, unique = true)
    private String isoCode;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmissionRecord> emissions = new ArrayList<>();

    public Country() {}

    public Country(String isoCode, String name) {
        this.isoCode = isoCode;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getIsoCode() { return isoCode; }
    public void setIsoCode(String isoCode) { this.isoCode = isoCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<EmissionRecord> getEmissions() { return emissions; }
    public void setEmissions(List<EmissionRecord> emissions) { this.emissions = emissions; }

    @Override
    public String toString() {
        return isoCode + " - " + name;
    }
}
