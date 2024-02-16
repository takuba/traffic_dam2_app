package com.example.traficoreto.helpers;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.example.traficoreto.structure.UserFavouriteStructure;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyPreferences {
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "UserId";
    private static final String KEY_LANG = "lang";
    private static final String PREF_NAME = "checkbox_prefs";
    private static final String FAV_IDS = "favourites";

    public static void setUserFavData(Context context, List<UserFavouriteStructure> nuevosDatos) {
        List<UserFavouriteStructure> datosExistente = getUserFavData(context);

        // Agregar los nuevos datos a la lista existente
        datosExistente.addAll(nuevosDatos);

        // Convertir a JSON la lista actualizada
        String jsonListaDatos = new Gson().toJson(datosExistente);

        // Guardar en SharedPreferences
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(FAV_IDS, jsonListaDatos);
        editor.apply();
    }
    public static void updateUserFavData(Context context, UserFavouriteStructure updatedData) {
        // Recuperar los datos existentes
        List<UserFavouriteStructure> datosExistente = getUserFavData(context);

        // Buscar el objeto que se va a actualizar y realizar la actualización
        for (int i = 0; i < datosExistente.size(); i++) {
            UserFavouriteStructure existingData = datosExistente.get(i);
                // Realizar la actualización (puedes modificar los campos según tus necesidades)
                existingData.setFav_id(updatedData.getFav_id());
                existingData.setSourceId(updatedData.getSourceId());
                existingData.setSourceId(updatedData.getType());
            Log.d("com.example.traficoreto.fernando","entro al if");

            // Puedes continuar actualizando otros campos según sea necesario
                break; // Se encontró el objeto, salir del bucle

        }

        // Convertir a JSON la lista actualizada
        String jsonListaDatos = new Gson().toJson(datosExistente);

        // Guardar en SharedPreferences
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(FAV_IDS, jsonListaDatos);
        editor.apply();
    }

    public static List<UserFavouriteStructure> getUserFavData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonListaDatos = preferences.getString(FAV_IDS, "");

        // Convertir de JSON a lista de objetos
        Type listType = new TypeToken<List<UserFavouriteStructure>>() {}.getType();
        List<UserFavouriteStructure> listaDatos = new Gson().fromJson(jsonListaDatos, listType);

        // Verificar si la lista es nula para evitar NullPointerException
        if (listaDatos == null) {
            listaDatos = new ArrayList<>();
        }

        return listaDatos;
    }

    public static void saveUserId(Context context, String userId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_ID, null);
    }
    public static void deleteUserId(Context context){
        setUrlRequest(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_USER_ID);
        editor.apply();
    }

    public static void setUrlRequest(Context context){
        SharedPreferences preferences = context.getSharedPreferences("MiArchivoDePreferencias",  Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("urlGuardada", "http://10.0.2.2:3000/");
        editor.apply();
    }
    public static String getUrlRequest(Context context){
        setUrlRequest(context);
        SharedPreferences preferences = context.getSharedPreferences("MiArchivoDePreferencias", Context.MODE_PRIVATE);
        return preferences.getString("urlGuardada", "");
    }

    public static void saveCheckboxState(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getCheckboxState(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }
    public static void saveLang(Context context, String lang) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LANG, lang);
        editor.apply();
    }

    public static String getLang(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANG, null);
    }

}
