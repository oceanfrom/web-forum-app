
# WEB-FORUM-APP
## Project Architecture
### Frontend
HTML templates using Thymeleaf for dynamic page generation.
CSS for styling.
### Backend
Use Java Servlet API for request handling.
Data processing logic (for entities).
### Database (PostgreSQL)
ORM: Hibernate for database interaction.
Database structure includes relationships between entities



## Development Plan
#### 1. Environment Setup and Project Structure - November 27, 2024
   - Install PostgreSQL and configure the database.
   - Create the project structure (directories, base classes).
   - Set up Hibernate and test the database connection. 
#### 2. Topic Functionality Implementation - December 1, 2024
   - Implement the topic list on the main page.
   - Add navigation to individual topic pages with detailed content display.
   - Implement login system
   - Integrate a like/dislike system for topics.
   - Develop the comment system:
   - Add, delete, like/dislike comments.
   - Enable topic deletion by the author.
#### 3. User dashboard - December 2, 2024
   - Add sections for "My Topics" and "Tracked Topics."
   - Implement a delete button for the user's own topics.
#### 4. Notifications - December 4, 2024
   - Implement logic for notifications about:
   - New likes on topics.
   - New comments on the user's topics.
   - Display notifications in the user dashboard.
#### 5. Sorting - December 5, 2024
   - Implement sorting options for topics:
   - Most popular (by likes).
   - Least popular (by dislikes).
   - By category.
#### 6. Search and Pagination - December 6, 2024
   - Implement search functionality by topic title on the main page.
   - Configure pagination for topics and comments.
#### 7. Registration - December 8, 2024