package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Arrays;

import common.Recept;
import common.ReceptMenadzer;

public class GlavniProzor extends JFrame {

    private static final long serialVersionUID = 1L;
    private ReceptMenadzer menadzer;
    private DefaultListModel<Recept> model;
    private JList<Recept> lista;
    private JTextField poljePretrage;
    private JLabel statusLabela;
    private JButton dodajBtn, obrisiBtn, izmeniBtn, omiljeniBtn, detaljiBtn;
    private JButton sortNazivBtn, sortKatBtn, sortTagBtn, sortOmiljeniBtn, resetBtn;

    public GlavniProzor() {
        menadzer = new ReceptMenadzer();
        model = new DefaultListModel<>();

        initComponents(); 
        createEvents();
        osvezi();
    }

    private void initComponents() {
        setTitle("Kuhinjski Asistent");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(10, 10));

        JMenuBar meniBar = new JMenuBar();
        JMenu meniFajl = new JMenu("Fajl");

        JMenuItem oNama = new JMenuItem("O nama");
        oNama.addActionListener(e -> JOptionPane.showMessageDialog(
            this,
            "Aplikaciju razvili:\n\nMatija Stojnić SI 32/23\nMihajlo Tomin SI 20/23",
            "O nama",
            JOptionPane.INFORMATION_MESSAGE
        ));

        JMenuItem izlaz = new JMenuItem("Izlaz");
        izlaz.addActionListener(e -> dispose());

        meniFajl.add(oNama);
        meniFajl.addSeparator();
        meniFajl.add(izlaz);
        meniBar.add(meniFajl);
        setJMenuBar(meniBar);

        lista = new JList<>(model);
        lista.setBackground(Color.WHITE);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lista.setFixedCellHeight(48);

        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Recept r) {
                    String prikaz = r.getNaziv();
                    String kat = (r.getKategorija() != null && !r.getKategorija().isEmpty()) ? r.getKategorija() : "Bez kategorije";
                    String tagovi = (r.getTagovi() != null && !r.getTagovi().isEmpty()) ? String.join(", ", r.getTagovi()) : "Bez tagova";

                    label.setText(prikaz + " | " + kat + " | " + tagovi);

                    if (r.isOmiljen()) {
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                        label.setForeground(new Color(255, 140, 0));
                    } else {
                        label.setFont(label.getFont().deriveFont(Font.PLAIN));
                        label.setForeground(Color.BLACK);
                    }
                }
                return label;
            }
        });

        JScrollPane scrollLista = new JScrollPane(lista);

        JPanel gornjiPanel = new JPanel(new BorderLayout(5, 5));
        poljePretrage = new JTextField();
        resetBtn = new JButton("Resetuj");
        gornjiPanel.add(new JLabel("🔎 Pretraga:"), BorderLayout.WEST);
        gornjiPanel.add(poljePretrage, BorderLayout.CENTER);
        gornjiPanel.add(resetBtn, BorderLayout.EAST);

        JPanel desniPanel = new JPanel();
        desniPanel.setLayout(new BoxLayout(desniPanel, BoxLayout.Y_AXIS));

        dodajBtn = new JButton("➕ Dodaj");
        obrisiBtn = new JButton("🗑 Obrisi");
        izmeniBtn = new JButton("✏ Izmeni");
        omiljeniBtn = new JButton("❤️ Omiljeni");
        detaljiBtn = new JButton("🔎 Detalji");
        sortNazivBtn = new JButton("Sort: Naziv");
        sortKatBtn = new JButton("Sort: Kategorija");
        sortTagBtn = new JButton("Sort: Tag");
        sortOmiljeniBtn = new JButton("Sort: Omiljeni");

        for (JButton btn : Arrays.asList(
                dodajBtn, obrisiBtn, izmeniBtn, omiljeniBtn, detaljiBtn,
                sortNazivBtn, sortKatBtn, sortTagBtn, sortOmiljeniBtn)) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(200, 30));
            desniPanel.add(Box.createVerticalStrut(5));
            desniPanel.add(btn);
        }

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollLista, desniPanel);
        splitPane.setDividerLocation(600);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);

        JPanel donjiPanel = new JPanel(new BorderLayout());
        statusLabela = new JLabel("Ukupno recepata: 0 | Omiljeni: 0");
        donjiPanel.add(statusLabela, BorderLayout.WEST);

        getContentPane().add(gornjiPanel, BorderLayout.NORTH);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(donjiPanel, BorderLayout.SOUTH);
    }

    private void createEvents() {
        dodajBtn.addActionListener(e -> dodajRecept());
        obrisiBtn.addActionListener(e -> obrisiRecept());
        izmeniBtn.addActionListener(e -> izmeniRecept());
        omiljeniBtn.addActionListener(e -> oznaciOmiljeni());
        detaljiBtn.addActionListener(e -> prikaziDetalje());
        sortNazivBtn.addActionListener(e -> { menadzer.sortirajPoNazivu(); osvezi(); });
        sortKatBtn.addActionListener(e -> { menadzer.sortirajPoKategoriji(); osvezi(); });
        sortTagBtn.addActionListener(e -> { menadzer.sortirajPoTagu(); osvezi(); });
        sortOmiljeniBtn.addActionListener(e -> { menadzer.sortirajPoOmiljenosti(); osvezi(); }); 
        resetBtn.addActionListener(e -> { poljePretrage.setText(""); osvezi(); });

        poljePretrage.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { osvezi(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { osvezi(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { osvezi(); }
        });
    }

    private void dodajRecept() {
        DodavanjeReceptaDijalog dijalog = new DodavanjeReceptaDijalog(this);
        dijalog.setVisible(true);
        if (dijalog.isPotvrđeno()) {
            Recept novi = dijalog.getRecept();
            if (novi.getNaziv() != null && !novi.getNaziv().isEmpty()) {
                menadzer.dodajRecept(novi);
                osvezi();
            } else {
                JOptionPane.showMessageDialog(this, "Recept mora imati naziv.", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void obrisiRecept() {
        Recept izabrani = lista.getSelectedValue();
        if (izabrani != null) {
            menadzer.obrisiRecept(izabrani);
            osvezi();
        }
    }

    private void izmeniRecept() {
        Recept izabrani = lista.getSelectedValue();
        if (izabrani == null) {
            JOptionPane.showMessageDialog(this, "Niste izabrali recept za izmenu.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        IzmenaReceptaDijalog dijalog = new IzmenaReceptaDijalog(this, izabrani, menadzer);

        dijalog.setVisible(true);

        if (dijalog.isPotvrđeno()) {
            osvezi();
        }
    }

    private void oznaciOmiljeni() {
        Recept r = lista.getSelectedValue();
        if (r != null) {
            r.setOmiljen(!r.isOmiljen());
            menadzer.sacuvajPromene(); 
            osvezi();
        }
    }

    private void prikaziDetalje() {
        Recept r = lista.getSelectedValue();
        if (r == null) {
            JOptionPane.showMessageDialog(this, "Niste izabrali nijedan recept.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DetaljiReceptaDijalog dijalog = new DetaljiReceptaDijalog(this, r);
        dijalog.setVisible(true);
    }

    private void osvezi() {
        model.clear();
        String filter = poljePretrage.getText().trim().toLowerCase();
        List<Recept> filtrirani = filter.isEmpty() ? menadzer.sviRecepti() : menadzer.filtriraj(filter);
        for (Recept r : filtrirani) model.addElement(r);

        statusLabela.setText("Ukupno recepata: " + menadzer.sviRecepti().size() +
                             " | Omiljeni: " + menadzer.omiljeniRecepti().size());
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new GlavniProzor().setVisible(true));
    }
}
