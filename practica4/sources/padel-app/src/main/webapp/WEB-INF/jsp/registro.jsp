<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro</title>
    <link rel="stylesheet" href="estilos-login.css">
</head>
<body>
    <div class="contenedor-login">
        <h2>Registro</h2>

        <c:if test="${not empty error}">
            <p style="color:#c0392b; text-align:center;"><c:out value="${error}"/></p>
        </c:if>

        <form action="registro" method="post">
            <div class="input-group">
                <label for="nombre">Nombre y apellidos</label>
                <input type="text" id="nombre" name="nombre" value="<c:out value='${nombre}'/>" required>
            </div>
            <div class="input-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" value="<c:out value='${email}'/>" required>
            </div>
            <div class="input-group">
                <label for="password">Contraseña (mínimo 6 caracteres)</label>
                <input type="password" id="password" name="password" minlength="6" required>
            </div>
            <div class="input-group">
                <label for="password2">Repite la contraseña</label>
                <input type="password" id="password2" name="password2" minlength="6" required>
            </div>
            <button type="submit" class="btn-login">Crear cuenta</button>
            <p class="signup-link">
                ¿Ya tienes cuenta? <a href="login">Inicia sesión</a>
            </p>
        </form>
    </div>
</body>
</html>
