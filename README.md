# WEB-FORUM-APP

A modern web forum application built using **Java**, **PostgreSQL**, and **Thymeleaf** for dynamic content rendering. This app allows users to create, comment on, and interact with topics while providing an intuitive, engaging user experience.

---

## 🏗️ Project Architecture

### Frontend
- **HTML Templates**: Dynamic page generation using **Thymeleaf**.
- **CSS**: Custom styles for a clean and responsive design.

### Backend
- **Java Servlet API**: Handles requests and business logic.
- **Data Processing Logic**: Manages entities and their relationships.

### Database: PostgreSQL
- **ORM**: **Hibernate** for seamless database interactions.
- **Database Structure**: Includes complex relationships between entities.

---

## 📅 Development Plan

### 1. Environment Setup and Project Structure - **November 27, 2024** (done)
- Install **PostgreSQL** and configure the database.
- Set up the project structure (directories, base classes).
- Configure **Hibernate** and test the database connection.

### 2. Topic Functionality Implementation - **December 1, 2024** (done)
- Display a **topic list** on the homepage.
- Implement **navigation** to individual topic pages.
- **Login System**: Secure user authentication.
- **Like/Dislike System**: Allow users to react to topics.
- **Comment System**:
    - Add, delete, like/dislike comments.
- Enable **topic deletion** by the author.

### 3. User Dashboard - **December 2, 2024** (done)
- Add sections for **"My Topics"** and **"Tracked (liked) Topics"**.
- Allow users to **update their profile** and information.
- Implement **roles** (User, Admin):
    - **Admin Features**: Delete topics, delete comments, delete users.
- **User Profile Viewing**: Allow users to view profiles of other users, including:
    - **Registration date**.
    - **Topics created** by the user.


### 4. Notifications - **December 4, 2024** (done)
- Implement notifications for:
    - New **likes** on topics.
    - New **comments** on a user’s topics.
- Display notifications in the **user dashboard**.

### 5. Sorting - **December 5, 2024** (done)
- Implement sorting options for topics:
    - **Most Popular** (by likes).
    - **Least Popular** (by dislikes).
    - Sort by **category**.

### 6. Search - **December 6, 2024** (done)
- **Search** functionality by **topic title**.

### 7. Registration - **December 8, 2024** (done)
- Implement user **registration** and authentication process.

### 8. Search user, improve forum navigation - **December 10, 2024** (done)
- Add user search, improve the transition between pages
---

## 🚀 Getting Started

### Prerequisites:
- **Java 11** or later.
- **PostgreSQL** database.
- **Maven** for project dependencies.
- IDE (e.g., IntelliJ IDEA, Eclipse).

### Endpoints:
- Register: http://localhost:8080/register
- Login: http://localhost:8080/login
- Forum Home: http://localhost:8080/forum - Displays the list of topics.
- Topic Page: http://localhost:8080/topic/id - Individual topic page.
- User Account: http://localhost:8080/account - View and update your profile.
- User Profile: http://localhost:8080/profile/id - View user profile.
- Users List: http://localhost:8080/users - List of all users.
- Notifications: http://localhost:8080/notifications - User notifications for new likes and comments.

### Setup:
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/web-forum-app.git
2. Open docker desktop(If you don't have it, download it)
    ```bash
   project-path/docker-compose build
   project-path/docker-compose up
