package com.junrrein.proyectofinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.junrrein.proyectofinal.backend.Repositorio;
import com.junrrein.proyectofinal.backend.Usuario;
import com.junrrein.proyectofinal.databinding.ActivityMainBinding;
import com.junrrein.proyectofinal.ui.ConfirmacionDialogFragment;
import com.junrrein.proyectofinal.ui.SeccionesPagerAdapter;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//hola
    static private final int AUTENTICAR_REQUEST = 1;

    ActivityMainBinding binding;
    ModeloUsuario modeloUsuario;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        modeloUsuario = new ViewModelProvider(this).get(ModeloUsuario.class);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null) {
            lanzarActividadAutenticacion();
        } else {
            modeloUsuario.setUsuario(firebaseUser.getUid());
            armarPantallaPrincipal();
        }
    }

    private void armarPantallaPrincipal() {
        SeccionesPagerAdapter pagerAdapter = new SeccionesPagerAdapter(getSupportFragmentManager());
        ViewPager pager = binding.viewPager;
        binding.viewPager.setAdapter(pagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(pager);

        actualizarIdDispositivo();
    }

    private void lanzarActividadAutenticacion() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        startActivityForResult(intent, AUTENTICAR_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTENTICAR_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                assert (firebaseUser != null);

                String idUsuario = firebaseUser.getUid();
                String nombre = firebaseUser.getDisplayName();
                String email = firebaseUser.getEmail();

                if (email == null) {
                    for (UserInfo profile : firebaseUser.getProviderData()) {
                        email = profile.getEmail();
                    }
                }

                Repositorio.guardarUsuario(new Usuario(idUsuario, nombre, email));
                modeloUsuario.setUsuario(idUsuario);

                armarPantallaPrincipal();
            } else {
                finish();
            }
        }
    }

    private void actualizarIdDispositivo() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(instanceIdResult -> {
                    String token = instanceIdResult.getToken();

                    Utils.observarUnaSolaVez(modeloUsuario.getUsuario(), usuario -> {
                        if (!token.equals(usuario.getIdDispositivo())) {
                            usuario.setIdDispositivo(token);
                            modeloUsuario.guardarUsuario(usuario);
                        }
                    });
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cerrar_sesion) {
            mostrarDialogCerrarSesion();
            return true;
        }

        if (item.getItemId() == R.id.eliminar_cuenta) {
            mostrarDialogEliminarCuenta();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarDialogCerrarSesion() {
        DialogFragment dialogFragment = new ConfirmacionDialogFragment(
                "¿Cerrar sesión?",
                "Si lo hace, deberá iniciar sesión nuevamente",
                "Cerrar sesión",
                this::cerrarSesion
        );

        dialogFragment.show(getSupportFragmentManager(), "CerrarSesionDialogFragment");
    }

    private void cerrarSesion() {
        AuthUI.getInstance().signOut(this)
                .addOnSuccessListener(aVoid -> lanzarActividadAutenticacion());
    }

    private void mostrarDialogEliminarCuenta() {
        DialogFragment dialogFragment = new ConfirmacionDialogFragment(
                "¿Eliminar cuenta?",
                "Esto eliminará todos los datos asociados a su cuenta, incluyendo los eventos creados",
                "Eliminar",
                this::eliminarCuenta
        );

        dialogFragment.show(getSupportFragmentManager(), "EliminarCuentaDialogFragment");
    }

    private void eliminarCuenta() {
        Repositorio.eliminarUsuarioYSusEventosCreados(modeloUsuario.idUsuario)
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        return AuthUI.getInstance().delete(this);
                    } else {
                        throw new Exception("Se falló en eliminar el usuario");
                    }
                })
                .addOnSuccessListener(aVoid -> lanzarActividadAutenticacion());
    }
}
