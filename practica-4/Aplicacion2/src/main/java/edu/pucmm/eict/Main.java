package edu.pucmm.eict;


import io.javalin.Javalin;

public class Main {
    static void main(String[] args) {
        Javalin app = Javalin.create(config ->{

            config.routes.get("/",ctx -> {
                ctx.result("Aplicacion 2");
            });


        });

        app.start(7001);

    }
}