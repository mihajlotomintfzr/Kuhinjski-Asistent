package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.io.File;

import common.Recept;

public class DetaljiReceptaDijalog extends JDialog {

    private static final long serialVersionUID = 1L;

    public DetaljiReceptaDijalog(JFrame roditelj, Recept recept) {
        super(roditelj, "Detalji recepta", true);
        initComponents(recept);
    }

    private void initComponents(Recept recept) {
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new GridBagLayout());

      
        infoPanel.add(new JLabel("Naziv:"), constraints(0, 0));
        infoPanel.add(new JLabel(safeString(recept.getNaziv())), constraints(1, 0));

      
        infoPanel.add(new JLabel("Opis:"), constraints(0, 1));
        infoPanel.add(new JLabel(safeString(recept.getOpis())), constraints(1, 1));

       
        infoPanel.add(new JLabel("Sastojci:"), constraints(0, 2));
        infoPanel.add(new JLabel(joinListSafely(recept.getSastojci())), constraints(1, 2));

       
        infoPanel.add(new JLabel("Kategorija:"), constraints(0, 3));
        infoPanel.add(new JLabel(safeString(recept.getKategorija())), constraints(1, 3));

     
        infoPanel.add(new JLabel("Tagovi:"), constraints(0, 4));
        infoPanel.add(new JLabel(joinListSafely(recept.getTagovi())), constraints(1, 4));

       
        infoPanel.add(new JLabel("Priprema:"), constraints(0, 5));
        JTextArea pripremaArea = new JTextArea(safeString(recept.getPriprema()));
        pripremaArea.setLineWrap(true);
        pripremaArea.setWrapStyleWord(true);
        pripremaArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(pripremaArea);
        scrollPane.setPreferredSize(new Dimension(250, 100));
        infoPanel.add(scrollPane, constraints(1, 5));

        
        JPanel slikaPanel = new JPanel(new BorderLayout());
        slikaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        JLabel slikaLabel = new JLabel();
        slikaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        slikaLabel.setVerticalAlignment(SwingConstants.CENTER);

        try {
            String putanja = recept.getSlikaPutanja();
            ImageIcon ikonica = null;

            
            URL url = getClass().getClassLoader().getResource(putanja);

            if (url != null) {
                ikonica = new ImageIcon(url);
            } else {
              
                File slikaFajl = new File(putanja);
                if (slikaFajl.exists()) {
                    ikonica = new ImageIcon(slikaFajl.getAbsolutePath());
                }
            }

            if (ikonica != null) {
                Image slika = ikonica.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                slikaLabel.setIcon(new ImageIcon(slika));
            } else {
                slikaLabel.setText("Slika nije dostupna");
                slikaLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } catch (Exception e) {
            slikaLabel.setText("Greška pri učitavanju slike");
            slikaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        slikaPanel.add(slikaLabel, BorderLayout.CENTER);

  
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, infoPanel, slikaPanel);
        splitPane.setDividerLocation(400);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);
        add(splitPane, BorderLayout.CENTER);

     
        JButton zatvoriBtn = new JButton("Zatvori");
        zatvoriBtn.addActionListener(e -> dispose());
        JPanel dugmePanel = new JPanel();
        dugmePanel.add(zatvoriBtn);

        add(dugmePanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(700, 500));
        pack();
        setLocationRelativeTo(getParent());
    }

  
    private GridBagConstraints constraints(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = x;
        gbc.gridy = y;
        if (y == 5 && x == 1) {
            gbc.fill = GridBagConstraints.BOTH;
        }
        return gbc;
    }

   
    private String safeString(String s) {
        return s != null ? s : "";
    }

  
    private String joinListSafely(java.util.List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return String.join(", ", list);
    }
}
