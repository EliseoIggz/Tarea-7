package com.example.tarea_7;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private RecyclerView RVD;
    private DeberesAdapter deberesAdapter;
    private ArrayList<Deberes> listaDeberes;
    private FloatingActionButton addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RVD = findViewById(R.id.RVDeberes);
        RVD.setLayoutManager(new LinearLayoutManager(this));
        RVD.setHasFixedSize(true);

        listaDeberes = new ArrayList<>();
        //Ejemplo para empezar con una tarea asignada
        listaDeberes.add(new Deberes("Trabajo final",
                                "Hacer lista de deberes",
                                "PMDM",
                                "20-12-2024",
                                "20:00",
                                false));

        // Instanciar y setear un adaptador para integrar la vista del item como base de la lista del recyclerView
        deberesAdapter = new DeberesAdapter(listaDeberes); // Pasamos el contexto actual que usaremos en el toast de borrar planta
        RVD.setAdapter(deberesAdapter);
        // Localizamos el boton de añadir deberes
        addButton = findViewById(R.id.addButton);
        // Configurar el evento de clic del botón agregar
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAgregarDeberes();
            }
        });
    }

    private void mostrarDialogoAgregarDeberes() {
        // Inflar el layout del diálogo
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View dialogView = inflater.inflate(R.layout.dialogo_deberes, null);

        // Crear el cuadro de diálogo
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).setCancelable(true).create();

        // Referenciar los campos y el botón del diálogo
        EditText etTitulo = dialogView.findViewById(R.id.etTitulo);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);
        Spinner spnAsignatura = dialogView.findViewById(R.id.spinnerAsignatura);
        EditText etFecha = dialogView.findViewById(R.id.etFecha);
        EditText etHora = dialogView.findViewById(R.id.etHora);
        CheckBox cbEstado = dialogView.findViewById(R.id.checkboxEstado);

        Button btnConfirmar = dialogView.findViewById(R.id.btnAceptar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);

        // Configurar el Datepicker con su evento al clicar el editText
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
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
                MainActivity.this,
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

        // Configurar el evento del botón confirmar
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = etTitulo.getText().toString();
                String descripcion = etDescripcion.getText().toString();
                String asignatura = spnAsignatura.getSelectedItem().toString();
                String fecha = etFecha.getText().toString();
                String hora = etHora.getText().toString();
                Boolean estado = cbEstado.isChecked();

                // Crear y agregar la nueva tarea
                Deberes nuevaTarea = new Deberes(titulo, descripcion, asignatura, fecha, hora, estado);
                listaDeberes.add(nuevaTarea);

                // Ordenar el ArrayList según la asignatura
                ordenarDeberesPorAsignatura();

                // Notificar al adaptador después de actualizar la lista
                deberesAdapter.notifyDataSetChanged();

                // Scroll al inicio si es necesario
                RVD.scrollToPosition(0);

                // Cerrar el cuadro de diálogo
                dialog.dismiss();

            }
        });
        // Mostrar el cuadro de diálogo
        dialog.show();
    }

    // Ordena los elementos de la lista por orden alfabetico segun la asignatura
    private void ordenarDeberesPorAsignatura() {
        Collections.sort(listaDeberes, new java.util.Comparator<Deberes>() {
            @Override
            public int compare(Deberes p1, Deberes p2) {
                return p1.getAsignatura().compareTo(p2.getAsignatura());
            }
        });
    }
}