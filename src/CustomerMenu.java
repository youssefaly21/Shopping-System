import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class CustomerMenu extends JPanel {

    public CustomerMenu(JFrame frame, Customer customer) {
        setLayout(new BorderLayout(0, 0));
        setBackground(StoreTheme.BG_WHITE);

        add(StoreTheme.headerPanel("Welcome, " + customer.getName() + "!"), BorderLayout.NORTH);

        JPanel productDisplayPanel = new JPanel();
        productDisplayPanel.setLayout(new BoxLayout(productDisplayPanel, BoxLayout.Y_AXIS));
        productDisplayPanel.setBackground(StoreTheme.BG_WHITE);
        JScrollPane scrollPane = new JScrollPane(productDisplayPanel);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(8, 12, 4, 12),
            BorderFactory.createLineBorder(StoreTheme.BORDER_LIGHT)
        ));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        buttonPanel.setBackground(StoreTheme.BG_WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(6, 12, 14, 12));

        JButton showProductsBtn     = StoreTheme.styledButton("Products");
        JButton browseByCategoryBtn = StoreTheme.styledButton("By Category", StoreTheme.PRIMARY_LIGHT, StoreTheme.PRIMARY);
        JButton addToCartBtn        = StoreTheme.styledButton("Add to Cart", StoreTheme.SUCCESS, new Color(30, 140, 65));
        JButton removeFromCartBtn   = StoreTheme.dangerButton("Remove Item");
        JButton viewCartBtn         = StoreTheme.styledButton("View Cart");
        JButton checkoutBtn         = StoreTheme.styledButton("Checkout", new Color(20, 120, 60), new Color(15, 95, 45));
        JButton backBtn             = StoreTheme.styledButton("Back", StoreTheme.TEXT_MUTED, new Color(90, 100, 120));

        buttonPanel.add(showProductsBtn);
        buttonPanel.add(browseByCategoryBtn);
        buttonPanel.add(addToCartBtn);
        buttonPanel.add(removeFromCartBtn);
        buttonPanel.add(viewCartBtn);
        buttonPanel.add(checkoutBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        java.util.function.Consumer<java.util.List<Product>> displayProducts = (products) -> {
            productDisplayPanel.removeAll();
            if (products.isEmpty()) {
                JLabel emptyLabel = new JLabel("  No products available.");
                emptyLabel.setFont(StoreTheme.FONT_BODY);
                emptyLabel.setForeground(StoreTheme.TEXT_MUTED);
                emptyLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
                productDisplayPanel.add(emptyLabel);
            } else {
                productDisplayPanel.add(Box.createVerticalStrut(6));
                for (Product p : products) {
                    JPanel card = new JPanel(new BorderLayout(12, 5));
                    card.setBorder(StoreTheme.cardBorder());
                    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
                    card.setBackground(StoreTheme.CARD_WHITE);

                    // Image thumbnail
                    JLabel imageLabel = new JLabel();
                    imageLabel.setPreferredSize(new Dimension(85, 85));
                    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    imageLabel.setOpaque(true);
                    imageLabel.setBackground(StoreTheme.BG_WHITE);
                    imageLabel.setBorder(BorderFactory.createLineBorder(StoreTheme.BORDER_LIGHT));
                    if (!p.getImagePath().isEmpty() && new File(p.getImagePath()).exists()) {
                        ImageIcon icon = new ImageIcon(p.getImagePath());
                        Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaled));
                    } else {
                        imageLabel.setText("No image");
                        imageLabel.setFont(StoreTheme.FONT_SMALL);
                        imageLabel.setForeground(StoreTheme.TEXT_MUTED);
                    }
                    card.add(imageLabel, BorderLayout.WEST);

                    // Info
                    JPanel infoPanel = new JPanel();
                    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                    infoPanel.setBackground(StoreTheme.CARD_WHITE);
                    infoPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

                    JLabel nameLabel = new JLabel(p.getName());
                    nameLabel.setFont(StoreTheme.FONT_SUBTITLE);
                    nameLabel.setForeground(StoreTheme.PRIMARY_DARK);
                    infoPanel.add(nameLabel);

                    JLabel detailLabel = new JLabel("ID: " + p.getProductID()
                        + "   |   $" + String.format("%.2f", p.getPrice())
                        + "   |   Stock: " + p.getStock());
                    detailLabel.setFont(StoreTheme.FONT_BODY);
                    detailLabel.setForeground(StoreTheme.TEXT_DARK);
                    infoPanel.add(detailLabel);

                    String catText = p.getCategory().isEmpty() ? "Uncategorized" : p.getCategory();
                    JLabel categoryLabel = new JLabel("Category: " + catText);
                    categoryLabel.setFont(StoreTheme.FONT_SMALL);
                    categoryLabel.setForeground(StoreTheme.ACCENT);
                    infoPanel.add(categoryLabel);

                    card.add(infoPanel, BorderLayout.CENTER);
                    productDisplayPanel.add(card);
                    productDisplayPanel.add(Box.createVerticalStrut(6));
                }
            }
            productDisplayPanel.revalidate();
            productDisplayPanel.repaint();
        };

        showProductsBtn.addActionListener(e -> displayProducts.accept(ProductStore.products));

        browseByCategoryBtn.addActionListener(e -> {
            if (CategoryStore.categories.isEmpty()) {
                productDisplayPanel.removeAll();
                JLabel lbl = new JLabel("  No categories available.");
                lbl.setFont(StoreTheme.FONT_BODY);
                lbl.setForeground(StoreTheme.TEXT_MUTED);
                lbl.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
                productDisplayPanel.add(lbl);
                productDisplayPanel.revalidate();
                productDisplayPanel.repaint();
                return;
            }
            String[] catArray = CategoryStore.categories.toArray(new String[0]);
            String chosen = (String) JOptionPane.showInputDialog(frame,
                "Select a category:", "Browse by Category",
                JOptionPane.QUESTION_MESSAGE, null, catArray, catArray[0]);
            if (chosen == null) return;

            ArrayList<Product> filtered = new ArrayList<>();
            for (Product p : ProductStore.products)
                if (p.getCategory().equalsIgnoreCase(chosen)) filtered.add(p);

            if (filtered.isEmpty()) {
                productDisplayPanel.removeAll();
                JLabel lbl = new JLabel("  No products in \"" + chosen + "\".");
                lbl.setFont(StoreTheme.FONT_BODY);
                lbl.setForeground(StoreTheme.TEXT_MUTED);
                lbl.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
                productDisplayPanel.add(lbl);
                productDisplayPanel.revalidate();
                productDisplayPanel.repaint();
            } else {
                displayProducts.accept(filtered);
            }
        });

        addToCartBtn.addActionListener(e -> {
            if (ProductStore.products.isEmpty()) { JOptionPane.showMessageDialog(frame, "No products available."); return; }
            String input = JOptionPane.showInputDialog(frame, "Enter Product ID to add:");
            if (input == null) return;
            try {
                int pid = Integer.parseInt(input.trim());
                Product found = null;
                for (Product p : ProductStore.products) if (p.getProductID() == pid) { found = p; break; }
                if (found == null) JOptionPane.showMessageDialog(frame, "Product not found.");
                else if (found.getStock() <= 0) JOptionPane.showMessageDialog(frame, "\"" + found.getName() + "\" is out of stock.");
                else { customer.getCart().addProduct(found); JOptionPane.showMessageDialog(frame, "\"" + found.getName() + "\" added to cart!"); }
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Enter a valid numeric ID."); }
        });

        removeFromCartBtn.addActionListener(e -> {
            if (customer.getCart().products.isEmpty()) { JOptionPane.showMessageDialog(frame, "Cart is empty."); return; }
            String input = JOptionPane.showInputDialog(frame, "Enter Product ID to remove:");
            if (input == null) return;
            try {
                int pid = Integer.parseInt(input.trim());
                Product found = null;
                for (Product p : customer.getCart().products) if (p.getProductID() == pid) { found = p; break; }
                if (found == null) JOptionPane.showMessageDialog(frame, "Not in cart.");
                else { customer.getCart().removeProduct(found); JOptionPane.showMessageDialog(frame, "\"" + found.getName() + "\" removed."); }
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Enter a valid numeric ID."); }
        });

        viewCartBtn.addActionListener(e -> {
            productDisplayPanel.removeAll();
            if (customer.getCart().products.isEmpty()) {
                JLabel lbl = new JLabel("  Your cart is empty.");
                lbl.setFont(StoreTheme.FONT_BODY);
                lbl.setForeground(StoreTheme.TEXT_MUTED);
                lbl.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
                productDisplayPanel.add(lbl);
            } else {
                double total = customer.getCart().calculateTotal();
                JLabel cartTitle = new JLabel("  Your Cart");
                cartTitle.setFont(StoreTheme.FONT_SUBTITLE);
                cartTitle.setForeground(StoreTheme.PRIMARY);
                cartTitle.setBorder(BorderFactory.createEmptyBorder(10, 8, 6, 8));
                productDisplayPanel.add(cartTitle);

                for (Product p : customer.getCart().products) {
                    JPanel card = new JPanel(new BorderLayout(10, 4));
                    card.setBorder(StoreTheme.cardBorder());
                    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
                    card.setBackground(StoreTheme.CARD_WHITE);

                    JLabel img = new JLabel();
                    img.setPreferredSize(new Dimension(40, 40));
                    img.setHorizontalAlignment(SwingConstants.CENTER);
                    if (!p.getImagePath().isEmpty() && new File(p.getImagePath()).exists()) {
                        ImageIcon icon = new ImageIcon(p.getImagePath());
                        Image scaled = icon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
                        img.setIcon(new ImageIcon(scaled));
                    }
                    card.add(img, BorderLayout.WEST);

                    JLabel info = new JLabel(p.getName() + "  ???  $" + String.format("%.2f", p.getPrice()));
                    info.setFont(StoreTheme.FONT_BODY);
                    info.setForeground(StoreTheme.TEXT_DARK);
                    card.add(info, BorderLayout.CENTER);

                    productDisplayPanel.add(card);
                    productDisplayPanel.add(Box.createVerticalStrut(4));
                }

                JSeparator sep = new JSeparator();
                sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
                sep.setForeground(StoreTheme.PRIMARY);
                productDisplayPanel.add(sep);

                JLabel totalLabel = new JLabel("  Total: $" + String.format("%.2f", total));
                totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                totalLabel.setForeground(StoreTheme.PRIMARY_DARK);
                totalLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 10, 8));
                productDisplayPanel.add(totalLabel);
            }
            productDisplayPanel.revalidate();
            productDisplayPanel.repaint();
        });

        checkoutBtn.addActionListener(e -> {
            if (customer.getCart().products.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Cart is empty. Add items first.");
                return;
            }
            double total = customer.getCart().calculateTotal();
            StringBuilder sb = new StringBuilder("  Receipt\n");
            sb.append("  " + "???".repeat(46)).append("\n");
            for (Product p : customer.getCart().products) {
                p.decreaseStock(1);
                sb.append("  ").append(p.getName()).append("  ???  $").append(String.format("%.2f", p.getPrice())).append("\n");
            }
            sb.append("  " + "???".repeat(46)).append("\n");
            sb.append("  Total Paid: $").append(String.format("%.2f", total)).append("\n\n");
            sb.append("  Thank you for your purchase!");
            customer.getCart().products.clear();

            productDisplayPanel.removeAll();
            JTextArea receipt = new JTextArea(sb.toString());
            receipt.setEditable(false);
            receipt.setFont(StoreTheme.FONT_MONO);
            receipt.setBackground(StoreTheme.CARD_WHITE);
            receipt.setForeground(StoreTheme.TEXT_DARK);
            receipt.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
            productDisplayPanel.add(receipt);
            productDisplayPanel.revalidate();
            productDisplayPanel.repaint();
        });

        backBtn.addActionListener(e -> {
            frame.setContentPane(new MainMenu(frame));
            frame.revalidate();
            frame.repaint();
        });
    }
}
