package main.hr.java.covidportal.model;

import checkers.igj.quals.I;

import java.time.LocalDate;

public class Cijepivo {
    private Long idCjepiva;
    private String nazivCjepiva;
    private String nazivProizvodaca;
    private Integer brojDoza;
    private Integer vremenskaRazlika;

    public Cijepivo(Long idCjepiva, String nazivCjepiva, String nazivProizvodaca, Integer brojDoza, Integer vremenskaRazlika) {
        this.idCjepiva = idCjepiva;
        this.nazivCjepiva = nazivCjepiva;
        this.nazivProizvodaca = nazivProizvodaca;
        this.brojDoza = brojDoza;
        this.vremenskaRazlika = vremenskaRazlika;
    }

    public Long getIdCjepiva() {
        return idCjepiva;
    }

    public void setIdCjepiva(Long idCjepiva) {
        this.idCjepiva = idCjepiva;
    }

    public String getNazivCjepiva() {
        return nazivCjepiva;
    }

    public void setNazivCjepiva(String nazivCjepiva) {
        this.nazivCjepiva = nazivCjepiva;
    }

    public String getNazivProizvodaca() {
        return nazivProizvodaca;
    }

    public void setNazivProizvodaca(String nazivProizvodaca) {
        this.nazivProizvodaca = nazivProizvodaca;
    }

    public Integer getBrojDoza() {
        return brojDoza;
    }

    public void setBrojDoza(Integer brojDoza) {
        this.brojDoza = brojDoza;
    }

    public Integer getVremenskaRazlika() {
        return vremenskaRazlika;
    }

    public void setVremenskaRazlika(Integer vremenskaRazlika) {
        this.vremenskaRazlika = vremenskaRazlika;
    }
}
