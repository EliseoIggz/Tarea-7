package com.example.tarea_7;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DeberesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Deberes>> listaDeberes;

    public DeberesViewModel() {
        listaDeberes = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<ArrayList<Deberes>> getListaDeberes() {
        return listaDeberes;
    }

    public void agregarDeber(Deberes deber) {
        ArrayList<Deberes> listaActual = new ArrayList<>(listaDeberes.getValue());
        listaActual.add(deber);
        listaDeberes.setValue(listaActual);
    }

    public void actualizarDeber(int position, Deberes deberActualizado) {
        ArrayList<Deberes> listaActual = new ArrayList<>(listaDeberes.getValue());
        listaActual.set(position, deberActualizado);
        listaDeberes.setValue(listaActual);
    }

    public void eliminarDeber(int position) {
        ArrayList<Deberes> listaActual = new ArrayList<>(listaDeberes.getValue());
        listaActual.remove(position);
        listaDeberes.setValue(listaActual);
    }
}

