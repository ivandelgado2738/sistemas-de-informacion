<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clubes</title>
    <link rel="stylesheet" href="estilos.css">
</head>
<body>
    <%@ include file="cabecera.jspf" %>

    <section class="lsita-cuadricula">
        <div class="section-header">
            <h2 class="titulo-scroll">Clubes</h2>
        </div>
        <c:if test="${empty clubes}">
            <p>Todavía no hay clubes.</p>
        </c:if>
        <div class="cards-grid">
            <c:forEach var="cl" items="${clubes}">
                <a href="club?id=${cl.idClub}" class="link-tarjeta-pista">
                    <div class="tarjeta-pista">
                        <img src="Bendinat-Urban-Country-Club1.jpg" alt="Foto del club" class="foto-pista">
                        <h3><c:out value="${cl.nombre}"/></h3>
                        <p><c:out value="${cl.direccion}"/></p>
                    </div>
                </a>
            </c:forEach>
        </div>
    </section>
</body>
</html>
