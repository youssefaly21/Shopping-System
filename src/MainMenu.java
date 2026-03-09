import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {

    public MainMenu(JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(StoreTheme.BG_WHITE);

        JPanel header = StoreTheme.headerPanel("Shopping Store");
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(StoreTheme.BG_WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(50, 80, 40, 80));

        JLabel subtitle = new JLabel("Choose your role to continue");
        subtitle.setFont(StoreTheme.FONT_SUBTITLE);
        subtitle.setForeground(StoreTheme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(subtitle);
        center.add(Box.createVerticalStrut(30));

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 20, 0));
        btnRow.setBackground(StoreTheme.BG_WHITE);
        btnRow.setMaximumSize(new Dimension(400, 50));

        JButton customerBtn = StoreTheme.styledButton("Customer");
        JButton adminBtn    = StoreTheme.styledButton("Admin", StoreTheme.PRIMARY_DARK, new Color(12, 45, 100));

        btnRow.add(customerBtn);
        btnRow.add(adminBtn);
        center.add(btnRow);

        add(center, BorderLayout.CENTER);

        JLabel footer = new JLabel("?? 2026 Shopping Store", SwingConstants.CENTER);
        footer.setFont(StoreTheme.FONT_SMALL);
        footer.setForeground(StoreTheme.TEXT_MUTED);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 12, 0));
        add(footer, BorderLayout.SOUTH);

        customerBtn.addActionListener(e -> {
            frame.setContentPane(new CustomerForm(frame));
            frame.revalidate();
            frame.repaint();
        });

        adminBtn.addActionListener(e -> {
            String pass = JOptionPane.showInputDialog(frame, "Enter Admin Password:");
            if (pass == null) return;
            Admin admin = new Admin("Admin1", "admin@test.com", "admin123", 30);
            if (!pass.equals(admin.getPassword())) {
                JOptionPane.showMessageDialog(frame, "Incorrect password!");
            } else {
                frame.setContentPane(new AdminMenu(frame, admin));
                frame.revalidate();
                frame.repaint();
            }
        });
    }
}
