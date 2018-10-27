package com.demo04.arquisoft.demo04;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button responderPreguntas;
    Button publicarPregunta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responderPreguntas = (Button) findViewById(R.id.responderPreguntasBoton);
        publicarPregunta = (Button) findViewById(R.id.publicarPreguntaBoton);

        responderPreguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VerPreguntasActivity.class);
                startActivity(intent);
            }
        });

        publicarPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PublicarPreguntaActivity.class);
                startActivity(intent);
            }
        });
    }
}
