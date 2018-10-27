package com.demo04.arquisoft.demo04.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo04.arquisoft.demo04.ResponderPreguntaActivity;
import com.demo04.arquisoft.demo04.TransferObjects.Pregunta;
import com.demo04.arquisoft.demo04.R;

import java.util.List;

public class PreguntasRecyclerViewAdapter extends RecyclerView.Adapter<PreguntasRecyclerViewAdapter.ViewHolder> {

    Context context;

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView nombre;
        private TextView contenido;
        private ImageView fotoPregunta;
        private Button responder;

        public ViewHolder(View itemView){
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombreTextView);
            contenido = (TextView) itemView.findViewById(R.id.contenidoTextView);
            fotoPregunta = (ImageView) itemView.findViewById(R.id.fotoPreguntaImageView);
            responder = (Button) itemView.findViewById(R.id.verPreguntaButton);
        }
    }

    public List<Pregunta> preguntas;

    public PreguntasRecyclerViewAdapter(List<Pregunta> preguntas, Context context) {
        this.preguntas = preguntas;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pregunta,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nombre.setText(preguntas.get(position).getNombre());
        holder.contenido.setText(preguntas.get(position).getContenido());
        holder.fotoPregunta.setImageBitmap(preguntas.get(position).getFotoPregunta());

        holder.responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ResponderPreguntaActivity.class);
                intent.putExtra("preguntaID",preguntas.get(position).getPreguntaId());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }
}
