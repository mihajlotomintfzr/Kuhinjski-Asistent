package common;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReceptMenadzer {
    private List<Recept> recepti = new ArrayList<>();
    private final String FAJL = "src/recepti.txt";

    public ReceptMenadzer() {
        ucitajTxt();
    }

    public void dodajRecept(Recept r) {
        recepti.add(r);
        sacuvajTxt();
    }

    public void obrisiRecept(Recept r) {
        recepti.remove(r);
        sacuvajTxt();
    }

    public List<Recept> sviRecepti() {
        return recepti;
    }

    public List<Recept> omiljeniRecepti() {
        return recepti.stream()
                .filter(Recept::isOmiljen)
                .collect(Collectors.toList());
    }

    
    public List<Recept> filtriraj(String filter) {
        String f = filter.toLowerCase();

        return recepti.stream()
                .filter(r -> {
                    boolean uNazivu = r.getNaziv() != null && r.getNaziv().toLowerCase().contains(f);
                    boolean uKategoriji = r.getKategorija() != null && r.getKategorija().toLowerCase().contains(f);
                    boolean uTagovima = r.getTagovi() != null && r.getTagovi().stream()
                            .anyMatch(tag -> tag != null && tag.toLowerCase().contains(f));
                    return uNazivu || uKategoriji || uTagovima;
                })
                .collect(Collectors.toList());
    }

    public void sortirajPoNazivu() {
        recepti.sort(Comparator.comparing(Recept::getNaziv));
    }

    public void sortirajPoKategoriji() {
        recepti.sort(Comparator.comparing(r -> r.getKategorija() != null ? r.getKategorija() : ""));
    }

    public void sortirajPoTagu() {
        recepti.sort((r1, r2) -> {
            String t1 = r1.getTagovi() != null && !r1.getTagovi().isEmpty()
                    ? r1.getTagovi().get(0).toLowerCase()
                    : "";
            String t2 = r2.getTagovi() != null && !r2.getTagovi().isEmpty()
                    ? r2.getTagovi().get(0).toLowerCase()
                    : "";
            return t1.compareTo(t2);
        });
    }

    public void sortirajPoOmiljenosti() {
        recepti.sort((r1, r2) -> Boolean.compare(!r1.isOmiljen(), !r2.isOmiljen()));
    }

    public void azurirajRecept(Recept stari, Recept novi) {
        int index = recepti.indexOf(stari);
        if (index != -1) {
            recepti.set(index, novi);
            sacuvajTxt();
        }
    }

    public void sacuvajPromene() {
        sacuvajTxt();
    }

    private void sacuvajTxt() {
        try (PrintWriter out = new PrintWriter(FAJL)) {
            for (Recept r : recepti) {
                out.println("Naziv: " + r.getNaziv());
                out.println("Opis: " + (r.getOpis() != null ? r.getOpis() : ""));
                out.println("Kategorija: " + (r.getKategorija() != null ? r.getKategorija() : ""));
                out.println("Tagovi: " + (r.getTagovi() != null ? String.join(", ", r.getTagovi()) : ""));
                out.println("Sastojci: " + (r.getSastojci() != null ? String.join(", ", r.getSastojci()) : ""));
                out.println("Slika: " + (r.getSlikaPutanja() != null ? r.getSlikaPutanja() : ""));
                out.println("Priprema:");
                if (r.getPriprema() != null && !r.getPriprema().isEmpty()) {
                    for (String linija : r.getPriprema().split("\n")) {
                        out.println(linija);
                    }
                }
                out.println("Omiljen: " + r.isOmiljen());
                out.println("---");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ucitajTxt() {
        recepti.clear();
        try (Scanner sc = new Scanner(new File(FAJL))) {
            Recept r = null;
            StringBuilder pripremaBuilder = new StringBuilder();
            boolean uPripremi = false;

            while (sc.hasNextLine()) {
                String linija = sc.nextLine().trim();

                if (linija.equals("---")) {
                    if (r != null && r.getNaziv() != null && !r.getNaziv().isBlank()) {
                        r.setPriprema(pripremaBuilder.toString().trim());
                        recepti.add(r);
                    }
                    r = null;
                    pripremaBuilder.setLength(0);
                    uPripremi = false;
                    continue;
                }

                if (r == null) {
                    r = new Recept();
                }

                if (linija.startsWith("Naziv: ")) {
                    r.setNaziv(linija.substring(7));
                } else if (linija.startsWith("Opis: ")) {
                    r.setOpis(linija.substring(6));
                } else if (linija.startsWith("Kategorija: ")) {
                    r.setKategorija(linija.substring(12));
                } else if (linija.startsWith("Tagovi: ")) {
                    String tagStr = linija.substring(8).trim();
                    List<String> tagovi = tagStr.isEmpty() ? new ArrayList<>() : Arrays.asList(tagStr.split(",\\s*"));
                    r.setTagovi(tagovi);
                } else if (linija.startsWith("Sastojci: ")) {
                    String sastojciStr = linija.substring(11).trim();
                    List<String> sastojci = sastojciStr.isEmpty() ? new ArrayList<>() : Arrays.asList(sastojciStr.split(",\\s*"));
                    r.setSastojci(sastojci);
                } else if (linija.startsWith("Slika: ")) {
                    r.setSlikaPutanja(linija.substring(7));
                } else if (linija.equals("Priprema:")) {
                    uPripremi = true;
                } else if (linija.startsWith("Omiljen: ")) {
                    r.setOmiljen(Boolean.parseBoolean(linija.substring(9).trim()));
                    uPripremi = false;
                } else {
                    if (uPripremi) {
                        pripremaBuilder.append(linija).append("\n");
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Fajl nije pronađen, pravim novi...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
