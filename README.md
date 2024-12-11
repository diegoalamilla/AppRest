# AppRest

## Instalación

1. Clona el repositorio:

    ```bash
    git clone https://github.com/diegoalamilla/AppRest.git
    ```

2. Navega al directorio del proyecto:

    ```bash
    cd AppRest
    ```

3. Compila el proyecto con Maven:

    ```bash
    mvn clean install
    ```

4. Inicia la aplicación:

    ```bash
    mvn spring-boot:run
    ```

5. Empaquetar la aplicación:

    ```bash
    mvn package
    ```
6. Ejecutar Jar
   
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
- **POST /alumnos/{id}/fotoPerfil** - Sube foto de perfil del alumno a un S3
- **POST /alumnos/{id}/session/login** - Inicia sesión y se guarda la sesión en una tabla en DynamoDB
- **POST /alumnos/{id}/session/verify** - Verfiica que la sesión del alumnio esté activa
- **POST /alumnos/{id}/session/logout** - Cierra la sesión del alumno

### Profesores

- **GET /profesores** - Obtiene la lista de profesores
- **POST /profesores** - Crea un nuevo profesor
- **PUT /profesores/{id}** - Actualiza un profesor existente
- **DELETE /profesores/{id}** - Elimina un profesor por ID

## Variables de entorno en application.properties

### AWS credentials

- aws.accessKeyId=${AWS_ACCESS_KEY_ID}
- aws.secretAccessKey=${AWS_SECRET_ACCESS_KEY}
- aws.sessionToken=${AWS_SESSION_TOKEN}
- 
### AWS s3 bucket

- aws.region=${AWS_REGION}
- aws.bucketName=${AWS_S3_BUCKET_NAME}

### AWS sns

- aws.topicArn=${AWS_SNS_ARN_TOPIC}

### Database
- spring.datasource.url=jdbc:mysql://${AWS_RDS_HOSTNAME}:${AWS_RDS_PORT}/${AWS_RDS_DB_NAME}
- spring.datasource.username=${AWS_RDS_USERNAME}
- spring.datasource.password=${AWS_RDS_PASSWORD}
