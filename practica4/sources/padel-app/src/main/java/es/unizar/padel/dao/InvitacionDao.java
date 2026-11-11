package es.unizar.padel.dao;

import java.util.List;
import java.util.Optional;

import es.unizar.padel.vo.Invitacion;


public interface InvitacionDao {
    Invitacion create(Invitacion invitacion);

    Optional<Invitacion> findById(long id_usuario,Long id_partido);

    List<Invitacion> findAll();

    /** @return true si el usuario existía y se modificó */
    boolean update(Invitacion inv);

    /** @return true si el usuario existía y se borró */
    boolean delete(long id_usuario, Long id_partido);
}
