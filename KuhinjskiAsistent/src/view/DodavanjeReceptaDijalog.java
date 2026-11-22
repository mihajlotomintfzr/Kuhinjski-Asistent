package view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import common.Recept;

public class DodavanjeReceptaDijalog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField nazivField;
    private JTextArea opisArea;
    private JTextField slikaField;
    private JTextField sastojciField;
    private JTextField kategorijaField;
    private JTextField tagoviField;
    private JTextArea pripremaArea;
    private boolean potvrđeno = false;

    public DodavanjeReceptaDijalog(JFrame roditelj) {
        super(roditelj, "Dodaj recept", true);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        nazivField = new JTextField(25);
        opisArea = new JTextArea(2, 25);
        slikaField = new JTextField(25);
        sastojciField = new JTextField(25);
        kategorijaField = new JTextField(25);
        tagoviField = new JTextField(25);
        pripremaArea = new JTextArea(6, 25);

        opisArea.setLineWrap(true);
        opisArea.setWrapStyleWord(true);
        JScrollPane opisScroll = new JScrollPane(opisArea);

        pripremaArea.setLineWrap(true);
        pripremaArea.setWrapStyleWord(true);
        JScrollPane pripremaScroll = new JScrollPane(pripremaArea);

        JPanel forma = new JPanel(new GridBagLayout());

        forma.add(new JLabel("Naziv:"), constraints(0, 0, GridBagConstraints.HORIZONTAL));
        forma.add(nazivField, constraints(1, 0, GridBagConstraints.HORIZONTAL));

        forma.add(new JLabel("Kratak opis jela:"), constraints(0, 1, GridBagConstraints.HORIZONTAL));
        forma.add(opisScroll, constraints(1, 1, GridBagConstraints.BOTH));

        forma.add(new JLabel("Putanja slike:"), constraints(0, 2, GridBagConstraints.HORIZONTAL));
        forma.add(slikaField, constraints(1, 2, GridBagConstraints.HORIZONTAL));

        forma.add(new JLabel("Sastojci:"), constraints(0, 3, GridBagConstraints.HORIZONTAL));
        forma.add(sastojciField, constraints(1, 3, GridBagConstraints.HORIZONTAL));

        forma.add(new JLabel("Priprema:"), constraints(0, 4, GridBagConstraints.BOTH));
        forma.add(pripremaScroll, constraints(1, 4, GridBagConstraints.BOTH));

        forma.add(new JLabel("Kategorija:"), constraints(0, 5, GridBagConstraints.HORIZONTAL));
        forma.add(kategorijaField, constraints(1, 5, GridBagConstraints.HORIZONTAL));

        forma.add(new JLabel("Tagovi:"), constraints(0, 6, GridBagConstraints.HORIZONTAL));
        forma.add(tagoviField, constraints(1, 6, GridBagConstraints.HORIZONTAL));

        add(forma, BorderLayout.CENTER);

        JPanel dugmici = new JPanel();
        JButton okBtn = new JButton("Dodaj");
        JButton cancelBtn = new JButton("Otkaži");
        dugmici.add(okBtn);
        dugmici.add(cancelBtn);
        add(dugmici, BorderLayout.SOUTH);

        okBtn.addActionListener(e -> {
            if (nazivField.getText().trim().isEmpty()
                    || kategorijaField.getText().trim().isEmpty()
                    || tagoviField.getText().trim().isEmpty()
                    || sastojciField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Polja Naziv, Kategorija, Tagovi i Sastojci su obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
            } else {
                potvrđeno = true;
                setVisible(false);
            }
        });

        cancelBtn.addActionListener(e -> {
            potvrđeno = false;
            setVisible(false);
        });

        setPreferredSize(new Dimension(600, 500));
        pack();
        setLocationRelativeTo(getParent());
    }

    private GridBagConstraints constraints(int x, int y, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = fill;
        return gbc;
    }

    public boolean isPotvrđeno() {
        return potvrđeno;
    }

    public Recept getRecept() {
        String naziv = nazivField.getText().trim();
        String opis = opisArea.getText().trim();
        String slika = slikaField.getText().trim();
        List<String> sastojci = Arrays.asList(sastojciField.getText().split("\\s*,\\s*"));
        String priprema = pripremaArea.getText().trim();
        String kategorija = kategorijaField.getText().trim();
        List<String> tagovi = Arrays.asList(tagoviField.getText().split("\\s*,\\s*"));

        return new Recept(naziv, opis, slika, sastojci, kategorija, tagovi, priprema);
    }
}
