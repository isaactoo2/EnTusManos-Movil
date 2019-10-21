package com.example.myapplication.entidades;

public class asesoria {
    private String idAsesoria, tituloAsesoria, cuerpoAsesoria, especialidad;
    private boolean isHeader;

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getIdAsesoria() {
        return idAsesoria;
    }

    public void setIdAsesoria(String idAsesoria) {
        this.idAsesoria = idAsesoria;
    }

    public String getTituloAsesoria() {
        return tituloAsesoria;
    }

    public void setTituloAsesoria(String tituloAsesoria) {
        this.tituloAsesoria = tituloAsesoria;
    }

    public String getCuerpoAsesoria() {
        return cuerpoAsesoria;
    }

    public void setCuerpoAsesoria(String cuerpoAsesoria) {
        this.cuerpoAsesoria = cuerpoAsesoria;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
