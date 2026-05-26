# OTP Testing Script for Job Portal Backend
# This script tests the OTP functionality end-to-end
# Usage: .\test-otp.ps1

param(
    [string]$Email = "",
    [string]$Otp = ""
)

# Colors for output
$Green = [System.ConsoleColor]::Green
$Red = [System.ConsoleColor]::Red
$Yellow = [System.ConsoleColor]::Yellow
$Cyan = [System.ConsoleColor]::Cyan
$White = [System.ConsoleColor]::White

function Write-Success { Write-Host $args -ForegroundColor $Green }
function Write-Error { Write-Host $args -ForegroundColor $Red }
function Write-Info { Write-Host $args -ForegroundColor $Cyan }
function Write-Warning { Write-Host $args -ForegroundColor $Yellow }

# Backend URL
$BackendUrl = "http://localhost:8080/api"

Write-Host ""
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║         🔐 Job Portal - OTP Testing Script               ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Step 1: Health Check
Write-Info "Step 1: Checking Backend Health..."
try {
    $response = Invoke-WebRequest -Uri "$BackendUrl/health" -Method GET -ErrorAction Stop
    $health = $response.Content | ConvertFrom-Json
    Write-Success "✅ Backend is running on port 8080"
    Write-Success "   Status: $($health.status)"
} catch {
    Write-Error "❌ Backend is not running!"
    Write-Error "   Error: $($_.Exception.Message)"
    Write-Info ""
    Write-Info "🚀 To start backend, run:"
    Write-Host "`$env:MAIL_USERNAME = 'marrisrisaivarun@gmail.com'" -ForegroundColor Yellow
    Write-Host "`$env:MAIL_PASSWORD = 'glwv ysxq sklq tuhd'" -ForegroundColor Yellow
    Write-Host "`$env:JWT_SECRET = 'qwertyuiopasdfghjklzxcvbnm123456'" -ForegroundColor Yellow
    Write-Host "cd backend" -ForegroundColor Yellow
    Write-Host "..\apache-maven-3.9.12\bin\mvn.cmd spring-boot:run" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# Step 2: Get or Ask for Email
if ($Email -eq "") {
    Write-Warning "Step 2: Enter Email Address for OTP Testing"
    $Email = Read-Host "   Email (e.g., test@gmail.com)"
}

if ($Email -eq "" -or $Email -notlike "*@*") {
    Write-Error "❌ Invalid email address!"
    exit 1
}

Write-Success "✅ Using email: $Email"
Write-Host ""

# Step 3: Send OTP
Write-Info "Step 3: Sending OTP to your email..."
try {
    $body = @{email = $Email} | ConvertTo-Json
    $headers = @{"Content-Type" = "application/json"}
    
    $response = Invoke-WebRequest -Uri "$BackendUrl/auth/otp-login" `
        -Method POST `
        -Headers $headers `
        -Body $body `
        -ErrorAction Stop
    
    $result = $response.Content | ConvertFrom-Json
    
    if ($result.otpSent -eq $true) {
        Write-Success "✅ OTP sent successfully!"
        Write-Success "   Message: $($result.message)"
        Write-Info "   Check your email at: $Email"
    } else {
        Write-Error "❌ Failed to send OTP"
        Write-Error "   Error: $($result.error)"
        exit 1
    }
} catch {
    Write-Error "❌ Error sending OTP:"
    Write-Error "   $($_.Exception.Message)"
    exit 1
}

Write-Host ""
Write-Warning "⏱️  The OTP is valid for 5 minutes. Check your email (including spam folder)."
Write-Host ""

# Step 4: Get or Ask for OTP
if ($Otp -eq "") {
    Write-Warning "Step 4: Enter the 6-digit OTP from your email"
    $Otp = Read-Host "   OTP Code"
}

if ($Otp -eq "" -or $Otp -notmatch "^\d{6}$") {
    Write-Error "❌ Invalid OTP! Must be 6 digits."
    exit 1
}

Write-Success "✅ Using OTP: $Otp"
Write-Host ""

# Step 5: Verify OTP
Write-Info "Step 5: Verifying OTP..."
try {
    $body = @{email = $Email; otp = $Otp} | ConvertTo-Json
    $headers = @{"Content-Type" = "application/json"}
    
    $response = Invoke-WebRequest -Uri "$BackendUrl/auth/verify-otp" `
        -Method POST `
        -Headers $headers `
        -Body $body `
        -ErrorAction Stop
    
    $result = $response.Content | ConvertFrom-Json
    
    if ($result.verified -eq $true) {
        Write-Success "✅ OTP Verified Successfully!"
        Write-Success "   Message: $($result.message)"
        Write-Success "   Email: $($result.email)"
        
        Write-Host ""
        Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Green
        Write-Host "║          🎉 OTP Testing Successful!                      ║" -ForegroundColor Green
        Write-Host "║     Your backend email service is working properly!      ║" -ForegroundColor Green
        Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Green
    } else {
        Write-Error "❌ OTP Verification Failed!"
        Write-Error "   $($result.message)"
        
        # Extract remaining attempts if available
        if ($result.remainingAttempts) {
            Write-Warning "   Remaining attempts: $($result.remainingAttempts)"
        }
        exit 1
    }
} catch {
    Write-Error "❌ Error verifying OTP:"
    Write-Error "   $($_.Exception.Message)"
    exit 1
}

Write-Host ""
Write-Info "Next Steps:"
Write-Host "1. Open http://localhost:3000 in your browser" -ForegroundColor White
Write-Host "2. Click 'Get OTP' on the login page" -ForegroundColor White
Write-Host "3. Enter your email address" -ForegroundColor White
Write-Host "4. Check your email and enter the OTP" -ForegroundColor White
Write-Host "5. Enjoy the Job Portal! 🚀" -ForegroundColor White
Write-Host ""
