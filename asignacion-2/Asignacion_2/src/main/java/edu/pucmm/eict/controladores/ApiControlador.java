package edu.pucmm.eict.controladores;

import edu.pucmm.eict.encapsulaciones.Estudiante;
import edu.pucmm.eict.servicios.EstudianteServices;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.jetbrains.annotations.NotNull;

public class ApiControlador  {

    private final static EstudianteServices fakeServices = EstudianteServices.getInstancia();


    public static void listarEstudiantes(@NotNull Context ctx) throws Exception {
        ctx.json(EstudianteServices.getInstancia().listaEstudiantes());
    }

    public static void estudiantePorMatricula(@NotNull Context ctx) throws Exception {
        Estudiante es = EstudianteServices.getEstudiantePorMatricula(ctx.pathParamAsClass("matricula", Integer.class).required().get());

        if(es!=null){
            ctx.json(es);
        }else{
            throw new NotFoundResponse("Estudiante no encontrado");
        }
    }

    public static void crearEstudiante(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
        //creando.
        ctx.json(fakeServices.crearEstudiante(tmp));
    }

    public static void actualizarEstudiante(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
        //creando.
        ctx.json(fakeServices.actualizarEstudiante(tmp));
    }

    public static void eliminarEstudiante(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        ctx.json(fakeServices.borrarEstudiante(ctx.pathParamAsClass("matricula", Integer.class).required().get()));
    }


}
