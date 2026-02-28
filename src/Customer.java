public class Customer extends User {
    private Cart cart;

    public Customer(String name, String email, String password,int age) {
        super(name, email, password,age);
        this.cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }
}