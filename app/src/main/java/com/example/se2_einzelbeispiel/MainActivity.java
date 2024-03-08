package com.example.se2_einzelbeispiel;

import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    private TextView serverResponse;
    private EditText mNr;
    private Button sendButton;
    private Button calculateButton;
    private TextView calculationResponse;

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
        calculateButton = findViewById(R.id.calculate_button);
        calculationResponse = findViewById(R.id.calculation_response);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToServer(view);
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonDivisorPairs(view);
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

    public void commonDivisorPairs(View view){
        String number = mNr.getText().toString();
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < number.length(); i++){
            for(int j = i + 1; j < number.length(); j++) {
                int digit1 = Character.getNumericValue(number.charAt(i));
                int digit2 = Character.getNumericValue(number.charAt(j));
                Log.d("commonDivisorPairs", "Comparing indices" + i + " and " + j + ": " + digit1 + " & " + digit2);
                if (commonDivisor(digit1, digit2)){
                    result.append(i).append(" - ").append(j).append("\n");
                    Log.d("commonDivisorPairs", "Found CD indices: " + i + " - " + j);
                }

            }
        }
        String concatenate = "Indices: \n" + result; // because of warning from setText
        calculationResponse.setText(concatenate);
    }

    private boolean commonDivisor(int a, int b){
        for (int d = 2; d <= Math.min(a, b); d++) {
            if (a % d == 0 && b % d== 0) {
                return true;
            }
        }
        return false;
    }
}