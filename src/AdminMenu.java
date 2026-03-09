import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class AdminMenu extends JPanel {

    public AdminMenu(JFrame frame, Admin admin) {
        setLayout(new BorderLayout(0, 0));
        setBackground(StoreTheme.BG_WHITE);

        add(StoreTheme.headerPanel("Admin Dashboard"), BorderLayout.NORTH);

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(StoreTheme.FONT_MONO);
        outputArea.setBackground(StoreTheme.CARD_WHITE);
        outputArea.setForeground(StoreTheme.TEXT_DARK);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 12, 5, 12),
            BorderFactory.createLineBorder(StoreTheme.BORDER_LIGHT)
        ));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBackground(StoreTheme.BG_WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 14, 12));

        JButton addProductBtn       = StoreTheme.styledButton("Add Product");
        JButton showProductsBtn     = StoreTheme.styledButton("Show Products");
        JButton changePriceBtn      = StoreTheme.styledButton("Price / Stock");
        JButton deleteProductBtn    = StoreTheme.dangerButton("Delete Product");
        JButton manageCategoriesBtn = StoreTheme.styledButton("Categories", StoreTheme.PRIMARY_LIGHT, StoreTheme.PRIMARY);
        JButton backBtn             = StoreTheme.styledButton("Back", StoreTheme.TEXT_MUTED, new Color(90, 100, 120));

        buttonPanel.add(addProductBtn);
        buttonPanel.add(showProductsBtn);
        buttonPanel.add(changePriceBtn);
        buttonPanel.add(deleteProductBtn);
        buttonPanel.add(manageCategoriesBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        showProductsBtn.addActionListener(e -> {
            if (ProductStore.products.isEmpty()) {
                outputArea.setText("  No products added yet.");
            } else {
                StringBuilder sb = new StringBuilder("  All Products:\n");
                sb.append("  " + "???".repeat(58)).append("\n");
                for (Product p : ProductStore.products) {
                    sb.append("  ID: ").append(p.getProductID())
                      .append("  |  Name: ").append(p.getName())
                      .append("  |  $").append(String.format("%.2f", p.getPrice()))
                      .append("  |  Stock: ").append(p.getStock())
                      .append("  |  Category: ").append(p.getCategory().isEmpty() ? "???" : p.getCategory())
                      .append("\n");
                    if (!p.getImagePath().isEmpty()) {
                        sb.append("       Image: ").append(p.getImagePath()).append("\n");
                    }
                }
                outputArea.setText(sb.toString());
            }
        });

        manageCategoriesBtn.addActionListener(e -> {
            String[] options = {"Add Category", "View Categories"};
            int choice = JOptionPane.showOptionDialog(frame,
                "Category Management",
                "Manage Categories",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

            if (choice == 0) {
                String catName = JOptionPane.showInputDialog(frame, "Enter new category name:");
                if (catName == null || catName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Category name cannot be empty.");
                    return;
                }
                String trimmed = catName.trim();
                for (String existing : CategoryStore.categories) {
                    if (existing.equalsIgnoreCase(trimmed)) {
                        JOptionPane.showMessageDialog(frame, "Category \"" + trimmed + "\" already exists.");
                        return;
                    }
                }
                CategoryStore.categories.add(trimmed);
                outputArea.setText("  Category \"" + trimmed + "\" added!\n\n  All categories: " + CategoryStore.categories);
            } else if (choice == 1) {
                if (CategoryStore.categories.isEmpty()) {
                    outputArea.setText("  No categories added yet.");
                } else {
                    StringBuilder sb = new StringBuilder("  All Categories:\n");
                    sb.append("  " + "???".repeat(36)).append("\n");
                    for (int i = 0; i < CategoryStore.categories.size(); i++) {
                        sb.append("  ").append(i + 1).append(". ").append(CategoryStore.categories.get(i)).append("\n");
                    }
                    outputArea.setText(sb.toString());
                }
            }
        });

        addProductBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(frame, "Enter Product ID (positive number):");
            if (idStr == null) return;
            int id;
            try { id = Integer.parseInt(idStr.trim()); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Invalid ID."); return; }
            if (!InputValidation.isValidProductID(id)) { JOptionPane.showMessageDialog(frame, "ID must be > 0."); return; }
            for (Product p : ProductStore.products) {
                if (p.getProductID() == id) { JOptionPane.showMessageDialog(frame, "ID already exists."); return; }
            }

            String name = JOptionPane.showInputDialog(frame, "Enter Product Name:");
            if (name == null || name.trim().isEmpty()) { JOptionPane.showMessageDialog(frame, "Name cannot be empty."); return; }

            String priceStr = JOptionPane.showInputDialog(frame, "Enter Product Price:");
            if (priceStr == null) return;
            double price;
            try { price = Double.parseDouble(priceStr.trim()); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Invalid price."); return; }
            if (!InputValidation.isValidPrice(price)) { JOptionPane.showMessageDialog(frame, "Price must be > 0."); return; }

            String stockStr = JOptionPane.showInputDialog(frame, "Enter Product Stock:");
            if (stockStr == null) return;
            int stock;
            try { stock = Integer.parseInt(stockStr.trim()); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Invalid stock."); return; }
            if (!InputValidation.isValidStock(stock)) { JOptionPane.showMessageDialog(frame, "Stock >= 0."); return; }

            String selectedCategory = "";
            if (CategoryStore.categories.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                    "No categories yet. Use 'Categories' to add one first.\nProduct will be added without a category.",
                    "No Categories", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String[] catArray = CategoryStore.categories.toArray(new String[0]);
                String chosen = (String) JOptionPane.showInputDialog(frame,
                    "Select a category:", "Product Category",
                    JOptionPane.QUESTION_MESSAGE, null, catArray, catArray[0]);
                if (chosen != null) selectedCategory = chosen;
            }

            String imagePath = "";
            int imgChoice = JOptionPane.showConfirmDialog(frame,
                "Add an image for this product?", "Product Image", JOptionPane.YES_NO_OPTION);
            if (imgChoice == JOptionPane.YES_OPTION) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Select Product Image");
                fc.setFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "jpeg", "gif"));
                if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                    imagePath = fc.getSelectedFile().getAbsolutePath();
            }

            Product np = new Product(id, name.trim(), price, stock, imagePath, selectedCategory);
            admin.addProduct(ProductStore.products, np);

            StringBuilder msg = new StringBuilder("  Product added successfully!\n");
            msg.append("  ID: ").append(id).append("  |  ").append(name.trim())
               .append("  |  $").append(String.format("%.2f", price))
               .append("  |  Stock: ").append(stock);
            if (!selectedCategory.isEmpty()) msg.append("\n  Category: ").append(selectedCategory);
            if (!imagePath.isEmpty()) msg.append("\n  Image: ").append(imagePath);
            outputArea.setText(msg.toString());
        });

        deleteProductBtn.addActionListener(e -> {
            if (ProductStore.products.isEmpty()) {
                outputArea.setText("  No products to delete.");
                return;
            }
            String idStr = JOptionPane.showInputDialog(frame, "Enter Product ID to delete:");
            if (idStr == null) return;
            int pid;
            try { pid = Integer.parseInt(idStr.trim()); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Invalid ID."); return; }

            Product found = null;
            for (Product p : ProductStore.products) {
                if (p.getProductID() == pid) { found = p; break; }
            }
            if (found == null) {
                outputArea.setText("  Product with ID " + pid + " not found.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame,
                "Delete \"" + found.getName() + "\" (ID: " + pid + ")?\nThis cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                ProductStore.products.remove(found);
                outputArea.setText("  Product \"" + found.getName() + "\" (ID: " + pid + ") deleted.");
            }
        });

        changePriceBtn.addActionListener(e -> {
            if (ProductStore.products.isEmpty()) { outputArea.setText("  No products available."); return; }
            String idStr = JOptionPane.showInputDialog(frame, "Enter Product ID:");
            if (idStr == null) return;
            int pid;
            try { pid = Integer.parseInt(idStr.trim()); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Invalid ID."); return; }

            Product found = null;
            for (Product p : ProductStore.products) { if (p.getProductID() == pid) { found = p; break; } }
            if (found == null) { outputArea.setText("  Product with ID " + pid + " not found."); return; }

            String[] options = {"Change Price", "Change Stock"};
            int choice = JOptionPane.showOptionDialog(frame,
                "What to change for \"" + found.getName() + "\"?", "Change Product",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                String s = JOptionPane.showInputDialog(frame, "Enter New Price:");
                if (s == null) return;
                try {
                    double np = Double.parseDouble(s.trim());
                    if (!InputValidation.isValidPrice(np)) { JOptionPane.showMessageDialog(frame, "Price must be > 0."); return; }
                    found.setPrice(np);
                    outputArea.setText("  Price updated!  " + found.getName() + " -> $" + String.format("%.2f", np));
                } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Invalid price."); }
            } else if (choice == 1) {
                String s = JOptionPane.showInputDialog(frame, "Enter New Stock:");
                if (s == null) return;
                try {
                    int ns = Integer.parseInt(s.trim());
                    if (!InputValidation.isValidStock(ns)) { JOptionPane.showMessageDialog(frame, "Stock >= 0."); return; }
                    found.setStock(ns);
                    outputArea.setText("  Stock updated!  " + found.getName() + " -> " + ns + " units");
                } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Invalid stock."); }
            }
        });

        backBtn.addActionListener(e -> {
            frame.setContentPane(new MainMenu(frame));
            frame.revalidate();
            frame.repaint();
        });
    }
}
