package com.sunnywalden;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import okhttp3.*;

import java.awt.*;
import java.io.IOException;

public class ChatGPTPlugin extends AnAction {

    private final OkHttpClient client = new OkHttpClient();
    private String apiKey;

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (apiKey == null) {
            LoginDialog loginDialog = new LoginDialog((Frame) null);
            loginDialog.setVisible(true);
            apiKey = loginDialog.getApiKey();
        }

        if (apiKey != null) {
            String response = getChatGPTResponse("Hello, ChatGPT!");
            Messages.showMessageDialog(response, "ChatGPT Response", Messages.getInformationIcon());
        }
    }

    private String getChatGPTResponse(String prompt) {
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/engines/davinci-codex/completions")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(MediaType.parse("application/json"), "{\"prompt\":\"" + prompt + "\",\"max_tokens\":50}"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}