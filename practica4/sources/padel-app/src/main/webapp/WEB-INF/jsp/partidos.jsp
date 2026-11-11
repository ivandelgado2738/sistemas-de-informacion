<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis partidos</title>
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="estilos-admin.css">
</head>
<body>
    <%@ include file="cabecera.jspf" %>

    <section class="lsita-cuadricula">
        <div class="section-header">
            <h2 class="titulo-scroll">Mis partidos</h2>
            <a href="clubes" class="see-all-link">Reservar otra pista</a>
        </div>

        <c:if test="${not empty mensaje}">
            <p class="mensaje mensaje-exito"><c:out value="${mensaje}"/></p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="mensaje mensaje-error"><c:out value="${error}"/></p>
        </c:if>

        <c:if test="${empty partidos}">
            <p>Todavía no tienes partidos. <a href="clubes">Reserva una pista</a>.</p>
        </c:if>

        <div class="cards-grid">
            <c:forEach var="p" items="${partidos}">
                <div class="tarjeta-pista">
                    <h3>${p.horaInicio}h - ${p.horaFin}h</h3>
                    <p><strong>${p.fecha}</strong></p>
                    <p><c:out value="${p.club}"/> · Pista ${p.numeroPista}</p>
                    <p>${p.jugadores} / 4 jugadores · ${p.privado ? 'Privado' : 'Abierto'} · ${p.precio} €</p>

                    <c:if test="${p.aceptados > 0}">
                        <p class="mensaje mensaje-error">
                            Cancelación en curso: ${p.aceptados} de ${p.jugadores} jugadores han aceptado
                        </p>
                    </c:if>

                    <c:choose>
                        <c:when test="${p.yoHeAceptado}">
                            <p><em>Ya has aceptado cancelar. Esperando al resto.</em></p>
                        </c:when>
                        <c:otherwise>
                            <form method="post" action="cancelarReserva" style="margin:0;">
                                <input type="hidden" name="reserva" value="${p.idReserva}">
                                <button type="submit" class="btn-admin btn-admin-secundario">
                                    ${p.jugadores > 1 ? 'Aceptar cancelación' : 'Cancelar reserva'}
                                </button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
    </section>
</body>
</html>
