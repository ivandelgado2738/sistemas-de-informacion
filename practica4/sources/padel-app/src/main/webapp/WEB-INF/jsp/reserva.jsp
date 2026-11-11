<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmar Reserva</title>
    <link rel="stylesheet" href="estilos-reserva.css">
</head>
<body>
    <div class="checkout-container">
        <div class="checkout-card">
            <div class="checkout-image-container">
                <img src="PISTA-7-scaled.jpg" alt="Pista de pádel" class="checkout-image">
            </div>
            <div class="checkout-content">
                <h2>Confirmar Reserva</h2>

                <c:if test="${not empty error}">
                    <p style="color:#c0392b;"><c:out value="${error}"/></p>
                </c:if>

                <div class="booking-info">
                    <p><strong>Club:</strong> <c:out value="${club.nombre}"/></p>
                    <p><strong>Pista:</strong> Pista ${numero} (${pista.cubierta ? 'cubierta' : 'exterior'})</p>
                    <p><strong>Fecha:</strong> ${horario.fecha}</p>
                    <p><strong>Hora:</strong> ${horario.horaInicio} - ${horario.horaFin} h</p>
                    <p><strong>Precio:</strong> ${horario.precio} €</p>
                </div>

                <form action="reserva" method="post" class="booking-form">
                    <input type="hidden" name="club" value="${idClub}">
                    <input type="hidden" name="numero" value="${numero}">
                    <input type="hidden" name="fecha" value="${horario.fechaIso}">
                    <input type="hidden" name="hora" value="${horario.horaInicio}">

                    <div class="input-group">
                        <label for="tipo-partido">Tipo de partido</label>
                        <select id="tipo-partido" name="tipo_partido" required>
                            <option value="invitacion">Por invitación (Privado)</option>
                            <option value="abierto">Abierto (Cualquiera se puede unir)</option>
                        </select>
                    </div>

                    <button type="submit" class="btn-checkout">Reservar Pista</button>
                    <a href="pista?club=${idClub}&numero=${numero}" class="btn-cancel">Cancelar</a>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
