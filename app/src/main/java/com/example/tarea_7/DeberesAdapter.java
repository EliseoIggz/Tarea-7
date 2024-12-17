package com.example.tarea_7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeberesAdapter extends RecyclerView.Adapter<DeberesAdapter.DeberesViewHolder> {

    private ArrayList<Deberes> listaDeberes;
    //private Context context;

    public DeberesAdapter(ArrayList<Deberes> listaDeberes/*, Context context*/) {
        this.listaDeberes = listaDeberes;
        //this.context = context; // Pasamos el contexto para poder referenciarlo en el toast al borrar la planta
    }

    @NonNull
    @Override
    public DeberesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new DeberesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeberesViewHolder holder, int position) {
        Deberes deber = listaDeberes.get(position);
        holder.titulo.setText(deber.getTitulo());
        holder.descripcion.setText(deber.getTitulo());
        holder.asignatura.setText(deber.getTitulo());
        holder.fecha.setText(deber.getTitulo());
        holder.hora.setText(deber.getTitulo());
        holder.estado.setText(deber.getTitulo());
    }

    @Override
    public int getItemCount() {
        return listaDeberes.size();
    }

    public class DeberesViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView descripcion;
        TextView asignatura;
        TextView fecha;
        TextView hora;
        TextView estado;

        public DeberesViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tvTitulo);
            descripcion = itemView.findViewById(R.id.tvDescripcion);
            asignatura = itemView.findViewById(R.id.tvAsignatura);
            fecha = itemView.findViewById(R.id.tvFecha);
            hora = itemView.findViewById(R.id.tvHora);
            estado = itemView.findViewById(R.id.tvEstado);
        }
    }
}
