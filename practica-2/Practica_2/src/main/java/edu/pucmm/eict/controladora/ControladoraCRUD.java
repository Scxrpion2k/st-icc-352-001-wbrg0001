package edu.pucmm.eict.controladora;

import edu.pucmm.eict.Clases.Database;
import edu.pucmm.eict.Clases.Producto;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladoraCRUD {

    public static void listar(@NotNull Context ctx) {

        Map<String, Object> modelo = new HashMap<>();
        List<Producto> productos = Database.getProductos();

        modelo.put("productos",productos);
        ctx.render("/html/Pagina_principal.html",modelo);
    }




}
