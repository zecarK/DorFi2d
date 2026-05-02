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
import java.util.logging.Level; 
import java.util.logging.Logger;
/**
 *
 * @author Nica
 */
public class LandlordLogInPage extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LandlordLogInPage() {
        this.setIconImage(new ImageIcon("src/images/DorFiLogo.png").getImage());
        this.setResizable(false);
        setTitle("Landlord Login");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // MAIN BACKGROUND
        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(new Color(230, 240, 255));
        
        // LEFT PANEL
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(260, 650));
        leftPanel.setBackground(new Color(225, 235, 250));
        leftPanel.setLayout(null);
        
        JLabel logo = new JLabel("DorFi");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logo.setForeground(new Color(0, 90, 180));
        logo.setBounds(30, 40, 200, 40);
        
        JLabel welcome = new JLabel("Welcome!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcome.setBounds(30, 120, 200, 40);
        
        JLabel desc = new JLabel(
                "<html>"
                + "Manage your dormitories and rooms effectively, "
                + "</html>"
        );
        
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        desc.setForeground(Color.DARK_GRAY);
        desc.setBounds(30, 170, 200, 120);
        
        leftPanel.add(logo);
        leftPanel.add(welcome);
        leftPanel.add(desc);
        
        // CENTER PANEL
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setPreferredSize(new Dimension(740, 650));
        centerPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        centerPanel.setLayout(null);
        
        // BACK BUTTON
        JLabel backBtn = new JLabel("← Back");
        
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        backBtn.setForeground(new Color(0, 90, 180));
        backBtn.setBounds(30, 20, 100, 30);
        
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                UserRoleSelection rs = new UserRoleSelection();
                rs.setVisible(true);
                
                dispose();
            }
        });
        
        centerPanel.add(backBtn);
        
        // LOGIN TITLE
        JLabel title = new JLabel("Sign in your Account");
        
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(new Color(0, 90, 180));
        title.setBounds(240, 100, 350, 50);
        
        centerPanel.add(title);
        
        // USERNAME LABEL
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userLabel.setBounds(240, 190, 100, 25);
        
        centerPanel.add(userLabel);
        
        // USERNAME FIELD
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBounds(240, 220, 320, 45);
        
        usernameField.setBorder(
                new CompoundBorder(
                        new LineBorder(new Color(210, 220, 240), 2, true),
                        new EmptyBorder(5, 15, 5, 15)
                )
        );
        
        centerPanel.add(usernameField);
        
        // PASSWORD LABEL
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        passLabel.setBounds(240, 290, 100, 25);
        
        centerPanel.add(passLabel);
        
        // PASSWORD FIELD
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBounds(240, 320, 320, 45);
        
        passwordField.setBorder(
                new CompoundBorder(
                        new LineBorder(new Color(210, 220, 240), 2, true),
                        new EmptyBorder(5, 15, 5, 15)
                )
        );
        
        centerPanel.add(passwordField);
        
        // LOGIN BUTTON
        JButton loginBtn = new JButton("LOG-IN");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(new Color(0, 90, 180));
        loginBtn.setFocusPainted(false);
        loginBtn.setBounds(240, 430, 320, 50);
        
        loginBtn.addActionListener(e -> loginLandlord());
        
        centerPanel.add(loginBtn);
        
        // CREATE ACCOUNT LABEL
        JLabel createAccount = new JLabel(
                "Create new landlord account"
        );
        
        createAccount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        createAccount.setForeground(new Color(0, 90, 180));
        createAccount.setBounds(310, 500, 200, 20);
        createAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        createAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LandlordAccountCreationPage acc = new LandlordAccountCreationPage();
                
                acc.setVisible(true);
                acc.setLocationRelativeTo(null);
                
                dispose();
            }
        });
        
        centerPanel.add(createAccount);
        
        // ADD PANELS
        background.add(leftPanel, BorderLayout.WEST);
        background.add(centerPanel, BorderLayout.CENTER);
        
        add(background);
        setVisible(true);
    }
     // LOGIN METHOD
    private void loginLandlord() {
        PreparedStatement ps;
        ResultSet rs;
        
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());
        
        String query =
                "SELECT * FROM admin_account " +
                "WHERE a_username = ? AND a_password = ?";
        
        try {
            ps = MyConnection.getConnection().prepareStatement(query);
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                AdminPage ap = new AdminPage(username);
                
                ap.setVisible(true);
                ap.pack();
                ap.setLocationRelativeTo(null);
                
                this.dispose();
            }
            
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "Incorrect username or password"
                );
                
                usernameField.setText("");
                passwordField.setText("");
            }
        }
        
        catch (SQLException ex) {
            Logger.getLogger(AdminLogInPage.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminLogInPage().setVisible(true);
        });
    }
}
 

