# SpeedFast - Semana 7

Aplicación de escritorio desarrollada en Java para gestionar pedidos de una empresa de reparto. En esta versión se incorporó persistencia con MySQL mediante JDBC, manteniendo la interfaz gráfica, el modelo orientado a objetos y la simulación concurrente desarrollada durante las semanas anteriores.

## Objetivo

Conectar la aplicación SpeedFast con una base de datos relacional para registrar y consultar pedidos, repartidores y entregas de forma persistente.

## Funcionalidades

- Registro de pedidos de comida, encomienda y express desde una interfaz Swing.
- Asignación automática del identificador de cada pedido mediante MySQL.
- Validación de dirección, distancia, peso y estado del embalaje.
- Registro de repartidores con disponibilidad y mochila térmica.
- Consulta de los pedidos persistidos mediante `JTable`.
- Asignación de repartidores según las reglas de cada tipo de pedido.
- Procesamiento concurrente de entregas mediante `Runnable` y `ExecutorService`.
- Registro de cada entrega con su pedido, repartidor, fecha y hora.
- Manejo de conexiones y recursos mediante `try-with-resources`.

## Reglas principales

- Un pedido de comida requiere un repartidor disponible con mochila térmica.
- Un pedido express requiere un repartidor disponible.
- Una encomienda debe pesar más de 0 kg y como máximo 50 kg.
- Una encomienda rechazada se registra para mantener su trazabilidad, pero no se entrega.
- Los pedidos procesados avanzan desde `PENDIENTE` a `EN_REPARTO` y finalmente a `ENTREGADO`.

## Base de datos

El esquema utiliza la base de datos `speedfast_db` y las siguientes tablas:

- `pedido`: almacena dirección, tipo, distancia y estado.
- `repartidor`: almacena el nombre del repartidor.
- `entrega`: relaciona un pedido con un repartidor y registra fecha y hora.

El script de creación se encuentra en:

```text
src/main/resources/database/speedfast_db.sql
```

El atributo `distancia_km` amplía el modelo propuesto para conservar el dato utilizado en los cálculos de tiempo de entrega.

## Estructura del proyecto

```text
src/main/
|-- java/
|   |-- dao/
|   |   |-- ConexionBD.java
|   |   |-- EntregaDAO.java
|   |   |-- PedidoDAO.java
|   |   `-- RepartidorDAO.java
|   |-- gestor/
|   |-- interfaces/
|   |-- main/
|   |-- modelo/
|   `-- vista/
`-- resources/
    |-- database/
    |   `-- speedfast_db.sql
    `-- vista/
        `-- SpeedFast_logo.png
```

## Clases de persistencia

- `ConexionBD`: establece la conexión con MySQL mediante `DriverManager`.
- `PedidoDAO`: guarda pedidos y consulta sus datos para mostrarlos en la tabla.
- `RepartidorDAO`: guarda repartidores y permite consultar todos los registros.
- `EntregaDAO`: registra la relación entre pedido y repartidor.
- `PedidoResumen`: representa los datos comunes recuperados desde la tabla `pedido`.

## Tecnologías

- Java 17.
- Java Swing.
- Maven.
- JDBC.
- MySQL y MySQL Connector/J.
- Colecciones y programación orientada a objetos.
- Concurrencia con `Runnable`, `ExecutorService` y `BlockingQueue`.

## Configuración

1. Abrir el proyecto en IntelliJ IDEA.
2. Configurar un JDK 17.
3. Recargar el proyecto Maven para descargar MySQL Connector/J.
4. Iniciar el servicio de MySQL.
5. Ejecutar `speedfast_db.sql` desde MySQL Workbench.
6. Abrir `Run > Edit Configurations` en IntelliJ.
7. Agregar la variable de entorno `SPEEDFAST_DB_PASSWORD` con la contraseña local de MySQL.
8. Ejecutar la clase `main.Main`.

La contraseña no se almacena en el repositorio. El usuario configurado para la conexión local es `root` y la aplicación utiliza el puerto predeterminado `3306`.

## Flujo de prueba

1. Registrar un pedido desde la ventana principal.
2. Comprobar que aparezca en `Listar Pedidos`.
3. Registrar o seleccionar un repartidor compatible.
4. Asignar el repartidor al pedido.
5. Iniciar la entrega y esperar la finalización de los hilos.
6. Revisar el resumen y la confirmación de persistencia en el área de texto.
7. Consultar las tablas `pedido`, `repartidor` y `entrega` en MySQL.

Los pedidos consultados en `JTable` provienen de MySQL. La asignación y simulación se realizan con los pedidos registrados durante la ejecución actual de la aplicación.

## Repositorio

[POOII SpeedFast Semana 7](https://github.com/Nicole88-94b/POOII_SpeedFast_Semana7)

## Autora

Nicole Ortega
