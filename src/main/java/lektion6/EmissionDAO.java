package lektion6;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Named
@ApplicationScoped
public class EmissionDAO implements Serializable {

    private EntityManagerFactory emf;

    @PostConstruct
    public void init() {
        emf = Persistence.createEntityManagerFactory("heroToZeroPU");
        initDemoDataIfEmpty();
    }

    private EntityManager em() {
        return emf.createEntityManager();
    }

    public void initDemoDataIfEmpty() {
        EntityManager em = em();
        try {
            Long count = em.createQuery("SELECT COUNT(c) FROM Country c", Long.class)
                    .getSingleResult();
            if (count != 0L) {
                return;
            }

            EntityTransaction tx = em.getTransaction();
            tx.begin();

            Country de  = new Country("DEU", "Deutschland");
            Country fr  = new Country("FRA", "Frankreich");
            Country us  = new Country("USA", "Vereinigte Staaten");
            Country cn  = new Country("CHN", "China");
            Country jp  = new Country("JPN", "Japan");
            Country in  = new Country("IND", "Indien");
            Country gb  = new Country("GBR", "Vereinigtes Königreich");
            Country br  = new Country("BRA", "Brasilien");
            Country ca  = new Country("CAN", "Kanada");
            Country au  = new Country("AUS", "Australien");

            em.persist(de); em.persist(fr); em.persist(us); em.persist(cn); em.persist(jp);
            em.persist(in); em.persist(gb); em.persist(br); em.persist(ca); em.persist(au);

            int[] years = {2020, 2021, 2022, 2023, 2024};
            double[] deVals = {644000, 650000, 620000, 610000, 600000};
            double[] frVals = {305000, 300000, 295000, 290000, 285000};
            double[] usVals = {4900000, 5000000, 5100000, 5050000, 4980000};
            double[] cnVals = {9020000, 9040000, 9060000, 9080000, 9100000};
            double[] jpVals = {1100000, 1080000, 1070000, 1050000, 1030000};
            double[] inVals = {2600000, 2700000, 2800000, 2900000, 3000000};
            double[] gbVals = {350000, 340000, 330000, 320000, 310000};
            double[] brVals = {480000, 490000, 500000, 510000, 520000};
            double[] caVals = {580000, 590000, 600000, 595000, 590000};
            double[] auVals = {420000, 430000, 440000, 445000, 450000};

            Country[] countries = {de, fr, us, cn, jp, in, gb, br, ca, au};
            double[][] values = {
                    deVals, frVals, usVals, cnVals, jpVals,
                    inVals, gbVals, brVals, caVals, auVals
            };

            for (int c = 0; c < countries.length; c++) {
                for (int y = 0; y < years.length; y++) {
                    em.persist(new EmissionRecord(
                            countries[c],
                            years[y],
                            values[c][y],
                            "Demo"
                    ));
                }
            }

            tx.commit();
        } finally {
            em.close();
        }
    }

    public List<Country> findAllCountries() {
        EntityManager em = em();
        try {
            return em.createQuery("SELECT c FROM Country c ORDER BY c.name", Country.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<EmissionRecord> findAllEmissions() {
        EntityManager em = em();
        try {
            return em.createQuery(
                    "SELECT e FROM EmissionRecord e " +
                            "ORDER BY e.country.name, e.year",
                    EmissionRecord.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public List<EmissionRecord> findByCountry(Long countryId) {
        EntityManager em = em();
        try {
            return em.createQuery(
                            "SELECT e FROM EmissionRecord e " +
                                    "WHERE e.country.id = :cid " +
                                    "ORDER BY e.year",
                            EmissionRecord.class
                    ).setParameter("cid", countryId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public EmissionRecord findLatestForCountry(Long countryId) {
        EntityManager em = em();
        try {
            return em.createQuery(
                            "SELECT e FROM EmissionRecord e " +
                                    "WHERE e.country.id = :cid " +
                                    "ORDER BY e.year DESC",
                            EmissionRecord.class
                    ).setParameter("cid", countryId)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    public void saveEmission(EmissionRecord record) {
        EntityManager em = em();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (record.getId() == null) {
                em.persist(record);
            } else {
                em.merge(record);
            }
            tx.commit();
        } finally {
            em.close();
        }
    }

    public void saveAll(List<EmissionRecord> list) {
        EntityManager em = em();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (EmissionRecord e : list) {
                em.merge(e);
            }
            tx.commit();
        } finally {
            em.close();
        }
    }

    public void deleteEmission(Long id) {
        EntityManager em = em();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            EmissionRecord managed = em.find(EmissionRecord.class, id);
            if (managed != null) {
                em.remove(managed);
            }
            tx.commit();
        } finally {
            em.close();
        }
    }
}
