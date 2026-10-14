package ma.projet.beans;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mariages")
public class Mariage {

    @EmbeddedId
    private MariagePK pk;

    @ManyToOne
    @MapsId("hommeId")
    @JoinColumn(name = "homme_id")
    private Homme homme;

    @ManyToOne
    @MapsId("femmeId")
    @JoinColumn(name = "femme_id")
    private Femme femme;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_fin")
    private Date dateFin;

    @Column(name = "nbr_enfants")
    private int nbrEnfants;

    public Mariage() {
    }

    public Mariage(Homme homme, Femme femme, Date dateDebut, Date dateFin, int nbrEnfants) {
        this.homme = homme;
        this.femme = femme;
        this.dateFin = dateFin;
        this.nbrEnfants = nbrEnfants;
        this.pk = new MariagePK(homme.getId(), femme.getId(), dateDebut);
    }

    public MariagePK getPk() {
        return pk;
    }

    public void setPk(MariagePK pk) {
        this.pk = pk;
    }

    public Homme getHomme() {
        return homme;
    }

    public void setHomme(Homme homme) {
        this.homme = homme;
        if (this.pk == null) {
            this.pk = new MariagePK();
        }
        if (homme != null) {
            this.pk.setHommeId(homme.getId());
        }
    }

    public Femme getFemme() {
        return femme;
    }

    public void setFemme(Femme femme) {
        this.femme = femme;
        if (this.pk == null) {
            this.pk = new MariagePK();
        }
        if (femme != null) {
            this.pk.setFemmeId(femme.getId());
        }
    }

    public Date getDateDebut() {
        return pk != null ? pk.getDateDebut() : null;
    }

    public void setDateDebut(Date dateDebut) {
        if (this.pk == null) {
            this.pk = new MariagePK();
        }
        this.pk.setDateDebut(dateDebut);
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getNbrEnfants() {
        return nbrEnfants;
    }

    public void setNbrEnfants(int nbrEnfants) {
        this.nbrEnfants = nbrEnfants;
    }
}
