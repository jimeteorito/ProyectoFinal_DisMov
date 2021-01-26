package com.junrrein.proyectofinal.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.junrrein.proyectofinal.R;
import com.junrrein.proyectofinal.backend.Ubicacion;
import com.junrrein.proyectofinal.databinding.ElegirUbicacionBinding;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;

public class ElegirUbicacionActivity extends AppCompatActivity {

    public static final String UBICACION_ACTUAL = "com.junrrein.proyectofinal.ui.ubicacion-actual";
    public static final String UBICACION_NUEVA = "com.junrrein.proyectofinal.ui.ubicacion-nueva";

    private static final String SOURCE_ID = "mi.fuente";
    private static final String ICON_ID = "mi.icono";
    private static final String MARKER_LAYER_ID = "mi.capa.marcadores";

    private Ubicacion ubicacionVieja;
    private ElegirUbicacionBinding binding;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private GeoJsonSource source;
    private LatLng posicionActual;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_token));
        binding = ElegirUbicacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Elegir ubicación");

        source = new GeoJsonSource(SOURCE_ID);
        ubicacionVieja = (Ubicacion) getIntent().getSerializableExtra(UBICACION_ACTUAL);

        if (ubicacionVieja != null)
            source.setGeoJson(Point.fromLngLat(ubicacionVieja.longitud, ubicacionVieja.latitud));

        binding.confirmarUbicacion.setEnabled(false);
        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(onMapReadyCallback);
    }

    private OnMapReadyCallback onMapReadyCallback = mapboxMap -> {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, this.onStyleLoaded);
    };

    private Style.OnStyleLoaded onStyleLoaded = style -> {
        style.addSource(source);
        style.addImage(ICON_ID, BitmapFactory.decodeResource(getResources(), R.drawable.mapbox_marker_icon_default));
        style.addLayer(generarMarkerLayer());

        mapboxMap.setCameraPosition(determinarPosicionDeCamaraInicial());
        mapboxMap.addOnMapClickListener(this.onMapClickListener);
    };

    private CameraPosition determinarPosicionDeCamaraInicial() {
        LatLng centro = new LatLng(-31.634788, -60.705824);

        if (ubicacionVieja != null) {
            centro.setLatitude(ubicacionVieja.latitud);
            centro.setLongitude(ubicacionVieja.longitud);
        }

        return new CameraPosition.Builder()
                .target(centro)
                .zoom(12.0)
                .build();
    }

    private SymbolLayer generarMarkerLayer() {
        return new SymbolLayer(MARKER_LAYER_ID, SOURCE_ID)
                .withProperties(iconImage(ICON_ID),
                        iconSize(1.2f),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true));
    }

    private MapboxMap.OnMapClickListener onMapClickListener = point -> {
        source.setGeoJson(Point.fromLngLat(point.getLongitude(), point.getLatitude()));
        posicionActual = point;
        binding.confirmarUbicacion.setEnabled(true);

        return false;
    };

    public void onCancelarClick(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void onConfirmarClick(View view) {
        Ubicacion ubicacion = new Ubicacion(posicionActual.getLatitude(),
                posicionActual.getLongitude());

        Intent resultIntent = new Intent();
        resultIntent.putExtra(UBICACION_NUEVA, ubicacion);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
