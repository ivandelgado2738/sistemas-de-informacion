<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Marcar Pagos</title>
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="estilos-admin.css">
</head>
<body>

    <div class="panel-admin">
        <a class="btn-volver" href="principalAdmin.html" style="margin-bottom:16px;">← Panel de administrador</a>
        <h1>Pagos del partido</h1>

        <c:if test="${not empty horario}">
            <p><strong><c:out value="${club.nombre}"/> · Pista ${horario.numeroPista}</strong></p>
            <p>${horario.fecha} — Horario: ${horario.horaInicio} - ${horario.horaFin} — Precio total: ${horario.precio} €</p>
        </c:if>

        <c:if test="${not empty error}">
            <p class="mensaje mensaje-error"><c:out value="${error}"/></p>
        </c:if>
        <c:if test="${todosPagado}">
            <p class="mensaje mensaje-exito">Todos los jugadores han pagado.</p>
        </c:if>

        <c:choose>
            <c:when test="${empty jugadores}">
                <p>Este partido no tiene jugadores.</p>
            </c:when>
            <c:otherwise>
                <table class="tabla-admin">
                    <tr>
                        <th>Jugador</th>
                        <th>Email</th>
                        <th>Importe</th>
                        <th>Estado del pago</th>
                        <th>Acción</th>
                    </tr>
                    <c:forEach var="j" items="${jugadores}">
                        <tr>
                            <td><c:out value="${j.nombre}"/></td>
                            <td><c:out value="${j.email}"/></td>
                            <td>${j.importe}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${j.pagado}"><span class="estado estado-pagado">Pagado</span></c:when>
                                    <c:otherwise><span class="estado estado-no-pagado">No pagado</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <form method="post" action="marcarPagos" style="margin:0;">
                                    <input type="hidden" name="reserva" value="${idReserva}">
                                    <input type="hidden" name="usuario" value="${j.idUsuario}">
                                    <input type="hidden" name="accion" value="${j.pagado ? 'nopagado' : 'pagado'}">
                                    <button type="submit" class="btn-admin ${j.pagado ? 'btn-admin-secundario' : ''}">
                                        ${j.pagado ? 'Marcar como no pagado' : 'Marcar como pagado'}
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>

        <a class="btn-volver" href="verReservas">Volver a Reservas</a>
    </div>

</body>
</html>
