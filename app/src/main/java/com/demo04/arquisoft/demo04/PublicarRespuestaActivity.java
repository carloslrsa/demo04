package com.demo04.arquisoft.demo04;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.contentful.java.cma.CMAClient;
import com.contentful.java.cma.model.CMAAsset;
import com.contentful.java.cma.model.CMAAssetFile;
import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cma.model.CMALink;
import com.contentful.java.cma.model.CMAType;
import com.contentful.java.cma.model.CMAUpload;
import com.demo04.arquisoft.demo04.TransferObjects.Pregunta;
import com.demo04.arquisoft.demo04.TransferObjects.Respuesta;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PublicarRespuestaActivity extends AppCompatActivity {

    private ImageView fotoRespuesta;
    private TextInputLayout autorRespuesta;
    private TextInputLayout contenidoRespuesta;
    private Button subirFotografia;
    private Button publicarRespuesta;

    Uri imagenUri = null;

    private Respuesta miRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_respuesta);

        setTitle("Houston, TUP - Responder");

        miRespuesta = new Respuesta();
        miRespuesta.setPreguntaId((String)getIntent().getStringExtra("preguntaID"));

        fotoRespuesta = (ImageView) findViewById(R.id.fotoPublicarRespuestaImageView);
        autorRespuesta = (TextInputLayout) findViewById(R.id.autorPublicarRespuestaTextInput);
        contenidoRespuesta = (TextInputLayout) findViewById(R.id.contenidoPublicarRespuestaTextInput);

        subirFotografia = (Button) findViewById(R.id.subirFotografiaPublicarRespuestaButton);
        publicarRespuesta = (Button) findViewById(R.id.publicarRespuestaButton);

        subirFotografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });

        publicarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autorRespuesta.getEditText().getText().length() > 0 && contenidoRespuesta.getEditText().getText().length() > 0){
                    empezarPublicacion();
                    new PublicarRespuestaActivity.PublicarRespuestaTask().execute(true,true,true);
                }
            }
        });

        Toast.makeText(this, "Llene los siguientes campos para su respuesta.", Toast.LENGTH_SHORT).show();
    }

    void empezarPublicacion(){
        subirFotografia.setEnabled(false);
        publicarRespuesta.setEnabled(false);
        autorRespuesta.setEnabled(false);
        contenidoRespuesta.setEnabled(false);

        Toast.makeText(this, "Publicando respuesta...", Toast.LENGTH_SHORT).show();
    }

    void terminarPublicacion(){
        Toast.makeText(this, "Respuesta publicada", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                imagenUri = data.getData();

                InputStream imageStream = getContentResolver().openInputStream(imagenUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                fotoRespuesta.setImageBitmap(selectedImage);
                fotoRespuesta.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(getApplicationContext(), "No escogió una imagen",Toast.LENGTH_LONG).show();
        }

    }

    private class PublicarRespuestaTask extends AsyncTask<Boolean, Boolean, Boolean> {
        CMAClient client;
        protected Boolean doInBackground(Boolean... urls) {
            client =
                    new CMAClient.Builder()
                            .setAccessToken("CFPAT-ecf0dc6bca0a0816546259b1967bc50566aaf5548c59f31fca46cdf905f9c99d")
                            .setSpaceId("qabuj9f8h9n1")
                            .setEnvironmentId("master")
                            .build();

            CMAEntry entry = null;

            String autor = autorRespuesta.getEditText().getText().toString();
            String contenido = contenidoRespuesta.getEditText().getText().toString();

            if(imagenUri!=null){
                /*Asset*/
                CMAUpload upload = null;
                try {
                    upload =
                            client
                                    .uploads()
                                    .create("qabuj9f8h9n1", getContentResolver().openInputStream(imagenUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                CMAAsset asset = new CMAAsset();
                asset
                        .getFields()
                        .setTitle("en-US","Nueva imagen")
                        .setDescription("en-US","Hola");

                CMAAssetFile assetFile = new CMAAssetFile()
                        .setUploadFrom(
                                new CMALink(CMAType.Upload).setId(upload.getId())
                        )
                        .setFileName(upload.getId()+".jpg")
                        .setContentType("image/jpeg");

                asset.getFields().setFile("en-US",assetFile);

                asset = client.assets().create(asset);

                client.assets().process(asset,"en-US");

                int intentos = 10;
                String url;
                do{
                    asset = client.assets().fetchOne("qabuj9f8h9n1", "master",asset.getId());
                    url = asset.getFields().localize("en-US").getFile().getUrl();
                    intentos--;

                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }while(intentos > 0 && (url == null || url.length() == 0));

                if (intentos <= 0) {
                    //throw new IllegalStateException("Could not finish processing for " + asset);
                    Toast.makeText(PublicarRespuestaActivity.this, "Ocurrió un problema al publicar la imagen", Toast.LENGTH_SHORT).show();
                    return  false;
                }
                asset = client.assets().publish(asset);
                /*-----------*/

                entry =
                        new CMAEntry()
                                .setField("preguntaId","en-US",miRespuesta.getPreguntaId())
                                .setField("autor","en-US",autor)
                                .setField("contenido","en-US",contenido)
                                .setField("foto","en-US",asset);
                ;

            }else{
                entry =
                        new CMAEntry()
                                .setField("preguntaId","en-US",miRespuesta.getPreguntaId())
                                .setField("autor","en-US",autor)
                                .setField("contenido","en-US",contenido)
                ;
            }

            entry = client.entries().create("respuesta", entry);

            client.entries().publish(entry);


            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            terminarPublicacion();
        }
    }

}
