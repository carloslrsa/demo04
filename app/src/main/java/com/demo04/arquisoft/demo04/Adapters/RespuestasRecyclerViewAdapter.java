package com.demo04.arquisoft.demo04.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo04.arquisoft.demo04.R;
import com.demo04.arquisoft.demo04.TransferObjects.Pregunta;
import com.demo04.arquisoft.demo04.TransferObjects.Respuesta;

import java.util.List;

public class RespuestasRecyclerViewAdapter extends RecyclerView.Adapter<RespuestasRecyclerViewAdapter.ViewHolder> {

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView nombre;
        private TextView contenido;
        private ImageView fotoRespuesta;

        public ViewHolder(View itemView){
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombreRespuestaItemTextView);
            contenido = (TextView) itemView.findViewById(R.id.contenidoRespuestaItemTextView);
            fotoRespuesta = (ImageView) itemView.findViewById(R.id.fotoRespuestaItemImageView);
        }
    }

    public List<Respuesta> respuestas;

    public RespuestasRecyclerViewAdapter(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    @NonNull
    @Override
    public RespuestasRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_respuesta,parent,false);
        RespuestasRecyclerViewAdapter.ViewHolder viewHolder = new RespuestasRecyclerViewAdapter.ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RespuestasRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.nombre.setText(respuestas.get(position).getNombre());
        holder.contenido.setText(respuestas.get(position).getContenido());
        holder.fotoRespuesta.setImageBitmap(respuestas.get(position).getFotoPregunta());
    }

    @Override
    public int getItemCount() {
        return respuestas.size();
    }
}
