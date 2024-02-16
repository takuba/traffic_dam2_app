package com.example.traficoreto;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import com.example.traficoreto.helpers.MyPreferences;

import java.util.Locale;

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(updateBaseContextLocale(base));
    }

    private static Context updateBaseContextLocale(Context context) {
        String lang = MyPreferences.getLang(context);

        if (lang.equals("es")){
            Log.d("com.example.traficoreto.fernando.lang", "es mu app");
            // Aquí configuras el idioma deseado, por ejemplo, español ("es")
            String languageCode = "es";
            Locale locale = new Locale(languageCode);
            Locale.setDefault(locale);

            Configuration configuration = new Configuration();
            configuration.setLocale(locale);
            Resources resources = context.getResources();
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return context.createConfigurationContext(configuration);
        }else {
            Log.d("com.example.traficoreto.fernando.lang", "en mu app2");
            String languageCode = "en";
            Locale locale = new Locale(languageCode);
            Locale.setDefault(locale);

            Configuration configuration = new Configuration();
            configuration.setLocale(locale);
            Resources resources = context.getResources();
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return context.createConfigurationContext(configuration);
        }


    }

    // Método estático para configurar el idioma
    public static void setLanguage(Context context) {
        context = updateBaseContextLocale(context);
    }
}
