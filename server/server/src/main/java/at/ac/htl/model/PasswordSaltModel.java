package at.ac.htl.model;

public class PasswordSaltModel {
    public String password;
    public String salt;

    public PasswordSaltModel(String password, String salt) {
        this.password = password;
        this.salt = salt;
    }
}
