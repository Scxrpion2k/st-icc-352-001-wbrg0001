package edu.puccm.eict;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String url;
        String tipo;
        do {
            IO.println("Introduce una URL: ");
            url = scanner.nextLine().trim();

            if(!esValida(url)){
                IO.println("URL invalida");
            }

            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
            tipo = response.headers().firstValue("Content-Type").orElse("");

            String body = response.body();

            Document document = Jsoup.parse(body,url);

            String html = document.toString();


            IO.println("\n");
            IO.println("1.Tipo de archivo: "+tipo);

            if(!tipo.isEmpty() && tipo.startsWith("text/html")){
                IO.println("1.Total de lineas: "+ totalLineas(html));
                IO.println("2.Total parrafos: "+totalParrafos(document));
                IO.println("3.Total de imagenes en parrafos: "+ totalImagenesParrafos(document));
                IO.println("4.Total de formularios por el metodo POST: " +totalFormPost(document));
                IO.println("4.Total de formularios por el metodo GET: " +totalFormGet(document));
                tipoInput(document);
                respuestaFormulario(document);
                return;
            };

        } while (!esValida(url));

        scanner.close();


    }

    public static boolean esValida(String url){
        try {
            HttpClient cliente = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            HttpResponse<Void> response = cliente.send(request, HttpResponse.BodyHandlers.discarding());

            return response.statusCode() >= 200 && response.statusCode() <400;



        } catch (Exception e){
            return false;
        }

    }

    public static int totalLineas(String html) throws IOException {
        return html.split("\\R").length;
    }

    public static int totalParrafos(Document document) throws IOException {
        Elements parrafos = document.select("p");
        return parrafos.size();
    }

    public static int totalImagenesParrafos(Document document){
        Elements imagenes = document.select("p img");
        return imagenes.size();
    }

    public static int totalFormPost(Document document){
        Elements formularioPOST = document.select("form[method=post]");
        return formularioPOST.size();

    }

    public static int totalFormGet(Document document){
        Elements formularioGET = document.select("form[method=get]");
        return formularioGET.size();

    }

    public static void tipoInput(Document document){

        Elements formularios = document.select("form");
        IO.println("5.Formularios con sus tipos de input:");

        int n = 1;

        for (Element formulario : formularios){

            IO.println("\nFormulario" +"["+ n + "]");

            Elements inputs = formulario.select("input");

            if(inputs.isEmpty()){
                IO.println("No se encontraron inpunts en el formulario");
            }

            for (Element input : inputs){
                String tipo = input.attr("type");
                String nombre = input.attr("name");

                IO.println("Nombre: " + nombre + " Tipo: "+tipo);
            }
            n++;

        }

    }

    public static void respuestaFormulario(Document document) throws IOException, InterruptedException {
        Elements formulario = document.select("form");
        String metodo;
        int n = 1;

        IO.println("\n6.Respuesta de formulario por el metodo POST:");


        for (Element form : formulario) {

            try {

                metodo = form.attr("method");
                if (metodo.equalsIgnoreCase("post")) {
                    IO.println("\nFormulario" + "[" + n + "]");
                    String action = form.absUrl("action");

                    HttpClient cliente = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(action)).header("matricula-id", "10154558")
                            .POST(HttpRequest.BodyPublishers.ofString("asignatura=practica1"))
                            .build();
                    HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());

                    IO.println("Respuesta: " + response.body());

                }

                n++;

            } catch (Exception e){
                IO.println("No fue posible procesar este formulario");
            }
        }


    }

}
