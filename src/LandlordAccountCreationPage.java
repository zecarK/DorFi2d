/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.*; 
import javax.swing.border.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.io.File; 
import java.io.FileInputStream; 
import java.sql.Connection; 
import java.sql.PreparedStatement; 
import java.sql.SQLException; 
import java.util.Calendar; 
import java.util.Date; 
import java.util.logging.Level; 
import java.util.logging.Logger;
/**
 *
 * @author Nica
 */
public class LandlordAccountCreationPage extends JFrame{
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField contactNoField;
    private JTextField usernameField;
    private JPasswordField passField;
    private JPasswordField confirmPassField;
    private JComboBox<String> genderField;
    private JButton uploadBtn;
    private JButton createAccountBtn;
    private JLabel photoLabel;
    private com.toedter.calendar.JDateChooser birthDateField;
    private File selectedFile = null;
    
    public LandlordAccountCreationPage() {
        this.setIconImage(new ImageIcon("src/images/DorFiLogo.png").getImage());
        this.setResizable(false);
        setTitle("Create Landlord Account");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // MAIN BACKGROUND
        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(new Color(230, 240, 255));
        
        // LEFT PANEL
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(260, 700));
        leftPanel.setBackground(new Color(225, 235, 250));
        leftPanel.setLayout(null);
        
        JLabel logo = new JLabel("DorFi");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logo.setForeground(new Color(0, 90, 180));
        logo.setBounds(30, 40, 200, 40);
        
        JLabel welcome = new JLabel("Create Account");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcome.setBounds(30, 120, 220, 40);
        
        JLabel desc = new JLabel(
                "<html>"
                + "Register as a landlord and start "
                + "managing dormitories easily."
                + "</html>"
        );
        
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        desc.setForeground(Color.DARK_GRAY);
        desc.setBounds(30, 180, 200, 120);
        
        leftPanel.add(logo);
        leftPanel.add(welcome);
        leftPanel.add(desc);
        
        // CENTER PANEL
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
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
                    LandlordLogInPage llog = new LandlordLogInPage();
                    llog.setVisible(true);
                    
                    dispose();
                }
        });
            
        centerPanel.add(backBtn);    
        
        // TITLE
        JLabel title = new JLabel("Create Landlord Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(0, 90, 180));
        title.setBounds(240, 40, 420, 40);
        
        centerPanel.add(title);
        
        // PROFILE PHOTO PANEL
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(null);
        photoPanel.setBackground(new Color(245, 248, 255));
        photoPanel.setBorder(new LineBorder(new Color(210, 220, 240),2,true));
        photoPanel.setBounds(580, 130, 220, 250);
        
        photoLabel = new JLabel();
        photoLabel.setBounds(35, 20, 150, 150);
        photoLabel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
        
        uploadBtn = new JButton("Upload Photo");
        uploadBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        uploadBtn.setForeground(Color.WHITE);
        uploadBtn.setBackground(new Color(0, 90, 180));
        uploadBtn.setFocusPainted(false);
        uploadBtn.setBounds(35, 190, 150, 40);
        
        uploadBtn.addActionListener(e -> uploadImage());
        
        photoPanel.add(photoLabel);
        photoPanel.add(uploadBtn);
        
        centerPanel.add(photoPanel);
        
        // FORM LABELS & FIELDS
        int labelX = 120;
        int fieldX = 280;
        int width = 260;
        int height = 40;
        
        // FIRST NAME
        JLabel firstNameLabel = new JLabel("First Name");
        firstNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        firstNameLabel.setBounds(labelX, 130, 120, 30);
        
        centerPanel.add(firstNameLabel);
        
        firstNameField = createTextField();
        firstNameField.setBounds(fieldX, 130, width, height);
        
        centerPanel.add(firstNameField);
        
        // LAST NAME
        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lastNameLabel.setBounds(labelX, 190, 120, 30);
        
        centerPanel.add(lastNameLabel);
        
        lastNameField = createTextField();
        lastNameField.setBounds(fieldX, 190, width, height);
        
        centerPanel.add(lastNameField);
        
        // BIRTHDATE
        JLabel birthLabel = new JLabel("Birth Date");
        birthLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        birthLabel.setBounds(labelX, 250, 120, 30);
        
        centerPanel.add(birthLabel);
        
        birthDateField = new com.toedter.calendar.JDateChooser();
        birthDateField.setBounds(fieldX, 250, 120, height);
        
        centerPanel.add(birthDateField);
        
        // GENDER
        genderField = new JComboBox<>(new String[] {"Select Gender","Male","Female","Others"});
        genderField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genderField.setBounds(420, 250, 120, height);
        
        centerPanel.add(genderField);
        
        // CONTACT NUMBER
        JLabel contactLabel = new JLabel("Contact Number");
        contactLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        contactLabel.setBounds(labelX, 310, 140, 30);
        
        centerPanel.add(contactLabel);
        
        contactNoField = createTextField();
        contactNoField.setBounds(fieldX, 310, width, height);
        
        centerPanel.add(contactNoField);
        
        // USERNAME
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        usernameLabel.setBounds(labelX, 370, 120, 30);
        
        centerPanel.add(usernameLabel);
        
        usernameField = createTextField();
        usernameField.setBounds(fieldX, 370, width, height);
        
        centerPanel.add(usernameField);
        
         // PASSWORD
         JLabel passLabel = new JLabel("Password");
         passLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
         passLabel.setBounds(labelX, 430, 120, 30);
         
         centerPanel.add(passLabel);
         
         passField = createPasswordField();
         passField.setBounds(fieldX, 430, width, height);
                 
         centerPanel.add(passField);
         
         // CONFIRM PASSWORD
         JLabel confirmLabel = new JLabel("Confirm Password");
         confirmLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
         confirmLabel.setBounds(labelX, 490, 140, 30);
         
         centerPanel.add(confirmLabel);
         
         confirmPassField = createPasswordField();
         confirmPassField.setBounds(fieldX, 490, width, height);
         
         centerPanel.add(confirmPassField);
         
         // CREATE ACCOUNT BUTTON
         createAccountBtn = new JButton("CREATE ACCOUNT");
         createAccountBtn.setFont(new Font("Segoe UI", Font.BOLD, 17));
         createAccountBtn.setForeground(Color.WHITE);
         createAccountBtn.setBackground(new Color(0, 90, 180));
         createAccountBtn.setFocusPainted(false);
         createAccountBtn.setBounds(280,580,260,50);
         
         createAccountBtn.addActionListener(e -> {
            try {
                createAccount();
            } catch (SQLException ex) {
                ex.printStackTrace();
                
                JOptionPane.showMessageDialog(this,"Database error: " + ex.getMessage());
            }
        });
         
         centerPanel.add(createAccountBtn);
         
         // ADD PANELS
         background.add(leftPanel, BorderLayout.WEST);
         background.add(centerPanel, BorderLayout.CENTER);
         
         add(background);
                
    }
    
    // CUSTOM TEXT FIELD
    private JTextField createTextField() {
        JTextField field = new JTextField();
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(new CompoundBorder(new LineBorder(new Color(210, 220, 240),2,true),new EmptyBorder(5, 15, 5, 15)));
        
        return field;
    }
    
    // CUSTOM PASSWORD FIELD
    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(new CompoundBorder(new LineBorder(new Color(210, 220, 240),2,true),new EmptyBorder(5, 15, 5, 15)));
        
        return field; 
    }
    
    // UPLOAD IMAGE
    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        
        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            
            ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
            
            Image img = icon.getImage().getScaledInstance(150,150,Image.SCALE_SMOOTH);
            
            photoLabel.setIcon(new ImageIcon(img));
        };
    }
    
    private void createAccount() throws SQLException {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String sex = genderField.getSelectedItem().toString();
        String contact = contactNoField.getText().trim();
        String username = usernameField.getText().trim();
        String password = String.valueOf(passField.getPassword()).trim();
        String confirmPass = String.valueOf(confirmPassField.getPassword()).trim();
        Date birthDate = birthDateField.getDate();

        if (firstName.equals("")) {
            JOptionPane.showMessageDialog(this,"First Name is required");
        }
        else if (lastName.equals("")) {
            JOptionPane.showMessageDialog(this,"Last Name is required");
        }
        else if (birthDate == null) {
            JOptionPane.showMessageDialog(this,"Birth Date is required");
        }
        else if (sex.equals("Select Gender")) {
            JOptionPane.showMessageDialog(this,"Please select gender");
        }
        else if (contact.equals("")) {
            JOptionPane.showMessageDialog(this,"Contact number is required");
        }
        else if (!contact.matches("\\d+")) {
            JOptionPane.showMessageDialog(this,"Contact must contain numbers only");
        }
        else if (username.equals("")) {
            JOptionPane.showMessageDialog(this,"Username is required");
        }
        else if (password.equals("")) {
            JOptionPane.showMessageDialog(this,"Password is required");
        }
        else if (!password.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this,"Passwords do not match");
        }
        else if (selectedFile == null) {
            JOptionPane.showMessageDialog(this,"Profile picture is required");
        }
        else {
            try {
                Connection con = MyConnection.getConnection();
                con.setAutoCommit(false);
                
                Calendar birthCal = Calendar.getInstance();
                birthCal.setTime(birthDate);
                birthCal.setTime(birthDate);
                
                Calendar today = Calendar.getInstance();
                
                int age = today.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
                
                if (today.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }
                
                java.sql.Date sqlDate = new java.sql.Date(birthDate.getTime());
                
                String query1 =
                        "INSERT INTO landlord_profile "
                        + "(username, first_name, "
                        + "last_name, age, sex, "
                        + "contact, profile_picture, "
                        + "birthday) "
                        + "VALUES (?,?,?,?,?,?,?,?)";
                
                PreparedStatement ps1 = con.prepareStatement(query1);
                
                ps1.setString(1, username);
                ps1.setString(2, firstName);
                ps1.setString(3, lastName);
                ps1.setInt(4, age);
                ps1.setString(5, sex);
                ps1.setString(6, contact);
                
                FileInputStream profilePicture = new FileInputStream(selectedFile);
                
                ps1.setBlob(7,profilePicture,selectedFile.length());
                
                ps1.setDate(8, sqlDate);
                
                int result1 = ps1.executeUpdate();
                
                String query2 = 
                        "INSERT INTO landlord_account "
                        + "(username, password) "
                        + "VALUES (?,?)";
                
                PreparedStatement ps2 = con.prepareStatement(query2);
                
                ps2.setString(1, username);
                ps2.setString(2, password);
                
                int result2 = ps2.executeUpdate();
                
                if (result1 > 0 && result2 > 0) {
                    con.commit();
                    
                    JOptionPane.showMessageDialog(this,"Account created successfully");
                    
                    clearFields();
                }
                else {
                    con.rollback();
                    
                    JOptionPane.showMessageDialog(this,"Failed to create account");
                }
                
            }catch (Exception ex) {
                Logger.getLogger(CreateAccountLandlord.class.getName()).log(Level.SEVERE, null, ex);
                      
                JOptionPane.showMessageDialog(this,"Error: " + ex.getMessage());
            }
            
        }
    }
    
    // CLEAR FIELDS
    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        contactNoField.setText("");
        usernameField.setText("");
        passField.setText("");
        confirmPassField.setText("");
        genderField.setSelectedIndex(0);
        birthDateField.setDate(null);
        photoLabel.setIcon(null);
        selectedFile = null;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LandlordAccountCreationPage acc = new LandlordAccountCreationPage();
            
            acc.setVisible(true);
            acc.setLocationRelativeTo(null);
        });
    }
}



