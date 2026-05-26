# 🚀 Quick Start Guide

## Welcome to the Job Portal Application!

This guide will help you get the application up and running in minutes.

## ⚡ Prerequisites Check

Before starting, make sure you have:

- ✅ **Java JDK 17+** installed (`java -version`)
  - **IMPORTANT:** You need JDK (not JRE) to compile the project
  - **Current Status:** If you see Java 1.8 or JRE, you need to upgrade
  - **Download JDK 17:**
    - **Recommended:** Eclipse Temurin (Adoptium) - https://adoptium.net/
    - Alternative: Oracle JDK - https://www.oracle.com/java/technologies/downloads/#java17
  - **After installation:**
    1. Set JAVA_HOME environment variable to JDK path
    2. Add `%JAVA_HOME%\bin` to PATH
    3. Restart PowerShell
    4. Verify: `java -version` should show 17 or higher
- ✅ **Maven 3.6+** - **Already included in project!** (`apache-maven-3.9.12` folder)
  - No separate installation needed
  - The project includes Maven locally
- ✅ **Node.js 16+** installed (`node -version`)
- ✅ **npm** installed (`npm -version`)

### 🔴 Common Issue: Java Installation

If you get error: **"No compiler is provided in this environment"**

- You have JRE instead of JDK
- Solution: Install JDK 17+ from above links

## 🎯 Step-by-Step Setup

### Option 1: Manual Setup (Recommended for first time)

#### Step 1: Start the Backend (Recommended)

Open a terminal/PowerShell window and run:

```powershell
.\run-backend-v2.ps1
```

Wait for the message: **"[OK] Job Portal Backend is ready!"**

The backend is now running on: `http://localhost:8080`

#### Step 2: Start the Frontend

Open a **NEW** terminal/PowerShell window:

```powershell
cd frontend
npm start
```

Your browser will automatically open to: `http://localhost:3000`

### Option 2: Quick Start Script

#### For Windows (PowerShell):

```powershell
# Start backend in background
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd backend; ..\apache-maven-3.9.12\bin\mvn.cmd spring-boot:run"

# Wait 30 seconds for backend to start
Start-Sleep -Seconds 30

# Start frontend
cd frontend
npm start
```

## 🎨 Using the Application

### 1. **Dashboard** (Home)

- View welcome message and feature list
- Navigate to different sections

### 2. **Skill Gap Analysis**

- Enter your skills (e.g., "Java, Python, React")
- Enter job requirements (e.g., "Java, Spring Boot, AWS")
- Click "Analyze Skills" to see missing skills

### 3. **Resume Score**

- Enter your skills
- Add years of experience
- Add education level (0-5)
- Get your resume quality score

### 4. **Chatbot**

- Ask questions about jobs, interviews, or career advice
- Get instant responses

### 5. **Job Match %**

- Compare your skills with job requirements
- See compatibility percentage

### 6. **Notifications**

- View real-time notifications
- Stay updated on platform activities

### 7. **Analytics**

- View platform statistics
- Monitor user and job metrics

### 8. **OTP Login**

- Enter your email
- Receive OTP (use 123456 for demo)
- Verify and login securely

## 🔧 Troubleshooting

### Backend Won't Start

**Problem:** Port 8080 is already in use

**Solution:**

```powershell
# Find and kill process on port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Frontend Can't Connect to Backend

**Problem:** "Failed to connect" errors in UI

**Solutions:**

1. Make sure backend is running (`http://localhost:8080/api/health` should work)
2. Check CORS configuration in backend
3. Clear browser cache
4. Restart both services

### Maven Build Fails

**Problem:** Dependencies won't download

**Solution:**

```powershell
cd backend
..\apache-maven-3.9.12\bin\mvn.cmd clean install -U
```

**Problem:** "No compiler is provided in this environment"

**Solution:**

- Install JDK 17+ (not JRE)
- Set JAVA_HOME to JDK installation directory
- Restart PowerShell and try again

### npm Install Fails

**Problem:** Package installation errors

**Solution:**

```powershell
cd frontend
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

## 📊 Verifying Setup

### Backend Health Check

Open browser to: `http://localhost:8080/api/health`

Expected response: `Backend is running!`

### H2 Database Console

URL: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:jobportaldb`
- Username: `sa`
- Password: (leave empty)

### Frontend Check

Open browser to: `http://localhost:3000`

You should see the Job Portal dashboard.

## 🎓 Key Features to Test

1. **AI Skill Gap Analysis**
   - Test with: "Java, Python" vs "Java, Python, AWS, Docker"
   - Should show: Missing skills (AWS, Docker)

2. **Resume Scoring**
   - Test with: 5 skills, 3 years experience, education level 2
   - Should get a score around 40-60

3. **Job Match**
   - Test with matching skills
   - Should show high percentage for good matches

4. **OTP Login**
   - Use demo OTP: `123456`
   - Should successfully login

## 💡 Tips

- **Keep both terminals open** while using the application
- **Backend must start first** before frontend
- **Check console logs** for any errors
- **Use Chrome DevTools** (F12) to debug frontend issues

## 🔥 Production Deployment

### Backend

```bash
cd backend
..\apache-maven-3.9.12\bin\mvn.cmd clean package
java -jar target/jobportal-backend-0.0.1-SNAPSHOT.jar
```

### Frontend

```bash
cd frontend
npm run build
# Deploy the 'build' folder to your hosting service
```

## 📞 Need Help?

- Check the main README.md for detailed documentation
- Review error messages in terminal/console
- Ensure all prerequisites are properly installed
- Try restarting both services

---

**🎉 Enjoy using the Job Portal Application!**

_For detailed API documentation and architecture details, refer to the main README.md_
