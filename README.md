# 🚀 Backend Fambo - ¡Aprendiendo y programando juntos en Stream!

¡Hola! Bienvenido a este proyecto. Soy nuevo en YouTube y he estado construyendo este backend en mi tiempo libre para practicar. Es una aplicación en **Spring Boot 4** estructurada con Arquitectura Hexagonal que tiene cositas de usuarios y una pequeña base para un gestor de alimentos (`foodManager`).

Ahora mismo **me he quedado un poco estancado** con algunas partes del código, ¡y por eso he decidido crear el canal de YouTube! Quiero que construyamos, resolvamos los bugs y mejoremos este proyecto juntos en los directos.

Si eres principiante, estás aprendiendo o tienes más experiencia y me quieres echar una mano, ¡eres más que bienvenido!

---

## 🛣️ Endpoints actuales del proyecto

* **Registro:** POST `/api/user/register`
* **Login:** POST `/api/user/login`
* **Logout:** POST `/api/user/logout`
* **Nombre:** PUT `/api/user/change-username`
* **Admin:** GET `/api/admin/users`
* **Salud:** GET `/health`

---

## 🍏 ¿De qué trata el módulo FoodManager?

La idea principal del proyecto es crear un **gestor de alimentos**. Queremos controlar el inventario de comida que tenemos en casa basándonos en el peso en gramos.

Actualmente, cada `Product` calcula su estado automáticamente en Java:
* **MISSING:** Si la cantidad actual es 0 gramos (¡hay que comprarlo ya!).
* **LOW:** Si hay comida, pero menos de la cantidad necesaria.
* **OK:** Si estamos bien abastecidos.

---

## 🎯 ¡Nuestra primera misión en el Stream!

¡Aquí es donde necesito tu ayuda! El código de los productos ya calcula su estado, pero **todavía no podemos usarlo desde fuera**. En los próximos directos vamos a programar juntos:

1. **El Controlador:** Crear el `FoodManagerController` para exponer los endpoints en la API.
2. **Los Endpoints:** Hacer los métodos para agregar un producto (`POST`), ver el inventario (`GET`) y actualizar los gramos (`PUT`).
3. **La Persistencia:** Conectar este módulo a una base de datos para que los alimentos no se borren al apagar el servidor.

---

## 🤝 Cómo puedes ayudarme (¡No tengas miedo de participar!)

No importa si estás empezando en la programación. Si ves un error en los directos o quieres sugerir una mejor forma de hacer las cosas:

1. **En el chat del Stream:** Coméntame en vivo qué crees que falla o qué podemos cambiar.
2. **Mando un Pull Request (PR):** Si quieres meterle mano al código directamente:
    * Haz un *Fork* de este repositorio.
    * Crea una rama con tu nombre: `git checkout -b mejora/tu-nombre`.
    * ¡Abriremos tu propuesta y la probaremos juntos en la pantalla durante el directo!

¡Pásate por el canal, saluda en el chat y vamos a sacar este backend adelante juntos! 🎬
