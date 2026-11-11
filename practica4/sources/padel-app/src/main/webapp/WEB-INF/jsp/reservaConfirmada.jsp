<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reserva Confirmada</title>
    <link rel="stylesheet" href="estilos-reserva.css">
</head>
<body>
    <div class="checkout-container">
        <div class="checkout-card">
            <div class="checkout-image-container">
                <img src="PISTA-7-scaled.jpg" alt="Pista de pádel" class="checkout-image">
            </div>
            <div class="checkout-content">
                <h2>¡Reserva confirmada!</h2>
                <div class="booking-info">
                    <p><strong>Club:</strong> <c:out value="${club.nombre}"/></p>
                    <p><strong>Pista:</strong> Pista ${numero}</p>
                    <p><strong>Fecha:</strong> ${horario.fecha}</p>
                    <p><strong>Hora:</strong> ${horario.horaInicio} - ${horario.horaFin} h</p>
                    <p><strong>Precio:</strong> ${horario.precio} €</p>
                </div>
                <a href="partidos" class="btn-checkout" style="text-align:center; text-decoration:none;">Ver mis partidos</a>
                <a href="principalRegistrado.html" class="btn-cancel">Volver al inicio</a>
            </div>
        </div>
    </div>
</body>
</html>
