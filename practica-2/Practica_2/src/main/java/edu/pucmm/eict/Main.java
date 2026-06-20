package edu.pucmm.eict;

import edu.pucmm.eict.Clases.Database;
import edu.pucmm.eict.Clases.Producto;
import edu.pucmm.eict.Clases.Usuario;
import io.javalin.Javalin;

import java.math.BigDecimal;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args){
        Javalin app = Javalin.create(config -> {
            Usuario admin = new Usuario("admin","admin","admin");



            config.routes.post("/login",ctx -> {
               String usuario = ctx.formParam("usuario");
               String password = ctx.formParam("password");

               for(Usuario u : Database.usuarios){
                   if(u.getNombre().equals(usuario) && u.getPassword().equals(password)){
                       ctx.sessionAttribute("usuario",u);
                       break;
                   }
               }
               ctx.redirect("/");
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

                ctx.redirect("/");


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

               ctx.redirect("/Productos");

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

                ctx.redirect("/Productos");
            });







        });
    }
}
