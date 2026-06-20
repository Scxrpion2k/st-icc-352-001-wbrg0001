package edu.pucmm.eict;

import edu.pucmm.eict.Clases.Usuario;
import io.javalin.Javalin;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args){
        Javalin app = Javalin.create(config -> {
            Usuario admin = new Usuario("admin","admin","admin");



        });
    }
}
