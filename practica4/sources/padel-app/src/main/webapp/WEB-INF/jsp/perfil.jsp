<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi perfil</title>
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="estilos-admin.css">
    <style>
        .form-perfil { display: flex; gap: 10px; align-items: flex-end; flex-wrap: wrap; margin-bottom: 20px; }
        .form-perfil label { display: flex; flex-direction: column; font-size: 0.9rem; font-weight: 600; color: #555; gap: 6px; }
        .form-perfil input { padding: 8px 10px; border: 1px solid #ccc; border-radius: 6px; font-size: 0.95rem; }
    </style>
</head>
<body>
    <%@ include file="cabecera.jspf" %>

    <div class="panel-admin" style="margin-top:20px;">
        <div style="display:flex; align-items:center; gap:16px; margin-bottom:20px;">
            <img src="user.png" alt="Foto de perfil" width="80">
            <div>
                <h1 style="margin:0;"><c:out value="${u.nombre}"/></h1>
                <p style="margin:4px 0 0 0;"><c:out value="${u.email}"/> · Nivel: <c:out value="${u.nivel}"/></p>
            </div>
        </div>

        <c:if test="${not empty mensaje}">
            <p class="mensaje mensaje-exito"><c:out value="${mensaje}"/></p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="mensaje mensaje-error"><c:out value="${error}"/></p>
        </c:if>

        <h3>Cambiar nombre</h3>
        <form method="post" action="perfil" class="form-perfil">
            <input type="hidden" name="accion" value="nombre">
            <label>Nombre y apellidos
                <input type="text" name="nombre" value="<c:out value='${u.nombre}'/>" required>
            </label>
            <button type="submit" class="btn-admin">Guardar</button>
        </form>

        <h3>Cambiar email</h3>
        <form method="post" action="perfil" class="form-perfil">
            <input type="hidden" name="accion" value="email">
            <label>Email
                <input type="email" name="email" value="<c:out value='${u.email}'/>" required>
            </label>
            <button type="submit" class="btn-admin">Guardar</button>
        </form>

        <h3>Cambiar contraseña</h3>
        <form method="post" action="perfil" class="form-perfil">
            <input type="hidden" name="accion" value="contrasena">
            <label>Contraseña actual <input type="password" name="actual" required></label>
            <label>Nueva <input type="password" name="nueva" minlength="6" required></label>
            <label>Repite la nueva <input type="password" name="nueva2" minlength="6" required></label>
            <button type="submit" class="btn-admin">Cambiar</button>
        </form>

        <a class="btn-admin btn-admin-secundario" href="baja">Darse de baja</a>
    </div>
</body>
</html>
