package com.junrrein.proyectofinal.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.junrrein.proyectofinal.backend.Evento;
import com.junrrein.proyectofinal.R;

import java.util.List;
import java.util.function.Consumer;

public class ListaEventosAdapter
        extends RecyclerView.Adapter<ListaEventosAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreEvento;
        TextView tipoEvento;
        TextView fechaHoraEvento;

        ViewHolder(View view) {
            super(view);

            this.nombreEvento = view.findViewById(R.id.nombreEvento);
            this.tipoEvento = view.findViewById(R.id.tipoEvento);
            this.fechaHoraEvento = view.findViewById(R.id.fechaHoraEvento);
        }

        void setClickListener(Runnable listener) {
            itemView.setOnClickListener(v -> listener.run());
        }
    }

    private List<Evento> eventos;
    private Consumer<String> mostradorEvento;
    private Consumer<List<Evento>> mostradorMapa;

    ListaEventosAdapter(List<Evento> eventos,
                        Consumer<String> mostradorEvento,
                        Consumer<List<Evento>> mostradorMapa) {
        this.eventos = eventos;
        this.mostradorEvento = mostradorEvento;
        this.mostradorMapa = mostradorMapa;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tarjeta_evento, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {
        if (position != 0) {
            Evento evento = eventos.get(position - 1);

            holder.nombreEvento.setText(evento.getNombre());
            holder.tipoEvento.setText(evento.getTipo());
            holder.fechaHoraEvento.setText(evento.getFechaInicio().toString() + " a las " + evento.getHoraInicio().toString());

            holder.setClickListener(() -> mostradorEvento.accept(evento.getId()));
        } else {
            holder.nombreEvento.setText("Mostrar en el mapa");
            holder.tipoEvento.setVisibility(View.GONE);
            holder.fechaHoraEvento.setVisibility(View.GONE);

            holder.setClickListener(() -> mostradorMapa.accept(eventos));
        }
    }

    @Override
    public int getItemCount() {
        if (eventos.size() != 0)
            return eventos.size() + 1;

        return 0;
    }
}
