package at.ac.htl;


import org.mindrot.jbcrypt.BCrypt;

public class TheTestMain {
    public static void main(String[] args) {

        String password = "mypassword";
        String hashedPassword = "$2a$10$HEXADECIMAL_SALT";
        if (BCrypt.checkpw(password, hashedPassword)) {
            System.out.println("korrekt");
            // Passwort ist korrekt
        } else {
            // Passwort ist falsch
            System.out.println("Falsch");
        }

    }
}
