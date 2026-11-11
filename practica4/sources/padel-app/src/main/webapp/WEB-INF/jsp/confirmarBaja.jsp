<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmar Baja</title>
    <link rel="stylesheet" href="estilos-login.css">
</head>
<body>
    <div class="contenedor-login">
        <h2>Confirmar Baja</h2>

        <c:if test="${not empty error}">
            <p style="color:#c0392b; text-align:center;"><c:out value="${error}"/></p>
        </c:if>

        <p style="text-align:center;">
            ¿Seguro que quieres darte de baja? Se borrarán tu cuenta, tus reservas y tus favoritos.
            Esta acción no se puede deshacer.
        </p>

        <form action="baja" method="post">
            <div class="input-group">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password"
                       placeholder="Introduce tu contraseña para confirmar" required>
            </div>
            <button type="submit" class="btn-login">Confirmar baja</button>
            <p class="signup-link"><a href="perfil">Cancelar</a></p>
        </form>
    </div>
</body>
</html>
