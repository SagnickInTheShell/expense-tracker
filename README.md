# 💰 Expense Tracker

> Full-stack personal finance management system built with Spring Boot and PostgreSQL

## 🚀 Features

✅ User Authentication (Signup/Login)  
✅ Add, Edit, Delete Expenses  
✅ Filter by Category & Date Range  
✅ Search Expenses  
✅ Monthly Summary with Budget Tracking  
✅ Yearly Analytics  
✅ Category-wise Breakdown

## 🛠️ Tech Stack

**Backend:**
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Maven

**Tools:**
- IntelliJ IDEA
- Postman
- pgAdmin
- Git & GitHub

## 📊 API Endpoints

### Authentication
- `POST /auth/signup` - Create new user
- `POST /auth/login` - User login

### Expenses
- `GET /expenses?userId={id}` - Get all expenses
- `POST /expenses` - Add new expense
- `PUT /expenses/{id}` - Update expense
- `DELETE /expenses/{id}` - Delete expense

### Analytics
- `GET /expenses/summary/monthly?userId={id}` - Monthly summary
- `GET /expenses/summary/yearly?userId={id}` - Yearly summary

## 🗄️ Database Schema

### Users Table
- id (Primary Key)
- email (Unique)
- name
- password
- monthlyBudget

### Expenses Table
- id (Primary Key)
- user_id (Foreign Key)
- title
- amount
- category
- paymentMethod
- expenseDate

## 🏃 Running Locally

### Prerequisites
- Java 17+
- PostgreSQL 14+
- Maven 3.6+

### Steps

1. Clone repository
```bash
git clone https://github.com/YOUR_USERNAME/expense-tracker.git
cd expense-tracker
```

2. Create PostgreSQL database
```sql
CREATE DATABASE "expense-tracker";
```

3. Configure `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/expense-tracker
spring.datasource.username=postgres
spring.datasource.password=your_password
```

4. Run application
```bash
./mvnw spring-boot:run
```

5. Test at `http://localhost:8080`

## 📝 Sample API Request

### Add Expense
```json
POST http://localhost:8080/expenses
Content-Type: application/json

{
  "userId": 1,
  "title": "Grocery Shopping",
  "amount": 1500,
  "category": "Food",
  "paymentMethod": "UPI",
  "expenseDate": "2025-10-20"
}
```
# 💰 Expense Tracker - Smart Personal Finance Manager

> Full-stack expense management system built with Spring Boot, PostgreSQL, and modern web technologies

![Dashboard Preview](screenshots/dashboard.png)

## 🚀 [Live Demo](#) (Coming soon)

---

## ✨ Features

### 🔐 Authentication
- User signup with validation
- Secure login system
- Session management with localStorage

### 💸 Expense Management
- ✅ Add expenses with details (title, amount, category, payment method, date)
- ✅ View all expenses in organized list
- ✅ Delete expenses
- ✅ Real-time search functionality
- ✅ Category-wise filtering

### 📊 Analytics & Insights
- ✅ Monthly spending summary
- ✅ Interactive pie chart (category breakdown)
- ✅ Budget tracking with alerts
- ✅ Over/under budget indicators
- ✅ Total expenses count

### 🎨 UI/UX
- ✅ Beautiful gradient design
- ✅ Dark/Light mode toggle
- ✅ Mobile responsive
- ✅ Smooth animations
- ✅ Loading states
- ✅ Modern card-based layout

---

## 🛠️ Tech Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.x** - Framework
- **Spring Data JPA** - Database ORM
- **PostgreSQL 16** - Database
- **Maven** - Dependency management

### Frontend
- **HTML5** - Structure
- **CSS3** - Styling (Gradients, Flexbox, Animations)
- **JavaScript ES6** - Logic
- **Bootstrap 5** - UI framework
- **Chart.js** - Data visualization

### Tools
- IntelliJ IDEA
- Postman
- pgAdmin
- Git & GitHub

---

## 📸 Screenshots

### Login Page
![Login](screenshots/login.png)

### Dashboard - Light Mode
![Dashboard Light](screenshots/dashboard.png)

### Dashboard - Dark Mode
![Dashboard Dark](screenshots/dark-mode.png)

### Add Expense
![Add Expense](screenshots/add-expense.png)


---

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    monthly_budget DECIMAL DEFAULT 10000.0
);
```

### Expenses Table
```sql
CREATE TABLE expenses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    title VARCHAR(255) NOT NULL,
    amount DECIMAL NOT NULL,
    category VARCHAR(100),
    payment_method VARCHAR(50),
    expense_date DATE
);
```

**Relationship:** One-to-Many (One user → Many expenses)

---

## 📡 API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/signup` | Create new user |
| POST | `/auth/login` | User login |

### Expenses
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/expenses?userId={id}` | Get all user expenses |
| POST | `/expenses` | Add new expense |
| DELETE | `/expenses/{id}` | Delete expense |
| GET | `/expenses/summary/monthly?userId={id}` | Monthly summary |

---

## 🏃 Running Locally

### Prerequisites
- Java 17 or higher
- PostgreSQL 14 or higher
- Maven 3.6+

### Steps

1. **Clone repository**
```bash
git clone https://github.com/YOUR_USERNAME/expense-tracker.git
cd expense-tracker
```

2. **Create PostgreSQL database**
```sql
CREATE DATABASE expense_tracker;
```

3. **Configure database**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/expense_tracker
spring.datasource.username=postgres
spring.datasource.password=Mysoreking0410
```

4. **Run application**
```bash
./mvnw spring-boot:run
```

5. **Open in browser**
```
http://localhost:8080/login.html
```

---

## 💡 Key Features Explained

### Search Functionality
Real-time search filters expenses by title or category as you type

### Dark Mode
Theme preference saved in localStorage, persists across sessions

### Budget Alerts
- 🟢 Green: Under 80% budget
- 🟠 Orange: 80-100% budget used
- 🔴 Red: Over budget

### Charts
Interactive pie chart shows category-wise spending distribution

---

## 🎯 Future Enhancements

- [ ] Export to Excel/PDF
- [ ] Receipt image upload
- [ ] AI spending insights
- [ ] Voice-powered expense entry
- [ ] Budget recommendations
- [ ] Recurring expenses
- [ ] Multi-currency support
- [ ] Email notifications
- [ ] Social expense splitting
- [ ] Mobile app

---

## 📚 What I Learned

### Spring Boot Concepts
- **Dependency Injection** with @Autowired
- **JPA/Hibernate** for automatic SQL generation
- **@Entity** annotations for database mapping
- **@RestController** for REST APIs
- **CORS** configuration

### Database Design
- One-to-Many relationships
- Foreign key constraints
- Query optimization
- JPQL custom queries

### Frontend Development
- Fetch API for HTTP requests
- localStorage for client-side storage
- Chart.js for data visualization
- Responsive design patterns
- Dark mode implementation

---

## 🐛 Known Issues

- No password hashing (for demo purposes)
- Basic authentication (no JWT)
- No input validation on frontend

---

## 👨‍💻 Developer

**Sagnick**  
📧 Email: sagnickparuk@gmail.com  
🔗 GitHub: [@SagnickInTheShell](https://github.com/SagnickInTheShell)

---

⭐ **Star this repo if you found it helpful!**