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

3. **Compile and Run**:

```bash
cd backend
..\apache-maven-3.9.12\bin\mvn.cmd clean compile
..\apache-maven-3.9.12\bin\mvn.cmd spring-boot:run
# Runs on http://localhost:8080
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

### Backend won't start

- Check JAVA_HOME is set to JDK 17
- Verify Maven is properly installed
- Check port 8080 is available

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
