import java.util.ArrayList;

public class Cart {

    protected ArrayList<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public double calculateTotal() {
        double total = 0;
        for(Product p : products) {
            total += p.getPrice();
        }
        return total;
    }

    public void showCart() {
        for(Product p : products) {
            System.out.println(p.getName() + " - " + p.getPrice());
        }
    }
}