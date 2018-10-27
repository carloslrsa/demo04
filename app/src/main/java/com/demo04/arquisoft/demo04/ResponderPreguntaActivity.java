package com.demo04.arquisoft.demo04;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.contentful.java.cda.QueryOperation;
import com.demo04.arquisoft.demo04.Adapters.PreguntasRecyclerViewAdapter;
import com.demo04.arquisoft.demo04.Adapters.RespuestasRecyclerViewAdapter;
import com.demo04.arquisoft.demo04.TransferObjects.Pregunta;
import com.demo04.arquisoft.demo04.TransferObjects.Respuesta;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.contentful.java.cda.image.ImageOption.https;

public class ResponderPreguntaActivity extends AppCompatActivity {

    RecyclerView recyclerViewRespuestas;
    RespuestasRecyclerViewAdapter adapter;

    View mLayout;
    CardView preguntaCardView;

    ImageView fotoPregunta;
    TextView autorPregunta;
    TextView contenidoPregunta;
    FloatingActionButton responder;

    Pregunta miPregunta;
    List<Respuesta> misRespuestas;

    ObtenerPreguntaRespuestasTask obtenerPreguntaRespuestasTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_pregunta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Houston, TUP - Respuestas");

        miPregunta = new Pregunta();

        miPregunta.setPreguntaId((String) getIntent().getStringExtra("preguntaID"));

        mLayout = (View) findViewById(R.id.mLayout);

        recyclerViewRespuestas = (RecyclerView) findViewById(R.id.preguntasRecyclerView);
        preguntaCardView = (CardView) mLayout.findViewById(R.id.preguntaCardView);

        fotoPregunta = (ImageView) mLayout.findViewById(R.id.fotoResponderPreguntaImageView);
        autorPregunta = (TextView) mLayout.findViewById(R.id.autorResponderPreguntaTextView);
        contenidoPregunta = (TextView) mLayout.findViewById(R.id.contenidoResponderPreguntaTextView);
        responder = (FloatingActionButton) findViewById(R.id.responderPreguntaButton);

        preguntaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarTamanoPregunta();
            }
        });

        recyclerViewRespuestas.setLayoutManager(new LinearLayoutManager(this));


        responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(getApplicationContext(),PublicarRespuestaActivity.class);
                intent.putExtra("preguntaID",miPregunta.getPreguntaId());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        obtenerPreguntaRespuestasTask.cancel(true);
    }

    private void inicializar(){
        recyclerViewRespuestas.setVisibility(View.GONE);
        responder.setVisibility(View.GONE);

        misRespuestas = new ArrayList<Respuesta>();
        adapter = new RespuestasRecyclerViewAdapter(misRespuestas);
        recyclerViewRespuestas.setAdapter(adapter);

        Toast.makeText(this, "Cargando respuestas...", Toast.LENGTH_SHORT).show();
        obtenerPreguntaRespuestasTask = new ObtenerPreguntaRespuestasTask();
        obtenerPreguntaRespuestasTask.execute(true,true,true);
    }


    private void setPregunta(String nombre, String contenido, Bitmap foto){
        miPregunta.setNombre(nombre);
        miPregunta.setContenido(contenido);
        if(foto != null)
            miPregunta.setFotoPregunta(foto);

        fotoPregunta.setImageBitmap(miPregunta.getFotoPregunta());
        autorPregunta.setText(miPregunta.getNombre());
        contenidoPregunta.setText(miPregunta.getContenido());
    }

    private void setRespuestas(CDAArray array){
        misRespuestas = new ArrayList<Respuesta>();

        for (CDAResource resource: array.items()) {
            CDAEntry entry = (CDAEntry) resource;
            Respuesta respuesta = new Respuesta();

            respuesta.setRespuestaId(entry.id());
            respuesta.setPreguntaId((String) entry.getField("preguntaId"));
            respuesta.setNombre((String) entry.getField("autor"));
            respuesta.setContenido((String) entry.getField("contenido"));

            try {
                CDAAsset fotoAsset = entry.getField("foto");
                String url = fotoAsset.urlForImageWith(https());
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());

                respuesta.setFotoPregunta(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            misRespuestas.add(respuesta);
        }
    }

    private void cambiarTamanoPregunta(){
        if(fotoPregunta.getVisibility() == View.VISIBLE){
            fotoPregunta.setVisibility(View.GONE);
        }else{
            fotoPregunta.setVisibility(View.VISIBLE);
        }
    }

    private class ObtenerPreguntaRespuestasTask extends AsyncTask<Boolean, Boolean, Boolean> {

        CDAClient client;
        CDAEntry entry;
        CDAArray array;

        protected Boolean doInBackground(Boolean... urls) {
            client =
                    CDAClient
                            .builder()
                            .setToken("930dedaa9f394398b905721bb1ae461322666bb420bdc174577818460bf31413")
                            .setSpace("qabuj9f8h9n1")
                            .build();

            entry = client
                    .fetch(CDAEntry.class)
                    .one(miPregunta.getPreguntaId());

            Bitmap bitmap = null;

            try {
                CDAAsset fotoAsset = entry.getField("foto");
                String url = fotoAsset.urlForImageWith(https());
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }

            array = client.fetch(CDAEntry.class)
                    .where("content_type", "respuesta")
                    .where("fields.preguntaId", QueryOperation.IsEqualTo, miPregunta.getPreguntaId())
                    .all();


            setPregunta((String) entry.getField("autor"),(String) entry.getField("contenido"),bitmap);
            setRespuestas(array);


            return true;
        }

        protected void onPostExecute(Boolean result) {
            preguntaCardView.setVisibility(View.VISIBLE);
            adapter = new RespuestasRecyclerViewAdapter(misRespuestas);
            recyclerViewRespuestas.setAdapter(adapter);
            responder.setVisibility(View.VISIBLE);
            recyclerViewRespuestas.setVisibility(View.VISIBLE);
            Toast.makeText(ResponderPreguntaActivity.this, "¡Respuestas cargadas!", Toast.LENGTH_SHORT).show();
        }
    }

}
