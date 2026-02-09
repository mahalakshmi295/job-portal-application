# 🚀 Professional Job Portal Application

A modern, full-stack job portal application with cutting-edge features including AI-based skill analysis, intelligent job matching, and comprehensive analytics. Built with React TypeScript frontend and Java Spring Boot backend.

![Job Portal](https://img.shields.io/badge/Status-Production%20Ready-success)
![Frontend](https://img.shields.io/badge/Frontend-React%20%2B%20TypeScript-blue)
![Backend](https://img.shields.io/badge/Backend-Spring%20Boot-green)

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)

## ✨ Features

### 🤖 AI & Intelligence

- **AI-Based Skill Gap Analysis** - Identifies missing skills and provides recommendations
- **Resume Quality Score System** - Evaluates resumes based on skills, experience, and education
- **Job Match Percentage** - Calculates compatibility between candidate and job requirements
- **Bias-Free Candidate Ranking** - Fair and unbiased candidate evaluation

### 💬 Communication

- **Intelligent Chatbot** - Job assistance and career guidance
- **Real-Time Notifications** - Instant updates on applications and opportunities

### 📊 Analytics & Insights

- **Analytics Dashboard** - Comprehensive platform metrics and insights
- **Visual Data Representation** - Clean, professional UI for data visualization

### 🔐 Security & Access

- **OTP-Based Login** - Secure, passwordless authentication
- **Role-Based Access Control (RBAC)** - Admin, Recruiter, and Candidate roles
- **Secure API Communication** with CORS support

## 🛠 Technology Stack

### Frontend

- **React 18** with TypeScript
- **CSS3** with modern gradients and animations
- **Fetch API** for backend communication
- **Responsive Design** for all device sizes

### Backend

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** for authentication
- **Spring Data JPA** for database operations
- **H2 Database** (in-memory for development)
- **Maven** for dependency management

## 📁 Project Structure

```
Job Portal Application/
├── frontend/                 # React TypeScript Frontend
│   ├── src/
│   │   ├── App.tsx          # Main application component
│   │   ├── App.css          # Professional styling
│   │   ├── SkillGap.tsx     # Skill gap analysis component
│   │   ├── ResumeScore.tsx  # Resume scoring component
│   │   ├── Chatbot.tsx      # Chatbot interface
│   │   ├── JobMatch.tsx     # Job matching component
│   │   ├── Notifications.tsx # Notifications display
│   │   ├── Analytics.tsx    # Analytics dashboard
│   │   └── OTPLogin.tsx     # OTP authentication
│   └── package.json
├── backend/                  # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/jobportal/backend/
│   │   │   │   ├── controller/      # REST Controllers
│   │   │   │   ├── model/           # Entity Models
│   │   │   │   ├── repository/      # JPA Repositories
│   │   │   │   ├── service/         # Business Logic
│   │   │   │   ├── security/        # Security Configuration
│   │   │   │   └── config/          # CORS & Other Config
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- **Java 17 or higher**
- **Maven 3.6+**
- **Node.js 16+ and npm**
- **Git**

### Backend Setup

1. **Navigate to backend directory**

   ```bash
   cd backend
   ```

2. **Run the Spring Boot application**

   ```bash
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

3. **Access H2 Console** (optional)
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:jobportaldb`
   - Username: `sa`
   - Password: (leave empty)

### Frontend Setup

1. **Navigate to frontend directory**

   ```bash
   cd frontend
   ```

2. **Install dependencies**

   ```bash
   npm install
   ```

3. **Start the development server**

   ```bash
   npm start
   ```

   The frontend will start on `http://localhost:3000`

### Quick Start (Both Services)

**Terminal 1 (Backend):**

```bash
cd backend
mvn spring-boot:run
```

**Terminal 2 (Frontend):**

```bash
cd frontend
npm start
```

## 🔌 API Endpoints

### Authentication

- `POST /api/auth/otp-login` - Request OTP for login
- `POST /api/auth/verify-otp` - Verify OTP

### User Management

- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users/email/{email}` - Get user by email
- `POST /api/users` - Create new user

### Job Management

- `GET /api/jobs` - Get all jobs
- `POST /api/jobs` - Create new job
- `GET /api/jobs/{id}` - Get job by ID
- `DELETE /api/jobs/{id}` - Delete job

### AI Features

- `POST /api/skill-gap` - Analyze skill gap
- `POST /api/resume-score` - Calculate resume score
- `POST /api/job-match` - Calculate job match percentage
- `POST /api/ranking` - Rank candidates (bias-free)

### Communication

- `POST /api/chatbot` - Chat with job assistant
- `GET /api/notifications` - Get notifications

### Analytics

- `GET /api/analytics` - Get platform analytics

### Health Check

- `GET /api/health` - Backend health status

## 🎨 UI Features

- **Modern Gradient Design** - Beautiful purple/blue gradient theme
- **Smooth Animations** - Fade-in effects and hover transitions
- **Responsive Layout** - Works on desktop, tablet, and mobile
- **Interactive Components** - Real-time feedback and loading states
- **Professional Typography** - Clean, readable fonts
- **Intuitive Navigation** - Easy-to-use tab-based interface

## 🔧 Configuration

### Backend Configuration (`application.properties`)

```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:jobportaldb
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```

### Frontend Configuration

All components use `http://localhost:8080` as the backend API URL. Update if your backend runs on a different port.

## 🧪 Testing

### Backend Tests

```bash
cd backend
mvn test
```

### Frontend Tests

```bash
cd frontend
npm test
```

## 📦 Production Build

### Backend

```bash
cd backend
mvn clean package
java -jar target/jobportal-backend-0.0.1-SNAPSHOT.jar
```

### Frontend

```bash
cd frontend
npm run build
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 Future Enhancements

- [ ] Integrate real AI/ML models for skill gap analysis
- [ ] Add email notification service
- [ ] Implement resume file upload and parsing
- [ ] Add advanced search and filtering
- [ ] Implement WebSocket for real-time notifications
- [ ] Add user profile management
- [ ] Integrate payment gateway for premium features
- [ ] Add job application tracking
- [ ] Implement interview scheduling
- [ ] Add company profiles and reviews

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- **Aavishkarr Projects** - Full Stack Development

## 🙏 Acknowledgments

- Spring Boot community for excellent documentation
- React community for awesome tools and libraries
- All contributors and supporters

---

**⭐ Star this repository if you find it helpful!**

_Built with ❤️ using React, TypeScript, Java, and Spring Boot_
