package lektion6;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(
        name = "emission_record",
        uniqueConstraints = @UniqueConstraint(columnNames = {"country_id", "year"})
)
public class EmissionRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "co2_kilotons", nullable = false)
    private Double co2Kilotons;

    @Column(name = "source", length = 255)
    private String source;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    public EmissionRecord() {}

    public EmissionRecord(Country country, Integer year,
                          Double co2Kilotons, String source) {
        this.country = country;
        this.year = year;
        this.co2Kilotons = co2Kilotons;
        this.source = source;
    }

    public Long getId() { return id; }
    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public Double getCo2Kilotons() { return co2Kilotons; }
    public void setCo2Kilotons(Double co2Kilotons) { this.co2Kilotons = co2Kilotons; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
