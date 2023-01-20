package at.ac.htl;


import org.mindrot.jbcrypt.BCrypt;

public class TheTestMain {
    public static void main(String[] args) {
        String password = "mypassword";
        String salt = "$2a$10$HEXADECIMAL_SALT";
        String hashedPassword = BCrypt.hashpw(password, salt);

        if (BCrypt.checkpw(password, hashedPassword)) {
            System.out.println("The password is correct.");
        } else {
            System.out.println("The password is incorrect.");
        }

    }
}
