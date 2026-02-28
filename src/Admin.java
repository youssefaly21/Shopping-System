import java.util.ArrayList;

public class Admin extends User {

    public Admin(String name, String email, String password,int age) {
        super(name, email, password,age);
    }

    public void addProduct(ArrayList<Product> products, Product product) {
        products.add(product);
    }
}