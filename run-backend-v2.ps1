# Job Portal Backend Launcher with .env Support
# This script reads configuration from .env file and starts the backend

param(
    [switch]$Setup,
    [switch]$Test,
    [switch]$Help
)

# Display help
if ($Help) {
    Write-Host @"
+----------------------------------------------------------------+
|        Job Portal Backend Launcher - Help                      |
+----------------------------------------------------------------+

Usage:
  .\run-backend-v2.ps1              # Start backend
  .\run-backend-v2.ps1 -Setup       # Setup environment (.env)
  .\run-backend-v2.ps1 -Test        # Test configuration
  .\run-backend-v2.ps1 -Help        # Show this help

Setup Instructions:
  1. Run: .\run-backend-v2.ps1 -Setup
  2. Edit .env file with your credentials
  3. Run: .\run-backend-v2.ps1 -Test (to verify)
  4. Run: .\run-backend-v2.ps1 (to start backend)

Required Configuration:
  - MAIL_USERNAME: Your Gmail address
  - MAIL_PASSWORD: 16-character Gmail App Password (with spaces)
  - JWT_SECRET: Secure key (minimum 32 characters)

Optional Configuration:
  - RAPIDAPI_KEY: For job search API
  - OPENAI_API_KEY: For AI chatbot
  - AFFINDA_API_TOKEN: For resume parser

Documentation:
  See ENVIRONMENT_SETUP.md for detailed configuration guide
"@
    exit 0
}

# Get script directory
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$EnvFile = Join-Path $ScriptDir ".env"
$EnvExampleFile = Join-Path $ScriptDir ".env.example"

# ============================================
# SETUP: Create .env from .env.example
# ============================================
if ($Setup) {
    Write-Host "Setting up environment..." -ForegroundColor Green
    Write-Host ""
    
    if (Test-Path $EnvFile) {
        Write-Host "[WARN] .env file already exists!" -ForegroundColor Yellow
        $response = Read-Host "Overwrite? (y/n)"
        if ($response -ne "y") {
            Write-Host "Setup cancelled." -ForegroundColor Yellow
            exit 0
        }
    }
    
    if (Test-Path $EnvExampleFile) {
        Copy-Item $EnvExampleFile $EnvFile
        Write-Host "[OK] Created .env file from .env.example" -ForegroundColor Green
    } else {
        Write-Host "[ERROR] .env.example not found!" -ForegroundColor Red
        exit 1
    }
    
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Cyan
    Write-Host "  1. Open .env file and edit the values"
    Write-Host "  2. Set MAIL_USERNAME and MAIL_PASSWORD"
    Write-Host "  3. Set JWT_SECRET (minimum 32 characters)"
    Write-Host "  4. Run: .\run-backend-v2.ps1 -Test"
    Write-Host "  5. Run: .\run-backend-v2.ps1"
    Write-Host ""
    
    exit 0
}

# ============================================
# Load .env file
# ============================================
$EnvVars = @{}

if (Test-Path $EnvFile) {
    Write-Host "[LOAD] Loading configuration from .env..." -ForegroundColor Cyan
    
    foreach ($line in Get-Content $EnvFile) {
        # Skip empty lines and comments
        if ([string]::IsNullOrWhiteSpace($line) -or $line.StartsWith("#")) {
            continue
        }
        
        $parts = $line -split "=", 2
        if ($parts.Count -eq 2) {
            $key = $parts[0].Trim()
            $value = $parts[1].Trim() -replace '^"|"$', ''
            $EnvVars[$key] = $value
        }
    }
    
    Write-Host "[OK] Loaded $(($EnvVars.Count)) environment variables" -ForegroundColor Green
} else {
    Write-Host "[ERROR] .env file not found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "First run setup:" -ForegroundColor Yellow
    Write-Host "  .\run-backend-v2.ps1 -Setup"
    Write-Host ""
    exit 1
}

# ============================================
# Extract variables
# ============================================
$MAIL_USERNAME = $EnvVars["MAIL_USERNAME"]
$MAIL_PASSWORD = $EnvVars["MAIL_PASSWORD"]
$JWT_SECRET = $EnvVars["JWT_SECRET"]
$RAPIDAPI_KEY = $EnvVars["RAPIDAPI_KEY"]
$OPENAI_API_KEY = $EnvVars["OPENAI_API_KEY"]
$AFFINDA_API_TOKEN = $EnvVars["AFFINDA_API_TOKEN"]
$ALLOWED_ORIGINS = $EnvVars["ALLOWED_ORIGINS"]
$ENVIRONMENT = $EnvVars["ENVIRONMENT"]
$JWT_EXPIRATION_MS = $EnvVars["JWT_EXPIRATION_MS"]
$MONGODB_URI = $EnvVars["MONGODB_URI"]
$MONGODB_DATABASE = $EnvVars["MONGODB_DATABASE"]
$MAIL_HOST = $EnvVars["MAIL_HOST"]
$MAIL_PORT = $EnvVars["MAIL_PORT"]

# Set defaults
if ([string]::IsNullOrEmpty($ALLOWED_ORIGINS)) { $ALLOWED_ORIGINS = "http://localhost:3000" }
if ([string]::IsNullOrEmpty($ENVIRONMENT)) { $ENVIRONMENT = "development" }
if ([string]::IsNullOrEmpty($JWT_EXPIRATION_MS)) { $JWT_EXPIRATION_MS = "86400000" }
if ([string]::IsNullOrEmpty($MAIL_HOST)) { $MAIL_HOST = "smtp.gmail.com" }
if ([string]::IsNullOrEmpty($MAIL_PORT)) { $MAIL_PORT = "587" }

# ============================================
# Validation
# ============================================
function Test-Configuration {
    Write-Host ""
    Write-Host "Configuration Validation:" -ForegroundColor Cyan
    Write-Host "=" * 60 -ForegroundColor Cyan
    
    $errors = @()
    $warnings = @()
    
    # Required: MAIL_USERNAME
    if ([string]::IsNullOrEmpty($MAIL_USERNAME) -or $MAIL_USERNAME -eq "your_email@gmail.com") {
        $errors += "MAIL_USERNAME: Not configured"
    } else {
        Write-Host "[OK] MAIL_USERNAME: $MAIL_USERNAME" -ForegroundColor Green
    }
    
    # Required: MAIL_PASSWORD
    if ([string]::IsNullOrEmpty($MAIL_PASSWORD) -or $MAIL_PASSWORD -eq "xxxx xxxx xxxx xxxx") {
        $errors += "MAIL_PASSWORD: Not configured"
    } else {
        if ($MAIL_PASSWORD.Length -eq 19) {
            Write-Host "[OK] MAIL_PASSWORD: Valid (16-char app password)" -ForegroundColor Green
        } else {
            $warnings += "MAIL_PASSWORD: Expected 19 characters (16 chars + 3 spaces), got $($MAIL_PASSWORD.Length)"
        }
    }
    
    # Required: JWT_SECRET
    if ([string]::IsNullOrEmpty($JWT_SECRET)) {
        $errors += "JWT_SECRET: Not configured"
    } elseif ($JWT_SECRET.Length -lt 32) {
        $errors += "JWT_SECRET: Too short ($($JWT_SECRET.Length) chars, minimum 32)"
    } else {
        Write-Host "[OK] JWT_SECRET: Valid ($($JWT_SECRET.Length) chars)" -ForegroundColor Green
    }
    
    # Optional: API Keys
    if ([string]::IsNullOrEmpty($RAPIDAPI_KEY)) {
        Write-Host "[OPTIONAL] RAPIDAPI_KEY: Not set (optional - using mock data)" -ForegroundColor Yellow
    } else {
        Write-Host "[OK] RAPIDAPI_KEY: Configured" -ForegroundColor Green
    }
    
    if ([string]::IsNullOrEmpty($OPENAI_API_KEY)) {
        Write-Host "[OPTIONAL] OPENAI_API_KEY: Not set (optional - using rule-based chatbot)" -ForegroundColor Yellow
    } else {
        Write-Host "[OK] OPENAI_API_KEY: Configured" -ForegroundColor Green
    }
    
    if ([string]::IsNullOrEmpty($AFFINDA_API_TOKEN)) {
        Write-Host "[OPTIONAL] AFFINDA_API_TOKEN: Not set (optional - using regex parsing)" -ForegroundColor Yellow
    } else {
        Write-Host "[OK] AFFINDA_API_TOKEN: Configured" -ForegroundColor Green
    }
    
    Write-Host "=" * 60 -ForegroundColor Cyan
    
    # Display warnings
    if ($warnings.Count -gt 0) {
        Write-Host ""
        Write-Host "[WARN] Warnings:" -ForegroundColor Yellow
        foreach ($warning in $warnings) {
            Write-Host "  • $warning" -ForegroundColor Yellow
        }
    }
    
    # Display errors
    if ($errors.Count -gt 0) {
        Write-Host ""
        Write-Host "[ERROR] Errors (must fix before running):" -ForegroundColor Red
        foreach ($error in $errors) {
            Write-Host "  • $error" -ForegroundColor Red
        }
        Write-Host ""
        return $false
    }
    
    return $true
}

# ============================================
# TEST mode
# ============================================
if ($Test) {
    $isValid = Test-Configuration
    if ($isValid) {
        Write-Host ""
        Write-Host "[OK] Configuration is valid! Ready to start backend." -ForegroundColor Green
        Write-Host ""
        Write-Host "Next: .\run-backend-v2.ps1" -ForegroundColor Cyan
        exit 0
    } else {
        Write-Host ""
        Write-Host "Fix the errors above and try again." -ForegroundColor Red
        Write-Host "Edit .env and run: .\run-backend-v2.ps1 -Test" -ForegroundColor Yellow
        exit 1
    }
}

# ============================================
# Validate before starting
# ============================================
Write-Host ""
$isValid = Test-Configuration

if (-not $isValid) {
    Write-Host ""
    Write-Host "[ERROR] Configuration has errors. Cannot start backend." -ForegroundColor Red
    Write-Host ""
    Write-Host "Fix the errors:" -ForegroundColor Yellow
    Write-Host "  1. Edit .env file" -ForegroundColor White
    Write-Host "  2. Run: .\run-backend-v2.ps1 -Test" -ForegroundColor White
    Write-Host "  3. Run: .\run-backend-v2.ps1" -ForegroundColor White
    Write-Host ""
    exit 1
}

# ============================================
# Set environment variables
# ============================================
Write-Host ""
Write-Host "Setting environment variables..." -ForegroundColor Cyan
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

Write-Host "[OK] Environment variables loaded" -ForegroundColor Green
Write-Host ""

# ============================================
# Check for Port 8080 conflict
# ============================================
Write-Host "[CHECK] Verifying port 8080 is free..." -ForegroundColor Cyan
$PortProcess = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | Select-Object -First 1
if ($PortProcess) {
    $PIDToKill = $PortProcess.OwningProcess
    Write-Host "[WARN] Port 8080 is occupied by PID $PIDToKill. Killing it..." -ForegroundColor Yellow
    Stop-Process -Id $PIDToKill -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 2
}
Write-Host "[OK] Port 8080 is available" -ForegroundColor Green
Write-Host ""

# ============================================
# Start Backend
# ============================================
Write-Host "[START] Starting Job Portal Backend..." -ForegroundColor Green
Write-Host "Backend URL: http://localhost:8080" -ForegroundColor Cyan
Write-Host "Frontend connects to: http://localhost:8080/api" -ForegroundColor Cyan
Write-Host ""
Write-Host "Logs:" -ForegroundColor Yellow

# Navigate to backend folder safely
$BackendPath = Join-Path $ScriptDir "backend"
if (Test-Path $BackendPath) {
    Set-Location $BackendPath
} else {
    Write-Host "[ERROR] Backend directory not found at $BackendPath" -ForegroundColor Red
    exit 1
}

$MavenCmd = Join-Path $ScriptDir "apache-maven-3.9.12\bin\mvn.cmd"
if (-not (Test-Path $MavenCmd)) {
    Write-Host "[ERROR] Maven not found at $MavenCmd" -ForegroundColor Red
    exit 1
}

& $MavenCmd spring-boot:run

# Error handling
if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "[ERROR] Backend failed to start with exit code: $LASTEXITCODE" -ForegroundColor Red
    Write-Host ""
    Write-Host "Troubleshooting:" -ForegroundColor Yellow
    Write-Host "  1. Test config: .\run-backend-v2.ps1 -Test" -ForegroundColor White
    Write-Host "  2. Check .env values are correct" -ForegroundColor White
    Write-Host "  3. Verify port 8080 is not in use: netstat -ano | findstr :8080" -ForegroundColor White
    Write-Host "  4. Check Java version: java -version (need Java 17+)" -ForegroundColor White
    Write-Host ""
    pause
}
