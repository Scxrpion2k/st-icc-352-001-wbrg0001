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


    public static void administrarProducto(@NotNull Context ctx) {
        Map<String, Object> modelo = new HashMap<>();
        List<Producto> productos = Database.getProductos();
        modelo.put("productos",productos);
        ctx.render("html/CRUD.html",modelo);

    }

    public static void modificarProducto(@NotNull Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        Producto productoEditar = null;

        for (Producto p : Database.Productos) {
            if (p.getId() == id) {
                productoEditar = p;
                break;
            }
        }

        if (productoEditar == null) {
            ctx.status(404).result("Producto no encontrado");
            return;
        }

        Map<String, Object> modelo = new HashMap<>();
        modelo.put("producto", productoEditar);

        ctx.render("html/Modificar_Producto.html", modelo);

    }

    public static void agregarProducto(@NotNull Context ctx) {
        Map<String, Object> modelo = new HashMap<>();
        ctx.render("html/Agregar_Producto.html");
    }
}
