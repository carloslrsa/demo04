package com.demo04.arquisoft.demo04.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.demo04.arquisoft.demo04.TransferObjects.Pregunta;
import com.demo04.arquisoft.demo04.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView nombre;
        private TextView contenido;
        private Button responder;

        public ViewHolder(View itemView){
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombreTextView);
            contenido = (TextView) itemView.findViewById(R.id.contenidoTextView);
            responder = (Button) itemView.findViewById(R.id.verPreguntaButton);
        }
    }

    public List<Pregunta> preguntas;

    public RecyclerViewAdapter(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pregunta,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre.setText(preguntas.get(position).getNombre());
        holder.contenido.setText(preguntas.get(position).getContenido());
    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }
}
