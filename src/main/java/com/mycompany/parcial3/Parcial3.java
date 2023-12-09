package com.mycompany.parcial3;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.io.IOException;
import java.util.Calendar;

public class Parcial3 {
    
    public static void main(String[] args) {
        
        Gson gson = new Gson();
        LinkedList<Lector> lectores = new LinkedList<>();
        LinkedList<Copia> copias = new LinkedList<>();
        LinkedList<Libro> libros = new LinkedList<>();
        LinkedList<Prestamo> prestamos = new LinkedList();
        LinkedList<Multa> multas = new LinkedList();
        
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
                
        cargarLectorDesdeArchivo();
        cargarCopiaDesdeArchivo();
        cargarPrestamoDesdeArchivo();
        cargarMultaDesdeArchivo();
        cargarLibroDesdeArchivo();
        
        get("/RegistrarLector/:numSocio/:nombre/:apellido/:direccion", (req, res) -> {
            
            res.type("application/json");
            
            String numsocio = req.params(":numSocio");
            String nombre = req.params(":nombre");
            String apellido = req.params(":apellido");
            String direccion = req.params(":direccion");
            
            Lector objlec = new Lector();
            
            for (int i = 0; i < lectores.size(); i++) {
                if (lectores.get(i).numSocio != numsocio) {
                    objlec.numSocio = numsocio;
                    objlec.apellido = apellido;
                    objlec.direccion = direccion;
                    objlec.nombre = nombre;
                    
                    lectores.add(objlec);
                }
            }
            return gson.toJson(objlec);
        });
        get("/RegistrarCopia/:identificador/:estado/:nombre", (req, res) -> {
            
            res.type("application/json");
            
            String identificador = req.params(":identificador");
            String estado = req.params(":estado");
            String nombre = req.params(":nombre");
            
            Copia objcopia = new Copia();
            for (int x = 0; x < copias.size(); x++) {
                if (copias.get(x).identificador != identificador) {
                    objcopia.estado = estado;
                    objcopia.identificador = identificador;
                    objcopia.nombre = nombre;
                    objcopia.lectorRef = null;
                    copias.add(objcopia);
                }
            }
            return gson.toJson(objcopia);
        });
        
        get("/PrestarLibro/:numSocio/:identificadorCopia/:fechaFin", (req, res) -> {
            res.type("application/json");
            
            String numSocio = req.params(":numSocio");
            String identificador = req.params(":identificadorCopia");
            int fechaFin = Integer.parseInt(req.params(":fechaFin"));
            
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
                    tempc.estado = ("Prestado");
                    tempc.lectorRef = templ;
                }
            }
            
            for (int i = 0; i < lectores.size(); i++) {
                if (lectores.get(i).numSocio.equals(numSocio) && copias.size() < 3) {
                    templ.copiaLec.add(tempc);
                }
            }
            
            Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(Calendar.DAY_OF_YEAR);
            
            Prestamo objpres = new Prestamo(hour, fechaFin, null, templ, tempc);
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
                        if(lectores.get(i).copiaLec.get(x).identificador.equals(identificador)){
                            lectores.get(i).copiaLec.remove(x);
                            templ=lectores.get(i);
                        }
                    }
                }
            }
            
            for (int i = 0; i < copias.size(); i++) {
                if (copias.get(i).identificador.equals(identificador)) {
                    if(copias.get(i).lectorRef.numSocio.equals(numSocio)){
                    copias.get(i).lectorRef = null;
                    }
                }
            }
            
            Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(Calendar.DAY_OF_YEAR);
            
            for (int i = 0; i < prestamos.size(); i++) {
                if (prestamos.get(i).lectorPrest.numSocio.equals(numSocio) && prestamos.get(i).copiaPrest.identificador.equals(identificador)) {
                    if (prestamos.get(i).fechaFin <= hour) {
                        prestamos.remove(i);
                    } else {
                        Multa objmulta = new Multa(hour, 0, templ, prestamos.get(i));
                        multas.add(objmulta);
                        prestamos.remove(i);
                        return gson.toJson(multas);
                    }
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
        
        guardarLectoresEnArchivo(lectores);
        guardarCopiasEnArchivo(copias);
        guardarPrestamosnArchivo(prestamos);
        guardarMultasEnArchivo(multas);
        guardarLibrosEnArchivo(libros);
    }
    
    static void guardarLectoresEnArchivo(LinkedList<Lector> lectores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Lector.obj"))) {
            oos.writeObject(lectores);
            System.out.println("Lista guardada en ");
        } catch (IOException e) {
        }
    }

    static void guardarCopiasEnArchivo(LinkedList<Copia> copias) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Copia.obj"))) {
            oos.writeObject(copias);
            System.out.println("Lista guardada en ");
        } catch (IOException e) {
        }
    }

    static void guardarPrestamosnArchivo(LinkedList<Prestamo> prestamos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Prestamo.obj"))) {
            oos.writeObject(prestamos);
            System.out.println("Lista guardada en ");
        } catch (IOException e) {
        }
    }

    static void guardarMultasEnArchivo(LinkedList<Multa> multas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Multa.obj"))) {
            oos.writeObject(multas);
            System.out.println("Lista guardada en ");
        } catch (IOException e) {
        }
    }

    static void guardarLibrosEnArchivo(LinkedList<Libro> libros) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Libro.obj"))) {
            oos.writeObject(libros);
            System.out.println("Lista guardada en ");
        } catch (IOException e) {
        }
    }
    
    static LinkedList<Lector> cargarLectorDesdeArchivo() {
        LinkedList<Lector> lista = new LinkedList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Lector.obj"))) {
            lista = (LinkedList<Lector>) ois.readObject();
            System.out.println("Lista cargada desde ");
        } catch (IOException | ClassNotFoundException e) {
        }
        return lista;
    }

    static LinkedList<Copia> cargarCopiaDesdeArchivo() {
        LinkedList<Copia> lista = new LinkedList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Copia.obj"))) {
            lista = (LinkedList<Copia>) ois.readObject();
            System.out.println("Lista cargada desde ");
        } catch (IOException | ClassNotFoundException e) {
        }
        return lista;
    }

    static LinkedList<Prestamo> cargarPrestamoDesdeArchivo() {
        LinkedList<Prestamo> lista = new LinkedList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Prestamo.obj"))) {
            lista = (LinkedList<Prestamo>) ois.readObject();
            System.out.println("Lista cargada desde ");
        } catch (IOException | ClassNotFoundException e) {
        }
        return lista;
    }    

    static LinkedList<Multa> cargarMultaDesdeArchivo() {
        LinkedList<Multa> lista = new LinkedList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Multa.obj"))) {
            lista = (LinkedList<Multa>) ois.readObject();
            System.out.println("Lista cargada desde ");
        } catch (IOException | ClassNotFoundException e) {
        }
        return lista;
    }

    static LinkedList<Libro> cargarLibroDesdeArchivo() {
        LinkedList<Libro> lista = new LinkedList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Libro.obj"))) {
            lista = (LinkedList<Libro>) ois.readObject();
            System.out.println("Lista cargada desde ");
        } catch (IOException | ClassNotFoundException e) {
        }
        return lista;
    }
}
