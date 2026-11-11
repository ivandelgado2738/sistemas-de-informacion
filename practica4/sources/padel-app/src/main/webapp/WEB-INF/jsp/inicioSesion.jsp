<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <title>Inicio Sesión</title>
    <link rel="stylesheet" href="estilos-login.css">
</head>
<body>
    <div class="contenedor-login">
        <h2>Inicio Sesión</h2>

        <c:if test="${param.registrado == '1'}">
            <p style="color:green; text-align:center;">Cuenta creada. Ya puedes iniciar sesión.</p>
        </c:if>

        <c:if test="${param.baja == '1'}">
            <p style="color:green; text-align:center;">Tu cuenta se ha dado de baja.</p>
        </c:if>

        <c:if test="${not empty error}">
            <p style="color:#c0392b; text-align:center;"><c:out value="${error}"/></p>
        </c:if>

        <form action="login" method="post">
            <div class="input-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" placeholder="Ingresa tu email"
                       value="<c:out value='${email}'/>" required>
            </div>
            <div class="input-group">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="Ingresa tu contraseña" required>
            </div>
            <button type="submit" class="btn-login">Entrar</button>
            <p class="signup-link">
                ¿No tienes cuenta? <a href="registro">Regístrate aquí</a>
            </p>
        </form>
    </div>
</body>
</html>
