<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Usuario</title>
    <link rel="stylesheet" href="estilos-login.css">
    <link rel="stylesheet" href="estilos-admin.css">
</head>
<body>

    <div class="contenedor-login">
        <h2>Editar Usuario</h2>

        <c:if test="${not empty error}">
            <p class="mensaje mensaje-error"><c:out value="${error}"/></p>
        </c:if>

        <form action="editarUsuario" method="post">
            <input type="hidden" name="id" value="${u.id}">
            <div class="input-group">
                <label for="nombre">Nombre:</label>
                <input type="text" id="nombre" name="nombre" value="<c:out value='${u.nombre}'/>" required>
            </div>

            <div class="input-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="<c:out value='${u.email}'/>" required>
            </div>

            <div class="input-group">
                <label for="estado">Estado:</label>
                <select id="estado" name="estado">
                    <option value="ACTIVO" ${u.estado == 'ACTIVO' ? 'selected' : ''}>Activo</option>
                    <option value="BLOQUEADO" ${u.estado == 'BLOQUEADO' ? 'selected' : ''}>Bloqueado</option>
                </select>
            </div>

            <button type="submit" class="btn-login">Guardar Cambios</button>
            <p style="text-align:center; margin-top: 14px;">
                <a href="gestionUsuarios">Cancelar</a>
            </p>
        </form>
    </div>

</body>
</html>
