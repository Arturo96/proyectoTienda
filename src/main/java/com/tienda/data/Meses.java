package com.tienda.data;

import java.util.ArrayList;
import java.util.List;

public class Meses {

    private static List<String> listado;

    static {

        listado = new ArrayList<String>() {

            {
                add("0");
                add("Enero");
                add("Febrero");
                add("Marzo");
                add("Abril");
                add("Mayo");
                add("Junio");
                add("Julio");
                add("Agosto");
                add("Septtiembre");
                add("Octubre");
                add("Noviembre");
                add("Diciembre");

            }

        };

    }

    public static List<String> getListado() {
        return listado;
    }
    
    public static String fechaEstandar(String fecha) {
         String[] parametrosFecha = fecha.split("-");
         return getListado().get(Integer.parseInt(parametrosFecha[1])) + " " +
                parametrosFecha[0] +  " " + parametrosFecha[2];
    }

}
