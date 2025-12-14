package lektion6;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("loginController")
@ViewScoped
public class LoginController implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String password;

    private List<Benutzer> benutzerListe;

    public LoginController() {
        this.benutzerListe = new ArrayList<>();
        this.benutzerListe.add(new Benutzer("Admin", "123"));
        this.benutzerListe.add(new Benutzer("User", "456"));
    }

    public void validateLogin(FacesContext context,
                              UIComponent component,
                              Object value) throws ValidatorException {

        String enteredPassword = (String) value;

        UIInput nameInput = (UIInput) component.findComponent("name");
        String username = null;
        if (nameInput != null) {
            Object submitted = nameInput.getSubmittedValue();
            if (submitted != null) {
                username = submitted.toString();
            } else {
                Object val = nameInput.getValue();
                if (val != null) {
                    username = val.toString();
                }
            }
        }

        this.name = username;

        for (Benutzer b : benutzerListe) {
            if (b.matches(username, enteredPassword)) {
                return;
            }
        }

        throw new ValidatorException(
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login falsch!", null)
        );
    }

    public String login() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        boolean isAdmin = "Admin".equals(this.name);

        ec.getSessionMap().put("loggedIn", Boolean.TRUE);
        ec.getSessionMap().put("isAdmin", isAdmin);
        ec.getSessionMap().put("username", this.name);

        if (isAdmin) {
            return "emission_admin?faces-redirect=true";
        } else {
            return "co2?faces-redirect=true";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
