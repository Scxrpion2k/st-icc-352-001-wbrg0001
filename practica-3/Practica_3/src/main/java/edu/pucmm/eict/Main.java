package edu.pucmm.eict;

import edu.pucmm.eict.Clases.Database;
import edu.pucmm.eict.Clases.Producto;
import edu.pucmm.eict.Clases.Usuario;
import edu.pucmm.eict.Clases.VentasProductos;
import edu.pucmm.eict.controladora.ControladoraCRUD;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {




    public static void main(String[] args){
        Javalin app = Javalin.create(config -> {
            Usuario admin = new Usuario("admin","admin","admin");
            Database.usuarios.add(admin);
            config.fileRenderer(new JavalinThymeleaf());

            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "/Publico";
                staticFileConfig.hostedPath = "/";
            });

            Producto producto1 = new Producto(1, "Mango", new BigDecimal("20.38"));
            Database.Productos.add(producto1);

            Producto producto2 = new Producto(2, "Manzana", new BigDecimal("15.50"));
            Database.Productos.add(producto2);

            Producto producto3 = new Producto(3, "Banana", new BigDecimal("10.00"));
            Database.Productos.add(producto3);

            Producto producto4 = new Producto(4, "Piña", new BigDecimal("35.75"));
            Database.Productos.add(producto4);

            Producto producto5 = new Producto(5, "Uva", new BigDecimal("28.90"));
            Database.Productos.add(producto5);

            Producto producto6 = new Producto(6, "Sandía", new BigDecimal("45.00"));
            Database.Productos.add(producto6);

            config.routes.apiBuilder(() -> {

                path("/Pagina_principal/", () -> {
                    get(ctx -> ctx.redirect("/Pagina_principal/listarProducto"));
                    get("/listarProducto", ControladoraCRUD::listar);
                    get("/administrar_producto",ControladoraCRUD::administrarProducto);
                    get("/agregar_producto",ControladoraCRUD::agregarProducto);
                    get("/modificar_producto/{id}",ControladoraCRUD::modificarProducto);
                    get("/Carrito_de_compra",ControladoraCRUD::carritoDeCompra);
                    get("/listarVentas",ControladoraCRUD::listarVenta);



                });
            });

            config.routes.get("/*",ctx -> {
                ctx.redirect("/Pagina_principal/listarProducto");
            });

            config.routes.get("/login",ctx -> {
               ctx.render("Publico/Login.html");
            });

            config.routes.before("/Pagina_principal/*",ctx -> {
               String path = ctx.path();

               if(path.contains("administrar_producto") || path.contains("agregar_producto")||
               path.contains("modificar_producto") || path.contains("listarVentas")){
                   Usuario usuario = ctx.sessionAttribute("usuario");

                   if(usuario == null || !usuario.getNombre().equals("admin")||!usuario.getPassword().equals("admin")){
                       ctx.redirect("/login");
                   }
               }
            });




            config.routes.post("/login",ctx -> {
               String usuario = ctx.formParam("usuario");
               String password = ctx.formParam("password");

               for(Usuario u : Database.usuarios){
                   if(u.getNombre().equals(usuario) && u.getPassword().equals(password)){
                       ctx.sessionAttribute("usuario",u);
                       break;
                   }
               }
               ctx.redirect("/Pagina_principal/listarProducto");
            });

            config.routes.post("/productos/crear",ctx -> {
                String nombre = ctx.formParam("nombre");
                String precioht = ctx.formParam("precio");

                if(nombre == null || nombre.isBlank()){
                    ctx.result("Nombre invalido");
                    return;
                }
                if (precioht == null || precioht.isBlank()){
                    ctx.result("Precio invalido");
                    return;
                }

                BigDecimal precio;

                try {
                    precio = new BigDecimal(precioht);
                } catch (Exception e) {
                    ctx.result("Precio invalido");
                    return;
                }

                Producto producto = new Producto(Database.Productos.size()+1,nombre,precio);

                Database.Productos.add(producto);

                ctx.redirect("/Pagina_principal/administrar_producto");


            });

            config.routes.post("/Productos/eliminar",ctx -> {
               String idht = ctx.formParam("id");

               if(idht == null || idht.isBlank()){
                   ctx.result("ID invalido");
                   return;
               }

               int id = Integer.parseInt(idht);

               for (int i = 0;i < Database.Productos.size();i++){
                   Producto p = Database.Productos.get(i);

                   if(p.getId() == id){
                       Database.Productos.remove(i);
                       break;
                   }
               }

               ctx.redirect("/Pagina_principal/administrar_producto");

            });

            config.routes.post("/Producto/actualizar",ctx -> {
                String idht = ctx.formParam("id");
                String nombre = ctx.formParam("nombre");
                String precioht = ctx.formParam("precio");

                if (idht == null || idht.isBlank()) {
                    ctx.status(400).result("ID inválido");
                    return;
                }

                int id = Integer.parseInt(idht);

                BigDecimal precio;

                try {
                    precio = new BigDecimal(precioht);
                } catch (Exception e) {
                    ctx.result("Precio invalido");
                    return;
                }

                for(Producto p : Database.Productos){
                    if(p.getId() == id){
                        if(nombre != null && !nombre.isBlank()){
                            p.setNombre(nombre);
                        }

                        p.setPrecio(precio);
                    }
                }

                ctx.redirect("/Pagina_principal/administrar_producto");
            });

            config.routes.post("/Carrito/agregar", ctx -> {

                int idProducto = Integer.parseInt(ctx.formParam("id"));

                Producto productoSeleccionado = null;

                for (Producto p : Database.getProductos()) {
                    if (p.getId() == idProducto) {
                        productoSeleccionado = p;
                        break;
                    }
                }

                if (productoSeleccionado != null) {

                    List<Producto> carrito = ctx.sessionAttribute("carrito");

                    if (carrito == null) {
                        carrito = new ArrayList<>();
                    }

                    carrito.add(productoSeleccionado);

                    ctx.sessionAttribute("carrito", carrito);
                }

                ctx.redirect("/Pagina_principal/listarProducto");
            });

            config.routes.post("/Carrito/eliminar", ctx -> {

                int id = Integer.parseInt(ctx.formParam("id"));

                List<Producto> carrito = ctx.sessionAttribute("carrito");

                if (carrito != null) {
                    for (int i = 0; i < carrito.size(); i++) {
                        if (carrito.get(i).getId() == id) {
                            carrito.remove(i);
                            break;
                        }
                    }
                    ctx.sessionAttribute("carrito", carrito);
                }

                ctx.redirect("/Pagina_principal/Carrito_de_compra");
            });

            config.routes.post("/Carrito/procesar_compra",ctx -> {
               List<Producto> carrito = ctx.sessionAttribute("carrito");

               if (carrito == null || carrito.isEmpty()){
                   ctx.redirect("/Pagina_principal/Carrito_de_compra");
                   return;
               }


               Usuario usuario = ctx.sessionAttribute("usuario");

               String nombreUsuario;

               if(usuario == null){
                   nombreUsuario = "Invitado";
               } else{
                   nombreUsuario = usuario.getNombre();
               }

                VentasProductos venta = new VentasProductos(Database.getVentas().size()+1, new Date(),nombreUsuario,new ArrayList<>(carrito));
                Database.Ventas.add(venta);

                ctx.sessionAttribute("carrito",new ArrayList<Producto>());
                ctx.redirect("/Pagina_principal/listarProducto");

            });

        });

        app.start(7000);
    }
}
