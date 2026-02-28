public abstract class User {
    protected String name;
    protected String email;
    protected String Password;
    protected  int age;

    public User(){
        name = "user";
        email = "test@yahoo.com";
        Password ="";
        age = 18;
    }
    public User(String name, String email,String password, int age){
        this.name = name;
        this.email = email;
        this.Password = password;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.Password = address;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return Password;
    }

    public int getAge() {
        return age;
    }
    public void DisplayUserInfo(){
        System.out.println(name);
        System.out.println(email);
        System.out.println(Password);
        System.out.println(age);
    }
    public String ChangeEmail(String NewEmail){
        email = NewEmail;
        return NewEmail;
    }
    // update profile
    public String ChangePassword(String NewPassword){
        Password = NewPassword;
        return NewPassword;
    }
    public int ChangeAge(int NewAge){
        age = NewAge;
        return NewAge;
    }
}
