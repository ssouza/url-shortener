

Serviço capaz de encurtar e atender requisições em urls encurtadas (redirecionamento).

## Pré-requisitos

* JDK 8
* Maven 3

## Parametrização

O registro é configurado para expirar `expiration.timeInMinutes` em minutos. O tempo pode ser alterado no arquivo `application.properties`.


## Build
É construído usando o Maven, executando:

```$ mvn clean install```

## Run
O JAR é gerado na pasta `target` do projeto e deve ser executado com:

```$ java -jar target/url-shortener.jar --server.port=8080```


### Request POST

POST => __http://localhost:8080/api/v1/shorten__

#### Content-Type

`application/json`

#### Body

```
{
  "url": "https://www.google.com"
}
```

#### Response

```
{
    "newUrl": "http://localhost:8080/api/v1/shorten/lCCvvDERKGREwfb",
    "expiresAt": "2019-07-25T21:18:49.050444"
}
```


### Request GET

A chamada GET __http://localhost:8080/api/v1/shorten/lCCvvDERKGREwfb__, do exemplo, irá redirecionar para o link __https://www.google.com__ salvo em BD, caso este não tenha seu período expirado.


