# Job Portal Application

This project is a full-stack job portal application with a professional frontend and backend. It integrates external APIs for real-time job search, AI-powered features, geolocation services, and advanced analytics. The application provides AI-based skill gap analysis, resume quality scoring, an intelligent chatbot, job matching with market data, and comprehensive analytics.

## Structure

- **frontend/**: React + TypeScript modern UI
- **backend/**: Java Spring Boot REST API with external API integrations

## Core Features

### 🤖 AI-Powered Features

1. **AI Skill Gap Analysis** - Uses real job market data from RapidAPI to identify skill gaps
2. **AI-Enhanced Chatbot** - OpenAI GPT-3.5 powered job assistance (with rule-based fallback)
3. **Resume Quality Scoring** - Affinda API for advanced resume parsing (with regex fallback)
4. **Job Match Percentage** - Real-time matching against live job postings

### 🌐 External API Integrations

5. **RapidAPI JSearch** - Real job listings from multiple sources (5,000 free searches/month)
6. **OpenAI GPT-3.5** - Intelligent conversational AI for career guidance
7. **Affinda Resume Parser** - Advanced resume data extraction (100 free resumes/month)
8. **ipapi.co Geolocation** - IP-based location detection (1,000 free requests/day)

### 📊 Analytics & Insights

9. **Market Analytics Dashboard** - Real-time job market trends and salary insights
10. **Skill Demand Tracking** - Industry skill trends from live job data
11. **Application Funnel Analytics** - Track your job search progress

### 🔐 Security Features

12. **OTP-based Login** - Email-based two-factor authentication
13. **Role-based Access Control (RBAC)** - Secure user permissions
14. **Bias-Free Candidate Ranking** - Fair algorithmic scoring

## API Integration Benefits

✅ **Real Job Data**: Access to thousands of live job postings  
✅ **AI Intelligence**: GPT-powered career advice and insights  
✅ **Market Accuracy**: Live salary data and skill demand trends  
✅ **Graceful Fallback**: Works perfectly without API keys (uses mock data)  
✅ **Cost Effective**: Free tiers cover most development needs ($0-$10/month for production)

## Getting Started

### Prerequisites

- Node.js 16+ and npm/yarn (Frontend)
- Java 17+ and Maven 3.8+ (Backend)
- Git
- API Keys (Optional - app works without them):
  - RapidAPI JSearch (Recommended for real job data)
  - OpenAI API Key (Optional for AI chatbot)
  - Affinda API Token (Optional for resume parsing)

### Frontend Setup

```bash
cd frontend
npm install
npm start
# Runs on http://localhost:3000
```

### Backend Setup

1. **Set JAVA_HOME** (if not already set):

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

2. **Configure API Keys** (Optional):

Edit `backend/src/main/resources/application.properties`:

```properties
# RapidAPI JSearch (Recommended)
rapidapi.key=your_rapidapi_key_here

# OpenAI (Optional)
openai.api.key=sk-your_openai_key_here

# Affinda (Optional)
affinda.api.token=your_affinda_token_here
```

**Note**: App works perfectly without API keys - uses high-quality mock data!

3. **Configure Environment Variables** (Required for email/OTP):

Create a `.env` file in the backend folder or set environment variables:

```powershell
# Email Configuration (Required for OTP Login)
$env:MAIL_USERNAME = "your_email@gmail.com"
$env:MAIL_PASSWORD = "your_app_password"  # Use Google App Password if using Gmail

# JWT Configuration (Required for authentication)
$env:JWT_SECRET = "your_jwt_secret_key_here_min_32_characters"

# Optional - Frontend CORS
$env:ALLOWED_ORIGINS = "http://localhost:3000"
```

**For Gmail Users**: Generate an [App Password](https://support.google.com/accounts/answer/185833) instead of using your main password.

4. **Compile and Run**:

**Exact Command for PowerShell:**

```powershell
# Set environment variables (REQUIRED for OTP/Email to work)
$env:MAIL_USERNAME = "your_email@gmail.com"
$env:MAIL_PASSWORD = "xxxx xxxx xxxx xxxx"  # Use Gmail App Password, NOT regular password
$env:JWT_SECRET = "your_jwt_secret_key_here_minimum_32_characters_long_string"
$env:ALLOWED_ORIGINS = "http://localhost:3000"

# Navigate to backend and run
cd backend
..\apache-maven-3.9.12\bin\mvn.cmd spring-boot:run
```

**Watch for these success messages:**

```
✅ Sample users initialized with secure password hashing
✅ Sample jobs initialized
✅ Job Portal Backend is ready!
🚀 Started JobPortalBackendApplication in 9.575 seconds (process running for 10.66)
```

**Using the Advanced Startup Script (Recommended)**:

The project includes an advanced startup script that handles `.env` configuration, environment variables, and Maven execution automatically.

```powershell
# 1. Setup your .env file (first time only)
.\run-backend-v2.ps1 -Setup

# 2. Edit the .env file with your credentials
notepad .env

# 3. Test your configuration
.\run-backend-v2.ps1 -Test

# 4. Start the backend
.\run-backend-v2.ps1
```

Benefits of using `run-backend-v2.ps1`:
- ✅ **Automatic .env Loading**: No need to set environment variables manually.
- ✅ **Configuration Validation**: Checks for missing keys and valid lengths.
- ✅ **Error Handling**: Provides helpful tips if the backend fails to start.
- ✅ **No Hardcoded Secrets**: Keeps your credentials secure in `.env`.


**Test the Backend**:

```bash
# Check health
curl http://localhost:8080/api/health

# View H2 Database Console
# Visit: http://localhost:8080/h2-console

# Test OTP (see test_otp.html)
# Open: test_otp.html in your browser
```

### Get API Keys (Optional)

See [API_INTEGRATION.md](API_INTEGRATION.md) for detailed setup instructions including:

- Step-by-step signup guides
- Free tier limits
- Testing commands
- Cost estimates
- Security best practices

## Project Structure

```
job-portal/
├── frontend/                # React TypeScript UI
│   ├── src/
│   │   ├── components/     # React components
│   │   ├── services/       # API services
│   │   └── types/          # TypeScript types
│   └── package.json
├── backend/                 # Spring Boot API
│   ├── src/main/java/
│   │   └── com/jobportal/backend/
│   │       ├── controller/ # REST endpoints
│   │       ├── service/    # Business logic + API integrations
│   │       ├── model/      # Data models
│   │       ├── repository/ # Data access
│   │       └── config/     # App & API configuration
│   └── pom.xml
├── README.md               # This file
└── API_INTEGRATION.md      # Detailed API setup guide
```

## Key API Endpoints

### Job Search (RapidAPI Integrated)

- `GET /api/jobs/search` - Search real jobs with filters
- `GET /api/jobs/details/{jobId}` - Get job details
- `GET /api/jobs/salary-estimate` - Get salary data
- `GET /api/jobs/trending` - Trending tech jobs

### AI Chatbot (OpenAI Integrated)

- `POST /api/chatbot/ai` - AI-enhanced responses
- `POST /api/chatbot/message` - Rule-based fallback

### Geolocation (ipapi.co Integrated)

- `GET /api/geo/location` - Get location from IP
- `POST /api/geo/geocode` - Address to coordinates
- `POST /api/geo/distance` - Calculate distance

### Analytics (Real Market Data)

- `GET /api/analytics/user/{userId}` - User dashboard
- `GET /api/analytics/job-market` - Market insights with real data
- `GET /api/analytics/funnel/{userId}` - Application funnel

### Resume & Skills

- `POST /api/resume/analyze` - AI resume analysis
- `POST /api/skills/gap-analysis` - Market-based skill gaps

## Technology Stack

### Frontend

- **React 18** with TypeScript
- **Axios** for API calls
- **React Router** for navigation
- **CSS3** for styling

### Backend

- **Spring Boot 3.2.0** (Java 17)
- **Spring Web** - REST APIs
- **Spring Security** - Authentication
- **Spring Data JPA** - Database access
- **Spring Mail** - Email OTP
- **Spring Cache** - API response caching
- **H2 Database** - In-memory storage
- **WebFlux** - External API calls

### External Services

- **RapidAPI JSearch** - Job listings
- **OpenAI GPT-3.5** - AI chatbot
- **Affinda** - Resume parsing
- **ipapi.co** - Geolocation

## Development Notes

### API Integration Strategy

- **Graceful Degradation**: All features work without API keys
- **Caching**: Reduces API calls (5-min for jobs, 1-hour for location)
- **Fallback Logic**: Mock data when APIs unavailable
- **Error Handling**: Try-catch blocks with user-friendly messages

### Testing

- Use `test_all_features.html` for comprehensive endpoint testing
- API documentation available in `API_INTEGRATION.md`
- All features testable without external API keys

### Cost Management

- **Free Tier Usage**: Covers most development needs
- **Caching**: Minimizes redundant API calls
- **Monitoring**: Track usage in respective dashboards
- **Estimated Cost**: $0-$10/month for small projects

## Contribution Guidelines

- Follow existing code patterns and naming conventions
- Add error handling for all API integrations
- Include fallback logic for external dependencies
- Update API_INTEGRATION.md when adding new APIs
- Test with and without API keys configured

## Future Enhancements

- [ ] Add more job board integrations (LinkedIn, Indeed)
- [ ] Implement WebSocket for real-time updates
- [ ] Add resume builder feature
- [ ] Create mobile app (React Native)
- [ ] Add interview preparation module
- [ ] Implement AI-powered interview simulator

## Troubleshooting

### ⚠️ OTP Not Sending?

**This is the #1 issue!** See [OTP_TROUBLESHOOTING.md](OTP_TROUBLESHOOTING.md) for complete guide.

**Quick Fix**:

1. Use **Gmail App Password** (NOT regular password)
   - Get it here: https://myaccount.google.com/apppasswords
2. Ensure **2-Factor Authentication** is enabled
3. Set correct environment variables:
   ```powershell
   $env:MAIL_USERNAME = "your_email@gmail.com"
   $env:MAIL_PASSWORD = "xxxx xxxx xxxx xxxx"  # 16-char App Password
   $env:JWT_SECRET = "minimum_32_characters_long"
   ```
4. Check logs for: `✅ OTP email sent successfully to:`

**Test OTP**:

- Open [test_otp.html](test_otp.html) in browser to test end-to-end
- Or use backend logs to see OTP generated

### Backend won't start - "Could not resolve placeholder 'MAIL_USERNAME'"

**Root Cause**: Required environment variables are not set before running the backend.

**Solutions**:

**Option 1 - Use the Advanced Startup Script (Recommended)**:

The `run-backend-v2.ps1` script is the easiest way to run the backend as it manages all environment variables for you.

```powershell
# Setup .env if you haven't already
.\run-backend-v2.ps1 -Setup

# Start the backend
.\run-backend-v2.ps1
```

**Option 2 - Use the Basic Startup Script**:

```powershell
# Edit run-backend.ps1 with your credentials
notepad run-backend.ps1

# Then run it
.\run-backend.ps1
```

**Option 2 - Set environment variables manually (What the script does)**:

```powershell
$env:MAIL_USERNAME = "your_email@gmail.com"
$env:MAIL_PASSWORD = "app_password_16_chars"
$env:JWT_SECRET = "your_jwt_secret_minimum_32_characters"
$env:ALLOWED_ORIGINS = "http://localhost:3000"
cd backend
..\apache-maven-3.9.12\bin\mvn.cmd spring-boot:run
```

**Option 3 - Create .env file in backend folder** (Experimental):

```properties
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=app_password_16_chars
JWT_SECRET=your_jwt_secret_minimum_32_characters
ALLOWED_ORIGINS=http://localhost:3000
```

### Gmail Setup Issues

- ❌ "Authentication failed" → Use [App Password](https://myaccount.google.com/apppasswords), not regular password
- ❌ "No app passwords available" → Enable [2-Factor Authentication](https://myaccount.google.com/security) first
- ❌ "Empty password" → Make sure you copied all 16 characters with spaces

### Backend starts but no database

- H2 in-memory database is created automatically
- Visit: `http://localhost:8080/h2-console` to view the database
- JDBC URL: Use the one shown in startup logs

### Backend won't start - Other errors

- Check JAVA_HOME is set to JDK 17: `java -version` should show 17.x.x
- Verify Maven is properly installed: `mvn -version`
- Check port 8080 is available: `netstat -ano | findstr :8080`
- Verify all required environment variables are set (see above)

### API not returning real data

- Verify API keys in application.properties
- Check API key validity in respective dashboards
- Review console logs for error messages
- App works fine with mock data if APIs unavailable

### CORS errors

- Backend already configured for localhost:3000
- Check frontend is running on correct port

## License

This project is for educational and development purposes.

---

**Note**: For detailed API setup instructions, cost analysis, and troubleshooting, see [API_INTEGRATION.md](API_INTEGRATION.md)

_Built with ❤️ using Spring Boot, React, and modern APIs_

# backend latest setup

# Set environment variables EXACTLY like this

$env:MAIL_USERNAME = "your_email@gmail.com"
$env:MAIL_PASSWORD = "xxxx xxxx xxxx xxxx" # Paste your 16-char App Password here
$env:JWT_SECRET = "your_jwt_secret_key_here_minimum_32_characters_long_string"
$env:ALLOWED_ORIGINS = "http://localhost:3000"

# Run backend

cd backend
..\apache-maven-3.9.12\bin\mvn.cmd spring-boot:run

````

---

## APPENDIX A: Complete API Endpoints Reference

### Authentication Endpoints

#### 1. OTP Login Request
```bash
POST /api/auth/otp-login
Content-Type: application/json

{
  "email": "user@example.com"
}
````

**Response (200 OK):**

```json
{
  "otpSent": true,
  "message": "OTP sent to your email. Valid for 5 minutes.",
  "email": "user@example.com"
}
```

#### 2. Verify OTP

```bash
POST /api/auth/verify-otp
Content-Type: application/json

{
  "email": "user@example.com",
  "otp": "123456"
}
```

**Response (200 OK):**

```json
{
  "success": true,
  "message": "OTP verified successfully",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400000
}
```

#### 3. Check OTP Status

```bash
GET /api/auth/otp-status?email=user@example.com
```

**Response (200 OK):**

```json
{
  "email": "user@example.com",
  "otpGenerated": true,
  "expiryTime": 1641234567000,
  "remainingTime": 245000
}
```

### Job Search Endpoints

#### 4. Search Jobs

```bash
GET /api/jobs/search?query=software%20engineer&location=New%20York&page=1&numPages=1
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**

```json
{
  "success": true,
  "jobs": [
    {
      "id": "job_1",
      "title": "Senior Software Engineer",
      "company": "Tech Corp",
      "location": "New York, NY",
      "salary": "$120,000 - $160,000",
      "jobDescription": "...",
      "jobHighlight": "..."
    }
  ],
  "totalResults": 1250,
  "page": 1,
  "pageSize": 10
}
```

#### 5. Get Job Details

```bash
GET /api/jobs/details/{jobId}
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**

```json
{
  "jobId": "job_1",
  "title": "Senior Software Engineer",
  "company": "Tech Corp",
  "location": "New York, NY",
  "salary": "$120,000 - $160,000",
  "fullDescription": "...",
  "requirements": ["Java", "Spring Boot", "PostgreSQL"],
  "benefits": ["Health Insurance", "401k", "Remote Work"],
  "appliedDate": "2024-01-15",
  "applicationDeadline": "2024-02-15"
}
```

#### 6. Get Salary Estimate

```bash
GET /api/jobs/salary-estimate?jobTitle=Software%20Engineer&location=San%20Francisco
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**

```json
{
  "jobTitle": "Software Engineer",
  "location": "San Francisco, CA",
  "avgSalary": 145000,
  "minSalary": 100000,
  "maxSalary": 200000,
  "currency": "USD",
  "dataPoints": 542,
  "lastUpdated": "2024-01-20"
}
```

#### 7. Get Trending Jobs

```bash
GET /api/jobs/trending?count=10&timeframe=month
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**

```json
{
  "trendingJobs": [
    {
      "jobTitle": "Data Scientist",
      "searchVolume": 5420,
      "growthRate": 12.5,
      "avgSalary": 125000,
      "topLocations": ["San Francisco", "New York", "Seattle"]
    }
  ],
  "lastUpdated": "2024-01-20T10:30:00Z"
}
```

### Chatbot Endpoints

#### 8. Send Chat Message

```bash
POST /api/chatbot
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "message": "How do I prepare for a technical interview?"
}
```

**Response (200 OK):**

```json
{
  "userMessage": "How do I prepare for a technical interview?",
  "response": "Here are key steps to prepare: 1. Review data structures...",
  "conversationId": "conv_12345",
  "suggestions": ["Mock interviews", "Algorithm practice", "System design"],
  "source": "ai" | "rule-based"
}
```

#### 9. Get Chat Suggestions

```bash
GET /api/chatbot/suggestions
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**

```json
{
  "suggestions": [
    "How do I find jobs?",
    "Can you help me improve my resume?",
    "What are common interview questions?",
    "How can I identify my skill gaps?",
    "What skills are in demand?"
  ],
  "categories": [
    "Job Search",
    "Resume Help",
    "Interview Prep",
    "Skill Development",
    "Career Advice"
  ]
}
```

#### 10. Get Chatbot Capabilities

```bash
GET /api/chatbot/capabilities
```

**Response (200 OK):**

```json
{
  "name": "Job Portal Assistant",
  "version": "1.0",
  "capabilities": [
    "Job Search Assistance",
    "Resume Analysis & Tips",
    "Interview Preparation",
    "Skill Gap Analysis",
    "Career Guidance",
    "Application Tips",
    "Salary Information"
  ],
  "supportedLanguages": ["English"],
  "isAiEnabled": true
}
```

### Resume & Skills Endpoints

#### 11. Analyze Resume

```bash
POST /api/resume/analyze
Authorization: Bearer {JWT_TOKEN}
Content-Type: multipart/form-data

file: [binary file]
```

**Response (200 OK):**

```json
{
  "analysisId": "analysis_1",
  "score": 78.5,
  "feedback": [
    {
      "section": "skills",
      "score": 85,
      "feedback": "Great technical skills section. Consider adding more certifications."
    },
    {
      "section": "experience",
      "score": 72,
      "feedback": "Experience section is good. Quantify achievements more."
    }
  ],
  "suggestions": [
    "Add more action verbs",
    "Highlight leadership experience",
    "Include metrics and results"
  ],
  "recommendedSkills": ["AWS", "Docker", "Kubernetes"]
}
```

#### 12. Skill Gap Analysis

```bash
POST /api/skills/gap-analysis
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "currentSkills": ["Java", "Spring Boot", "SQL"],
  "targetRole": "Senior Software Engineer",
  "targetLocation": "San Francisco"
}
```

**Response (200 OK):**

```json
{
  "currentSkills": ["Java", "Spring Boot", "SQL"],
  "requiredSkills": [
    "Java",
    "Spring Boot",
    "SQL",
    "Cloud",
    "Kubernetes",
    "Microservices"
  ],
  "missingSkills": ["Cloud", "Kubernetes", "Microservices"],
  "skillDemand": {
    "Cloud": 95,
    "Kubernetes": 87,
    "Microservices": 92
  },
  "learningPath": [
    {
      "skill": "Cloud Computing",
      "timeToLearn": "3-6 months",
      "resources": ["AWS Course", "Azure Fundamentals"],
      "jobMarketDemand": "Very High"
    }
  ]
}
```

### Geolocation Endpoints

#### 13. Get Current Location

```bash
GET /api/geo/location
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**

```json
{
  "ip": "203.0.113.0",
  "city": "San Francisco",
  "state": "CA",
  "country": "United States",
  "latitude": 37.7749,
  "longitude": -122.4194,
  "timezone": "America/Los_Angeles",
  "isp": "Example ISP"
}
```

#### 14. Geocode Address

```bash
POST /api/geo/geocode
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "address": "1600 Pennsylvania Avenue NW, Washington, DC"
}
```

**Response (200 OK):**

```json
{
  "address": "1600 Pennsylvania Avenue NW, Washington, DC",
  "latitude": 38.8951,
  "longitude": -77.0369,
  "city": "Washington",
  "state": "DC",
  "zipCode": "20500",
  "country": "United States"
}
```

#### 15. Calculate Distance

```bash
POST /api/geo/distance
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "from": {
    "latitude": 37.7749,
    "longitude": -122.4194
  },
  "to": {
    "latitude": 34.0522,
    "longitude": -118.2437
  }
}
```

**Response (200 OK):**

```json
{
  "distance": 380.7,
  "unit": "miles",
  "duration": "6 hours 30 minutes",
  "route": "I-5 South"
}
```

### Analytics Endpoints

#### 16. Get User Analytics

```bash
GET /api/analytics/user/{userId}
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**

```json
{
  "userId": "user_123",
  "jobsViewed": 45,
  "jobsApplied": 12,
  "jobsSaved": 8,
  "jobsShortlisted": 3,
  "applicationConversion": 26.7,
  "averageScorePerResume": 76.2,
  "skillsMatched": ["Java", "Spring Boot", "PostgreSQL"],
  "topSearches": ["Software Engineer", "Backend Developer"],
  "activityTrend": {
    "week": 15,
    "month": 67
  }
}
```

#### 17. Get Job Market Analytics

```bash
GET /api/analytics/job-market
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**

```json
{
  "totalJobsAvailable": 125420,
  "topSkills": [
    {
      "skill": "Cloud Computing",
      "demand": 95,
      "jobs": 45230,
      "avgSalary": 145000
    }
  ],
  "topCompanies": ["Google", "Amazon", "Microsoft"],
  "salaryTrends": {
    "juniorLevel": 75000,
    "midLevel": 110000,
    "seniorLevel": 155000
  },
  "hiringTrend": "Increasing",
  "marketInsight": "Tech hiring is up 15% this quarter"
}
```

#### 18. Get Application Funnel Analytics

```bash
GET /api/analytics/funnel/{userId}
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**

```json
{
  "userId": "user_123",
  "funnel": {
    "jobsViewed": 120,
    "jobsInterested": 45,
    "jobsApplied": 12,
    "jobsShortlisted": 3,
    "jobsInterviewed": 2,
    "jobsOffered": 1
  },
  "conversionRates": {
    "viewedToApplied": 10.0,
    "appliedToShortlisted": 25.0,
    "shortlistedToInterview": 66.7,
    "interviewToOffer": 50.0
  },
  "recommendations": ["Improve resume quality", "Apply to more relevant jobs"]
}
```

---

## APPENDIX B: Environment Variables Configuration

### Complete .env File Template

```properties
# Email Configuration (REQUIRED for OTP)
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=xxxx xxxx xxxx xxxx
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587

# JWT Configuration (REQUIRED)
JWT_SECRET=your_jwt_secret_key_here_minimum_32_characters_long_string
JWT_EXPIRATION_MS=86400000

# CORS Configuration (REQUIRED)
ALLOWED_ORIGINS=http://localhost:3000

# API Keys (OPTIONAL - App works without them)
RAPIDAPI_KEY=your_rapidapi_key_here
OPENAI_API_KEY=sk-your_openai_key_here
AFFINDA_API_TOKEN=your_affinda_token_here

# Database Configuration (Optional - H2 is default)
MONGODB_URI=mongodb://localhost:27017/jobportal
MONGODB_DATABASE=jobportal

# Application Environment
ENVIRONMENT=development
```

### PowerShell Setup Script

```powershell
# Set ALL Required Environment Variables
$env:MAIL_USERNAME = "your_email@gmail.com"
$env:MAIL_PASSWORD = "xxxx xxxx xxxx xxxx"
$env:MAIL_HOST = "smtp.gmail.com"
$env:MAIL_PORT = "587"
$env:JWT_SECRET = "your_jwt_secret_key_here_minimum_32_characters_long_string"
$env:JWT_EXPIRATION_MS = "86400000"
$env:ALLOWED_ORIGINS = "http://localhost:3000"

# Optional API Keys
$env:RAPIDAPI_KEY = "your_rapidapi_key_here"
$env:OPENAI_API_KEY = "sk-your_openai_key_here"
$env:AFFINDA_API_TOKEN = "your_affinda_token_here"

# Environment Type
$env:ENVIRONMENT = "development"

# Verify variables are set
Write-Host "Environment variables configured:"
Write-Host "MAIL_USERNAME: $env:MAIL_USERNAME"
Write-Host "JWT_SECRET: Set ($(if($env:JWT_SECRET.Length -ge 32) { 'Valid' } else { 'Invalid' }))"

# Run backend
cd backend
..\apache-maven-3.9.12\bin\mvn.cmd spring-boot:run
```

### Windows Batch File Script (run-backend.bat)

```batch
@echo off
REM Set environment variables for Job Portal Backend
set MAIL_USERNAME=your_email@gmail.com
set MAIL_PASSWORD=xxxx xxxx xxxx xxxx
set MAIL_HOST=smtp.gmail.com
set MAIL_PORT=587
set JWT_SECRET=your_jwt_secret_key_here_minimum_32_characters_long_string
set JWT_EXPIRATION_MS=86400000
set ALLOWED_ORIGINS=http://localhost:3000
set RAPIDAPI_KEY=your_rapidapi_key_here
set OPENAI_API_KEY=sk-your_openai_key_here
set AFFINDA_API_TOKEN=your_affinda_token_here
set ENVIRONMENT=development

cd backend
..\apache-maven-3.9.12\bin\mvn.cmd spring-boot:run
pause
```

### Linux/Mac Bash Script (run-backend.sh)

```bash
#!/bin/bash

# Set environment variables for Job Portal Backend
export MAIL_USERNAME="your_email@gmail.com"
export MAIL_PASSWORD="xxxx xxxx xxxx xxxx"
export MAIL_HOST="smtp.gmail.com"
export MAIL_PORT="587"
export JWT_SECRET="your_jwt_secret_key_here_minimum_32_characters_long_string"
export JWT_EXPIRATION_MS="86400000"
export ALLOWED_ORIGINS="http://localhost:3000"
export RAPIDAPI_KEY="your_rapidapi_key_here"
export OPENAI_API_KEY="sk-your_openai_key_here"
export AFFINDA_API_TOKEN="your_affinda_token_here"
export ENVIRONMENT="development"

# Verify Java installation
java -version

# Navigate to backend and run
cd backend
../apache-maven-3.9.12/bin/mvn spring-boot:run
```

---

## APPENDIX C: cURL Command Examples

### 1. OTP Login

```bash
curl -X POST http://localhost:8080/api/auth/otp-login \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com"}'
```

### 2. Verify OTP

```bash
curl -X POST http://localhost:8080/api/auth/verify-otp \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "otp": "123456"}'
```

### 3. Search Jobs (with auth token)

```bash
curl -X GET "http://localhost:8080/api/jobs/search?query=software%20engineer&location=New%20York&page=1&numPages=1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Search Jobs (without auth for testing)

```bash
curl -X GET "http://localhost:8080/api/jobs/search?query=python%20developer&location=remote"
```

### 5. Get Salary Estimate

```bash
curl -X GET "http://localhost:8080/api/jobs/salary-estimate?jobTitle=Senior%20Software%20Engineer&location=San%20Francisco" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 6. Send Chat Message

```bash
curl -X POST http://localhost:8080/api/chatbot \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"message": "How do I improve my resume?"}'
```

### 7. Get Chatbot Suggestions

```bash
curl -X GET http://localhost:8080/api/chatbot/suggestions \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 8. Analyze Resume (multipart upload)

```bash
curl -X POST http://localhost:8080/api/resume/analyze \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/path/to/resume.pdf"
```

### 9. Skill Gap Analysis

```bash
curl -X POST http://localhost:8080/api/skills/gap-analysis \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "currentSkills": ["Java", "Spring Boot", "SQL"],
    "targetRole": "Senior Software Engineer",
    "targetLocation": "San Francisco"
  }'
```

### 10. Get User Location

```bash
curl -X GET http://localhost:8080/api/geo/location \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 11. Get User Analytics

```bash
curl -X GET http://localhost:8080/api/analytics/user/user_123 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 12. Get Job Market Analytics

```bash
curl -X GET http://localhost:8080/api/analytics/job-market \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 13. Health Check

```bash
curl -X GET http://localhost:8080/api/health
```

### 14. Get H2 Database Console

```bash
# Open in browser:
http://localhost:8080/h2-console

# JDBC URL: jdbc:h2:mem:testdb
# Username: sa
# Password: (leave blank)
```

---

## APPENDIX D: React Component Implementation Examples

### OTP Login Component

```tsx
// frontend/src/OTPLogin.tsx
import React, { useState } from "react";
import axios from "axios";

interface OTPLoginProps {
  onLoginSuccess: (email: string) => void;
}

const OTPLogin: React.FC<OTPLoginProps> = ({ onLoginSuccess }) => {
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [step, setStep] = useState<"email" | "otp">("email");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const handleSendOTP = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await axios.post(
        "http://localhost:8080/api/auth/otp-login",
        {
          email: email,
        },
      );

      setMessage("OTP sent successfully! Check your email.");
      setStep("otp");
    } catch (err: any) {
      setError(err.response?.data?.error || "Failed to send OTP");
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyOTP = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await axios.post(
        "http://localhost:8080/api/auth/verify-otp",
        {
          email: email,
          otp: otp,
        },
      );

      if (response.data.success) {
        // Store JWT token
        localStorage.setItem("jwtToken", response.data.token);
        onLoginSuccess(email);
      }
    } catch (err: any) {
      setError(err.response?.data?.error || "Invalid OTP");
    } finally {
      setLoading(false);
    }
  };

  if (step === "email") {
    return (
      <div className="otp-login-form">
        <h2>OTP Login</h2>
        <input
          type="email"
          placeholder="Enter your email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          disabled={loading}
        />
        <button onClick={handleSendOTP} disabled={loading || !email}>
          {loading ? "Sending..." : "Send OTP"}
        </button>
        {error && <p className="error">{error}</p>}
        {message && <p className="success">{message}</p>}
      </div>
    );
  }

  return (
    <div className="otp-login-form">
      <h2>Verify OTP</h2>
      <p>Enter the 6-digit OTP sent to {email}</p>
      <input
        type="text"
        placeholder="Enter OTP"
        value={otp}
        onChange={(e) => setOtp(e.target.value.slice(0, 6))}
        maxLength={6}
        disabled={loading}
      />
      <button onClick={handleVerifyOTP} disabled={loading || otp.length !== 6}>
        {loading ? "Verifying..." : "Verify OTP"}
      </button>
      <button onClick={() => setStep("email")} disabled={loading}>
        Back
      </button>
      {error && <p className="error">{error}</p>}
    </div>
  );
};

export default OTPLogin;
```

### Job Search Component

```tsx
// frontend/src/JobSearch.tsx
import React, { useState, useEffect } from "react";
import axios from "axios";

interface Job {
  id: string;
  title: string;
  company: string;
  location: string;
  salary: string;
  jobDescription: string;
}

const JobSearch: React.FC = () => {
  const [query, setQuery] = useState("software engineer");
  const [location, setLocation] = useState("");
  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const searchJobs = async () => {
    try {
      setLoading(true);
      setError("");

      const token = localStorage.getItem("jwtToken");
      const response = await axios.get(
        "http://localhost:8080/api/jobs/search",
        {
          params: {
            query: query,
            location: location,
            page: 1,
            numPages: 1,
          },
          headers: token ? { Authorization: `Bearer ${token}` } : {},
        },
      );

      setJobs(response.data.jobs || []);
    } catch (err: any) {
      setError(err.response?.data?.error || "Failed to search jobs");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    searchJobs();
  }, []);

  return (
    <div className="job-search-container">
      <div className="search-form">
        <input
          type="text"
          placeholder="Job title (e.g., Software Engineer)"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
        <input
          type="text"
          placeholder="Location (optional)"
          value={location}
          onChange={(e) => setLocation(e.target.value)}
        />
        <button onClick={searchJobs} disabled={loading}>
          {loading ? "Searching..." : "Search"}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="jobs-list">
        {jobs.length === 0 ? (
          <p>No jobs found</p>
        ) : (
          jobs.map((job) => (
            <div key={job.id} className="job-card">
              <h3>{job.title}</h3>
              <p className="company">{job.company}</p>
              <p className="location">{job.location}</p>
              <p className="salary">{job.salary}</p>
              <p className="description">{job.jobDescription}</p>
              <button>View Details</button>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default JobSearch;
```

### Resume Score Component

```tsx
// frontend/src/ResumeScore.tsx
import React, { useState } from "react";
import axios from "axios";

interface ResumeScore {
  score: number;
  feedback: Array<{
    section: string;
    score: number;
    feedback: string;
  }>;
  suggestions: string[];
  recommendedSkills: string[];
}

const ResumeScore: React.FC = () => {
  const [file, setFile] = useState<File | null>(null);
  const [score, setScore] = useState<ResumeScore | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleAnalyzeResume = async () => {
    if (!file) {
      setError("Please select a resume file");
      return;
    }

    try {
      setLoading(true);
      setError("");

      const formData = new FormData();
      formData.append("file", file);

      const token = localStorage.getItem("jwtToken");
      const response = await axios.post(
        "http://localhost:8080/api/resume/analyze",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${token}`,
          },
        },
      );

      setScore(response.data);
    } catch (err: any) {
      setError(err.response?.data?.error || "Failed to analyze resume");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="resume-score-container">
      <h2>Resume Quality Score</h2>

      <div className="upload-section">
        <input
          type="file"
          accept=".pdf,.doc,.docx"
          onChange={(e) => setFile(e.target.files?.[0] || null)}
        />
        <button onClick={handleAnalyzeResume} disabled={loading || !file}>
          {loading ? "Analyzing..." : "Analyze Resume"}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      {score && (
        <div className="score-results">
          <div className="overall-score">
            <h3>Overall Score: {score.score}/100</h3>
            <div className="progress-bar">
              <div style={{ width: `${score.score}%` }}></div>
            </div>
          </div>

          <div className="feedback-sections">
            {score.feedback.map((item) => (
              <div key={item.section} className="feedback-item">
                <h4>
                  {item.section} - {item.score}/100
                </h4>
                <p>{item.feedback}</p>
              </div>
            ))}
          </div>

          <div className="suggestions">
            <h4>Suggestions for Improvement:</h4>
            <ul>
              {score.suggestions.map((suggestion, idx) => (
                <li key={idx}>{suggestion}</li>
              ))}
            </ul>
          </div>

          <div className="recommended-skills">
            <h4>Recommended Skills to Add:</h4>
            <div className="skill-tags">
              {score.recommendedSkills.map((skill) => (
                <span key={skill} className="skill-tag">
                  {skill}
                </span>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ResumeScore;
```

---

## APPENDIX E: Backend Service Implementation Examples

### OTP Service

```java
// backend/src/main/java/com/jobportal/backend/service/OTPService.java
package com.jobportal.backend.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OTPService {

    private static final int OTP_LENGTH = 6;
    private static final long OTP_VALIDITY_MINUTES = 5;
    private Map<String, OTPData> otpStore = new HashMap<>();

    public String generateOTP(String email) {
        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Store OTP with expiry
        OTPData otpData = new OTPData(otp, System.currentTimeMillis() + (OTP_VALIDITY_MINUTES * 60 * 1000));
        otpStore.put(email, otpData);

        System.out.println("✅ OTP Generated for " + email + ": " + otp);
        return otp;
    }

    public boolean verifyOTP(String email, String otp) {
        OTPData otpData = otpStore.get(email);

        if (otpData == null) {
            return false;
        }

        if (System.currentTimeMillis() > otpData.expiryTime) {
            otpStore.remove(email);
            return false;
        }

        if (otpData.otp.equals(otp)) {
            otpStore.remove(email);
            return true;
        }

        return false;
    }

    public long getRemainingTime(String email) {
        OTPData otpData = otpStore.get(email);
        if (otpData == null) return -1;

        long remaining = otpData.expiryTime - System.currentTimeMillis();
        return remaining > 0 ? remaining : -1;
    }

    private static class OTPData {
        String otp;
        long expiryTime;

        OTPData(String otp, long expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
    }
}
```

### Email Service

```java
// backend/src/main/java/com/jobportal/backend/service/EmailService.java
package com.jobportal.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendOtpEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your Job Portal OTP - Valid for 5 minutes");
            message.setText("Your OTP is: " + otp + "\n\n" +
                "This OTP is valid for 5 minutes.\n" +
                "Please do not share this OTP with anyone.\n\n" +
                "If you didn't request this, please ignore this email.\n\n" +
                "Regards,\n" +
                "Job Portal Team");

            mailSender.send(message);
            logger.info("✅ OTP email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("❌ Failed to send OTP email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendWelcomeEmail(String to, String userName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Welcome to Job Portal!");
            message.setText("Dear " + userName + ",\n\n" +
                "Welcome to Job Portal - Your AI-Powered Career Platform!\n\n" +
                "Features you can enjoy:\n" +
                "• AI Skill Gap Analysis\n" +
                "• Resume Quality Scoring\n" +
                "• Intelligent Job Matching\n" +
                "• Market Analytics\n" +
                "• Career Chatbot\n\n" +
                "Get started now and find your perfect job!\n\n" +
                "Regards,\n" +
                "Job Portal Team");

            mailSender.send(message);
            logger.info("✅ Welcome email sent to: {}", to);
        } catch (Exception e) {
            logger.error("❌ Failed to send welcome email to: {}", to, e);
        }
    }
}
```

### Job Search Service

```java
// backend/src/main/java/com/jobportal/backend/service/JobSearchService.java
package com.jobportal.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.jobportal.backend.config.ApiConfig;
import java.util.*;

@Service
public class JobSearchService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private ApiConfig apiConfig;

    @Cacheable(value = "jobSearchCache", key = "#query + '-' + #location")
    public Map<String, Object> searchJobs(String query, String location, int page, int numPages) {
        Map<String, Object> result = new HashMap<>();

        try {
            if (!apiConfig.isRapidApiConfigured()) {
                // Return mock data
                return getMockJobData(query, location);
            }

            // Call RapidAPI JSearch
            String url = "https://jsearch.p.rapidapi.com/search?query=" + query +
                        "&location=" + location +
                        "&page=" + page +
                        "&num_pages=" + numPages;

            Map<String, Object> apiResponse = webClient.get()
                .uri(url)
                .header("X-RapidAPI-Key", apiConfig.getRapidApiKey())
                .header("X-RapidAPI-Host", apiConfig.getRapidApiHost())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

            return apiResponse;
        } catch (Exception e) {
            return getMockJobData(query, location);
        }
    }

    private Map<String, Object> getMockJobData(String query, String location) {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> jobs = new ArrayList<>();

        Map<String, Object> job = new HashMap<>();
        job.put("id", "job_1");
        job.put("title", query);
        job.put("company", "Tech Company");
        job.put("location", location.isEmpty() ? "Remote" : location);
        job.put("salary", "$80,000 - $120,000");
        job.put("jobDescription", "Looking for a talented " + query);

        jobs.add(job);
        data.put("jobs", jobs);
        data.put("success", true);
        data.put("message", "Mock data - API not configured");

        return data;
    }

    public Map<String, Object> getJobDetails(String jobId) {
        Map<String, Object> result = new HashMap<>();
        result.put("jobId", jobId);
        result.put("title", "Senior Software Engineer");
        result.put("company", "Tech Corp");
        result.put("location", "San Francisco, CA");
        result.put("salary", "$120,000 - $160,000");
        result.put("fullDescription", "We are looking for...");
        result.put("requirements", Arrays.asList("Java", "Spring Boot", "PostgreSQL"));
        return result;
    }

    public Map<String, Object> getSalaryEstimate(String jobTitle, String location) {
        Map<String, Object> result = new HashMap<>();
        result.put("jobTitle", jobTitle);
        result.put("location", location);
        result.put("avgSalary", 145000);
        result.put("minSalary", 100000);
        result.put("maxSalary", 200000);
        result.put("currency", "USD");
        return result;
    }
}
```

### Chatbot Service

```java
// backend/src/main/java/com/jobportal/backend/service/ChatbotService.java
package com.jobportal.backend.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ChatbotService {

    private Map<String, String> knowledgeBase = new HashMap<>();

    public ChatbotService() {
        initializeKnowledgeBase();
    }

    public Map<String, Object> processMessage(String message) {
        Map<String, Object> result = new HashMap<>();
        String response = generateResponse(message);

        result.put("response", response);
        result.put("source", "rule-based");
        result.put("timestamp", System.currentTimeMillis());

        return result;
    }

    private String generateResponse(String message) {
        String lowerMessage = message.toLowerCase();

        if (lowerMessage.contains("interview")) {
            return "Here are key tips for interview preparation:\n" +
                "1. Research the company thoroughly\n" +
                "2. Practice common interview questions\n" +
                "3. Prepare examples of your achievements\n" +
                "4. Mock interviews are very helpful\n" +
                "5. Dress professionally and arrive early";
        }

        if (lowerMessage.contains("resume")) {
            return "Tips to improve your resume:\n" +
                "1. Keep it to 1-2 pages\n" +
                "2. Use action verbs (designed, developed, managed)\n" +
                "3. Quantify your achievements\n" +
                "4. Tailor it for each job\n" +
                "5. Proofread carefully";
        }

        if (lowerMessage.contains("skill")) {
            return "Trending skills in tech industry:\n" +
                "1. Cloud Computing (AWS, Azure)\n" +
                "2. Machine Learning & AI\n" +
                "3. DevOps & Kubernetes\n" +
                "4. Data Engineering\n" +
                "5. Cybersecurity";
        }

        if (lowerMessage.contains("salary")) {
            return "Salary negotiation tips:\n" +
                "1. Research market rates\n" +
                "2. Know your worth\n" +
                "3. Don't disclose first\n" +
                "4. Negotiate professionally\n" +
                "5. Consider total compensation";
        }

        if (lowerMessage.contains("job") || lowerMessage.contains("search")) {
            return "Job search strategies:\n" +
                "1. Use multiple job boards\n" +
                "2. Network actively\n" +
                "3. Customize applications\n" +
                "4. Track your applications\n" +
                "5. Follow up appropriately";
        }

        return "I'm here to help with career advice! You can ask me about:\n" +
            "• Interview preparation\n" +
            "• Resume tips\n" +
            "• Skill development\n" +
            "• Salary negotiation\n" +
            "• Job search strategies";
    }
}
```

---

## APPENDIX F: Complete pom.xml Dependencies

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.jobportal</groupId>
    <artifactId>jobportal-backend</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>JobPortalBackend</name>
    <description>Job Portal Backend with AI Integration</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>

    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <!-- Spring Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- Spring Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Spring Mail -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>

        <!-- Spring Cache -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>

        <!-- Spring WebFlux for async HTTP calls -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>

        <!-- H2 Database -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Jackson for JSON -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.11.5</version>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- DevTools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## APPENDIX G: Error Handling Patterns

### Global Exception Handler

```java
// backend/src/main/java/com/jobportal/backend/exception/GlobalExceptionHandler.java
package com.jobportal.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid input");
        response.put("message", e.getMessage());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Internal server error");
        response.put("message", e.getMessage());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Unexpected error");
        response.put("message", e.getMessage());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
```

### Try-Catch Pattern for API Calls

```java
// Common pattern in services
try {
    // API Call or Database Operation
    Map<String, Object> result = externalApiCall();
    return result;
} catch (HttpClientErrorException e) {
    logger.error("API Error: {}", e.getMessage());
    return getFallbackData();
} catch (TimeoutException e) {
    logger.error("API Timeout: {}", e.getMessage());
    return getCachedData();
} catch (Exception e) {
    logger.error("Unexpected error: {}", e.getMessage());
    return getDefaultResponse();
}
```

---

## APPENDIX H: Testing Guide

### Unit Test Example

```java
// backend/src/test/java/com/jobportal/backend/service/OTPServiceTest.java
package com.jobportal.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class OTPServiceTest {

    private OTPService otpService;

    @BeforeEach
    public void setUp() {
        otpService = new OTPService();
    }

    @Test
    public void testOTPGeneration() {
        String email = "test@example.com";
        String otp = otpService.generateOTP(email);

        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"));
    }

    @Test
    public void testOTPVerification() {
        String email = "test@example.com";
        String otp = otpService.generateOTP(email);

        boolean isValid = otpService.verifyOTP(email, otp);
        assertTrue(isValid);
    }

    @Test
    public void testInvalidOTP() {
        String email = "test@example.com";
        otpService.generateOTP(email);

        boolean isValid = otpService.verifyOTP(email, "000000");
        assertFalse(isValid);
    }
}
```

### Integration Test Example

```java
// backend/src/test/java/com/jobportal/backend/controller/AuthControllerTest.java
package com.jobportal.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testOTPLogin() throws Exception {
        String requestBody = "{\"email\": \"test@example.com\"}";

        mockMvc.perform(post("/api/auth/otp-login")
            .contentType("application/json")
            .content(requestBody))
            .andExpect(status().isOk());
    }

    @Test
    public void testInvalidEmail() throws Exception {
        String requestBody = "{\"email\": \"invalid-email\"}";

        mockMvc.perform(post("/api/auth/otp-login")
            .contentType("application/json")
            .content(requestBody))
            .andExpect(status().isBadRequest());
    }
}
```

### Frontend Test Example

```tsx
// frontend/src/__tests__/OTPLogin.test.tsx
import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import axios from "axios";
import OTPLogin from "../OTPLogin";

jest.mock("axios");

describe("OTPLogin Component", () => {
  const mockOnLoginSuccess = jest.fn();

  it("renders email input field", () => {
    render(<OTPLogin onLoginSuccess={mockOnLoginSuccess} />);
    const emailInput = screen.getByPlaceholderText(/Enter your email/i);
    expect(emailInput).toBeInTheDocument();
  });

  it("sends OTP when email is provided", async () => {
    (axios.post as jest.Mock).mockResolvedValueOnce({
      data: { otpSent: true },
    });

    render(<OTPLogin onLoginSuccess={mockOnLoginSuccess} />);

    const emailInput = screen.getByPlaceholderText(/Enter your email/i);
    fireEvent.change(emailInput, { target: { value: "test@example.com" } });

    const sendButton = screen.getByText(/Send OTP/i);
    fireEvent.click(sendButton);

    await waitFor(() => {
      expect(axios.post).toHaveBeenCalledWith(
        "http://localhost:8080/api/auth/otp-login",
        { email: "test@example.com" },
      );
    });
  });
});
```

---

## APPENDIX I: Deployment & Production Checklist

### Pre-Deployment Checklist

```markdown
## Backend Deployment Checklist

### 1. Code Quality

- [ ] All console.log/System.out.println statements removed (except critical logs)
- [ ] Code follows consistent naming conventions
- [ ] No hardcoded secrets or credentials in code
- [ ] All dependencies are secure and up-to-date
- [ ] Code has been reviewed and tested

### 2. Security

- [ ] JWT_SECRET is strong (min 32 characters) and unique
- [ ] MAIL_PASSWORD is using App Password, not Gmail password
- [ ] CORS is configured for production domain only
- [ ] HTTPS is enabled (enforced redirect)
- [ ] Database credentials are secured
- [ ] API keys are stored securely (AWS Secrets Manager, etc.)
- [ ] SQL injection prevention verified
- [ ] CSRF protection enabled

### 3. Performance

- [ ] Database indices are created for frequently queried columns
- [ ] Caching is configured (Redis recommended for production)
- [ ] API response times < 500ms
- [ ] Database connection pooling is configured
- [ ] CDN is set up for static assets
- [ ] Lazy loading for images implemented

### 4. Logging & Monitoring

- [ ] Structured logging implemented (JSON format)
- [ ] Logging levels set appropriately (ERROR, WARN, INFO)
- [ ] Log retention policy defined
- [ ] Monitoring/alerting tools integrated (DataDog, New Relic, etc.)
- [ ] Error tracking tool configured (Sentry, etc.)

### 5. Database

- [ ] Database backups are automated
- [ ] Database is migrated to production (PostgreSQL/MySQL)
- [ ] Database credentials are unique and strong
- [ ] Database is not publicly accessible
- [ ] Automated backups verified
- [ ] Point-in-time recovery tested

### 6. Infrastructure

- [ ] Docker image is created and tested
- [ ] Docker image is stored in private registry
- [ ] Kubernetes deployment files are configured
- [ ] Load balancing is set up
- [ ] Auto-scaling policies are configured
- [ ] Health checks are configured

### 7. Testing

- [ ] All unit tests pass (100% success rate)
- [ ] Integration tests pass
- [ ] E2E tests pass
- [ ] Load testing completed (1000+ concurrent users)
- [ ] Security testing completed
- [ ] API rate limiting tested

### 8. Documentation

- [ ] API documentation is complete
- [ ] Deployment documentation is written
- [ ] Rollback procedure documented
- [ ] Troubleshooting guide created
- [ ] Security guidelines documented

### 9. Monitoring Post-Deployment

- [ ] Error rate < 0.5%
- [ ] API response time < 200ms (p95)
- [ ] CPU usage < 70%
- [ ] Memory usage < 80%
- [ ] Disk usage < 80%
```

### Docker Configuration

```dockerfile
# Dockerfile
FROM maven:3.8.6-openjdk-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /build/backend/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xmx512m -Xms256m"

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose for Local Development

```yaml
# docker-compose.yml
version: "3.8"

services:
  backend:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - MAIL_USERNAME=${MAIL_USERNAME}
      - MAIL_PASSWORD=${MAIL_PASSWORD}
      - JWT_SECRET=${JWT_SECRET}
      - ALLOWED_ORIGINS=http://localhost:3000
      - RAPIDAPI_KEY=${RAPIDAPI_KEY}
      - OPENAI_API_KEY=${OPENAI_API_KEY}
    depends_on:
      - postgres
    networks:
      - job-portal

  postgres:
    image: postgres:15
    environment:
      - POSTGRES_USER=jobportal
      - POSTGRES_PASSWORD=jobportal
      - POSTGRES_DB=jobportal
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - job-portal

  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "3000:3000"
    environment:
      - REACT_APP_API_URL=http://localhost:8080
    depends_on:
      - backend
    networks:
      - job-portal

volumes:
  postgres_data:

networks:
  job-portal:
    driver: bridge
```

---

## APPENDIX J: Performance Optimization Tips

### 1. Backend Optimization

```java
// Enable caching for frequently accessed data
@Cacheable(value = "jobCache", key = "#jobId", unless = "#result == null")
public Job getJob(String jobId) {
    return jobRepository.findById(jobId).orElse(null);
}

// Use async/await for non-blocking operations
@Async
public void sendOtpEmailAsync(String email, String otp) {
    emailService.sendOtpEmail(email, otp);
}

// Use pagination for large result sets
@GetMapping("/jobs")
public Page<Job> getJobs(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size) {
    return jobRepository.findAll(PageRequest.of(page, size));
}

// Optimize database queries with select projections
@Query("SELECT new map(j.id as id, j.title as title, j.company as company) FROM Job j")
List<Map<String, Object>> getJobSummaries();
```

### 2. Frontend Optimization

```tsx
// Use React.memo to prevent unnecessary re-renders
const JobCard = React.memo(({ job }: { job: Job }) => {
  return <div>{job.title}</div>;
});

// Lazy load components
const ResumeScore = lazy(() => import("./ResumeScore"));

// Optimize API calls with debouncing
const debouncedSearch = debounce((query: string) => {
  searchJobs(query);
}, 500);

// Use useMemo to cache expensive calculations
const sortedJobs = useMemo(() => {
  return jobs.sort((a, b) => b.salary - a.salary);
}, [jobs]);
```

### 3. Database Optimization

```sql
-- Create indices for frequently searched columns
CREATE INDEX idx_job_title ON jobs(title);
CREATE INDEX idx_job_location ON jobs(location);
CREATE INDEX idx_user_email ON users(email);

-- Use connection pooling
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

-- Enable query optimization
spring.jpa.properties.hibernate.generate_statistics=true
spring.jpa.properties.hibernate.use_sql_comments=true
```

---

## APPENDIX K: Troubleshooting Common Issues

### Issue: "Could not resolve placeholder 'MAIL_USERNAME'"

**Cause**: Environment variables not set before running backend

**Solution**:

```powershell
# Set all required environment variables FIRST
$env:MAIL_USERNAME = "your_email@gmail.com"
$env:MAIL_PASSWORD = "xxxx xxxx xxxx xxxx"
$env:JWT_SECRET = "minimum_32_characters_long"

# THEN run backend
cd backend
..\apache-maven-3.9.12\bin\mvn.cmd spring-boot:run
```

### Issue: "No compiler is provided in this environment"

**Cause**: JRE installed instead of JDK

**Solution**:

```powershell
# Check Java version
java -version

# Download and install JDK 17 from: https://adoptium.net/
# Set JAVA_HOME environment variable
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot"

# Verify
java -version  # Should show 17.x.x
```

### Issue: OTP Not Sending

**Cause**: Gmail App Password not used, or 2FA not enabled

**Solution**:

1. Enable 2-Factor Authentication: https://myaccount.google.com/security
2. Generate App Password: https://myaccount.google.com/apppasswords
3. Use the 16-character App Password (with spaces)
4. Verify in logs: "✅ OTP email sent successfully to:"

### Issue: Port 8080 Already in Use

**Cause**: Another process using port 8080

**Solution**:

```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /PID {PID} /F

# Or change port in application.properties
server.port=8090
```

### Issue: CORS Error in Frontend

**Cause**: Backend CORS not configured for frontend URL

**Solution**:

```java
// In CorsConfig.java or SecurityConfig
@CrossOrigin(origins = "http://localhost:3000")

// Or environment variable
$env:ALLOWED_ORIGINS = "http://localhost:3000"
```

---

## APPENDIX L: API Rate Limiting & Usage

### Rate Limiting Configuration

```yaml
# application.properties
# Configure rate limiting for public APIs
spring.mvc.throw-exception-if-no-handler-found=true
spring.web.resources.add-mappings=false
# For RapidAPI JSearch: 5,000 requests/month (free tier)
# For OpenAI: Depends on plan
# For Affinda: 100 resumes/month (free tier)
# For ipapi.co: 1,000 requests/day (free tier)
```

### Implementing Custom Rate Limiting

```java
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, RateLimitData> rateLimits = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 100;
    private static final int WINDOW_TIME = 3600; // 1 hour in seconds

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = getClientIp(request);
        RateLimitData data = rateLimits.getOrDefault(clientIp, new RateLimitData());

        if (data.isLimited()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }

        data.increment();
        rateLimits.put(clientIp, data);
        return true;
    }
}
```

---

## APPENDIX M: Security Best Practices

### Input Validation

```java
@PostMapping("/api/jobs/search")
public ResponseEntity<?> searchJobs(
    @Valid @RequestParam String query,
    @Valid @RequestParam(required = false) String location) {

    // Query validation
    if (query == null || query.trim().isEmpty()) {
        return ResponseEntity.badRequest().body("Query cannot be empty");
    }

    if (query.length() > 100) {
        return ResponseEntity.badRequest().body("Query too long");
    }

    // Sanitize input
    String sanitizedQuery = query.replaceAll("[<>\"']", "");

    return ResponseEntity.ok(searchService.search(sanitizedQuery, location));
}
```

### JWT Token Validation

```java
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}
```

---

## APPENDIX N: Frequently Asked Questions

### Q: Can the app work without API keys?

**A**: Yes! All features work perfectly with mock data. API keys are optional:

- ✅ Resume scoring (regex-based fallback)
- ✅ Skill gap analysis (market data included)
- ✅ Job search (mock data included)
- ✅ Chatbot (rule-based fallback)

### Q: How to get API keys?

**A**:

- **RapidAPI**: https://rapidapi.com/ (5,000 free searches/month)
- **OpenAI**: https://platform.openai.com (free credits for new accounts)
- **Affinda**: https://www.affinda.com (100 free resumes/month)

### Q: How to deploy to production?

**A**: Use Docker + Kubernetes:

```bash
docker build -t jobportal:latest .
docker push your-registry/jobportal:latest
kubectl apply -f deployment.yaml
```

### Q: How to monitor the application?

**A**: Configure logging and monitoring:

```properties
logging.level.com.jobportal.backend=DEBUG
management.endpoints.web.exposure.include=health,metrics
```

### Q: How to scale the application?

**A**:

1. Use load balancer (Nginx, HAProxy)
2. Implement database connection pooling
3. Enable caching (Redis)
4. Use CDN for static content
5. Implement auto-scaling in Kubernetes

---

_Last Updated: January 2026_
