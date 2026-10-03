# SpeedFast - Semana 8

Aplicación de escritorio desarrollada en Java para gestionar los pedidos, repartidores y entregas de una empresa de reparto. Esta versión integra una interfaz gráfica Swing con una base de datos MySQL y organiza las operaciones de persistencia mediante el patrón DAO.

## Objetivo

Implementar operaciones CRUD completas y conectarlas con la interfaz gráfica, manteniendo separadas las responsabilidades del modelo, la lógica del sistema, la presentación y el acceso a datos.

## Funcionalidades

- Registro de pedidos de comida, encomienda y express.
- Asignación automática de identificadores mediante MySQL.
- Consulta, modificación y eliminación de pedidos desde una `JTable`.
- Registro, consulta, modificación y eliminación de repartidores.
- Persistencia de la disponibilidad y la mochila térmica de cada repartidor.
- Asignación de repartidores según las reglas de cada tipo de pedido.
- Actualización persistente de los estados `PENDIENTE`, `EN_REPARTO` y `ENTREGADO`.
- Procesamiento concurrente de entregas mediante `Runnable` y `ExecutorService`.
- Registro de entregas asociadas a pedidos y repartidores.
- Consulta, corrección de fecha y hora, y eliminación de entregas.
- Validación para impedir que un pedido no entregado sea registrado como entrega.
- Validación para evitar dos entregas asociadas al mismo pedido desde la interfaz.
- Manejo seguro de conexiones y sentencias SQL mediante `try-with-resources` y `PreparedStatement`.

## Reglas principales

- Un pedido de comida requiere un repartidor disponible con mochila térmica.
- Un pedido express requiere un repartidor disponible.
- Una encomienda debe pesar más de 0 kg y como máximo 50 kg.
- Una encomienda rechazada se registra para mantener su trazabilidad, pero no puede iniciar una entrega.
- Una entrega solo puede registrarse cuando el pedido se encuentra en estado `ENTREGADO`.
- Los identificadores son generados por MySQL y no se modifican desde la interfaz.
- Los pedidos o repartidores asociados a una entrega no pueden eliminarse mientras exista esa relación.

## Arquitectura

El proyecto distribuye sus responsabilidades de la siguiente manera:

- `modelo`: representa pedidos, repartidores, entregas y estados del dominio.
- `dao.interfaces`: define los contratos CRUD de cada entidad.
- `dao.impl`: implementa las consultas SQL y transforma los resultados de MySQL en objetos Java.
- `gestor`: coordina el registro, la asignación, los cambios de estado y la simulación de entregas.
- `vista`: contiene las ventanas Swing y valida los datos ingresados antes de llamar a los DAO.
- `interfaces`: conserva los comportamientos polimórficos de cancelación, despacho y seguimiento.
- `resources`: contiene el script SQL y el logotipo utilizado por la aplicación.

Las clases `PedidoResumen` y `EntregaResumen` transportan los datos comunes recuperados desde MySQL para mostrarlos y modificarlos sin reconstruir todas las subclases del modelo.

## Base de datos

La aplicación utiliza la base de datos `speedfast_db` y tres tablas relacionadas:

- `repartidor`: almacena nombre, mochila térmica y disponibilidad.
- `pedido`: almacena dirección, tipo, distancia, estado y repartidor asignado.
- `entrega`: relaciona un pedido con un repartidor y registra la fecha y hora de entrega.

El script de creación se encuentra en:

```text
src/main/resources/database/speedfast_db.sql
```

Las claves foráneas protegen la integridad de los datos. Por esta razón, si se desea eliminar un pedido o un repartidor que ya participa en una entrega, primero debe eliminarse el registro correspondiente de `entrega`.

## Estructura del proyecto

```text
src/main/
|-- java/
|   |-- dao/
|   |   |-- ConexionBD.java
|   |   |-- interfaces/
|   |   |   |-- EntregaDAO.java
|   |   |   |-- PedidoDAO.java
|   |   |   `-- RepartidorDAO.java
|   |   `-- impl/
|   |       |-- EntregaDAOImpl.java
|   |       |-- PedidoDAOImpl.java
|   |       `-- RepartidorDAOImpl.java
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

## Tecnologías utilizadas

- Java 17.
- Java Swing.
- Maven.
- JDBC.
- MySQL y MySQL Connector/J.
- Programación orientada a objetos y colecciones.
- Concurrencia con `Runnable`, `ExecutorService` y `BlockingQueue`.

## Configuración y ejecución

1. Abrir el proyecto en IntelliJ IDEA.
2. Configurar un JDK 17.
3. Recargar Maven para descargar MySQL Connector/J.
4. Iniciar el servicio de MySQL.
5. Ejecutar `speedfast_db.sql` desde MySQL Workbench.
6. Abrir `Run > Edit Configurations` en IntelliJ IDEA.
7. Agregar la variable de entorno `SPEEDFAST_DB_PASSWORD` con la contraseña local de MySQL.
8. Ejecutar la clase `main.Main`.

La contraseña no se almacena en el repositorio. La conexión utiliza el usuario local `root`, el puerto `3306` y la base de datos `speedfast_db`.

## Flujo de prueba sugerido

1. Registrar un repartidor y comprobar que aparezca en su tabla.
2. Modificar su disponibilidad o mochila térmica.
3. Registrar un pedido y verificarlo en `Gestionar Pedidos`.
4. Modificar su dirección o distancia.
5. Asignar un repartidor compatible al pedido.
6. Iniciar la simulación y esperar que el pedido avance a `ENTREGADO`.
7. Volver a abrir la lista de pedidos y confirmar el estado almacenado en MySQL.
8. Abrir `Gestionar Entregas` y comprobar el registro generado.
9. Probar la modificación de fecha y hora de una entrega.
10. Comprobar que la interfaz impida registrar una segunda entrega para el mismo pedido.

## Repositorio

[POOII SpeedFast Semana 8](https://github.com/Nicole88-94b/POOII_SpeedFast_Semana8)

## Autora

Nicole Ortega
