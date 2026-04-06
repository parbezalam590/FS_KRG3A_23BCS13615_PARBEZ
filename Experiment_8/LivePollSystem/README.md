# Experiment 8 - App Security and Full-Stack Integration (LivePoll System)

This implementation provides a complete full-stack security flow:
- Spring Boot backend with Spring Security
- JWT authentication for local login/register
- Google OAuth 2.0 login integration
- Role-based access control (RBAC) with USER and ADMIN roles
- React frontend integrated with secured backend APIs
- CORS configuration for frontend-backend communication

## Project Structure

- `backend`: Spring Boot API with security + poll management
- `frontend`: React (Vite) client with authentication and role-aware pages

## Security Concepts Implemented

### 1) Authentication vs Authorization
- Authentication verifies identity:
  - Local login using email/password
  - Google OAuth login using external provider
- Authorization controls access:
  - `ROLE_USER`: can vote in polls
  - `ROLE_ADMIN`: can create, close, and delete polls

### 2) Security Filter Chain
- Custom JWT filter (`JwtAuthenticationFilter`) extracts and validates bearer token
- Filter sets authenticated user in security context
- Spring Security then applies route rules and method-level checks

### 3) OAuth 2.0 (Google Login)
- Backend endpoint: `/oauth2/authorization/google`
- On success, backend creates/loads user and redirects to frontend with JWT
- Frontend stores token and uses it in authenticated API calls

### 4) RBAC
- URL-based role checks in `SecurityConfig`
- Method-level checks using `@PreAuthorize`

## Backend Details

### Tech
- Spring Boot 3.3.2
- Spring Security
- Spring Data JPA
- H2 in-memory database
- JWT (jjwt)
- OAuth2 Client (Google)

### Default Demo Users (auto-seeded)
- Admin:
  - email: `admin@livepoll.com`
  - password: `Admin@123`
- User:
  - email: `user@livepoll.com`
  - password: `User@123`

### Important Backend Endpoints

#### Public
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /oauth2/authorization/google`

#### Authenticated
- `GET /api/auth/me`
- `GET /api/polls`
- `GET /api/polls/{id}`

#### USER only
- `POST /api/polls/{id}/vote`

#### ADMIN only
- `POST /api/admin/polls`
- `POST /api/admin/polls/{id}/close`
- `DELETE /api/admin/polls/{id}`

### Google OAuth Setup
Update in `backend/src/main/resources/application.properties`:
- `spring.security.oauth2.client.registration.google.client-id`
- `spring.security.oauth2.client.registration.google.client-secret`

Google Console authorized redirect URI:
- `http://localhost:8080/login/oauth2/code/google`

## Frontend Details

### Features
- Login/Register with JWT
- Google OAuth login button
- Dashboard for authenticated users
- Voting UI for USER role
- Admin page for creating/managing polls (ADMIN role)
- Protected routes based on auth + role

### Environment
Create `.env` from `.env.example` in frontend:
- `VITE_API_BASE_URL=http://localhost:8080`

## Run Instructions

## 1) Backend
Prerequisites:
- Java 17+
- Maven installed on system

Commands:
1. `cd backend`
2. `mvn spring-boot:run`

Backend runs at:
- `http://localhost:8080`

## 2) Frontend
Prerequisites:
- Node.js + npm

Commands:
1. `cd frontend`
2. `npm install`
3. `npm run dev`

Frontend runs at:
- `http://localhost:5173`

## Complete Security Flow Implemented
1. User logs in (local JWT or Google OAuth)
2. Backend creates/validates authentication
3. Frontend stores JWT and sends bearer token on secured requests
4. Backend JWT filter validates token for each request
5. Access is granted/denied by role rules and method annotations

## Learning Outcomes Mapping
This implementation demonstrates:
- Real backend API security with Spring Security
- Authentication and authorization separation
- OAuth 2.0 integration with Google
- RBAC with roles and `@PreAuthorize`
- Secure frontend-backend integration with CORS and bearer tokens
