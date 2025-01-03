package com.example.tarea_7;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
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
        AlertDialog.Builder dialogoTarea = new AlertDialog.Builder(getActivity());
        // Inflar el layout del diálogo con el xml que hemos creado antes
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialogo_deberes, null);
        dialogoTarea.setView(dialogView);

        // Establece el título
        dialogoTarea.setTitle("Tarea");

        // Referenciar los campos y el botón del diálogo
        EditText etTitulo = dialogView.findViewById(R.id.etTitulo);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);
        Spinner spnAsignatura = dialogView.findViewById(R.id.spinnerAsignatura);
        EditText etFecha = dialogView.findViewById(R.id.etFecha);
        EditText etHora = dialogView.findViewById(R.id.etHora);
        CheckBox cbEstado = dialogView.findViewById(R.id.checkboxEstado);

        Bundle args = getArguments();
        Deberes tareaSeleccionada = args != null ? args.getParcelable("tareaSeleccionada") : null;

        if (tareaSeleccionada != null) {
            etTitulo.setText(tareaSeleccionada.getTitulo());
            etDescripcion.setText(tareaSeleccionada.getDescripcion());
            // Configurar el Spinner para que tenga la asignatura de tareaSeleccionada
            String asignatura = tareaSeleccionada.getAsignatura();
            ArrayAdapter adapter = (ArrayAdapter) spnAsignatura.getAdapter();
            int position = adapter.getPosition(asignatura);
            if (position >= 0) { // Asegurarse de que la asignatura existe en el adaptador
                spnAsignatura.setSelection(position);
            }
            etFecha.setText(tareaSeleccionada.getFecha()); // Asume que el formato es correcto (e.g., "YYYY-MM-DD")
            etHora.setText(tareaSeleccionada.getHora()); // Asume que el formato es correcto (e.g., "HH:MM")
            cbEstado.setChecked(tareaSeleccionada.isEstado());
        }

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
        dialogoTarea.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
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
                        //((MainActivity) getActivity()).agregarTarea(nuevaTarea);
                        // Preguntar a Miguel si se puede hacer asi

                        // Crear un Intent y enviar la tarea como Parcelable
                        Bundle result = new Bundle();
                        result.putParcelable("nuevaTarea", nuevaTarea);
                        getParentFragmentManager().setFragmentResult("tareaKey", result);

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
        return dialogoTarea.create();

    }
}
