/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.*; 
import javax.swing.border.*; 
import java.awt.*; 
import java.awt.event.*;

/**
 *
 * @author Nica
 */
public class UserRoleSelection extends javax.swing.JFrame{
    //Stores the currently selected role.
    private String selectedRole = ""; 
    //panel card for each user role
    private JPanel studentCard; 
    private JPanel landlordCard; 
    private JPanel adminCard;  
    
    //Creates and initializes the role selection window.
    public UserRoleSelection() {
        this.setIconImage(new ImageIcon("src/images/DorFiLogo.png").getImage());
        this.setResizable(false);
        setTitle("Role Selection"); 
        setSize(1100, 650); 
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLayout(new BorderLayout()); //for five regions 
        
        // MAIN BACKGROUND
        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(new Color(230, 240, 255));
        
        // LEFT PANEL
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(260, 650));
        leftPanel.setBackground(new Color(225, 235, 250));
        leftPanel.setLayout(null); //stop using a Layout Manager.
        
        JLabel logo = new JLabel("DorFi");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logo.setForeground(new Color(0, 90, 180));
        logo.setBounds(30, 40, 200, 40);
        
        JLabel welcome = new JLabel("Welcome!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcome.setBounds(30, 120, 200, 40);// x, y, width, height
        
        JLabel desc = new JLabel("<html>We're here to help students and landlords manage dormitories easier.</html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        desc.setForeground(Color.DARK_GRAY);
        desc.setBounds(30, 170, 200, 80);
        
        leftPanel.add(logo);
        leftPanel.add(welcome);
        leftPanel.add(desc);
        
        // CENTER PANEL
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(40, 40, 40, 40)); //Top, Left, Bottom, Right. pushes the content away from the edges of the panel   
        centerPanel.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Choose Your Role", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(new Color(0, 90, 180));
        
        centerPanel.add(title, BorderLayout.NORTH);
        
        // ROLE CARDS PANEL
        JPanel cardsPanel = new JPanel();
        cardsPanel.setBackground(Color.WHITE);
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 100)); //Alignment, HorizontalGap, VerticalGap
        
        landlordCard = createRoleCard("🏠", "Landlord");
        studentCard = createRoleCard("🎓", "Student");
        adminCard = createRoleCard("🛡", "Admin");
        
        cardsPanel.add(landlordCard);
        cardsPanel.add(studentCard);
        cardsPanel.add(adminCard);
        
        centerPanel.add(cardsPanel, BorderLayout.CENTER);
        
        // BUTTON PANEL
        // Create a container specifically for the button to give us control over its positioning
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        
        JButton nextBtn = new JButton("NEXT");
        //removes border line
        nextBtn.setFocusPainted(false);
        nextBtn.setBackground(new Color(0, 90, 180));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nextBtn.setPreferredSize(new Dimension(180, 50));
        
        // A Lambda expression that tells the button: "When clicked, run the proceedRole() method"
        nextBtn.addActionListener(e -> proceedRole());
        
        buttonPanel.add(nextBtn);
        
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        // Aligns leftPanel to the left side of the main container
        background.add(leftPanel, BorderLayout.WEST);
        //Aligns main content (centerPanel with the cards) to fill the remaining middle space
        background.add(centerPanel, BorderLayout.CENTER);
        
        add(background);
}
    //Creates a clickable role card panel.
    //@param icon Emoji or icon representing the role
    //@param role Name of the role
    //@return JPanel role card
    private JPanel createRoleCard(String icon, String role) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(180, 180));
        card.setBackground(new Color(245, 248, 255));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Add 10 pixels of padding to the top of the label itself
        iconLabel.setBorder(new javax.swing.border.EmptyBorder(10, 0, 0, 0));
        
        card.add(Box.createVerticalGlue()); // Acts like an invisible spring that pushes content from the top to the center
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15))); // Creates a fixed invisible spacer of 15 pixels between the icon and the text
        card.add(roleLabel);
        card.add(Box.createVerticalGlue());// Another invisible spring at the bottom that pushes content upward to make content stays perfectly centered
        
        // CLICK EVENT
        // Attaches a listener to detect mouse actions on the card
        card.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                // Saves the chosen role in a string (e.g., "landlord") for later use in the logic
                selectedRole = role.toLowerCase();
                // Clears any previously selected blue borders so only one card is highlighted
                resetBorders();
                
                // Updates the clicked card's border to a thick blue (4px) to show it is selected
                card.setBorder(
                        new LineBorder(new Color(0, 90, 255), 4, true)
                );
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                // Changes the mouse pointer to a pointing hand, signaling to the user that the card is clickable
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
           });
        return card;
    }
    
    // RESET CARD BORDERS
    //Resets the borders of all role cards
    //back to the default border.
    // This method is called every time a card is clicked
    private void resetBorders() {
        // Resets the Landlord card to its original thin (2px) light gray border
        landlordCard.setBorder(
                new LineBorder(Color.LIGHT_GRAY, 2, true));
        // Resets the Student card to its original state
        studentCard.setBorder(
                new LineBorder(Color.LIGHT_GRAY, 2, true));
        // Resets the Admin card to its original state
        adminCard.setBorder(
                new LineBorder(Color.LIGHT_GRAY, 2, true));
    }
    
    // NEXT BUTTON
    private void proceedRole() {
        // Checks if the user clicked the Student card (using .equals for safe string comparison)
        if ("student".equals(selectedRole)){
            // Creates a new instance of the StudentHomePage class
            StudentBrowsingPage shp = new StudentBrowsingPage();
            // Makes the new Student window visible to the user
            shp.setVisible(true);
            // Automatically sizes the window to fit the preferred size of its components
            shp.pack();
            shp.setLocationRelativeTo(null);
            shp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // Closes and removes the current "Role Selection" window from memory
            this.dispose();
        }
        // Checks if the user clicked the Landlord card
        else if("landlord".equals(selectedRole)){
            // Creates the login screen specifically for landlords
            LandlordLogInPage llog = new LandlordLogInPage();
            llog.setVisible(true);
            llog.pack();
            llog.setLocationRelativeTo(null);
            llog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.dispose();
        }
        // Checks if the user clicked the Admin card
        else if("admin".equals(selectedRole)){
            AdminLogInPage al = new AdminLogInPage();
            al.setVisible(true);
            al.pack();
            al.setLocationRelativeTo(null);
            al.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.dispose();
        }
        // Validation: If no card was clicked, the selectedRole string will still be empty
        else if (selectedRole.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a role first.");
        }
    }
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
            new UserRoleSelection().setVisible(true);
        });
        }
}
