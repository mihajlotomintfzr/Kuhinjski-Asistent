package view;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import common.Recept;
import common.ReceptMenadzer;

public class IzmenaReceptaDijalog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField nazivField;
    private JTextField kategorijaField;
    private JTextField tagField;
    private JTextField sastojciField;
    private JTextArea opisArea;
    private JTextArea pripremaArea;
    private JButton btnSacuvaj;
    private JButton btnOtkazi;
    private boolean potvrdjeno = false;

    private Recept original;
    private ReceptMenadzer menadzer;

    public IzmenaReceptaDijalog(JFrame roditelj, Recept recept, ReceptMenadzer menadzer) {
        super(roditelj, "Izmena recepta", true);
        this.original = recept;
        this.menadzer = menadzer;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(roditelj);

        JLabel lblNaziv = new JLabel("Naziv:");
        JLabel lblKategorija = new JLabel("Kategorija:");
        JLabel lblTag = new JLabel("Tagovi :");
        JLabel lblSastojci = new JLabel("Sastojci :");
        JLabel lblOpis = new JLabel("Opis:");
        JLabel lblPriprema = new JLabel("Priprema:");

        nazivField = new JTextField(safe(recept.getNaziv()));
        kategorijaField = new JTextField(safe(recept.getKategorija()));
        tagField = new JTextField(String.join(", ", safeList(recept.getTagovi())));
        sastojciField = new JTextField(String.join(", ", safeList(recept.getSastojci())));

        opisArea = new JTextArea(safe(recept.getOpis()));
        opisArea.setLineWrap(true);
        opisArea.setWrapStyleWord(true);
        JScrollPane scrollOpis = new JScrollPane(opisArea);

        pripremaArea = new JTextArea(safe(recept.getPriprema()));
        pripremaArea.setLineWrap(true);
        pripremaArea.setWrapStyleWord(true);
        JScrollPane scrollPriprema = new JScrollPane(pripremaArea);

        btnSacuvaj = new JButton("Sačuvaj");
        btnOtkazi = new JButton("Otkaži");

        btnSacuvaj.addActionListener(e -> {
            String naziv = nazivField.getText().trim();
            String kategorija = kategorijaField.getText().trim();
            String tagovi = tagField.getText().trim();

            if (naziv.isEmpty() || kategorija.isEmpty() || tagovi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Polja Naziv, Kategorija i Tagovi su obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Recept izmenjen = new Recept();
            izmenjen.setNaziv(naziv);
            izmenjen.setKategorija(kategorija);
            izmenjen.setOpis(opisArea.getText().trim());
            izmenjen.setPriprema(pripremaArea.getText().trim());
            izmenjen.setSastojci(Arrays.asList(sastojciField.getText().trim().split("\\s*,\\s*")));
            izmenjen.setTagovi(Arrays.asList(tagovi.split("\\s*,\\s*")));
            izmenjen.setSlikaPutanja(original.getSlikaPutanja()); 
            izmenjen.setOmiljen(original.isOmiljen()); 
            menadzer.azurirajRecept(original, izmenjen); 

            potvrdjeno = true;
            dispose();
        });

        btnOtkazi.addActionListener(e -> dispose());

        JPanel contentPanel = new JPanel();
        GroupLayout gl = new GroupLayout(contentPanel);
        contentPanel.setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(
            gl.createParallelGroup(Alignment.LEADING)
                .addGroup(gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup(Alignment.TRAILING)
                        .addComponent(lblNaziv)
                        .addComponent(lblKategorija)
                        .addComponent(lblTag)
                        .addComponent(lblSastojci)
                        .addComponent(lblOpis)
                        .addComponent(lblPriprema))
                    .addGroup(gl.createParallelGroup(Alignment.LEADING)
                        .addComponent(nazivField)
                        .addComponent(kategorijaField)
                        .addComponent(tagField)
                        .addComponent(sastojciField)
                        .addComponent(scrollOpis)
                        .addComponent(scrollPriprema)))
                .addGroup(Alignment.TRAILING,
                    gl.createSequentialGroup()
                        .addComponent(btnOtkazi)
                        .addComponent(btnSacuvaj))
        );

        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(Alignment.BASELINE)
                    .addComponent(lblNaziv)
                    .addComponent(nazivField))
                .addGroup(gl.createParallelGroup(Alignment.BASELINE)
                    .addComponent(lblKategorija)
                    .addComponent(kategorijaField))
                .addGroup(gl.createParallelGroup(Alignment.BASELINE)
                    .addComponent(lblTag)
                    .addComponent(tagField))
                .addGroup(gl.createParallelGroup(Alignment.BASELINE)
                    .addComponent(lblSastojci)
                    .addComponent(sastojciField))
                .addGroup(gl.createParallelGroup(Alignment.LEADING)
                    .addComponent(lblOpis)
                    .addComponent(scrollOpis, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                .addGroup(gl.createParallelGroup(Alignment.LEADING)
                    .addComponent(lblPriprema)
                    .addComponent(scrollPriprema, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(gl.createParallelGroup(Alignment.BASELINE)
                    .addComponent(btnOtkazi)
                    .addComponent(btnSacuvaj))
        );

        getContentPane().add(contentPanel);
    }

    public boolean isPotvrđeno() {
        return potvrdjeno;
    }

    private String safe(String text) {
        return text != null ? text : "";
    }

    private List<String> safeList(List<String> list) {
        return list != null ? list : Collections.emptyList();
    }
}
