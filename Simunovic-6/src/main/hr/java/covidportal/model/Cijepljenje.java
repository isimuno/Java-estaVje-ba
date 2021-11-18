package main.hr.java.covidportal.model;

import java.time.LocalDate;

public class Cijepljenje {
    private Long idCjepljenja;
    private Osoba idOsobe;
    private Cijepivo idCjepiva;
    private LocalDate datumCijepljenja;

    public Cijepljenje(Long idCjepljenja, Osoba idOsobe, Cijepivo idCjepiva, LocalDate datumCijepljenja) {
        this.idCjepljenja = idCjepljenja;
        this.idOsobe = idOsobe;
        this.idCjepiva = idCjepiva;
        this.datumCijepljenja = datumCijepljenja;
    }

    public Long getIdCjepljenja() {
        return idCjepljenja;
    }

    public void setIdCjepljenja(Long idCjepljenja) {
        this.idCjepljenja = idCjepljenja;
    }

    public Osoba getIdOsobe() {
        return idOsobe;
    }

    public void setIdOsobe(Osoba idOsobe) {
        this.idOsobe = idOsobe;
    }

    public Cijepivo getIdCjepiva() {
        return idCjepiva;
    }

    public void setIdCjepiva(Cijepivo idCjepiva) {
        this.idCjepiva = idCjepiva;
    }

    public LocalDate getDatumCijepljenja() {
        return datumCijepljenja;
    }

    public void setDatumCijepljenja(LocalDate datumCijepljenja) {
        this.datumCijepljenja = datumCijepljenja;
    }
}
