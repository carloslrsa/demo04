package com.demo04.arquisoft.demo04;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.contentful.java.cma.CMACallback;
import com.contentful.java.cma.CMAClient;
import com.contentful.java.cma.model.CMAEntry;
import com.demo04.arquisoft.demo04.Adapters.RecyclerViewAdapter;
import com.demo04.arquisoft.demo04.TransferObjects.Pregunta;


import java.util.ArrayList;
import java.util.List;

public class VerPreguntasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPregunta;
    private RecyclerViewAdapter adapterPregunta;
    private List<Pregunta> misPreguntas = new ArrayList<Pregunta>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_preguntas);

        recyclerViewPregunta = (RecyclerView) findViewById(R.id.preguntasRecyclerView);
        recyclerViewPregunta.setLayoutManager(new LinearLayoutManager(this));

        /*adapterPregunta = new RecyclerViewAdapter(obtenerPreguntas());

        */

        new ObtenerPreguntasTask().execute(true,true,true);

        //new PublicarPreguntaTask().execute(true,true,true);

    }

    public void MostrarPreguntas(CDAArray array){
        for (CDAResource resource: array.items()) {
            CDAEntry entry = (CDAEntry) resource;
            String autor = entry.getField("autor");
            String contenido = entry.getField("contenido");
            misPreguntas.add(new Pregunta(autor,contenido));
        }


        adapterPregunta = new RecyclerViewAdapter(misPreguntas);
        recyclerViewPregunta.setAdapter(adapterPregunta);

    }

    public void PublicarPregunta(){
        Toast.makeText(this, "Pregunta publicada", Toast.LENGTH_SHORT).show();
    }


    private class ObtenerPreguntasTask extends AsyncTask<Boolean, Boolean, Boolean> {

        CDAClient client;
        CDAArray array;

        protected Boolean doInBackground(Boolean... urls) {
            client =
                    CDAClient
                            .builder()
                            .setToken("930dedaa9f394398b905721bb1ae461322666bb420bdc174577818460bf31413")
                            .setSpace("qabuj9f8h9n1")
                            .build();

            array = client.fetch(CDAEntry.class)
                    .where("content_type", "pregunta")
                    .all();

            return true;
        }

        protected void onProgressUpdate(Boolean... progress) {
        }

        protected void onPostExecute(Boolean result) {
            MostrarPreguntas(array);
        }
    }

    private class PublicarPreguntaTask extends AsyncTask<Boolean, Boolean, Boolean> {

        CMAClient client;

        protected Boolean doInBackground(Boolean... urls) {
            client =
                    new CMAClient.Builder()
                            .setAccessToken("CFPAT-ecf0dc6bca0a0816546259b1967bc50566aaf5548c59f31fca46cdf905f9c99d")
                            .setSpaceId("qabuj9f8h9n1")
                            .setEnvironmentId("master")
                            .build();

            CMAEntry entry =
                    new CMAEntry()
                    .setField("autor","en-US","Koko Ontón")
                    .setField("contenido","en-US","Prosor por qué soy tan homosexual?");


            client.entries().async().create("pregunta", entry, new CMACallback<CMAEntry>() {
                @Override
                protected void onFailure(RuntimeException exception) {
                    super.onFailure(exception);
                }

                @Override
                protected void onSuccess(CMAEntry result) {
                    client.entries()
                            .async()
                            .publish(result, new CMACallback<CMAEntry>() {
                                @Override
                                protected void onFailure(RuntimeException exception) {
                                    super.onFailure(exception);
                                }
                                @Override
                                protected void onSuccess(CMAEntry result) {
                                    PublicarPregunta();
                                }
                            });
                }
            });

            return true;
        }

        protected void onProgressUpdate(Boolean... progress) {
        }

        protected void onPostExecute(Boolean result) {
        }
    }
}
