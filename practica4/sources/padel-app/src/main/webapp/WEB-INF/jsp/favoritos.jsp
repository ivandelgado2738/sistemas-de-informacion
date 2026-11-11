<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clubes favoritos</title>
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="estilos-admin.css">
</head>
<body>
    <%@ include file="cabecera.jspf" %>

    <section class="lsita-cuadricula">
        <div class="section-header">
            <h2 class="titulo-scroll">Clubes favoritos</h2>
            <a href="clubes" class="see-all-link">Ver todos los clubes</a>
        </div>

        <c:if test="${not empty error}">
            <p class="mensaje mensaje-error"><c:out value="${error}"/></p>
        </c:if>
        <c:if test="${empty clubes}">
            <p>Aún no tienes clubes favoritos. Entra en un <a href="clubes">club</a> y pulsa «Añadir a favoritos».</p>
        </c:if>

        <div class="cards-grid">
            <c:forEach var="cl" items="${clubes}">
                <div class="tarjeta-pista">
                    <a href="club?id=${cl.idClub}" class="link-tarjeta-pista">
                        <img src="Bendinat-Urban-Country-Club1.jpg" alt="Foto del club" class="foto-pista">
                        <h3><c:out value="${cl.nombre}"/></h3>
                        <p><c:out value="${cl.direccion}"/></p>
                    </a>
                    <form method="post" action="favoritos" style="margin:0;">
                        <input type="hidden" name="club" value="${cl.idClub}">
                        <input type="hidden" name="accion" value="quitar">
                        <input type="hidden" name="volver" value="favoritos">
                        <button type="submit" class="btn-admin btn-admin-secundario">Quitar de favoritos</button>
                    </form>
                </div>
            </c:forEach>
        </div>
    </section>
</body>
</html>
