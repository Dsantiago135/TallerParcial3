# TallerParcial3#

Diagrama de flujo: https://drive.google.com/file/d/1hK-lDAFJR9FU2HBmtanmDJdKElIhq8UU/view?usp=sharing

Este Proyecto consta de 8 opciones en las cuales podras registar tanto libros como lectores, prestar libros, devolver libros, etc.

puedes acceder a estas funciones desde

-http://localhost:4567/PrestarLibro/102/45/354

-http://localhost:4567/RegistrarCopia/28/Disponible/Spiderman

-http://localhost:4567/RegistrarLector/2463/Pedro/Hernandez/cr24n56

-http://localhost:4567/PrestarLibro/2463/28/364

-http://localhost:4567/DevolverLibro/2463/28

-http://localhost:4567/VerMultas

-http://localhost:4567/VerCopias

-http://localhost:4567/VerLectores

-http://localhost:4567/VerPrestamos


Para el registro de copias es necesario el numero identificador y su estado (Disponible, Prestado).

Para el registro de lectores es necesario un numero de socio, su nombre, su apellido y su direccion.

Para el prestamo de libros se necesita l numero de socio del lector y el numero de identificacion de la copia a prestar y en numero del año del fin del prestamo.

Para la devolucion de libros se necesita el numero de socio del lector y el numero de identificacion de la copia a devolver.

La generacion de la multa se hace automaticamente tomando el dia actual del dispositivo.

El programa ya trae 2 lectores y 2 libros cargados por defecto.
