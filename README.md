# Photo Storage Application

A cloud-based photo storage service that allows users to upload, manage, and organize their photos. The application supports JPG format and includes user management, album creation, and subscription plans.

## Features

- User registration and authentication
- Photo upload and management (JPG format only)
- Album creation and organization
- Admin panel for user and system management
- Subscription plan management
- MinIO object storage integration

## Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- PostgreSQL database
- MinIO object storage server
- Docker (optional, for containerized deployment)

## Technologies Used

- **Backend**: Java Spring Boot
- **Database**: PostgreSQL
- **Object Storage**: MinIO
- **Containerization**: Docker, Docker Compose
- **Build Tool**: Maven

## Database

Application uses PostgreSQL database. For starting the application you need a Postgres server (default: `jdbc:postgresql://localhost:5432/storage`) with a created database named 'storage'. The database contains eight tables:

* **users** - contains information about application users
* **authorities** - contains authorities and roles available for application users
* **users_authorities** - link table between users and authorities
* **plans** - contains plans available to users (for future application scale)
* **subscriptions** - contains information about users' plans, issue and expiry date (for future application scale); upon registration user gets a free plan until 2050-01-01
* **photos** - contains information about uploaded user photos
* **albums** - contains information about created user albums
* **albums_photos** - link table between albums and photos

## Storage

The application uses MinIO object storage for storing files. All MinIO properties are stored in storage.properties. To start the application, you need a MinIO server running at `localhost:9000`, with access and secret keys configured for the application, and created buckets for photos and thumbnails.

## Installation

### Local Development Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd storage-application
   ```

2. Set up PostgreSQL database:
   - Install PostgreSQL
   - Create a database named `storage`
   - Update database connection settings in `application.properties`

3. Set up MinIO:
   - Install and run MinIO server
   - Create buckets for photos and thumbnails
   - Update MinIO connection settings in `storage.properties`

4. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### Docker Setup

The application can also be run using Docker Compose:

```bash
docker-compose up -d
```

## Usage

The application starts on `http://localhost:8080` by default. There is a default admin account with username `root` and password `root`.

### Available Endpoints for Users

#### Authentication
* `POST /signup` - User registration
  ```json
  {
    "username": "username",
    "password": "password",
    "email": "username@gmail.com"
  }
  ```
* `POST /login` - Login
  ```json
  {
    "username": "username",
    "password": "password"
  }
  ```

#### User Management
* `GET /user` - Get current user information
* `PUT /user/change/email` - Change user's email
  ```json
  {
    "email": "new email"
  }
  ```
* `PUT /user/change/password` - Change user's password
  ```json
  {
    "oldPassword": "old password",
    "newPassword": "new password",
    "repeatNewPassword": "new password"
  }
  ```
* `DELETE /user` - Delete current user's account

#### Photos
* `GET /photos` - Show all user's photos
* `POST /photos/upload` - Upload photos (multipart/form-data)
* `GET /photo/{photoId}` - Show photo by ID
* `GET /photo/{photoId}/download` - Download photo by ID
* `DELETE /photo/{photoId}/delete` - Delete photo by ID
* `POST /photos/download` - Download selected photos
  ```json
  {
    "ids": [1,2,3,4,...]
  }
  ```
* `DELETE /photos` - Delete selected photos
  ```json
  {
    "ids": [1,2,3,...]
  }
  ```

#### Albums
* `GET /albums` - Show all user's albums
* `POST /album` - Create album
  ```json
  {
    "title": "album title",
    "photoIds": [1,2,3,...]
  }
  ```
* `GET /album/{albumId}` - Show album by ID
* `PUT /album/{albumId}` - Edit album by ID
  ```json
  {
    "title": "new album title",
    "photoIds": [1,2,3,...]
  }
  ```
* `GET /album/{albumId}/download` - Download album by ID
* `DELETE /album/{albumId}` - Delete album by ID

#### Subscriptions
* `GET /subscription` - Show user's subscription
* `GET /plans` - Show available plans

## Available Endpoints for Admins

#### Admin Management
* `POST /admin/signup` - Admin registration
  ```json
  {
    "username": "user",
    "password": "password",
    "email": "user@gmail.com"
  }
  ```

#### User Management
* `GET /admin/users` - Show all users
* `GET /admin/user/{userId}` - Show user by ID
* `POST /admin/user` - Create user
  ```json
  {
    "username": "user",
    "password": "password",
    "email": "user@gmail.com",
    "usedSpace": 0,
    "authorities": [
      {
        "id": 1,
        "name": "ADMIN"
      }
    ]
  }
  ```
* `PUT /admin/user` - Edit user
  ```json
  {
    "id": 1,
    "username": "user",
    "password": "password",
    "email": "user@gmail.com",
    "usedSpace": 0,
    "authorities": [
      {
        "id": 1,
        "name": "ADMIN"
      }
    ]
  }
  ```
* `DELETE /admin/user/{userId}` - Delete user by ID

#### Authority Management
* `GET /admin/authorities` - Show all authorities
* `GET /admin/authority/{authorityId}` - Show authority by ID
* `POST /admin/authority` - Create authority
  ```json
  {
    "name": "USER"
  }
  ```
* `PUT /admin/authority` - Edit authority
  ```json
  {
    "id": 1,
    "name": "ADMIN"
  }
  ```
* `DELETE /admin/authority/{authorityId}` - Delete authority by ID

#### Plan Management
* `GET /admin/plans` - Show all plans
* `GET /admin/plan/{planId}` - Show plan by ID
* `POST /admin/plan` - Create plan
  ```json
  {
    "title": "50GB",
    "availableSpace": 50000000000,
    "monthlyPrice": 1.99,
    "yearlyPrice": 19.99,
    "isActive": true
  }
  ```
* `PUT /admin/plan` - Edit plan
  ```json
  {
    "id": 1,
    "title": "50GB",
    "availableSpace": 50000000000,
    "monthlyPrice": 1.99,
    "yearlyPrice": 19.99,
    "isActive": true
  }
  ```
* `DELETE /admin/plan/{planId}` - Delete plan by ID

#### Subscription Management
* `GET /admin/subscriptions` - Show all subscriptions
* `GET /admin/subscription/{subscriptionId}` - Show subscription by ID
* `POST /admin/subscription` - Create subscription
  ```json
  {
    "userId": 1,
    "plan": {
      "id": 1,
      "title": "FREE",
      "availableSpace": 10000000000,
      "monthlyPrice": 0,
      "yearlyPrice": 0,
      "isActive": true
    },
    "issuedDate": "2023-04-11",
    "expiredDate": "2050-01-01",
    "isActive": true
  }
  ```
* `PUT /admin/subscription` - Edit subscription
  ```json
  {
    "id": 1,
    "userId": 1,
    "plan": {
      "id": 1,
      "title": "FREE",
      "availableSpace": 10000000000,
      "monthlyPrice": 0,
      "yearlyPrice": 0,
      "isActive": true
    },
    "issuedDate": "2023-04-11",
    "expiredDate": "2050-01-01",
    "isActive": true
  }
  ```
* `DELETE /admin/subscription/{subscriptionId}` - Delete subscription by ID

## Configuration

The application uses several configuration files:

* `application.properties` - Main application configuration (database, server settings)
* `storage.properties` - MinIO storage configuration (access keys, endpoints)
* `pom.xml` - Maven dependencies and build configuration
* `Dockerfile` - Docker image configuration
* `docker-compose.yml` - Docker Compose configuration for multi-container deployment

## Environment Variables

The following environment variables can be configured:

* `DB_HOST` - Database host (default: localhost)
* `DB_PORT` - Database port (default: 5432)
* `DB_NAME` - Database name (default: storage)
* `DB_USER` - Database username
* `DB_PASSWORD` - Database password
* `MINIO_ENDPOINT` - MinIO server endpoint
* `MINIO_ACCESS_KEY` - MinIO access key
* `MINIO_SECRET_KEY` - MinIO secret key
* `SERVER_PORT` - Application server port (default: 8080)

## Project Structure

```
storage-application/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── storage/
│   │   │           ├── controller/     # API controllers
│   │   │           ├── model/          # Data models
│   │   │           ├── repository/     # Database repositories
│   │   │           ├── service/        # Business logic
│   │   │           └── StorageApplication.java  # Main application class
│   │   └── resources/
│   │       ├── application.properties # Application configuration
│   │       └── storage.properties     # Storage configuration
│   └── test/                          # Unit and integration tests
├── Dockerfile
├── docker-compose.yml
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Commit your changes (`git commit -m 'Add some amazing feature'`)
5. Push to the branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

If you encounter any issues or have questions about the application, please open an issue in the repository.