package ph.id.printerhouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Servizi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Il campo nome non può essere vuoto")
    private String name;

    @NotBlank(message = "Il campo descrizione non può essere vuoto")
    @Column(length=2500)
    private String descrizione;

    @NotBlank(message = "Il campo foto non può essere vuoto")
    private String urlFoto;

    @NotNull
    private String linkDettaglio;

    public String getLinkDettaglio() {
        return linkDettaglio;
    }

    public void setLinkDettaglio(String linkDettaglio) {
        this.linkDettaglio = linkDettaglio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}
