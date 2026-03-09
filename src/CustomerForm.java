import javax.swing.*;
import java.awt.*;

public class CustomerForm extends JPanel {

    public CustomerForm(JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(StoreTheme.BG_WHITE);

        add(StoreTheme.headerPanel("Customer Registration"), BorderLayout.NORTH);

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(StoreTheme.CARD_WHITE);
        formCard.setBorder(StoreTheme.cardBorder());

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(StoreTheme.BG_WHITE);
        GridBagConstraints wgbc = new GridBagConstraints();
        wgbc.insets = new Insets(0, 40, 0, 40);
        wrapper.add(formCard, wgbc);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(StoreTheme.FONT_BODY_BOLD);
        nameLabel.setForeground(StoreTheme.TEXT_DARK);
        formCard.add(nameLabel, gbc);
        JTextField nameField = new JTextField(18);
        nameField.setFont(StoreTheme.FONT_BODY);
        gbc.gridx = 1;
        formCard.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(StoreTheme.FONT_BODY_BOLD);
        emailLabel.setForeground(StoreTheme.TEXT_DARK);
        formCard.add(emailLabel, gbc);
        JTextField emailField = new JTextField(18);
        emailField.setFont(StoreTheme.FONT_BODY);
        gbc.gridx = 1;
        formCard.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(StoreTheme.FONT_BODY_BOLD);
        passLabel.setForeground(StoreTheme.TEXT_DARK);
        formCard.add(passLabel, gbc);
        JPasswordField passwordField = new JPasswordField(18);
        passwordField.setFont(StoreTheme.FONT_BODY);
        gbc.gridx = 1;
        formCard.add(passwordField, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(StoreTheme.FONT_BODY_BOLD);
        ageLabel.setForeground(StoreTheme.TEXT_DARK);
        formCard.add(ageLabel, gbc);
        JTextField ageField = new JTextField(18);
        ageField.setFont(StoreTheme.FONT_BODY);
        gbc.gridx = 1;
        formCard.add(ageField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(16, 12, 4, 12);
        JButton registerBtn = StoreTheme.styledButton("Register", StoreTheme.SUCCESS, new Color(30, 140, 65));
        formCard.add(registerBtn, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(4, 12, 12, 12);
        JButton backBtn = StoreTheme.styledButton("??? Back", StoreTheme.TEXT_MUTED, new Color(90, 100, 120));
        formCard.add(backBtn, gbc);

        add(wrapper, BorderLayout.CENTER);

        registerBtn.addActionListener(e -> {
            String name     = nameField.getText().trim();
            String email    = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String ageText  = ageField.getText().trim();

            if (!InputValidation.isValidName(name)) {
                JOptionPane.showMessageDialog(frame, "Name must contain letters only.");
                return;
            }
            if (!InputValidation.isValidEmail(email)) {
                JOptionPane.showMessageDialog(frame, "Invalid email format (e.g. user@example.com).");
                return;
            }
            if (!InputValidation.isValidPassword(password)) {
                JOptionPane.showMessageDialog(frame, "Password must be at least 6 characters.");
                return;
            }
            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Age must be a number.");
                return;
            }
            if (!InputValidation.isValidAge(age)) {
                JOptionPane.showMessageDialog(frame, "Age must be between 1 and 120.");
                return;
            }

            Customer customer = new Customer(name, email, password, age);
            JOptionPane.showMessageDialog(frame, "Welcome, " + name + "!");
            frame.setContentPane(new CustomerMenu(frame, customer));
            frame.revalidate();
            frame.repaint();
        });

        backBtn.addActionListener(e -> {
            frame.setContentPane(new MainMenu(frame));
            frame.revalidate();
            frame.repaint();
        });
    }
}
