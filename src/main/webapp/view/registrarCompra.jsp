

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="../../favicon.ico">

        <title>Registro de compras</title>

        <!-- Bootstrap core CSS -->
        <link href="${pageContext.servletContext.contextPath}/bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">

        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/estilos.css">

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link href="../../assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="${pageContext.servletContext.contextPath}/css/jumbotron.css" rel="stylesheet">

        <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
        <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
        <script src="../../assets/js/ie-emulation-modes-warning.js"></script>

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>

    <body>

        <!-- Header -->

        <!-- navbar-fixed-top para poner el header arriba -->
        <nav class="navbar navbar-inverse navbar-fixed-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="${pageContext.servletContext.contextPath}/index.jsp">SAGPT</a>
                </div>
                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">

                        <li>
                            <a href="${pageContext.servletContext.contextPath}/view/consultaProductos.jsp">Consultar productos</a></li>
                            <c:choose>

                            <c:when test='${sessionScope.SESION.equals("admin")}'>
                                <li><a href="${pageContext.servletContext.contextPath}/ServletListadoClientes">Consultar clientes</a></li>
                                <li><a href="${pageContext.servletContext.contextPath}/ServletListadoProveedores">Proveedores</a></li>
                                <li><a href="${pageContext.servletContext.contextPath}/view/listarCompras.jsp">Compras</a></li>
                                </c:when>

                            <c:when test='${sessionScope.SESION.equals("empleado")}'>
                                <li><a href="${pageContext.servletContext.contextPath}/ServletListadoClientes">Consultar clientes</a></li>

                                <li><a href="${pageContext.servletContext.contextPath}/view/listarCompras.jsp">Compras</a></li>
                                </c:when>
                            </c:choose>
                    </ul>
                    <c:choose>

                        <c:when test="${! empty sessionScope.SESION}">

                            <form class="navbar-form navbar-right" 
                                  action="${pageContext.servletContext.contextPath}/ServletUsuario" 
                                  method="POST">
                                <label id="nombreUsuario">${sessionScope.USUARIO}</label>
                                <button name="btnAccion" value="signOut" type="submit" class="btn btn-success">Sign out</button>
                            </form>

                        </c:when>

                        <c:otherwise>
                            <form 
                                action="${pageContext.servletContext.contextPath}/ServletUsuario"
                                class="navbar-form navbar-right" method="POST">

                                <div class="form-group">
                                    <input name="txtEmail" type="email" placeholder="Email" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <input name="txtPassword" type="password" placeholder="Password" class="form-control" required>
                                </div>
                                <button name="btnAccion" value="signIn" type="submit" class="btn btn-success">Sign in</button>
                            </form>
                        </c:otherwise>

                    </c:choose>        

                </div><!--/.navbar-collapse -->
            </div>
        </nav>

        <!-- Fin Header -->                        

        <!-- Main jumbotron for a primary marketing message or call to action -->
        <div class="jumbotron margen">
            <div class="container">

                <h2 class="text-center">Ingreso de compra</h2>
                <div class="temporal">
                    <form class="form-horizontal" method="POST" 
                          action="${pageContext.servletContext.contextPath}/ServletCompra">
                        <div class="form-group">
                            <label for="documento" class="col-sm-5 control-label">Documento</label>

                            <div class="col-sm-3">
                                <input type="text" name="txtDocumento" class="form-control" id="documento" required>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        <div class="form-group">
                            <label for="tipodocumento" class="col-sm-5 control-label">Tipo de Documento</label>

                            <div class="col-sm-3">
                                <select name="txttipoDocumento" class="form-control" id="tipodocumento">
                                    <option value="1">Cédula de ciudadanía</option>
                                    <option value="2">Tarjeta de identidad</option>
                                    <option value="3">Cédula de Extranjería</option>
                                </select>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        <div class="form-group">
                            <label for="nombres" class="col-sm-5 control-label">Nombres</label>
                            <div class="col-sm-3">
                                <input type="text" name="txtNombres" class="form-control" id="nombres" required>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        <div class="form-group">
                            <label for="apellidos" class="col-sm-5 control-label">Apellidos</label>
                            <div class="col-sm-3">
                                <input type="text" name="txtApellidos" class="form-control" id="apellidos" required>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        <div class="form-group">
                            <label for="telefono" class="col-sm-5 control-label">Teléfono</label>
                            <div class="col-sm-3">
                                <input type="text" name="txtTelefono" class="form-control" id="telefono" required>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        <div class="form-group">
                            <label for="direccion" class="col-sm-5 control-label">Dirección</label>
                            <div class="col-sm-3">
                                <input type="text" name="txtDireccion" class="form-control" id="direccion" required>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        <div class="form-group">
                            <label for="correo" class="col-sm-5 control-label">Email</label>
                            <div class="col-sm-3">
                                <input type="email" name="txtEmail" class="form-control" id="correo" required>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        <!-- Info del producto -->
                        <div id="infoproductos">
                            <div class="form-group">
                                <label for="nombrePdto1" class="col-sm-5 control-label">Nombre del producto</label>
                                <div class="col-sm-3">
                                   
                                    <select id="nombrePdto1" name="txtNombrePdto1" class="form-control" >
                                        <c:forEach var="producto" items="${sessionScope.PRODUCTOS}">
                                            <option value="${producto.getNombrepdto()}">
                                                <c:out value="${producto.getNombrepdto()}"></c:out>
                                            </option>
                                        </c:forEach>
                                        
                                    </select>
                                <!--    <input type="text" name="" class="form-control" id="nombrePdto1" required>-->
                                </div>
                                <div class="col-sm-4"></div>
                            </div>
                            <div class="form-group">
                                <label for="cantidadPdto1" class="col-sm-5 control-label">Unidades compradas</label>
                                <div class="col-sm-3">
                                    <input type="text" name="txtCantidadPdto1" class="form-control" id="cantidadPdto1" required>
                                </div>
                                <div class="col-sm-4"></div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="fechacompra" class="col-sm-5 control-label">Fecha de compra</label>
                            <div class="col-sm-3">
                                <input type="date" name="txtFechacompra" class="form-control" id="fechacompra" required>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        <!-- Fin Info del producto -->
                        <div class="form-group">
                            <div class="col-sm-offset-5 col-sm-4">
                                <button type="submit" class="btn btn-primary">Ingresar compra</button>
                                <button id="agregarpdto" type="button" class="btn btn-success">Agregar producto</button>
                                <input type="hidden" id="numProductos" name="numProductos" value="1" />
                                <input type="hidden" id="numProductostienda" name="numProductostienda" value="${sessionScope.PRODUCTOS.size()}" />
                                <input type="hidden" name="txtUsuario" value="${sessionScope.USUARIO}" />
                            </div>
                        </div>
                    </form>
                </div>

                <h3 class="text-center"><strong><c:out value="${sessionScope.MENSAJE}"></c:out></strong></h3>

                </div>
            </div>

            <div class="container">
                <!-- Example row of columns -->
                <div class="row">

                </div>

                <hr>

                <footer>
                    <p>&copy; 2017 Company, Inc.</p>
                </footer>
            </div> <!-- /container -->


            <!-- Bootstrap core JavaScript
            ================================================== -->
            <!-- Placed at the end of the document so the pages load faster -->
            <script src="${pageContext.servletContext.contextPath}/js/jquery-3.2.1.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/js/acciones.js"></script>
        <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
        <script src="${pageContext.servletContext.contextPath}/js/bootstrap.min.js"></script>
        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script>
    </body>
</html>


