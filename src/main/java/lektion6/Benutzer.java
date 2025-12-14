package lektion6;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Benutzer {

    private String name;
    private String passwordHash;

    public Benutzer() {
    }

    public Benutzer(String name, String plainPassword) {
        this.name = name;
        this.passwordHash = hash(plainPassword);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPlainPassword(String plainPassword) {
        this.passwordHash = hash(plainPassword);
    }

    public boolean matches(String name, String plainPassword) {
        return Objects.equals(this.name, name)
                && Objects.equals(this.passwordHash, hash(plainPassword));
    }

    private static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 nicht verfügbar", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Benutzer other)) return false;
        return Objects.equals(name, other.name)
                && Objects.equals(passwordHash, other.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, passwordHash);
    }
}
