package com.junrrein.proyectofinal.backend;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EnviadorNotificaciones {

    private static final String DIRECCION_FIREBASE = "https://fcm.googleapis.com/fcm/send";
    private static final String TOKEN_FIREBASE_MESSAGING = "AAAAUxCQhc8:APA91bEuC8Gq37Q4-nVMhH4tdEWPf03ZMWcS0YGbf458K1uknCNvSftmL1YOik3Wwcow7lXWX0O1-AafQoK_D5tqWBkaKnmCSdmzTarLWx2E72V86MD5BbvSHVlp8iC4-ejDzSTWw8Xs";

    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static void enviar(String titulo,
                              String mensaje,
                              List<String> idDispositivos) {
        executor.execute(() -> enviarNotificacionInternal(titulo, mensaje, idDispositivos));
    }

    private static void enviarNotificacionInternal(String titulo,
                                                   String mensaje,
                                                   List<String> idDispositivos) {
        JSONObject body = armarMensaje(titulo, mensaje, idDispositivos);
        HttpURLConnection connection = null;

        try {
            URL url = new URL(DIRECCION_FIREBASE);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "key=" + TOKEN_FIREBASE_MESSAGING);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.toString().getBytes();
                os.write(input, 0, input.length);
            }

            try (InputStream is = connection.getInputStream()) {
                String result = convertInputStreamToString(is);
                Log.d("Respuesta del servidor de Firebase", result);
            }
        } catch (Exception e) {
            Log.d("Error al enviar notificacion", e.getLocalizedMessage());
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    private static JSONObject armarMensaje(String titulo,
                                           String mensaje,
                                           List<String> idDispositivos) {
        JSONObject result = new JSONObject();
        JSONObject notification = new JSONObject();

        try {
            notification.put("title", titulo);
            notification.put("body", mensaje);
            result.put("notification", notification);
            result.put("registration_ids", new JSONArray(idDispositivos));
        } catch (Exception e) {
            Log.d("Error al armar el mensaje", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }

        inputStream.close();

        return result.toString();
    }
}
