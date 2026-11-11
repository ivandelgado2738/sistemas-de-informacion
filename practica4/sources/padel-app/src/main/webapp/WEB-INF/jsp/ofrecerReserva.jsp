<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ofrecer Reserva</title>
    <link rel="stylesheet" href="estilos-login.css">
    <link rel="stylesheet" href="estilos-admin.css">
</head>
<body>

    <div class="contenedor-login">
        <p style="text-align:center;"><a href="principalAdmin.html">← Panel de administrador</a></p>
        <h2>Ofrecer reserva de pistas</h2>

        <c:if test="${not empty error}">
            <p class="mensaje mensaje-error"><c:out value="${error}"/></p>
        </c:if>

        <form action="ofrecerReserva" method="post">
            <div class="input-group">
                <label for="pista">Pista:</label>
                <select id="pista" name="pista" required>
                    <option value="">-- seleccione una pista --</option>
                    <c:forEach var="p" items="${pistas}">
                        <option value="${p.valor}"><c:out value="${p.texto}"/></option>
                    </c:forEach>
                </select>
            </div>

            <div class="input-group">
                <label for="fecha">Fecha:</label>
                <input type="date" id="fecha" name="fecha" required>
            </div>

            <div class="input-group">
                <label for="horaInicio">Hora inicio:</label>
                <input type="time" id="horaInicio" name="horaInicio" required>
            </div>

            <div class="input-group">
                <label for="horaFin">Hora fin:</label>
                <input type="time" id="horaFin" name="horaFin" required>
            </div>

            <div class="input-group">
                <label for="precio">Precio (€):</label>
                <input type="number" id="precio" name="precio" min="0" step="1" required>
            </div>

            <button type="submit" class="btn-login">Publicar reserva</button>
            <p style="text-align:center; margin-top: 14px;">
                <a href="verReservas">Cancelar</a>
            </p>
        </form>
    </div>

</body>
</html>
