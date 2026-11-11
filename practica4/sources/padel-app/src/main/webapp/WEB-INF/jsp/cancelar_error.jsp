<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error al Cancelar</title>
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="estilos-admin.css">
</head>
<body>
    <div class="panel-admin" style="max-width: 500px;">
        <h1>Error en la cancelación</h1>
        <p class="mensaje mensaje-error"><c:out value="${mensaje}"/></p>
        <a class="btn-admin" href="verReservas?club=${idClub}">Volver al listado de reservas</a>
    </div>
</body>
</html>
