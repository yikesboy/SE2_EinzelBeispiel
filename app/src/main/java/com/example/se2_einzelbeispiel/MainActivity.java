package com.example.se2_einzelbeispiel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {

    private TextView serverResponse;
    private EditText mNr;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        serverResponse = findViewById(R.id.server_response);
        mNr = findViewById(R.id.mNr);
        sendButton = findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToServer(view);
            }
        });
    }

    public void connectToServer(View view){
        final String serverDomain = "se2-submission.aau.at";
        final int serverPort = 20080;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(serverDomain, serverPort);

                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader readerInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String number = mNr.getText().toString();
                    writer.println(number);

                    String response = readerInput.readLine();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            serverResponse.setText(response);
                        }
                    });

                    socket.close();

                } catch (IOException e){
                   e.printStackTrace();
                }

            }
        }).start();

    }
}