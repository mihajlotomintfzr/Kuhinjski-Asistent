package common;

import java.util.List;

public class Recept {
    private String naziv;
    private String opis;
    private String slikaPutanja;
    private List<String> sastojci;
    private String kategorija;
    private List<String> tagovi;
    private String priprema;
    private boolean omiljen;

    public Recept() {}

    public Recept(String naziv, String opis, String slikaPutanja, List<String> sastojci,
                  String kategorija, List<String> tagovi, String priprema) {
        this.naziv = naziv;
        this.opis = opis;
        this.slikaPutanja = slikaPutanja;
        this.sastojci = sastojci;
        this.kategorija = kategorija;
        this.tagovi = tagovi;
        this.priprema = priprema;
        this.omiljen = false;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getSlikaPutanja() {
        return slikaPutanja;
    }

    public void setSlikaPutanja(String slikaPutanja) {
        this.slikaPutanja = slikaPutanja;
    }

    public String getSlika() {
        return slikaPutanja;
    }

    public List<String> getSastojci() {
        return sastojci;
    }

    public void setSastojci(List<String> sastojci) {
        this.sastojci = sastojci;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public List<String> getTagovi() {
        return tagovi;
    }

    public void setTagovi(List<String> tagovi) {
        this.tagovi = tagovi;
    }

    public String getPriprema() {
        return priprema;
    }

    public void setPriprema(String priprema) {
        this.priprema = priprema;
    }

    public boolean isOmiljen() {
        return omiljen;
    }

    public void setOmiljen(boolean omiljen) {
        this.omiljen = omiljen;
    }

    @Override
    public String toString() {
        String sviTagovi = (tagovi != null && !tagovi.isEmpty()) ? String.join(", ", tagovi) : "Bez tagova";
        String prikazKategorije = (kategorija != null && !kategorija.isEmpty()) ? kategorija : "Bez kategorije";
        String oznaka = omiljen ? "" : "";
        return naziv + " " + oznaka + " | " + prikazKategorije + " | " + sviTagovi;

    }


  
}
