package main.hr.java.covidportal.model;

import main.hr.java.covidportal.enumeracija.VrijednostiSimptoma;

import java.io.Serializable;

/**
 * Klasa Simptom za kreiranje instance simptoma
 * nasljeđuje ImenovaniEntitet i njenu varijablu naziv
 */
public class Simptom extends ImenovaniEntitet implements Serializable {
    private VrijednostiSimptoma vrijednost;

    public Simptom(Long id, String naziv, VrijednostiSimptoma vrijednost) {
        super(id,naziv);
        this.vrijednost = vrijednost;
    }

    public VrijednostiSimptoma getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(VrijednostiSimptoma vrijednost) {
        this.vrijednost = vrijednost;
    }
}
