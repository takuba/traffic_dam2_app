package com.example.traficoreto.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class AlertDialogHelper {

    public static void showCheckboxDialog(Context context, String title, String[] items, RecyclerView recyclerView, Activity listscreen,String urlRequest,String userId) {
        boolean[] checkedItems = new boolean[items.length]; // Para mantener el estado de los checkboxes
        for (int i = 0; i < items.length; i++) {
            checkedItems[i] = MyPreferences.getCheckboxState(context, "checkbox_" + i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // Manejar el clic en un checkbox
                        checkedItems[which] = isChecked;
                        if (isChecked) {
                            // Marcar el checkbox seleccionado
                            checkedItems[which] = true;

                            // Desactivar los demás checkboxes
                            for (int i = 0; i < items.length; i++) {
                                if (i != which) {
                                    ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                    checkedItems[i] = false;
                                }
                            }
                        }
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Código a ejecutar al hacer clic en Aceptar
                        // Puedes acceder a los estados de los checkboxes a través del array checkedItems
                        for (int i = 0; i < checkedItems.length; i++) {
                            MyPreferences.saveCheckboxState(context, "checkbox_" + i, checkedItems[i]);
                           // Log.d("com.example.traficoreto.fernando", String.valueOf(checkedItems[0]));
                            if (checkedItems[i]) {
                                // El checkbox en la posición i está marcado
                                // Realiza la lógica correspondiente
                               // Toast.makeText(context, "Checkbox en la posición " + i + " marcado", Toast.LENGTH_SHORT).show();
                                // examn.setText("ox en la posici");
                                switch(i) {
                                    case 0:
                                        Log.d("com.example.traficoreto.fernando", "entróoo al case 0");
                                        UsersHelpers.getFavourites("24","cameras",urlRequest+"fav/"+userId+"/cameras",context,recyclerView);
                                        break;
                                    case 1:
                                        Log.d("com.example.traficoreto.fernando", "entróoo al case 1");
                                        UsersHelpers.getFavourites2("24","incidences",urlRequest+"fav/"+userId+"/incidences",context,recyclerView);
                                        break;
                                    case 2:
                                        UsersHelpers.getFavourites3("24","flowmeter",urlRequest+"fav/"+userId+"/flowmeter",context,recyclerView);
                                        break;
                                    default:
                                        //Log.d("com.example.traficoreto.fernando", "selecionado nada");
                                }
                            } else if (!checkedItems[0]&&!checkedItems[1]&&!checkedItems[2]) {
                                HelperFuntions.getAllItem("cameras","1",recyclerView,urlRequest,context,userId);
                                //Log.d("com.example.traficoreto.fernando", "entro al checkedItems 0 else if de alert");
                            }
                        }
                        dialog.dismiss(); // Cierra el cuadro de diálogo
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Restaurar el estado anterior de los checkboxes en caso de cancelación
                        for (int i = 0; i < items.length; i++) {
                            checkedItems[i] = MyPreferences.getCheckboxState(context, "checkbox_" + i);
                        }
                        // Código a ejecutar al hacer clic en Cancelar
                        dialog.dismiss(); // Cierra el cuadro de diálogo
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // Restaurar el estado anterior de los checkboxes en caso de cierre del diálogo
                        for (int i = 0; i < items.length; i++) {
                            checkedItems[i] = MyPreferences.getCheckboxState(context, "checkbox_" + i);
                        }
                    }
                })
                .show();
    }
    public static void changeLanguage(Context context, String title, String[] items, Activity settingScreen) {
        boolean[] checkedItems = new boolean[items.length]; // Para mantener el estado de los checkboxes
        for (int i = 0; i < items.length; i++) {
            checkedItems[i] = MyPreferences.getCheckboxState(context, "checkbox_" + i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // Manejar el clic en un checkbox
                        checkedItems[which] = isChecked;
                        if (isChecked) {
                            // Marcar el checkbox seleccionado
                            checkedItems[which] = true;

                            // Desactivar los demás checkboxes
                            for (int i = 0; i < items.length; i++) {
                                if (i != which) {
                                    ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                    checkedItems[i] = false;
                                }
                            }
                        }
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Código a ejecutar al hacer clic en Aceptar
                        // Puedes acceder a los estados de los checkboxes a través del array checkedItems
                        for (int i = 0; i < checkedItems.length; i++) {
                            //MyPreferences.saveCheckboxState(context, "checkbox_" + i, checkedItems[i]);
                            // Log.d("com.example.traficoreto.fernando", String.valueOf(checkedItems[0]));
                            if (checkedItems[i]) {
                                // El checkbox en la posición i está marcado
                                switch(i) {
                                    case 0:
                                        Log.d("com.example.traficoreto.fernando.lang", Locale.getDefault().getDisplayLanguage());

                                        Log.d("com.example.traficoreto.fernando.lang", "entróoo al case 0");
                                        if (Locale.getDefault().getDisplayLanguage().equals("English")){
                                            Log.d("com.example.traficoreto.fernando.lang", "entróoo al case 0 if");
                                            MyPreferences.saveLang(context,"es");
                                            UsersHelpers.seLanguage(context,"es");
                                            settingScreen.recreate();
                                        }
                                        //UsersHelpers.getFavourites("24","cameras",urlRequest+"fav/"+userId+"/cameras",context,recyclerView);
                                        break;
                                    case 1:
                                        Log.d("com.example.traficoreto.fernando.lang", Locale.getDefault().getDisplayLanguage());
                                        if (Locale.getDefault().getDisplayLanguage().equals("español")){
                                            Log.d("com.example.traficoreto.fernando", "entróoo al case 1 if");
                                            MyPreferences.saveLang(context,"en");
                                            UsersHelpers.seLanguage(context,"en");
                                            settingScreen.recreate();
                                        }
                                        //UsersHelpers.getFavourites2("24","incidences",urlRequest+"fav/"+userId+"/incidences",context,recyclerView);
                                        break;
                                    default:
                                        //Log.d("com.example.traficoreto.fernando", "selecionado nada");
                                }
                            } else if (!checkedItems[0]&&!checkedItems[1]&&!checkedItems[2]) {
                                //HelperFuntions.getAllItem("cameras","1",recyclerView,urlRequest,context,userId);
                                //Log.d("com.example.traficoreto.fernando", "entro al checkedItems 0 else if de alert");
                            }
                        }
                        dialog.dismiss(); // Cierra el cuadro de diálogo
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Restaurar el estado anterior de los checkboxes en caso de cancelación
                        for (int i = 0; i < items.length; i++) {
                            //checkedItems[i] = MyPreferences.getCheckboxState(context, "checkbox_" + i);
                        }
                        // Código a ejecutar al hacer clic en Cancelar
                        dialog.dismiss(); // Cierra el cuadro de diálogo
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // Restaurar el estado anterior de los checkboxes en caso de cierre del diálogo
                        for (int i = 0; i < items.length; i++) {
                           // checkedItems[i] = MyPreferences.getCheckboxState(context, "checkbox_" + i);
                        }
                    }
                })
                .show();
    }
}
