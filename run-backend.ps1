# Job Portal Backend Startup Script
# This script sets required environment variables and starts the backend
# IMPORTANT: Configure the values below before running!

# ============================================
# REQUIRED: Email Configuration (for OTP)
# ============================================
# Get App Password from: https://myaccount.google.com/apppasswords
# MUST be 16-character app password with spaces, NOT your Gmail password!
$MAIL_USERNAME = "your_email@gmail.com"
$MAIL_PASSWORD = "xxxx xxxx xxxx xxxx"  # 16-char app password with spaces

# ============================================
# REQUIRED: JWT Secret (for authentication)
# ============================================
# Must be at least 32 characters long! Change this to something secure!
$JWT_SECRET = "your_jwt_secret_key_here_minimum_32_chars_long_string_123456"

# ============================================
# OPTIONAL: API Keys (app works without these)
# ============================================
$RAPIDAPI_KEY = ""  # From: https://rapidapi.com/
$OPENAI_API_KEY = ""  # From: https://platform.openai.com/
$AFFINDA_API_TOKEN = ""  # From: https://www.affinda.com/

# ============================================
# OPTIONAL: CORS Configuration
# ============================================
$ALLOWED_ORIGINS = "http://localhost:3000"
$ENVIRONMENT = "development"
$JWT_EXPIRATION_MS = "86400000"  # 24 hours in milliseconds

# ============================================
# OPTIONAL: Database Configuration
# ============================================
$MONGODB_URI = "mongodb://localhost:27017/jobportal"
$MONGODB_DATABASE = "jobportal"
$MAIL_HOST = "smtp.gmail.com"
$MAIL_PORT = "587"

# ============================================
# Display Configuration Status
# ============================================
Write-Host "🚀 Starting Job Portal Backend..." -ForegroundColor Green
Write-Host ""
Write-Host "Configuration Status:" -ForegroundColor Cyan
Write-Host "  MAIL_USERNAME: $MAIL_USERNAME" -ForegroundColor $(if ($MAIL_USERNAME -eq "your_email@gmail.com") { "Yellow" } else { "Green" })
Write-Host "  JWT_SECRET: $(if ($JWT_SECRET.Length -ge 32) { '✓ Valid length' } else { '✗ Too short!' })" -ForegroundColor $(if ($JWT_SECRET.Length -ge 32) { "Green" } else { "Red" })
Write-Host "  RAPIDAPI_KEY: $(if ([string]::IsNullOrEmpty($RAPIDAPI_KEY)) { '(optional)' } else { '✓ Configured' })" -ForegroundColor Cyan
Write-Host "  OPENAI_API_KEY: $(if ([string]::IsNullOrEmpty($OPENAI_API_KEY)) { '(optional)' } else { '✓ Configured' })" -ForegroundColor Cyan
Write-Host ""

# ============================================
# Validation
# ============================================
$hasErrors = $false

if ($MAIL_USERNAME -eq "your_email@gmail.com") {
    Write-Host "⚠️  WARNING: MAIL_USERNAME is not configured!" -ForegroundColor Yellow
    Write-Host "    Update the script with your email address." -ForegroundColor Yellow
    $hasErrors = $true
}

if ($MAIL_PASSWORD -eq "xxxx xxxx xxxx xxxx") {
    Write-Host "⚠️  WARNING: MAIL_PASSWORD is not configured!" -ForegroundColor Yellow
    Write-Host "    Set your 16-character Gmail App Password." -ForegroundColor Yellow
    $hasErrors = $true
}

if ($JWT_SECRET.Length -lt 32) {
    Write-Host "❌ ERROR: JWT_SECRET must be at least 32 characters!" -ForegroundColor Red
    $hasErrors = $true
}

if ($hasErrors) {
    Write-Host ""
    Write-Host "Please configure the required values above and try again." -ForegroundColor Red
    Write-Host "Press Enter to exit..." -ForegroundColor Yellow
    Read-Host
    exit 1
}

# ============================================
# Set Environment Variables & Run Backend
# ============================================
$env:MAIL_USERNAME = $MAIL_USERNAME
$env:MAIL_PASSWORD = $MAIL_PASSWORD
$env:JWT_SECRET = $JWT_SECRET
$env:RAPIDAPI_KEY = $RAPIDAPI_KEY
$env:OPENAI_API_KEY = $OPENAI_API_KEY
$env:AFFINDA_API_TOKEN = $AFFINDA_API_TOKEN
$env:ALLOWED_ORIGINS = $ALLOWED_ORIGINS
$env:ENVIRONMENT = $ENVIRONMENT
$env:JWT_EXPIRATION_MS = $JWT_EXPIRATION_MS
$env:MONGODB_URI = $MONGODB_URI
$env:MONGODB_DATABASE = $MONGODB_DATABASE
$env:MAIL_HOST = $MAIL_HOST
$env:MAIL_PORT = $MAIL_PORT

Write-Host "✓ Environment variables configured and exported" -ForegroundColor Green
Write-Host ""

# Navigate to backend folder
cd backend

# Run Maven with properties passed as Java system properties
Write-Host "Running: mvn spring-boot:run with environment configuration..." -ForegroundColor Yellow
Write-Host ""

..\apache-maven-3.9.12\bin\mvn.cmd `
  -Dspring-boot.run.jvmArguments="-Dmail.username=$MAIL_USERNAME -Dmail.password=$MAIL_PASSWORD -Djwt.secret=$JWT_SECRET" `
  spring-boot:run

# Keep the terminal open if there's an error
if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "❌ Backend failed to start with exit code: $LASTEXITCODE" -ForegroundColor Red
    Write-Host ""
    Write-Host "Troubleshooting tips:" -ForegroundColor Yellow
    Write-Host "  1. Check if MAIL_USERNAME and MAIL_PASSWORD are correct" -ForegroundColor White
    Write-Host "  2. Verify JWT_SECRET is at least 32 characters" -ForegroundColor White
    Write-Host "  3. Ensure port 8080 is not already in use" -ForegroundColor White
    Write-Host "  4. Check logs above for specific error messages" -ForegroundColor White
    Write-Host ""
    Write-Host "Press Enter to exit..." -ForegroundColor Yellow
    Read-Host
}
