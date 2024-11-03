# AppRest

## Instalación

1. Clona el repositorio:

    ```bash
    git clone https://github.com/tu_usuario/tu_repositorio.git
    ```

2. Navega al directorio del proyecto:

    ```bash
    cd tu_repositorio
    ```

3. Compila el proyecto con Maven:

    ```bash
    mvn clean install
    ```

4. Inicia la aplicación:

    ```bash
    mvn spring-boot:run
    ```

### Usar con Docker

Si tienes un Dockerfile configurado, puedes crear una imagen de Docker y correrla en un contenedor:

1. Construye la imagen de Docker:

    ```bash
    docker build -t gestion-alumnos-profesores .
    ```

2. Ejecuta el contenedor:

    ```bash
    docker run -p 8080:8080 gestion-alumnos-profesores
    ```
## Endpoints

### Alumnos

- **GET /alumnos** - Obtiene la lista de alumnos
- **POST /alumnos** - Crea un nuevo alumno
- **PUT /alumnos/{id}** - Actualiza un alumno existente
- **DELETE /alumnos/{id}** - Elimina un alumno por ID

### Profesores

- **GET /profesores** - Obtiene la lista de profesores
- **POST /profesores** - Crea un nuevo profesor
- **PUT /profesores/{id}** - Actualiza un profesor existente
- **DELETE /profesores/{id}** - Elimina un profesor por ID
