$(document).ready(function () {
    $("#agregarpdto").click(function() {
        var intId = ($("#infoproductos div").length) / 6 + 1;
        var fieldWrapperNombre = $("<div class=\"form-group\"/>");
        var flabelNombre = $("<label for=\"nombrePdto" + intId + "\" \n\
        class=\"col-sm-5 control-label\"> Nombre del producto </label>");
        
        var numPdtos = document.getElementById("numProductostienda").value;
        
        var fOption = "";
        
        for (var i = 0; i < numPdtos; i++) {
            var aux = document.getElementsByTagName('option')[i].value;
            fOption += "<option value=\"" + aux + "\" >" + aux + "</option>";
        }
        
        
        var fcampoNombre = $("<div class=\"col-sm-3\"> <select name=\"txtNombrePdto" + intId + "\" class=\"form-control\" >\n\
                               " + fOption + " </select></div>");
        var fDivNombre = $("<div class=\"col-sm-4\"></div>");
        
        var fieldWrapperCantidad = $("<div class=\"form-group\"/>");
        var flabelCantidad = $("<label for=\"cantidadPdto" + intId + "\" \n\
        class=\"col-sm-5 control-label\"> Unidades compradas </label>");
        var fcampoCantidad = $("<div class=\"col-sm-3\"> <input type=\"text\" name=\"txtCantidadPdto" + intId + "\" class=\"form-control\" id=\"nombrePdto" + intId + "\" required ></div>");
        var fDivCantidad = $("<div class=\"col-sm-4\"></div>"); 
        
             
        var removeButton = $("<button type=\"button\" class=\"btn btn-danger\" value=\"-\" >Quitar producto</button>");
        
        removeButton.click(function() {
            $(this).parent().remove();
            intId = document.getElementById("numProductos").value;
            intId--;
            document.getElementById("numProductos").value = intId;
        });
        
        fieldWrapperNombre.append(flabelNombre);
        fieldWrapperNombre.append(fcampoNombre);
        fieldWrapperNombre.append(fDivNombre);
        fieldWrapperNombre.append(removeButton);
        fieldWrapperNombre.append(fieldWrapperCantidad);
        fieldWrapperNombre.append(flabelCantidad);
        fieldWrapperNombre.append(fcampoCantidad);
        fieldWrapperNombre.append(fDivCantidad);
        
        $("#infoproductos").append(fieldWrapperNombre);
        
        document.getElementById("numProductos").value = intId;
        
        
        
        
        
        
    });
});