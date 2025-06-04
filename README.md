# SubTracker Backend

This repo holds the backend for **SubTracker**, a subscription tracking app built for personal use and just for fun within a laboratory work at Web Programming course. It is mainly built with Java **Spring Boot**, and provides secure authentication, subscription management, and permission-based guest access using role-based control.


##  Features

- JWT-based authentication (stored in HttpOnly cookies)
- Context switching between owner and guest (Read-Only & Read-Write)
- Role-based access control (`OWNER`, `GUEST_RW`, `GUEST_RO`)
- Subscription CRUD operations (CREATE, READ, UPDATE & DELETE Subscriptions)
- Guest permission management


##  Running Locally

**Prerequisites**: you have to have installed *Java 18* at least, along with *Maven support* and *PostgreSQL* tools.


### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/felycianovac/SubTracker_backend
   cd subtracker-backend
2. Copy the example environment file and configure your local settings:

   - **Windows (CMD)**:
     ```cmd
     copy .env.example .env
     ```

   - **Unix/Linux/macOS**:
     ```bash
     cp .env.example .env
     ```

3. Edit the `.env` file with your own configuration:
   ```env
   DB_URL=db_url
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   JWT_SECRET_KEY=your_secret_key
   ```

4. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```

The backend will start at:  `http://localhost:8080`

---

## API Overview

All endpoints are prefixed with `/api` and secured using JWT in HttpOnly cookies. Role-based access is enforced via Spring Security annotations.

### Auth API (`/api/auth`)


| Method | Endpoint             | Description                                |
|--------|----------------------|--------------------------------------------|
| POST   | `/register`          | Register a new user  & receive a JWT                      |
| POST   | `/login`             | Log in and receive JWT   |
| POST   | `/logout`            | Clear JWT cookie                           |
| GET    | `/current`           | Get current logged-in user info            |
| POST   | `/switch-context`    | Switch to a guest context (if authorized)  |
| POST   | `/revert-context`    | Revert back to owner context               |

### Subscriptions API (`/api/subscriptions`)

> Requires `OWNER` or `GUEST_RW` role for create, update, and delete

| Method | Endpoint             | Description                                  |
|--------|----------------------|----------------------------------------------|
| GET    | `/`                  | Get paginated list of subscriptions by context |
| GET    | `/{id}`              | Get a subscription by ID                    |
| POST   | `/`                  | Create a new subscription                   |
| PUT    | `/`                  | Update an existing subscription             |
| DELETE | `/{id}`              | Delete a subscription                       |

Query Parameters:
- `contextUserId`: ID of the user whose subscriptions are being accessed
- `page`, `size`: Pagination options (defaults: 0 and 10) 


### Permissions API (`/api/permissions`)

> Used by OWNER to manage guest access

| Method | Endpoint             | Description                                  |
|--------|----------------------|----------------------------------------------|
| POST   | `/add`               | Grant a guest user access                    |
| PUT    | `/update`            | Update a guest's permission level            |
| DELETE | `/delete`            | Remove a guest's access                      |
| GET    | `/contexts`          | List all accessible user contexts            |
| GET    | `/guests`            | List all guests with access to current user  |

---

## API Documentation

For detailed API documentation and request/response schemas, run the app and visit: `http://localhost:8080/swagger-ui/index.html`. Then type `app-docs` in the filter/search bar. 


## Running with the Frontend

To use the backend together with the SubTracker frontend, make sure the frontend project is set up and running.

Frontend repository: [SubTracker Frontend](https://github.com/felycianovac/SubTrack/tree/feat/backend-integration) (Integrated-backend branch)

The frontend is configured to interact with the backend at `http://localhost:8080`. Make sure both applications are running simultaneously for full functionality.
