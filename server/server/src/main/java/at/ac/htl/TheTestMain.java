package at.ac.htl;


import org.mindrot.jbcrypt.BCrypt;

public class TheTestMain {
    public static void main(String[] args) {
        String password = "mypassword";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(hashedPassword);
    }
}
