package edu.pucmm.eict.controladora;

import edu.pucmm.eict.Clases.Database;
import edu.pucmm.eict.Clases.Producto;
import edu.pucmm.eict.Clases.VentasProductos;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladoraCRUD {

    public static void listar(@NotNull Context ctx) {

        Map<String, Object> modelo = new HashMap<>();
        List<Producto> productos = Database.getProductos();

        List<Producto> carrito = ctx.sessionAttribute("carrito");

        int carritoContador;

        if (carrito == null) {
            carritoContador = 0;
        } else {
            carritoContador = carrito.size();
        }

        modelo.put("productos",productos);
        modelo.put("carritoCount", carritoContador);
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
        ctx.render("html/Agregar_Producto.html",modelo);
    }

    public static void carritoDeCompra(@NotNull Context ctx) {
        List<Producto> carrito = ctx.sessionAttribute("carrito");
        Map<String, Object> modelo = new HashMap<>();

        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        int totalProductos = carrito.size();

        BigDecimal totalPrecio = BigDecimal.ZERO;

        for (Producto p : carrito) {
            totalPrecio = totalPrecio.add(p.getPrecio());
        }

        modelo.put("productos", carrito);
        modelo.put("totalProductos", totalProductos);
        modelo.put("totalPrecio", totalPrecio);

        ctx.render("html/Carrito_Compra.html", modelo);

    }

    public static void listarVenta(@NotNull Context ctx) {
        Map<String, Object> modelo = new HashMap<>();
        List<VentasProductos> ventas = Database.getVentas();

        modelo.put("ventas",ventas);
        ctx.render("html/Ventas.html",modelo);
    }


}
