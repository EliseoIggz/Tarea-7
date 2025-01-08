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
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
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

        // Iniciamos el RecyvlerView
        RVD = findViewById(R.id.RVDeberes);
        RVD.setLayoutManager(new LinearLayoutManager(this));
        RVD.setHasFixedSize(true);

        listaDeberes = new ArrayList<>();
        // Ejemplo para empezar con una tarea asignada
        listaDeberes.add(new Deberes("Trabajo final",
                                "Hacer lista de deberes",
                                "PMDM",
                                "20-12-2024",
                                "20:00",
                                false));

        // Instanciar y setear un adaptador para integrar la vista del item como base de la lista del recyclerView
        deberesAdapter = new DeberesAdapter(listaDeberes); // Pasamos el contexto actual que usaremos en el toast de borrar la tarea
        RVD.setAdapter(deberesAdapter);
        // Localizamos el boton de añadir deberes
        addButton = findViewById(R.id.addButton);
        // Configurar el evento de clic del botón agregar
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos un dialogo de la clase Dialogo y lo enseñamos
                DialogFragment dialogFragment = new Dialogo();
                dialogFragment.show(getSupportFragmentManager(), "Dialogo tarea");
            }
        });

        // Configurar escucha de resultados del fragmento
        getSupportFragmentManager().setFragmentResultListener("tareaKey", this, (tareaKey, result) -> {
            Deberes nuevaTarea = result.getParcelable("nuevaTarea");
            agregarTarea(nuevaTarea);
        });

        // Configurar el listener del click en un item de la lista para abrir el menu
        deberesAdapter.setOnItemClickListener(position -> {
            showBottomSheetMenu(position);
        });
    }

    public void agregarTarea(Deberes nuevaTarea) {
        // Añadir la nueva tarea a la lista
        listaDeberes.add(nuevaTarea);

        // Ordenar la lista si es necesario
        ordenarDeberesPorAsignatura();

        // Notificar al adaptador que los datos han cambiado
        deberesAdapter.notifyDataSetChanged();
    }

    private void ordenarDeberesPorAsignatura() {
        Collections.sort(listaDeberes, new java.util.Comparator<Deberes>() {
            @Override
            public int compare(Deberes p1, Deberes p2) {
                return p1.getAsignatura().compareTo(p2.getAsignatura());
            }
        });
    }

    private void showBottomSheetMenu(int position) {
        BottomSheetDialog dialogoMenu = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.opciones_dialog, null);
        dialogoMenu.setContentView(bottomSheetView);

        TextView modificar = bottomSheetView.findViewById(R.id.modificarTarea);
        TextView eliminar = bottomSheetView.findViewById(R.id.eliminarTarea);
        TextView cambiarEstado = bottomSheetView.findViewById(R.id.cambiarEstado);

        modificar.setOnClickListener(v -> {
            // Cerrar el menu
            dialogoMenu.dismiss();
            DialogFragment dialogoTarea = new Dialogo();

            // Pasar el objeto seleccionado al diálogo
            Bundle bundleDatos = new Bundle();
            bundleDatos.putParcelable("tareaSeleccionada", listaDeberes.get(position));
            dialogoTarea.setArguments(bundleDatos);

            dialogoTarea.show(getSupportFragmentManager(), "Dialogo tarea");
        });

        eliminar.setOnClickListener(v -> {
            dialogoMenu.dismiss();
            listaDeberes.remove(position);
            deberesAdapter.notifyItemRemoved(position);
        });

        cambiarEstado.setOnClickListener(v -> {
            dialogoMenu.dismiss();
            Deberes deber = listaDeberes.get(position);
            deber.setEstado(!deber.isEstado());
            deberesAdapter.notifyItemChanged(position);
        });

        dialogoMenu.show();
    }
}