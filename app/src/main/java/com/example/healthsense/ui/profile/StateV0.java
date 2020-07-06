package com.example.healthsense.ui.profile;

/*
Clase que se encarga de crear una estructura para el manejo de datos dentro de los spinners personalizados */
public class StateV0 {
    private String title;
    private boolean selected;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
