package com.example.tarea_7;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView RVD;
    private DeberesAdapter deberesAdapter;
    private ArrayList<Deberes> listaDeberes;
    private Button addButton;


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
        //Ejemplo para empezar con una planta la App
        listaDeberes.add(new Deberes("Trabajo final",
                                "Hacer lista de deberes",
                                "PMDM",
                                "20-12-2024",
                                "20:00",
                                false));

        // Instanciar y setear un adaptador para integrar la vista del item como base de la lista del recyclerView
        deberesAdapter = new DeberesAdapter(listaDeberes, this); // Pasamos el contexto actual que usaremos en el toast de borrar planta
        RVD.setAdapter(deberesAdapter);
        // Localizamos el boton de añadir plantas
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

        Button btnConfirmar = dialogView.findViewById(R.id.btnAceptar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);

        // Configurar el evento del botón confirmar
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = etTitulo.getText().toString();
                String descripcion = etDescripcion.getText().toString();
                String asignatura = spnAsignatura.getSelectedItem().toString();


                // Crear y agregar la nueva tarea
                Deberes nuevaTarea = new Deberes();
                listaDeberes.add(nuevaTarea);

                // Ordenar el ArrayList según el tiempo de riego
                ordenarPlantasPorTiempoRiego();

                // Notificar al adaptador después de actualizar la lista
                plantaAdapter.notifyDataSetChanged();

                // Scroll al inicio si es necesario
                recyclerView.scrollToPosition(0);

                // Cerrar el cuadro de diálogo
                dialog.dismiss();

            }
        });
        // Mostrar el cuadro de diálogo
        dialog.show();
    }
}