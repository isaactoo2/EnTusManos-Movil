package com.example.myapplication.entidades;

public class foros {
    private String idForo, tituloForo, cuerpoForo, userForo, categoriaForo, fechaForo, idUserForo;
    private boolean isHeader;

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getIdUserForo() {
        return idUserForo;
    }

    public void setIdUserForo(String idUserForo) {
        this.idUserForo = idUserForo;
    }

    public String getIdForo() {
        return idForo;
    }

    public void setIdForo(String idForo) {
        this.idForo = idForo;
    }

    public String getTituloForo() {
        return tituloForo;
    }

    public void setTituloForo(String tituloForo) {
        this.tituloForo = tituloForo;
    }

    public String getCuerpoForo() {
        return cuerpoForo;
    }

    public void setCuerpoForo(String cuerpoForo) {
        this.cuerpoForo = cuerpoForo;
    }

    public String getUserForo() {
        return userForo;
    }

    public void setUserForo(String userForo) {
        this.userForo = userForo;
    }

    public String getCategoriaForo() {
        return categoriaForo;
    }

    public void setCategoriaForo(String categoriaForo) {
        this.categoriaForo = categoriaForo;
    }

    public String getFechaForo() {
        return fechaForo;
    }

    public void setFechaForo(String fechaForo) {
        this.fechaForo = fechaForo;
    }
}
