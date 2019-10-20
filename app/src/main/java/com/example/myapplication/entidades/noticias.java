package com.example.myapplication.entidades;

public class noticias {

    private String idNoticia, tituloNoticia, cuerpoNoticia, fechaNoticia, userNoticia, photoNoticia;
    private boolean isHeader;

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getIdNoticia() {
        return idNoticia;
    }

    public void setIdNoticia(String idNoticia) {
        this.idNoticia = idNoticia;
    }

    public String getTituloNoticia() {
        return tituloNoticia;
    }

    public void setTituloNoticia(String tituloNoticia) {
        this.tituloNoticia = tituloNoticia;
    }

    public String getCuerpoNoticia() {
        return cuerpoNoticia;
    }

    public void setCuerpoNoticia(String cuerpoNoticia) {
        this.cuerpoNoticia = cuerpoNoticia;
    }

    public String getFechaNoticia() {
        return fechaNoticia;
    }

    public void setFechaNoticia(String fechaNoticia) {
        this.fechaNoticia = fechaNoticia;
    }

    public String getUserNoticia() {
        return userNoticia;
    }

    public void setUserNoticia(String userNoticia) {
        this.userNoticia = userNoticia;
    }

    public String getPhotoNoticia() {
        return photoNoticia;
    }

    public void setPhotoNoticia(String photoNoticia) {
        this.photoNoticia = photoNoticia;
    }
}
