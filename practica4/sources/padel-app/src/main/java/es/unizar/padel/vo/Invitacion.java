package es.unizar.padel.vo;

public class Invitacion {
    private final Long id_usuario;
    private final Long id_partido;

    public Invitacion(Long id_usuario,Long id_partido){
        this.id_usuario=id_usuario;
        this.id_partido=id_partido;
    }

    public Long getIdUsuario() {return id_usuario;}
    public Long getIdPartido() {return id_partido;}
}
