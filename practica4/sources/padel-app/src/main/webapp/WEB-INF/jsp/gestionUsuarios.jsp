<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Usuarios</title>
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="estilos-admin.css">
</head>
<body>

    <div class="panel-admin">
        <a class="btn-volver" href="principalAdmin.html" style="margin-bottom:16px;">← Panel de administrador</a>
        <h1>Gestión de Usuarios</h1>

        <c:if test="${not empty mensaje}">
            <p class="mensaje mensaje-exito"><c:out value="${mensaje}"/></p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="mensaje mensaje-error"><c:out value="${error}"/></p>
        </c:if>

        <table class="tabla-admin">
            <tr>
                <th>Nombre</th>
                <th>Email</th>
                <th>Rol</th>
                <th>Estado</th>
                <th>Editar</th>
                <th>Acción</th>
            </tr>
            <c:forEach var="u" items="${usuarios}">
                <tr>
                    <td><c:out value="${u.nombre}"/></td>
                    <td><c:out value="${u.email}"/></td>
                    <td>${u.rol == 'ADMIN' ? 'Administrador' : 'Cliente'}</td>
                    <td>
                        <c:choose>
                            <c:when test="${u.estado == 'ACTIVO'}"><span class="estado estado-activo">Activo</span></c:when>
                            <c:otherwise><span class="estado estado-inactivo">Bloqueado</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td><a class="btn-admin" href="editarUsuario?id=${u.id}">Editar</a></td>
                    <td>
                        <c:if test="${u.id != sessionScope.usuario.id}">
                            <form method="post" action="gestionUsuarios" style="margin:0;">
                                <input type="hidden" name="usuario" value="${u.id}">
                                <input type="hidden" name="accion" value="${u.estado == 'ACTIVO' ? 'bloquear' : 'activar'}">
                                <button type="submit" class="btn-admin ${u.estado == 'ACTIVO' ? 'btn-admin-secundario' : ''}">
                                    ${u.estado == 'ACTIVO' ? 'Bloquear' : 'Activar'}
                                </button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>

</body>
</html>
