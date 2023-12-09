package com.mycompany.parcial3;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

public class Parcial3 {

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

        Gson gson = new Gson();
        LinkedList<Lector> lectores = new LinkedList<>();
        LinkedList<Copia> copias = new LinkedList<>();
        LinkedList<Libro> libros = new LinkedList<>();
        LinkedList<Prestamo> prestamos = new LinkedList();
        LinkedList<Multa> multas = new LinkedList();

        get("/CargarDatos", (req, res) -> {
            res.type("application.json");

            FileInputStream filecopi = new FileInputStream("copiaLib.txt");
            ObjectInputStream obscopi = new ObjectInputStream(filecopi);
            Copia copialib = (Copia) obscopi.readObject();
            obscopi.close();
            copias.add(copialib);

            FileInputStream filelec = new FileInputStream("lector.txt");
            ObjectInputStream obslec = new ObjectInputStream(filelec);
            Lector lector = (Lector) obslec.readObject();
            obslec.close();
            lectores.add(lector);

            FileInputStream filepres = new FileInputStream("prestamo.txt");
            ObjectInputStream obspres = new ObjectInputStream(filepres);
            Prestamo prestam = (Prestamo) obspres.readObject();
            obspres.close();
            prestamos.add(prestam);

            return gson.toJson(null);
        });
        Libro libro1 = new Libro("Señor anillos", "Novela", "2323", 0);
        libros.add(libro1);
        Libro libro2 = new Libro("thor", "Novela", "2323", 0);
        libros.add(libro2);
        Lector lector1 = new Lector("102", "Esteban", "Hurtado", "cll69n");
        lectores.add(lector1);
        Lector lector2 = new Lector("231", "Juan", "Perez", "cll69n");
        lectores.add(lector2);
        Copia copia1 = new Copia("34", "Disponible", "Thor");
        copias.add(copia1);
        Copia copia2 = new Copia("45", "Disponible", "Señor de los anillos");
        copias.add(copia2);

        get("/RegistrarLector/:numSocio/:nombre/:apellido/:direccion", (req, res) -> {

            res.type("application/json");

            String numsocio = req.params(":numSocio");
            String nombre = req.params(":nombre");
            String apellido = req.params(":apellido");
            String direccion = req.params(":direccion");

            Lector objlec = new Lector(numsocio, nombre, apellido, direccion);
            lectores.add(objlec);

            return gson.toJson(objlec);
        });
        get("/RegistrarCopia/:identificador/:estado/:nombre", (req, res) -> {

            res.type("application/json");

            String identificador = req.params(":identificador");
            String estado = req.params(":estado");
            String nombre = req.params(":nombre");

            Copia objcopia = new Copia(identificador, estado, nombre);
            copias.add(objcopia);

            return gson.toJson(objcopia);
        });

        get("/PrestarLibro/:numSocio/:identificadorCopia", (req, res) -> {
            res.type("application/json");

            String numSocio = req.params(":numSocio");
            String identificador = req.params(":identificadorCopia");

            Copia tempc = new Copia();
            Lector templ = new Lector();

            for (int i = 0; i < lectores.size(); i++) {
                if (lectores.get(i).numSocio.equals(numSocio)) {
                    templ = lectores.get(i);
                }
            }

            for (int i = 0; i < copias.size(); i++) {
                if (copias.get(i).identificador.equals(identificador)) {
                    tempc = copias.get(i);
                    tempc.lectorRef = templ;
                }
            }

            for (int i = 0; i < lectores.size(); i++) {
                if (lectores.get(i).numSocio.equals(numSocio) && copias.size() < 3) {
                    templ.copiaLec.add(tempc);
                }
            }

            Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(Calendar.DAY_OF_MONTH);
            Prestamo objpres = new Prestamo(hour, 0, null, templ, tempc);
            prestamos.add(objpres);

            return gson.toJson(objpres);
        });

        get("/DevolverLibro/:numSocio/:identificadorCopia", (req, res) -> {
            res.type("application/json");
            String numSocio = req.params(":numSocio");
            String identificador = req.params(":identificadorCopia");
            Lector templ = new Lector();

            for (int i = 0; i < lectores.size(); i++) {
                if (lectores.get(i).numSocio.equals(numSocio)) {
                    for (int x = 0; x > lectores.get(i).copiaLec.size(); x++) {
                        lectores.get(i).copiaLec.remove(x);
                        templ = lectores.get(i);
                    }
                }
            }
            for (int i = 0; i < copias.size(); i++) {
                if (copias.get(i).lectorRef.equals(identificador)) {
                    copias.get(i).lectorRef = null;
                }
            }

            Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);

            for (int i = 0; i < prestamos.size(); i++) {
                if (prestamos.get(i).fechaFin <= hour) {
                    prestamos.remove(i);
                } else {
                    prestamos.remove(i);
                    Multa objmulta = new Multa(0, 0, templ, prestamos.get(i));
                    multas.add(objmulta);
                    return gson.toJson(multas);
                }
            }

            return gson.toJson(prestamos);
        });

        get(("/VerMultas"), (req, res) -> {
            res.type("application/json");
            if (!multas.isEmpty()) {
                for (int i = 0; i < multas.size(); i++) {
                    System.out.println(multas.get(i));
                }
            } else {
                System.out.println("No hay Multas vigentes");
            }
            return gson.toJson(multas);
        });

        get(("/VerPrestamos"), (req, res) -> {
            res.type("application/json");
            if (!prestamos.isEmpty()) {
                for (int i = 0; i < prestamos.size(); i++) {
                    System.out.println(prestamos.get(i));
                }
            } else {
                System.out.println("No hay Multas vigentes");
            }
            return gson.toJson(prestamos);
        });

        get(("/VerCopias"), (req, res) -> {
            res.type("application/json");

            return gson.toJson(copias);
        });

        get(("/VerLectores"), (req, res) -> {
            res.type("application/json");

            return gson.toJson(lectores);
        });

        for (int i = 0; i < copias.size(); i++) {
            FileOutputStream fichero;
            fichero = new FileOutputStream("copiaLib.txt");
            ObjectOutputStream crear = new ObjectOutputStream(fichero);
            crear.writeObject(copias.get(i));
        }
        for (int i = 0; i < lectores.size(); i++) {
            FileOutputStream fichero;
            fichero = new FileOutputStream("lector.txt");
            ObjectOutputStream crear = new ObjectOutputStream(fichero);
            crear.writeObject(lectores.get(i));
        }
        for (int i = 0; i < prestamos.size(); i++) {
            FileOutputStream fichero;
            fichero = new FileOutputStream("prestamo.txt");
            ObjectOutputStream crear = new ObjectOutputStream(fichero);
            crear.writeObject(prestamos.get(i));
        }
    }
}
