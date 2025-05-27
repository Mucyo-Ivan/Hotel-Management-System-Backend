# 🏨 Hotel Booking Management System - Backend

A robust **Java Spring Boot** backend application for managing hotel bookings. This system handles **User Management**, **Hotel and Room Management**, **Room Bookings**, **Booking History and Cancellations**, **Billing with Email Notifications**, and secure **JWT-based Authentication**. Built with clean architecture, RESTful APIs, Spring Security, JPA, and MySQL.

---

## 📌 Features

- ✅ User Registration & Authentication (JWT-based)
- 🏢 Hotel & Room CRUD operations
- 📅 Room booking and real-time availability
- 🔁 Booking history, cancellations, and triggers
- 💵 Billing system with email notifications
- 🔐 Role-based access control (`ADMIN`, `CUSTOMER`)
- 🧪 Unit & Integration testing with JUnit

---

## 🛠️ Tech Stack

| Layer               | Technology Used                   |
|--------------------|------------------------------------|
| Backend Framework  | Spring Boot, Spring Data JPA       |
| Security           | Spring Security, JWT               |
| Database           | MySQL                              |
| ORM                | Hibernate                          |
| Email Service      | JavaMailSender                     |
| Testing            | JUnit, Mockito                     |
| API Documentation  | Swagger/OpenAPI                    |
| Build Tool         | Maven                              |

---

## 📂 Project Structure

Hotel_Booking_Management_System_Backend/
│
├── src/main/java/com/hotelbooking/
│ ├── config/ # JWT & security config
│ ├── controllers/ # REST controllers
│ ├── dto/ # Data transfer objects
│ ├── exceptions/ # Global exception handling
│ ├── models/ # JPA entities
│ ├── repositories/ # JPA repositories
│ ├── services/ # Business logic
│ └── HotelBookingApplication.java
│
├── src/main/resources/
│ ├── application.properties # DB & mail configs
│
├── src/test/java/ # Unit & integration tests
│
├── pom.xml # Maven dependencies
└── README.md

yaml
Copy
Edit

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL Server

### 1. Clone the repository

```bash
git clone https://github.com/YourUsername/Hotel_Booking_Management_System_Backend.git
cd Hotel_Booking_Management_System_Backend
2. Configure the database
In src/main/resources/application.properties:

properties
Copy
Edit
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_booking_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Secret
jwt.secret=your_jwt_secret

# Email Config
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
3. Build and run
bash
Copy
Edit
mvn clean install
mvn spring-boot:run
🔐 Authentication
JWT-based

Roles: ADMIN, CUSTOMER

Protected endpoints require Authorization header:

http
Copy
Edit
Authorization: Bearer <your_jwt_token>
📬 API Endpoints
Use Swagger at: http://localhost:8080/swagger-ui/

Public Routes
POST /api/auth/register - Register new user

POST /api/auth/login - Login and get JWT

Admin Routes
POST /api/hotels - Add new hotel

POST /api/rooms - Add room to hotel

GET /api/bookings/all - View all bookings

User Routes
GET /api/hotels - View all hotels

GET /api/rooms/hotel/{hotelId} - View available rooms

POST /api/bookings - Book a room

GET /api/bookings/user - View my bookings

DELETE /api/bookings/{bookingId} - Cancel booking

🔄 MySQL Triggers
A trigger on the bookings table ensures availability is updated on booking/cancellation:

sql
Copy
Edit
CREATE TRIGGER update_room_availability
AFTER INSERT ON bookings
FOR EACH ROW
BEGIN
  UPDATE rooms SET is_available = false WHERE id = NEW.room_id;
END;
📧 Email Notification
After successful booking or cancellation, a confirmation email is sent to the user.

🧪 Running Tests
bash
Copy
Edit
mvn test
✅ Future Improvements
Online payment integration (Stripe/PayPal)

Room recommendation system

Booking calendar with availability view

Mobile app integration (Flutter)

🤝 Contributing
Fork the repo

Create your branch (git checkout -b feature/feature-name)

Commit your changes (git commit -m 'Add feature')

Push to the branch (git push origin feature-name)

Open a Pull Request

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

👨‍💻 Author
Mucyo Ivan
📧 ivan.mucyo@example.com
🌐 LinkedIn | GitHub






