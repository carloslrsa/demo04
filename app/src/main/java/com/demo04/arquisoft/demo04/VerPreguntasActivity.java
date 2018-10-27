package com.demo04.arquisoft.demo04;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.demo04.arquisoft.demo04.Adapters.PreguntasRecyclerViewAdapter;
import com.demo04.arquisoft.demo04.TransferObjects.Pregunta;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.contentful.java.cda.image.ImageOption.https;

public class VerPreguntasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPregunta;
    private PreguntasRecyclerViewAdapter adapterPregunta;
    private List<Pregunta> misPreguntas = new ArrayList<Pregunta>();

    ObtenerPreguntasTask obtenerPreguntasTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_preguntas);

        setTitle("Houston, TUP - Preguntas");

        recyclerViewPregunta = (RecyclerView) findViewById(R.id.preguntasRecyclerView);
        recyclerViewPregunta.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        
        inicializar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        obtenerPreguntasTask.cancel(true);
    }

    private void inicializar(){
        Toast.makeText(this, "Cargando preguntas...", Toast.LENGTH_SHORT).show();
        misPreguntas = new ArrayList<Pregunta>();
        
        adapterPregunta = new PreguntasRecyclerViewAdapter(misPreguntas,getApplicationContext());
        recyclerViewPregunta.setAdapter(adapterPregunta);

        obtenerPreguntasTask = new ObtenerPreguntasTask();
        obtenerPreguntasTask.execute(true,true,true);
    }

    public void MostrarPreguntas(CDAArray array){
        for (CDAResource resource: array.items()) {
            CDAEntry entry = (CDAEntry) resource;
            Pregunta pregunta = new Pregunta();

            pregunta.setPreguntaId(entry.id());
            pregunta.setNombre((String) entry.getField("autor"));
            pregunta.setContenido((String) entry.getField("contenido"));

            try {
                CDAAsset fotoAsset = entry.getField("foto");
                String url = fotoAsset.urlForImageWith(https());
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());

                pregunta.setFotoPregunta(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            misPreguntas.add(pregunta);
        }
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

            MostrarPreguntas(array);
            return true;
        }

        protected void onPostExecute(Boolean result) {
            adapterPregunta = new PreguntasRecyclerViewAdapter(misPreguntas,getApplicationContext());
            recyclerViewPregunta.setAdapter(adapterPregunta);

            Toast.makeText(VerPreguntasActivity.this, "¡Preguntas cargadas!", Toast.LENGTH_SHORT).show();
        }
    }

}
