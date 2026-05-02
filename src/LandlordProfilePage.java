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
public class LandlordProfilePage extends JFrame {

    private String username;
    private Connection con;
    private int currentPage = 1;
    private final int roomsPerPage = 3;
    private List<Object[]> roomList = new ArrayList<>();

    // UI Styling Constants
    private final Color primaryBlue = new Color(0, 90, 180);
    private final Color bgLight = new Color(245, 248, 255);
    private final Color sidebarColor = new Color(25, 35, 50);

    // Core Components
    private JPanel parentPanel;
    private JPanel roomManagementPanel;
    private JLabel landlordNameLabel;

    public LandlordProfilePage(String username) {
        this.username = username;
        this.con = MyConnection.getConnection(); //
        
        setupWindow();
        initModernComponents();
        loadLandlordData();
        refreshRoomDisplay();
    }

    private void setupWindow() {
        setTitle("DorFi Landlord Dashboard");
        setSize(1200, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void initModernComponents() {
        // SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 800));
        sidebar.setBackground(sidebarColor);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel brand = new JLabel(" DorFi");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 28));
        brand.setForeground(Color.WHITE);
        brand.setBorder(new EmptyBorder(30, 20, 10, 0));
        sidebar.add(brand);

        landlordNameLabel = new JLabel("Welcome, Landlord");
        landlordNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        landlordNameLabel.setForeground(new Color(150, 160, 180));
        landlordNameLabel.setBorder(new EmptyBorder(0, 25, 40, 0));
        sidebar.add(landlordNameLabel);

        // Navigation
        sidebar.add(createNavButton("Manage Rooms", true));
        sidebar.add(createNavButton("Profile Settings", false));

        sidebar.add(Box.createVerticalGlue());

        // Bottom Actions
        JButton logout = new JButton("Sign Out");
        logout.setForeground(new Color(255, 100, 100));
        logout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logout.setContentAreaFilled(false);
        logout.setBorder(new EmptyBorder(20, 0, 40, 0));
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);
        logout.addActionListener(e -> {
            new LandlordLogInPage().setVisible(true); //
            dispose();
        });
        sidebar.add(logout);

        add(sidebar, BorderLayout.WEST);

        // MAIN CONTENT
        parentPanel = new JPanel(new CardLayout());
        roomManagementPanel = new JPanel(new BorderLayout());
        roomManagementPanel.setBackground(bgLight);
        
        parentPanel.add(roomManagementPanel, "rooms");
        add(parentPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(active ? Color.WHITE : new Color(170, 180, 200));
        btn.setBackground(sidebarColor);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 25, 0, 0));
        return btn;
    }

    private void loadLandlordData() {
        try {
            String query = "SELECT lname FROM landlord_acc WHERE l_username = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                landlordNameLabel.setText("Welcome, " + rs.getString("lname"));
            }
        } catch (SQLException e) {
            System.out.println("Error loading landlord: " + e.getMessage());
        }
    }

    private void refreshRoomDisplay() {
        roomList.clear();
        try {
            // Updated to reflect your Tbl_Employee/Room structure
            String query = "SELECT * FROM rooms WHERE landlord_user = ?"; 
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                roomList.add(new Object[]{
                    rs.getInt("room_id"), rs.getString("room_name"), 
                    rs.getString("description"), rs.getDouble("price"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error fetching rooms: " + e.getMessage());
        }
        showPage(1);
    }

    private void showPage(int page) {
        roomManagementPanel.removeAll();
        currentPage = page;

        JPanel grid = new JPanel(new GridLayout(2, 2, 25, 25));
        grid.setBackground(bgLight);
        grid.setBorder(new EmptyBorder(30, 30, 30, 30));

        // First Card: Add Room Action
        grid.add(createAddActionCard());

        // Fill remaining slots with rooms
        int start = (page - 1) * roomsPerPage;
        int end = Math.min(start + roomsPerPage, roomList.size());

        for (int i = start; i < end; i++) {
            Object[] room = roomList.get(i);
            grid.add(createRoomCard(room));
        }

        roomManagementPanel.add(grid, BorderLayout.CENTER);
        
        // Pagination Controls
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(bgLight);
        JButton prev = new JButton("Previous");
        JButton next = new JButton("Next");
        prev.setEnabled(page > 1);
        next.setEnabled(end < roomList.size());
        
        prev.addActionListener(e -> showPage(currentPage - 1));
        next.addActionListener(e -> showPage(currentPage + 1));
        
        footer.add(prev);
        footer.add(next);
        roomManagementPanel.add(footer, BorderLayout.SOUTH);

        roomManagementPanel.revalidate();
        roomManagementPanel.repaint();
    }

    private JPanel createAddActionCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new DashedBorder(primaryBlue, 2));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel icon = new JLabel("+", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI", Font.BOLD, 48));
        icon.setForeground(primaryBlue);
        
        JLabel text = new JLabel("Add New Room", SwingConstants.CENTER);
        text.setFont(new Font("Segoe UI", Font.BOLD, 16));
        text.setBorder(new EmptyBorder(0, 0, 40, 0));

        card.add(icon, BorderLayout.CENTER);
        card.add(text, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new AddRoom(username).setVisible(true); //
                dispose();
            }
        });
        return card;
    }

    private JPanel createRoomCard(Object[] roomData) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));

        JPanel info = new JPanel(new GridLayout(3, 1));
        info.setBackground(Color.WHITE);
        info.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel((String) roomData[1]);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JLabel price = new JLabel("₱" + roomData[3] + " / month");
        price.setForeground(primaryBlue);
        price.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel status = new JLabel((String) roomData[4]);
        status.setForeground(roomData[4].equals("Available") ? new Color(46, 204, 113) : Color.GRAY);

        info.add(title);
        info.add(price);
        info.add(status);

        card.add(info, BorderLayout.CENTER);
        return card;
    }

    // Custom Border for the Add Card
    class DashedBorder extends AbstractBorder {
        private Color color;
        private int thickness;

        public DashedBorder(Color color, int thickness) {
            this.color = color;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(color);
            float[] dash = {10.0f};
            g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            g2d.drawRect(x + 5, y + 5, width - 10, height - 10);
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        // Run with a sample username for testing
        SwingUtilities.invokeLater(() -> new LandlordProfilePage("admin").setVisible(true));
    }
}