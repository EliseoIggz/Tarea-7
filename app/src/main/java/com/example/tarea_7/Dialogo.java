package com.example.tarea_7;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Dialogo extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Construir el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Inflar el layout del diálogo con el xml que hemos creado antes
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialogo_deberes, null);
        builder.setView(dialogView);

        // Establece el título
        builder.setTitle("Crear tarea");

        // Referenciar los campos y el botón del diálogo
        EditText etTitulo = dialogView.findViewById(R.id.etTitulo);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);
        Spinner spnAsignatura = dialogView.findViewById(R.id.spinnerAsignatura);
        EditText etFecha = dialogView.findViewById(R.id.etFecha);
        EditText etHora = dialogView.findViewById(R.id.etHora);
        CheckBox cbEstado = dialogView.findViewById(R.id.checkboxEstado);

        // Configurar el Datepicker con su evento al clicar el editText
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireActivity(),
                        (viewFecha, year, month, dayOfMonth) -> {
                            etFecha.setText(dayOfMonth + "-" + (month+1) + "-" + year);
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH) // Año, Mes, Día Actuales
                );
                datePickerDialog.show();
            }
        });

        // Configurar el Timepicker con su evento al clicar el editText
        etHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        requireActivity(),
                        (viewHora, hourOfDay, minute) -> {
                            etHora.setText(hourOfDay + ":" + minute);
                        },
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE),
                        true // Hora inicial, Minuto inicial, formato 24h
                );
                timePickerDialog.show();
            }
        });


        // Añadir botones de aceptar y cancelar
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Ejecutar al pulsar el botón aceptar
                        String titulo = etTitulo.getText().toString();
                        String descripcion = etDescripcion.getText().toString();
                        String asignatura = spnAsignatura.getSelectedItem().toString();
                        String fecha = etFecha.getText().toString();
                        String hora = etHora.getText().toString();
                        Boolean estado = cbEstado.isChecked();

                        // Crear y agregar la nueva tarea
                        Deberes nuevaTarea = new Deberes(titulo, descripcion, asignatura, fecha, hora, estado);

                        //Llamar al método agregarTarea() en MainActivity para agregar la nueva tarea desde el dialog
                        ((MainActivity) getActivity()).agregarTarea(nuevaTarea);

                        // Cerrar el cuadro de diálogo
                        dialog.dismiss();
                    }
                    // Mostrar el cuadro de diálogo
                    //        dialog.show();
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Ejecutar al pulsar el botón cancelar
                        dialog.cancel();
                    }
                });

        // Devuelve el diálogo
        return builder.create();

    }
}
