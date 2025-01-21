package com.example.tarea_7;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
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
    private Deberes deberesVolcado;


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

//        // Escritura de prueba, borrar despues de la primera ejecucion
//        SQLiteDatabase bdWrite = new BD1(this).getWritableDatabase();
//        // Insertar un usuario mediante ContentValues
//        ContentValues valores = new ContentValues();
//        valores.put("titulo", "Examen 1");
//        valores.put("descripcion", "Hacer entregables 4 y 5");
//        valores.put("asignatura", "PMDM");
//        valores.put("fecha", "28-01-2025");
//        valores.put("hora", "09:00");
//        valores.put("estado", 1);
//        bdWrite.insert("deberes", null, valores);

        //Ejemplo para empezar con una tarea asignada
        listaDeberes = new ArrayList<>();
        // Leer datos de la BD al iniciar
        SQLiteDatabase bdRead = new BD1(this).getReadableDatabase();

        Cursor cursorRead = bdRead.query("deberes", null, null , null, null, null, null);
        if (cursorRead.moveToFirst()) {
            do {
                 long id = cursorRead.getLong(0);
                 String titulo = cursorRead.getString(1);
                 String descripcion = cursorRead.getString(2);
                 String asignatura = cursorRead.getString(3);
                 String fecha = cursorRead.getString(4);
                 String hora = cursorRead.getString(5);
                 boolean estado;
                 if (cursorRead.getInt(6) == 1){
                     estado = true;
                 }else{
                     estado = false;
                 }
                deberesVolcado = new Deberes(titulo, descripcion, asignatura, fecha, hora, estado);
                deberesVolcado.setId(id);
                listaDeberes.add(deberesVolcado);
            } while (cursorRead.moveToNext());
            cursorRead.close();
        }


        // Instanciar y setear un adaptador para integrar la vista del item como base de la lista del recyclerView
        deberesAdapter = new DeberesAdapter(listaDeberes); // Pasamos el contexto actual que usaremos en el toast de borrar planta
        RVD.setAdapter(deberesAdapter);
        // Localizamos el boton de añadir deberes
        addButton = findViewById(R.id.addButton);
        // Configurar el evento de clic del botón agregar
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new Dialogo();
                dialogFragment.show(getSupportFragmentManager(), "Dialogo tarea");
            }
        });

        // Configurar escucha de resultados del fragmento
        getSupportFragmentManager().setFragmentResultListener("tareaKey", this, (tareaKey, result) -> {
            Deberes nuevaTarea = result.getParcelable("nuevaTarea");
            if (nuevaTarea != null) {
                if (nuevaTarea.getId() > 0) {
                    // La tarea ya existe, actualizarla
                    updateBD(nuevaTarea);
                    agregarTarea(nuevaTarea);
                } else {
                    // Es una tarea nueva
                    agregarTarea(nuevaTarea);
                }
            }
        });

        deberesAdapter.setOnItemClickListener(position -> {
            showBottomSheetMenu(position);
        });
    }

    public void agregarTarea(Deberes nuevaTarea) {
        // Escribir la tarea en la BD
        nuevaTarea.setId(writeBD(nuevaTarea));
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
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.opciones_dialog, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView modificar = bottomSheetView.findViewById(R.id.modificarTarea);
        TextView eliminar = bottomSheetView.findViewById(R.id.eliminarTarea);
        TextView cambiarEstado = bottomSheetView.findViewById(R.id.cambiarEstado);

        modificar.setOnClickListener(v -> {
            // Cerrar el menu
            bottomSheetDialog.dismiss();
            DialogFragment dialogFragment = new Dialogo();

            // Pasar el objeto seleccionado al diálogo
            Bundle args = new Bundle();
            args.putParcelable("tareaSeleccionada", listaDeberes.get(position));
            dialogFragment.setArguments(args);

            dialogFragment.show(getSupportFragmentManager(), "Dialogo tarea");
        });

        eliminar.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            //Borrar de la BD
            deleteBD(listaDeberes.get(position).getId());
            listaDeberes.remove(position);
            deberesAdapter.notifyItemRemoved(position);
        });

        cambiarEstado.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Deberes deber = listaDeberes.get(position);
            changeStatus(deber);
            deber.setEstado(!deber.isEstado());
            deberesAdapter.notifyItemChanged(position);
        });

        bottomSheetDialog.show();
    }

    private long writeBD (Deberes tarea){
        SQLiteDatabase bdWrite = new BD1(this).getWritableDatabase();
        // Insertar un usuario mediante ContentValues
        ContentValues valores = new ContentValues();
        valores.put("titulo", tarea.getTitulo());
        valores.put("descripcion", tarea.getDescripcion());
        valores.put("asignatura", tarea.getAsignatura());
        valores.put("fecha", tarea.getFecha());
        valores.put("hora", tarea.getHora());
        if(tarea.isEstado()){
            valores.put("estado", 1);
        }else {
            valores.put("estado", 0);
        }
        long id = bdWrite.insert("deberes", null, valores);
        return id;
    }

    private void deleteBD(long id) {
        SQLiteDatabase bdWrite = new BD1(this).getWritableDatabase();

        // Condición para eliminar registros (WHERE)
        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(id) }; // Convertir el ID a cadena

        // Eliminar registros y devolver el número de filas afectadas
        bdWrite.delete("deberes", whereClause, whereArgs);
    }

    private void updateBD(Deberes tarea) {
        SQLiteDatabase bdWrite = new BD1(this).getWritableDatabase();

        // Crear los valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put("titulo", tarea.getTitulo());
        valores.put("descripcion", tarea.getDescripcion());
        valores.put("asignatura", tarea.getAsignatura());
        valores.put("fecha", tarea.getFecha());
        valores.put("hora", tarea.getHora());
        valores.put("estado", tarea.isEstado() ? 1 : 0);

        // Condición para la actualización (WHERE)
        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(tarea.getId()) };
        bdWrite.update("deberes", valores, whereClause, whereArgs);
        // Cerrar la base de datos
        bdWrite.close();
    }

    private void changeStatus(Deberes tarea){
        SQLiteDatabase bdWrite = new BD1(this).getWritableDatabase();

        // Condición para editar registros (WHERE)
        ContentValues valores = new ContentValues();
        valores.put("estado", !tarea.isEstado() ? 1 : 0);

        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(tarea.getId()) }; // Convertir el ID a cadena

        // Eliminar registros y devolver el número de filas afectadas
        bdWrite.update("deberes", valores, whereClause, whereArgs);
        // Cerrar la base de datos
        bdWrite.close();
    }
}