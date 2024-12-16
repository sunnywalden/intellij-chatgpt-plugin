package com.sunnywalden.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import okhttp3.*;
import java.io.IOException;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private String apiKey;

    public LoginDialog(Frame parent) {
        super(parent, "Login to OpenAI", true);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理登录逻辑
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                loginToOpenAI(username, password);
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public String getApiKey() {
        return apiKey;
    }

    private void loginToOpenAI(String username, String password) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/auth/login")
                .post(RequestBody.create(MediaType.parse("application/json"),
                        "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(LoginDialog.this, "Login failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    SwingUtilities.invokeLater(() -> {
                        apiKey = JOptionPane.showInputDialog(LoginDialog.this, "Enter your API_KEY:", "API_KEY", JOptionPane.PLAIN_MESSAGE);
                        if (apiKey != null && !apiKey.isEmpty()) {
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(LoginDialog.this, "API_KEY cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(LoginDialog.this, "Login failed: " + response.message(), "Error", JOptionPane.ERROR_MESSAGE));
                }
            }
        });
    }
}