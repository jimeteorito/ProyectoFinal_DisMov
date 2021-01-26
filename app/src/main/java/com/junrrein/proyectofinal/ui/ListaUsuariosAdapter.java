package com.junrrein.proyectofinal.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.junrrein.proyectofinal.R;
import com.junrrein.proyectofinal.backend.Usuario;

import java.util.List;

public class ListaUsuariosAdapter
        extends RecyclerView.Adapter<ListaUsuariosAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.texto_elemento);
        }
    }

    private List<Usuario> usuarios;

    ListaUsuariosAdapter(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elemento_lista, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(usuarios.get(position).getNombreOEmail());
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }
}
