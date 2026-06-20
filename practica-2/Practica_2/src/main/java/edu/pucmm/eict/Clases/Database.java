package edu.pucmm.eict.Clases;

import java.util.ArrayList;
import java.util.List;

public class Database {
    public static List<Usuario> usuarios = new ArrayList<>();
    public static List<Producto> Productos = new ArrayList<>();
    public static List<VentasProductos> Ventas = new ArrayList<>();

    public static List<Usuario> getUsuarios() {
        return usuarios;
    }

    public static void setUsuarios(List<Usuario> usuarios) {
        Database.usuarios = usuarios;
    }

    public static List<Producto> getProductos() {
        return Productos;
    }

    public static void setProductos(List<Producto> productos) {
        Productos = productos;
    }

    public static List<VentasProductos> getVentas() {
        return Ventas;
    }

    public static void setVentas(List<VentasProductos> ventasProductos) {
        Ventas = ventasProductos;
    }
}
