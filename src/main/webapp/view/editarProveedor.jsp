

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

        <title>Consulta de Productos</title>

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
                                <li class="active"><a href="${pageContext.servletContext.contextPath}/view/registrarCompra.jsp">Registrar compra</a></li>
                                </c:when>

                            <c:when test='${sessionScope.SESION.equals("empleado")}'>
                                <li><a href="${pageContext.servletContext.contextPath}/ServletListadoClientes">Consultar clientes</a></li>

                                <li class="active"><a href="${pageContext.servletContext.contextPath}/view/registrarCompra.jsp">Registrar compra</a></li>
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

                <h2 class="text-center">Actualización de proveedores</h2>
                <div class="temporal">
                    <form class="form-horizontal" method="POST" 
                          action="${pageContext.servletContext.contextPath}/ServletProveedor">
                        <div class="form-group">
                            <label for="codigoprov" class="col-sm-5 control-label">Código del proveedor</label>

                            <div class="col-sm-3">
                                <input type="text" name="txtCodigoprov" class="form-control" id="codigoprov" readonly>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        
                        <div class="form-group">
                            <label for="nombreprov" class="col-sm-5 control-label">Nombre del proveedor</label>
                            <div class="col-sm-3">
                                <input type="text" value="${sessionScope.PROVEEDOR.getNombreprov()}" name="txtNombreprov" class="form-control" id="nombreprov" required>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        <div class="form-group">
                            <label for="telefonoprov" class="col-sm-5 control-label">Teléfono</label>
                            <div class="col-sm-3">
                                <input type="text" value="${sessionScope.PROVEEDOR.getTelefonoprov()}" name="txtTelefonoprov" class="form-control" id="telefonoprov" required>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        <div class="form-group">
                            <label for="direccionprov" class="col-sm-5 control-label">Dirección</label>
                            <div class="col-sm-3">
                                <input type="text" value="${sessionScope.PROVEEDOR.getDireccionprov()}" name="txtDireccionprovo" class="form-control" id="direccionprov" required>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        <div class="form-group">
                            <label for="emailprov" class="col-sm-5 control-label">Correo electrónico</label>
                            <div class="col-sm-3">
                                <input type="email" value="${sessionScope.PROVEEDOR.getEmailprov()}" name="txtEmailprov" class="form-control" id="emailprov" required>
                            </div>
                            <div class="col-sm-4"></div>
                        </div>
                        
                  
                        <div class="form-group">
                            <div class="col-sm-offset-5 col-sm-4">
                                <button type="submit" class="btn btn-primary">Editar proveedor</button>
                                
                                
                            </div>
                        </div>
                    </form>
                </div>

                <h3 class="text-center"><strong><c:out value="${sessionScope.MSJPROV}"></c:out></strong></h3>

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


