public class InputValidation {

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("[a-zA-Z ]+");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidAge(int age) {
        return age >= 1 && age <= 120;
    }

    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidStock(int stock) {
        return stock >= 0;
    }

    public static boolean isValidProductID(int id) {
        return id > 0;
    }
}
