# API Integration Documentation

## 🔌 External APIs Integrated

### 1. **RapidAPI JSearch** (Job Search)

- **Purpose**: Real-time job listings from multiple sources
- **Base URL**: `https://jsearch.p.rapidapi.com`
- **Free Tier**: 5,000 requests/month
- **Cost**: Free → $9.99/month (Basic)
- **Endpoints Used**:
  - `/search` - Search jobs with filters
  - `/job-details` - Get detailed job information
  - `/estimated-salary` - Salary estimates by role/location

**Setup:**

```bash
1. Visit https://rapidapi.com/
2. Sign up (Google/GitHub/Email)
3. Search for "JSearch by OpenWeb Ninja"
4. Subscribe to "Basic" plan (FREE)
5. Copy API Key from dashboard
6. Add to .env: RAPIDAPI_KEY=your_key
```

**Example Request:**

```bash
curl -X GET "https://jsearch.p.rapidapi.com/search?query=python+developer&location=San+Francisco" \
  -H "X-RapidAPI-Key: YOUR_KEY" \
  -H "X-RapidAPI-Host: jsearch.p.rapidapi.com"
```

**Features:**

- ✅ Multi-source job aggregation
- ✅ Real-time salary data
- ✅ Company information with logos
- ✅ Apply links and job descriptions
- ✅ Location-based filtering

---

### 2. **OpenAI GPT API** (Enhanced Chatbot)

- **Purpose**: AI-powered conversational assistant
- **Base URL**: `https://api.openai.com/v1`
- **Free Tier**: $5 credit for new accounts
- **Cost**: ~$0.002 per 1K tokens (GPT-3.5-turbo)
- **Model Used**: `gpt-3.5-turbo`

**Setup:**

```bash
1. Visit https://platform.openai.com/
2. Sign up and verify email
3. Add payment method (required, even for free tier)
4. Navigate to API Keys
5. Create new secret key (starts with 'sk-')
6. Add to .env: OPENAI_API_KEY=sk-your_key
```

**Example Request:**

```bash
curl https://api.openai.com/v1/chat/completions \
  -H "Authorization: Bearer YOUR_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gpt-3.5-turbo",
    "messages": [
      {"role": "system", "content": "You are a helpful job portal assistant."},
      {"role": "user", "content": "How do I improve my resume?"}
    ],
    "max_tokens": 200
  }'
```

**Features:**

- ✅ Context-aware responses
- ✅ Natural language understanding
- ✅ Personalized job advice
- ✅ Interview preparation tips
- ✅ Career guidance

**Fallback:** If not configured, uses built-in rule-based chatbot

---

### 3. **Affinda Resume Parser** (Resume Analysis)

- **Purpose**: Extract structured data from resumes
- **Base URL**: `https://api.affinda.com/v3`
- **Free Tier**: 100 resumes/month
- **Cost**: Free → $49/month (Starter)

**Setup:**

```bash
1. Visit https://www.affinda.com/resume-parser
2. Sign up for free account
3. Go to Dashboard → API Keys
4. Copy API token
5. Add to .env: AFFINDA_API_TOKEN=your_token
```

**Features:**

- ✅ Parse PDF/DOCX resumes
- ✅ Extract skills, experience, education
- ✅ Contact information extraction
- ✅ Work history analysis
- ✅ Skill categorization

**Fallback:** If not configured, uses built-in regex-based parser

---

### 4. **ipapi.co** (Geolocation)

- **Purpose**: IP-based location detection
- **Base URL**: `https://ipapi.co`
- **Free Tier**: 1,000 requests/day
- **Cost**: FREE → $12/month (Professional)

**Setup:**

```bash
No API key required for free tier!
Just works out of the box.
```

**Example Request:**

```bash
curl https://ipapi.co/json/
```

**Features:**

- ✅ IP to location conversion
- ✅ City, region, country detection
- ✅ Latitude/longitude coordinates
- ✅ Timezone information
- ✅ Currency detection

---

## 📊 API Usage in Application

### Job Search Endpoints

```
GET /api/jobs/search?query=software+engineer&location=San+Francisco
GET /api/jobs/details/{jobId}
GET /api/jobs/salary-estimate?jobTitle=Software+Engineer&location=SF
GET /api/jobs/trending?category=technology
```

### AI Chatbot Endpoints

```
POST /api/chatbot/ai
Body: { "message": "How do I prepare for interviews?" }
```

### Geolocation Endpoints

```
GET /api/geo/location?ip=8.8.8.8
POST /api/geo/geocode
Body: { "address": "1600 Amphitheatre Parkway, Mountain View, CA" }
```

---

## 🔒 Security Best Practices

### API Key Storage

```bash
# ✅ Good
RAPIDAPI_KEY=abc123  # In .env file (not committed to Git)

# ❌ Bad
rapidapi.key=abc123  # Hardcoded in application.properties
```

### gitignore Configuration

```gitignore
# Environment files with API keys
.env
.env.local
.env.production

# Spring Boot properties with secrets
application-secrets.properties
```

### Environment-Specific Keys

```bash
# Development
RAPIDAPI_KEY=dev_key_here

# Staging
RAPIDAPI_KEY=staging_key_here

# Production
RAPIDAPI_KEY=prod_key_here
```

---

## 💰 Cost Analysis

### Monthly Cost Estimates

**Scenario 1: Small Project (Free Tier)**

- RapidAPI: 5,000 job searches → **$0**
- OpenAI: 100K tokens (~50 conversations) → **$0.20**
- Affinda: 100 resumes → **$0**
- ipapi.co: 30K requests → **$0**
- **Total: ~$0.20/month**

**Scenario 2: Medium Traffic**

- RapidAPI: 50,000 searches → **$9.99/month (Basic)**
- OpenAI: 1M tokens (~500 conversations) → **$2.00**
- Affinda: 500 resumes → **$49/month (Starter)**
- ipapi.co: 30K requests → **$0**
- **Total: ~$60.99/month**

**Scenario 3: High Traffic**

- RapidAPI: 500,000 searches → **$99.99/month (Pro)**
- OpenAI: 10M tokens (~5,000 conversations) → **$20.00**
- Affinda: 2,000 resumes → **$149/month (Professional)**
- ipapi.co: 150K requests → **$12/month (Professional)**
- **Total: ~$280.99/month**

---

## 📈 Performance Optimization

### Caching Strategy

```java
// Job search results cached for 5 minutes
@Cacheable(value = "jobSearchCache", key = "#query + '_' + #location")

// Location data cached for 1 hour
@Cacheable(value = "locationCache", key = "#ipAddress")
```

### Rate Limiting

```java
// Implement request throttling for production
@RateLimiter(name = "externalAPI", fallbackMethod = "fallbackMethod")
```

### Fallback Mechanisms

- **Job Search**: Returns mock data if API fails
- **Chatbot**: Uses rule-based system if OpenAI unavailable
- **Resume Parser**: Uses regex extraction if Affinda fails
- **Geolocation**: Returns default location if ipapi fails

---

## 🧪 Testing APIs

### Test All Endpoints

Open `test_all_features.html` in browser and test:

1. ✅ Job Search with real data
2. ✅ AI Chatbot responses
3. ✅ Resume parsing
4. ✅ Geolocation detection

### Verify API Configuration

```bash
# Check if APIs are configured
curl http://localhost:8080/api/jobs/search?query=developer
# If "source": "Mock Data" → API not configured
# If "source": "RapidAPI JSearch" → API working!
```

---

## 🆘 Troubleshooting

### Issue: "Mock Data" in responses

**Solution:** API key not configured. Add key to `.env` file.

### Issue: "401 Unauthorized"

**Solution:** Invalid API key. Double-check the key in RapidAPI dashboard.

### Issue: "429 Too Many Requests"

**Solution:** Exceeded rate limit. Wait or upgrade plan.

### Issue: OpenAI "Insufficient funds"

**Solution:** Add payment method or use free tier credits.

---

## 📚 Additional Resources

- **RapidAPI Documentation**: https://docs.rapidapi.com/
- **OpenAI API Docs**: https://platform.openai.com/docs
- **Affinda API Docs**: https://docs.affinda.com/
- **ipapi.co Docs**: https://ipapi.co/api/

---

## 🎯 Next Steps

1. ✅ Sign up for RapidAPI (required for job search)
2. ⚪ Sign up for OpenAI (optional - enhanced chatbot)
3. ⚪ Sign up for Affinda (optional - resume parsing)
4. ✅ Configure `.env` file with API keys
5. ✅ Test endpoints using `test_all_features.html`
6. ✅ Monitor API usage in respective dashboards

---

**Note:** All APIs have free tiers sufficient for development and small-scale production. Upgrade as needed based on traffic.
