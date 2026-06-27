package edu.pucmm.eict;


import io.javalin.Javalin;

public class Main {
    static void main(String[] args) {
        Javalin app = Javalin.create(config ->{

        config.routes.get("/",ctx -> {
           ctx.result("Prueba");
        });

        });

        app.start(7000);

    }
}
