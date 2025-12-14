package lektion6;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named
@RequestScoped
public class SecurityBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public void checkAdminAccess() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        Boolean loggedIn = (Boolean) ec.getSessionMap().get("loggedIn");
        Boolean isAdmin  = (Boolean) ec.getSessionMap().get("isAdmin");

        if (!Boolean.TRUE.equals(loggedIn) || !Boolean.TRUE.equals(isAdmin)) {
            ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
        }
    }

    public void checkUserAccess() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        Boolean loggedIn = (Boolean) ec.getSessionMap().get("loggedIn");

        if (!Boolean.TRUE.equals(loggedIn)) {
            ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
        }
    }

    public void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
    }
}
