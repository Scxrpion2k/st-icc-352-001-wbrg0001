package edu.pucmm.eict;
import io.javalin.Javalin;

record Usuario(String usuario, String password){

}

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {

            config.staticFiles.add(staticFile -> {
                staticFile.directory = "/publico";
                staticFile.hostedPath = "/";
            });

            config.routes.before("/*", ctx -> {
                String ruta = ctx.path();

                Usuario usuario = ctx.sessionAttribute("usuario");

                if(ruta.equals("/login.html") ||
                        ruta.equals("/procesar-login")) {
                    return;
                }



                if(usuario == null){
                    ctx.redirect("/login.html");
                }
            });

            config.routes.get("/pagina_inicio", ctx -> {
                ctx.result("Bienvenidos");
            });

            config.routes.post("/procesar-login", ctx -> {

                String usuario = ctx.formParam("usuario");
                String password = ctx.formParam("password");

                if(usuario.equals("admin") &&
                        password.equals("admin")){

                    ctx.sessionAttribute(
                            "usuario",
                            new Usuario(usuario,password)
                    );

                    ctx.redirect("/pagina_inicio");

                }else{
                    ctx.result("Usuario o contraseña incorrectos");
                }
            });


        }).start(7000);
    }
}
