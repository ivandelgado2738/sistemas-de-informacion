<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Horarios</title>
    <link rel="stylesheet" href="estilos.css">
</head>
<body>
    <%@ include file="cabecera.jspf" %>

    <section class="lsita-cuadricula">
        <div class="section-header">
            <h2 class="titulo-scroll"><c:out value="${club.nombre}"/> · Pista ${numero} · Horarios libres</h2>
            <a href="club?id=${idClub}" class="see-all-link">← Pistas del club</a>
        </div>
        <c:if test="${empty horarios}">
            <p>No hay horarios libres para esta pista.</p>
        </c:if>
        <div class="cards-grid">
            <c:forEach var="h" items="${horarios}">
                <a href="reserva?club=${idClub}&numero=${numero}&fecha=${h.fechaIso}&hora=${h.horaInicio}" class="link-tarjeta-pista">
                    <div class="tarjeta-pista">
                        <h3>${h.horaInicio}h - ${h.horaFin}h</h3>
                        <p>${h.fecha} · ${h.precio} €</p>
                    </div>
                </a>
            </c:forEach>
        </div>
    </section>
</body>
</html>
