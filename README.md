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

## 👨‍💻 Developer

**Sagnick**  
📧 Email: sagnickparuk@gmail.com  
🔗 GitHub: [@SagnickInTheShell](https://github.com/YOUR_USERNAME)

---

⭐ **Star this repo if you found it helpful!**