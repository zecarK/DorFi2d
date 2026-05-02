/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Nica
 */
public class StudentBrowsingPage extends JFrame {

    Connection con;
    private JPanel roomContainer;
    private JTextField searchField;
    private JComboBox<String> municipalityDropdown;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private List<Object[]> roomList = new ArrayList<>();

    public StudentBrowsingPage() {
        // Database Connection
        con = MyConnection.getConnection();
        
        // Window Setup
        this.setIconImage(new ImageIcon("src/images/DorFiLogo.png").getImage());
        this.setResizable(false);
        setTitle("DorFi - Student Home");
        setSize(1250, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Use BorderLayout for the main Frame structure
        setLayout(new BorderLayout());

        Color primary = new Color(0, 90, 180);
        Color lightBlue = new Color(230, 240, 255);

        // ==========================================
        // 1. TOP NAVBAR (Stays fixed at the top)
        // ==========================================
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setPreferredSize(new Dimension(1250, 70));
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JLabel logo = new JLabel("DorFi");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(primary);
        logo.setBorder(new EmptyBorder(0, 25, 0, 0));

        JPanel navRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        navRight.setOpaque(false);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(primary);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new UserRoleSelection().setVisible(true);
            dispose();
        });

        navRight.add(backBtn);
        navBar.add(logo, BorderLayout.WEST);
        navBar.add(navRight, BorderLayout.EAST);
        add(navBar, BorderLayout.NORTH);

        // ==========================================
        // 2. SCROLLABLE CONTENT AREA
        // ==========================================
        // This panel holds Hero + Filters + Rooms in a vertical stack
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(lightBlue);

        // --- HERO SECTION ---
        JPanel heroPanel = new JPanel(null); // Absolute layout for title/subtitle
        heroPanel.setBackground(primary);
        heroPanel.setPreferredSize(new Dimension(1250, 180));
        heroPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JLabel title = new JLabel("Find Your Perfect Dorm");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setBounds(60, 35, 500, 40);

        JLabel subtitle = new JLabel("Browse available rooms near your school.");
        subtitle.setForeground(new Color(220, 230, 250));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        subtitle.setBounds(60, 80, 650, 30);

        heroPanel.add(title);
        heroPanel.add(subtitle);
        scrollContent.add(heroPanel);

        // --- FILTER SECTION ---
        JPanel filterWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filterWrapper.setOpaque(false);
        filterWrapper.setBorder(new EmptyBorder(25, 0, 15, 0));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        filterPanel.setPreferredSize(new Dimension(1150, 80)); // Widened to prevent wrapping issues
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));

        // Initialize Filter Components
        searchField = createStyledTextField(200);
        municipalityDropdown = new JComboBox<>();
        municipalityDropdown.setPreferredSize(new Dimension(160, 35));
        minPriceField = createStyledTextField(100);
        maxPriceField = createStyledTextField(100);

        JButton searchBtn = createStyledButton("Search", primary, Color.WHITE);
        JButton resetBtn = createStyledButton("Reset", new Color(240, 240, 240), Color.DARK_GRAY);

        // Add to Filter Panel
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Municipality:"));
        filterPanel.add(municipalityDropdown);
        filterPanel.add(new JLabel("Min ₱:"));
        filterPanel.add(minPriceField);
        filterPanel.add(new JLabel("Max ₱:"));
        filterPanel.add(maxPriceField);
        filterPanel.add(searchBtn);
        filterPanel.add(resetBtn);

        filterWrapper.add(filterPanel);
        scrollContent.add(filterWrapper);

        // --- ROOMS SECTION ---
        roomContainer = new JPanel(new GridLayout(0, 3, 25, 25)); // Dynamic rows, 3 columns
        roomContainer.setBackground(lightBlue);
        roomContainer.setBorder(new EmptyBorder(10, 50, 50, 50));
        scrollContent.add(roomContainer);

        // Wrap everything in a JScrollPane
        JScrollPane mainScroll = new JScrollPane(scrollContent);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(20); // Faster scrolling
        add(mainScroll, BorderLayout.CENTER);

        // ==========================================
        // 3. ACTION LISTENERS & DATA LOAD
        // ==========================================
        searchBtn.addActionListener(e -> applyFilters());
        resetBtn.addActionListener(e -> resetFilters());

        loadMunicipalities();
        loadRooms();
    }
    // Helper to create consistent text fields
    private JTextField createStyledTextField(int width) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(width, 35));
        tf.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return tf;
    }

    // Helper to create consistent buttons
    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(110, 35));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void resetFilters() {
        searchField.setText("");
        minPriceField.setText("");
        maxPriceField.setText("");
        municipalityDropdown.setSelectedIndex(0);
        displayRooms(roomList);
    }

    // =========================
    // LOAD MUNICIPALITIES
    // =========================
    private void loadMunicipalities() {

        try {

            municipalityDropdown.addItem("All");

            String query =
                    "SELECT DISTINCT municipality FROM location_table";

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                municipalityDropdown.addItem(
                        rs.getString("municipality")
                );
            }

        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // LOAD ROOMS
    // =========================
    private void loadRooms() {

        roomList.clear();

        try {

            String query =
                    "SELECT r.room_id, r.room_number, d.dorm_name, " +
                    "r.rent, l.brgy, l.municipality " +
                    "FROM room_table r " +
                    "JOIN dorm_building d ON r.dorm_id = d.dorm_id " +
                    "JOIN location_table l ON d.location_id = l.location_id";

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                roomList.add(new Object[] {
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("dorm_name"),
                        rs.getDouble("rent"),
                        rs.getString("brgy"),
                        rs.getString("municipality")
                });
            }

            displayRooms(roomList);

        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // DISPLAY ROOMS
    // =========================
    private void displayRooms(List<Object[]> list) {

        roomContainer.removeAll();

        for(Object[] room : list) {

            roomContainer.add(
                    createRoomCard(
                            (int) room[0],
                            (String) room[1],
                            (String) room[2],
                            (double) room[3],
                            (String) room[4],
                            (String) room[5]
                    )
            );
        }

        roomContainer.revalidate();
        roomContainer.repaint();
    }

    // =========================
    // FILTERS
    // =========================
    private void applyFilters() {

        String keyword = searchField.getText().toLowerCase();

        String municipality =
                municipalityDropdown.getSelectedItem().toString();

        double min = 0;
        double max = Double.MAX_VALUE;

        try {

            if(!minPriceField.getText().isEmpty()) {
                min = Double.parseDouble(minPriceField.getText());
            }

            if(!maxPriceField.getText().isEmpty()) {
                max = Double.parseDouble(maxPriceField.getText());
            }

        }

        catch(Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid price input"
            );

            return;
        }

        List<Object[]> filtered = new ArrayList<>();

        for(Object[] room : roomList) {

            String roomNo = ((String) room[1]).toLowerCase();
            String dorm = ((String) room[2]).toLowerCase();

            double rent = (double) room[3];

            String muni = (String) room[5];

            boolean matchKeyword =
                    roomNo.contains(keyword) ||
                    dorm.contains(keyword);

            boolean matchMunicipality =
                    municipality.equals("All") ||
                    muni.equals(municipality);

            boolean matchPrice =
                    rent >= min && rent <= max;

            if(matchKeyword && matchMunicipality && matchPrice) {

                filtered.add(room);
            }
        }

        displayRooms(filtered);
    }

    // =========================
    // ROOM CARD
    // =========================
    private JPanel createRoomCard(
            int roomId,
            String roomNo,
            String dormName,
            double rent,
            String brgy,
            String municipality
    ) {

        Color primary = new Color(0, 90, 180);

        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(320, 320));
        card.setBackground(Color.WHITE);

        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220,220,220), 1, true),
                new EmptyBorder(0,0,10,0)
        ));

        // IMAGE
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(320, 180));

        ImageIcon icon = getFirstImage(roomId);

        if(icon != null) {

            Image img = icon.getImage().getScaledInstance(
                    320,
                    180,
                    Image.SCALE_SMOOTH
            );

            imageLabel.setIcon(new ImageIcon(img));
        }

        else {

            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(220,230,250));
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setText("No Image");
        }

        // INFO PANEL
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(15,15,15,15));

        JLabel dormLabel = new JLabel(dormName);
        dormLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel roomLabel = new JLabel("Room " + roomNo);
        roomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel locationLabel =
                new JLabel(brgy + ", " + municipality);

        locationLabel.setForeground(Color.GRAY);

        JLabel priceLabel =
                new JLabel("₱ " + rent + " / month");

        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(primary);

        JButton viewBtn = new JButton("View Details");

        viewBtn.setBackground(primary);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFocusPainted(false);
        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        viewBtn.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Room ID: " + roomId
            );
        });

        infoPanel.add(dormLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(roomLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(locationLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(viewBtn);

        card.add(imageLabel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    // =========================
    // GET ROOM IMAGE
    // =========================
    private ImageIcon getFirstImage(int roomId) {

        try {

            String query =
                    "SELECT room_photo FROM room_image " +
                    "WHERE room_id = ? LIMIT 1";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setInt(1, roomId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                return new ImageIcon(
                        rs.getString("room_photo")
                );
            }
        }

        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new StudentHomePage().setVisible(true);
        });
    }
}
