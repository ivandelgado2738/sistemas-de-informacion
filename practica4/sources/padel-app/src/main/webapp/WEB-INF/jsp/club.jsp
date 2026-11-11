<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${club.nombre}"/></title>
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="estilos-admin.css">
</head>
<body>
    <%@ include file="cabecera.jspf" %>

    <section class="lsita-cuadricula">
        <div class="section-header">
            <h2 class="titulo-scroll"><c:out value="${club.nombre}"/> · Pistas</h2>
            <a href="clubes" class="see-all-link">← Todos los clubes</a>
        </div>
        <p><c:out value="${club.direccion}"/> · Tel. <c:out value="${club.telefono}"/></p>

        <c:if test="${not empty error}">
            <p style="color:#c0392b;"><c:out value="${error}"/></p>
        </c:if>
        <c:if test="${not empty sessionScope.usuario}">
            <form method="post" action="favoritos" style="margin-bottom:16px;">
                <input type="hidden" name="club" value="${club.idClub}">
                <input type="hidden" name="volver" value="club?id=${club.idClub}">
                <input type="hidden" name="accion" value="${esFavorito ? 'quitar' : 'marcar'}">
                <button type="submit" class="auth-btn btn-primary" style="border:1px solid green; cursor:pointer;">
                    ${esFavorito ? '★ Quitar de favoritos' : '☆ Añadir a favoritos'}
                </button>
            </form>
        </c:if>
        <c:if test="${empty pistas}">
            <p>Este club no tiene pistas.</p>
        </c:if>
        <div class="cards-grid">
            <c:forEach var="p" items="${pistas}">
                <a href="pista?club=${p.idClub}&numero=${p.numero}" class="link-tarjeta-pista">
                    <div class="tarjeta-pista">
                        <img src="PISTA-7-scaled.jpg" alt="Foto de la pista" class="foto-pista">
                        <h3>Pista ${p.numero}</h3>
                        <p>${p.cubierta ? 'Cubierta' : 'Exterior'}</p>
                    </div>
                </a>
            </c:forEach>
        </div>
    </section>
</body>
</html>
