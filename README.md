# 📚 Library Management System

Sistema web completo para la gestión de bibliotecas, desarrollado como proyecto académico con enfoque Full Stack. Permite registrar libros, gestionar usuarios, controlar préstamos y devoluciones, y mantener un historial detallado de operaciones.

---

## 🚀 Tecnologías utilizadas

### 🖥️ Frontend
- Angular
- TypeScript
- Tailwind CSS (opcional)
- Vercel para despliegue

### ⚙️ Backend
- Spring Boot (Java)
- Spring Data JPA
- Spring Security (opcional)
- PostgreSQL
- Render para despliegue

---

## 🧩 Funcionalidades principales

- 🔍 Registro y búsqueda de libros por título, autor, ISBN o categoría.
- 👥 Gestión de usuarios (roles: administrador, lector).
- 📥 Registro de préstamos y devoluciones de libros.
- ⏰ Control de fechas límite y penalización por retrasos.
- 📊 Historial completo de préstamos por usuario.
- 🔐 Sistema de login y autenticación básica.
- 🌐 Despliegue completo en la nube (Vercel + Render).

---

## 📂 Estructura del repositorio

```
library-management-system/
├── frontend/ # Proyecto Angular
├── backend/ # Proyecto Spring Boot
└── README.md
```

---

## 🔗 Enlaces del proyecto

- 🖥️ **Frontend (Angular)**: [https://library-management-frontend.vercel.app/](https://library-management-frontend.vercel.app/)
- ⚙️ **Backend (Spring Boot)**: [https://library-management-backend.onrender.com/](https://library-management-backend.onrender.com/)
- 📦 **Repositorio Frontend**: [github.com/Emerson147/library-management-frontend](https://github.com/Emerson147/library-management-frontend)
- 🗃️ **Repositorio Backend**: [github.com/Emerson147/library-management-backend](https://github.com/Emerson147/library-management-backend)

---

## 🛠️ Cómo ejecutar el proyecto localmente

### 🔧 Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run

🧪 Frontend (Angular)
cd frontend
npm install
ng serve
```

