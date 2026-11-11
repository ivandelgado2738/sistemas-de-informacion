<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ver Reservas</title>
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="estilos-admin.css">
</head>
<body>

    <div class="panel-admin">
        <a class="btn-volver" href="principalAdmin.html" style="margin-bottom:16px;">← Panel de administrador</a>
        <h1>Reservas de pistas</h1>

        <c:choose>
            <c:when test="${empty clubes}">
                <p>No hay ningún club en la base de datos.</p>
            </c:when>
            <c:otherwise>
                <%-- Selector de club: al cambiarlo se recarga la página con ?club=ID --%>
                <form method="get" action="verReservas">
                    <label for="club">Club:</label>
                    <select id="club" name="club" onchange="this.form.submit()">
                        <c:forEach var="cl" items="${clubes}">
                            <option value="${cl.idClub}" ${cl.idClub == idClub ? 'selected' : ''}>
                                <c:out value="${cl.nombre}"/>
                            </option>
                        </c:forEach>
                    </select>
                </form>

                <p>
                    <a class="btn-admin" href="ofrecerReserva">Ofrecer Reserva</a>
                </p>

                <c:choose>
                    <c:when test="${empty filas}">
                        <p>Este club no tiene horarios ofertados.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="tabla-admin">
                            <tr>
                                <th>Pista</th>
                                <th>Fecha</th>
                                <th>Horario</th>
                                <th>Precio</th>
                                <th>Jugadores</th>
                                <th>Estado</th>
                                <th>Pagos</th>
                                <th>Cancelar</th>
                            </tr>

                            <c:forEach var="f" items="${filas}">
                                <tr>
                                    <td>Pista ${f.numeroPista}</td>
                                    <td>${f.fecha}</td>
                                    <td>${f.horaInicio} - ${f.horaFin}</td>
                                    <td>${f.precio} €</td>
                                    <td>${f.jugadores} / 4</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${f.reservada}">
                                                <span class="estado estado-pagado">Reservada</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="estado estado-inactivo">Sin reservar</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${f.reservada}">
                                            <a class="btn-admin" href="marcarPagos?reserva=${f.idReserva}">Ver Pagos</a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <form method="post" action="cancelarHorario" style="margin:0;">
                                            <input type="hidden" name="club" value="${idClub}">
                                            <input type="hidden" name="pista" value="${f.numeroPista}">
                                            <input type="hidden" name="fecha" value="${f.fechaIso}">
                                            <input type="hidden" name="hora" value="${f.horaInicio}">
                                            <button type="submit" class="btn-admin btn-admin-secundario">Cancelar</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </div>

</body>
</html>
