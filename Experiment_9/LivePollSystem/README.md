# Experiment 9 - Secure and Scalable Full Stack System (LivePoll)

This project implements a secure and scalable full-stack system using Spring Boot and React, with:
- Spring Security filter chain and JWT authentication
- Google OAuth 2.0 login integration
- Role-Based Access Control (RBAC) for endpoint protection
- JPA-based database performance optimization
- Secure frontend-backend integration using CORS

## Objectives Coverage

1. Implement backend security using Spring Security and filter chains
   - `SecurityConfig` defines route authorization and security filters.
   - `JwtAuthenticationFilter` validates bearer tokens per request.
2. Enable authentication using OAuth (Google Login)
   - `/oauth2/authorization/google` starts OAuth login.
   - OAuth success handler issues JWT and redirects to frontend.
3. Apply RBAC for endpoint protection
   - URL-level restrictions for `/api/admin/**`.
   - Method-level checks with `@PreAuthorize`.
4. Optimize database interactions using JPA and performance techniques
   - Fetch-optimized poll queries with `@EntityGraph`.
   - Indexed high-traffic columns in poll, option, and vote tables.
   - Atomic vote-count updates to reduce write race issues.
   - Hibernate batching and fetch-size tuning in application properties.
5. Integrate secure backend with React frontend using CORS
   - Explicit CORS allow-list configured in backend.
   - Frontend sends JWT as `Authorization: Bearer <token>`.

## Project Structure

- `backend`: Spring Boot API with security, auth, and poll management
- `frontend`: React (Vite) app with auth flow and role-aware routes

## Backend Stack

- Spring Boot 3.3.2
- Spring Security
- Spring OAuth2 Client
- Spring Data JPA
- H2 database (in-memory)
- JWT (jjwt)

## Security and Access Rules

- Public:
  - `POST /api/auth/register`
  - `POST /api/auth/login`
  - `GET /oauth2/authorization/google`
- Authenticated:
  - `GET /api/auth/me`
  - `GET /api/polls`
  - `GET /api/polls/{id}`
- USER role:
  - `POST /api/polls/{id}/vote`
- ADMIN role:
  - `POST /api/admin/polls`
  - `POST /api/admin/polls/{id}/close`
  - `DELETE /api/admin/polls/{id}`

## Performance Optimizations Added

- JPA fetch optimization:
  - Poll list/detail queries load options efficiently using `@EntityGraph`.
- Database indexing:
  - Poll creation time/status and vote relation columns are indexed.
- Write path optimization:
  - Voting uses an atomic SQL update to increment option counts safely.
- Hibernate tuning:
  - `hibernate.jdbc.batch_size`, ordered insert/update, and batch fetch size.
- API transaction boundaries:
  - Read methods use `@Transactional(readOnly = true)`.

## Default Demo Users

- Admin: `admin@livepoll.com` / `Admin@123`
- User: `user@livepoll.com` / `User@123`

## Google OAuth Setup

Update in `backend/src/main/resources/application.properties`:
- `spring.security.oauth2.client.registration.google.client-id`
- `spring.security.oauth2.client.registration.google.client-secret`

Google console redirect URI:
- `http://localhost:8080/login/oauth2/code/google`

## Frontend Setup

Create `.env` in `frontend`:
- `VITE_API_BASE_URL=http://localhost:8080`

## Run Instructions

### 1) Backend

1. `cd backend`
2. `mvn spring-boot:run`

Runs on `http://localhost:8080`

### 2) Frontend

1. `cd frontend`
2. `npm install`
3. `npm run dev`

Runs on `http://localhost:5173`
