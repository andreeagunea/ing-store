# Tea store API

A tea store api built with Java, SpringBoot and Maven. It allows users to add, update 
or searches for teas stored in the database. The application uses JWT authentication.

# Technologies
- Maven version 3.3.4 
- Java Version 21.0 
- SpringBoot
- Spring Security 
- JWT 
- H2 Database 
- Lombok 
- JUnit 5 
- Mockito

# Auth endpoints

- POST api/auth/register  -  Register new user
- POST api/auth/login     -  Login with existing user
Admin user is created at startup with user: "adminUser" and password "admin123"

# User endpoints

- GET /api/users - Get all users registered
- PUT /api/users/role/{id} - Update role of user by id

# Tea endpoints

- GET api/teas                                   - Get all teas available in the db
- GET api/teas/search?name=name                  - Get the tea by name
- GET api/teas/search/category?category=category - Get the tea by category
- GET api/teas/search/price?min=min&max=max      - Get the tea with prince between min and max
- POST api/teas  body: RequestTea                - Add tea
- PUT api/teas/price/{id}                        - Update price of tea with id

**Steps for testing:**
1. Start the service - it starts on http://localhost:8080
2. Register new user or skip step if using admin user
3. Login with the selected user
4. Copy the token returned by the login request
5. Add the token as Bearer Token to each following request

Only Admins can perform role updates, see all users, add new teas and update tea price.
