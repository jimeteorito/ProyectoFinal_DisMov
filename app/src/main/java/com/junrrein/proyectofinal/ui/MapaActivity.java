package com.junrrein.proyectofinal.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.junrrein.proyectofinal.R;
import com.junrrein.proyectofinal.databinding.BurbujaMapaBinding;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.BubbleLayout;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;

public class MapaActivity extends AppCompatActivity {

    public static final String EVENTOS_MAPA = "com.junrrein.proyectofinal.eventos-mapa";
    public static final String ID_USUARIO = "com.junrrein.proyectofinal.id-usuario";

    private static final String SOURCE_ID = "mi.fuente";
    private static final String ICON_ID = "mi.icono";
    private static final String MARKER_LAYER_ID = "mi.capa.marcadores";
    private static final String BURBUJA_LAYER_ID = "mi.capa.burbujas";
    private static final String PROPERTY_ID = "id";
    private static final String PROPERTY_TITULO = "titulo";
    private static final String PROPERTY_DESCRIPCION = "descripcion";
    private static final String PROPERTY_SELECCIONADO = "seleccionado";

    private MapView mapView;
    private MapboxMap mapboxMap;
    private String idUsuario;
    private List<EventoMapa> eventos;
    private FeatureCollection featureCollection;
    private GeoJsonSource source;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_token));
        setContentView(R.layout.mapa);
        setTitle("Mapa");

        idUsuario = getIntent().getStringExtra(ID_USUARIO);
        eventos = (ArrayList<EventoMapa>) getIntent().getSerializableExtra(EVENTOS_MAPA);
        featureCollection = generarFeatures();
        source = new GeoJsonSource(SOURCE_ID, featureCollection);

        mapView = findViewById(R.id.mapView);
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
        style.addImages(generarBurbujas());
        style.addLayer(generarBurbujaLayer());

        mapboxMap.setCameraPosition(determinarPosicionDeCamaraInicial());
        mapboxMap.addOnMapClickListener(this.onMapClickListener);
    };

    private CameraPosition determinarPosicionDeCamaraInicial() {
        if (eventos.size() == 1) {
            LatLng punto = new LatLng(eventos.get(0).latitud, eventos.get(0).longitud);

            return new CameraPosition.Builder()
                    .target(new LatLng(punto))
                    .zoom(13.0)
                    .build();
        } else {
            int[] padding = {200, 200, 200, 200};

            return mapboxMap.getCameraForLatLngBounds(calcularLimitesMapa(), padding);
        }
    }

    private FeatureCollection generarFeatures() {
        List<Feature> features = new ArrayList<>();

        for (EventoMapa evento : eventos) {
            Feature feature = Feature.fromGeometry(Point.fromLngLat(evento.longitud, evento.latitud));
            feature.addStringProperty(PROPERTY_ID, evento.id);
            feature.addStringProperty(PROPERTY_TITULO, evento.nombre);
            feature.addStringProperty(PROPERTY_DESCRIPCION,
                    evento.fecha.toString() + " a las " + evento.hora.toString());
            feature.addBooleanProperty(PROPERTY_SELECCIONADO, false);

            features.add(feature);
        }

        return FeatureCollection.fromFeatures(features);
    }

    private SymbolLayer generarMarkerLayer() {
        return new SymbolLayer(MARKER_LAYER_ID, SOURCE_ID)
                .withProperties(iconImage(ICON_ID),
                        iconSize(1.2f),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true));
    }

    private HashMap<String, Bitmap> generarBurbujas() {
        HashMap<String, Bitmap> result = new HashMap<>();
        assert (featureCollection.features() != null);

        for (Feature feature : featureCollection.features()) {
            BurbujaMapaBinding binding = BurbujaMapaBinding.inflate(getLayoutInflater());
            @SuppressWarnings("deprecation") BubbleLayout bubbleLayout = (BubbleLayout) binding.getRoot();

            binding.infoWindowTitle.setText(feature.getStringProperty(PROPERTY_TITULO));
            binding.infoWindowDescription.setText(feature.getStringProperty(PROPERTY_DESCRIPCION));

            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            bubbleLayout.measure(measureSpec, measureSpec);

            float measuredWidth = bubbleLayout.getMeasuredWidth();
            bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);

            Bitmap bitmap = generarBitmapDeView(bubbleLayout);
            result.put(feature.getStringProperty(PROPERTY_ID), bitmap);
        }

        return result;
    }

    private static Bitmap generarBitmapDeView(@NonNull View view) {
        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(measureSpec, measureSpec);

        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();

        view.layout(0, 0, measuredWidth, measuredHeight);
        Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private SymbolLayer generarBurbujaLayer() {
        return new SymbolLayer(BURBUJA_LAYER_ID, SOURCE_ID)
                .withProperties(
                        iconImage("{id}"),
                        iconAnchor(Property.ICON_ANCHOR_BOTTOM),
                        iconAllowOverlap(true),
                        iconOffset(new Float[]{-2f, -35f}))
                .withFilter(eq(get(PROPERTY_SELECCIONADO), literal(true)));
    }

    private MapboxMap.OnMapClickListener onMapClickListener = point -> {
        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);

        List<Feature> marcadoresClickeados = mapboxMap.queryRenderedFeatures(screenPoint, MARKER_LAYER_ID);

        if (!marcadoresClickeados.isEmpty()) {
            manejarMarcadorClickeado(marcadoresClickeados.get(0));
            return true;
        }

        List<Feature> burbujasClickeadas = mapboxMap.queryRenderedFeatures(screenPoint, BURBUJA_LAYER_ID);

        if (!burbujasClickeadas.isEmpty() && eventos.size() > 1) {
            manejarBurbujaClickeada(burbujasClickeadas.get(0));
            return true;
        }

        return false;
    };

    private void manejarMarcadorClickeado(Feature marcadorClickeado) {
        String idMarcadorClickeado = marcadorClickeado.getStringProperty(PROPERTY_ID);
        assert (featureCollection.features() != null);

        for (Feature feature : featureCollection.features()) {
            String idFeature = feature.getStringProperty(PROPERTY_ID);

            if (idMarcadorClickeado.equals(idFeature)) {
                boolean estaSeleccionado = feature.getBooleanProperty(PROPERTY_SELECCIONADO);
                feature.addBooleanProperty(PROPERTY_SELECCIONADO, !estaSeleccionado);
                refrescarSource();

                break;
            }
        }
    }

    private void manejarBurbujaClickeada(Feature burbujaClickeada) {
        String idBurbujaClickeada = burbujaClickeada.getStringProperty(PROPERTY_ID);

        Intent intent = new Intent(this, DetalleEventoActivity.class);
        intent.putExtra(DetalleEventoActivity.ID_USUARIO, idUsuario);
        intent.putExtra(DetalleEventoActivity.ID_EVENTO, idBurbujaClickeada);
        startActivity(intent);
    }

    private void refrescarSource() {
        source.setGeoJson(featureCollection);
    }

    private LatLngBounds calcularLimitesMapa() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        assert (featureCollection.features() != null);

        for (EventoMapa evento : eventos) {
            LatLng punto = new LatLng(evento.latitud, evento.longitud);
            builder.include(punto);
        }

        return builder.build();
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
