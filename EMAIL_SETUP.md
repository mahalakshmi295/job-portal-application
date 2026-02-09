# 📧 Email Setup Guide for OTP Feature

## Overview

The Job Portal application sends OTP (One-Time Password) codes via email for secure passwordless authentication.

---

## 🚀 Quick Setup (Gmail)

### Step 1: Enable 2-Step Verification

1. Go to [Google Account Security](https://myaccount.google.com/security)
2. Click on **"2-Step Verification"**
3. Follow the setup wizard to enable it

### Step 2: Generate App Password

1. Once 2-Step Verification is enabled, go back to [Security Settings](https://myaccount.google.com/security)
2. Find **"App passwords"** section (may need to search for it)
3. Click **"App passwords"**
4. Select:
   - **App:** Mail
   - **Device:** Other (Custom name) → Type "Job Portal"
5. Click **"Generate"**
6. Copy the **16-character password** (e.g., `abcd efgh ijkl mnop`)

### Step 3: Configure application.properties

Open: `backend/src/main/resources/application.properties`

Update these lines:

```properties
spring.mail.username=marrisrisaivarun@gmail.com
spring.mail.password='lvgawqmxoasecsxf'
```

**Important:**

- Remove spaces from the app password (use `abcdefghijklmnop`, not `abcd efgh ijkl mnop`)
- Use your actual Gmail address
- This is NOT your regular Gmail password!

### Step 4: Restart Backend

```powershell
# Stop the running backend (Ctrl+C)
# Then restart:
cd backend
..\apache-maven-3.9.12\bin\mvn.cmd spring-boot:run
```

---

## 📧 Other Email Providers

### Microsoft Outlook / Hotmail

```properties
spring.mail.host=smtp.office365.com
spring.mail.port=587
spring.mail.username=your-email@outlook.com
spring.mail.password=your-password
```

**Note:** Outlook requires enabling "Less secure app access" or using app-specific password

### Yahoo Mail

```properties
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
spring.mail.username=your-email@yahoo.com
spring.mail.password=your-app-password
```

**Note:** Yahoo requires generating an app password at [Yahoo Account Security](https://login.yahoo.com/account/security)

### Custom SMTP Server

```properties
spring.mail.host=smtp.your-domain.com
spring.mail.port=587
spring.mail.username=your-email@your-domain.com
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## 🧪 Testing the Email Feature

### 1. Start the Application

Make sure both frontend and backend are running.

### 2. Test OTP Login

1. Open: http://localhost:3000
2. Enter your real email address
3. Click **"Send OTP"**
4. Check your email inbox for OTP code
5. Enter the 6-digit OTP
6. Click **"Verify"**

### 3. Check Logs

Backend console should show:

```
🔑 Generated OTP for user@example.com: 123456 (expires at ...)
✅ OTP email sent successfully to: user@example.com
```

---

## 🔧 Troubleshooting

### Issue 1: "Failed to send email"

**Cause:** Invalid credentials or app password not configured

**Solution:**

- Double-check email and app password in `application.properties`
- Make sure 2-Step Verification is enabled
- Regenerate app password if needed

### Issue 2: "Authentication failed"

**Cause:** Using regular password instead of app password

**Solution:**

- Gmail requires app-specific password, NOT your regular password
- Follow Step 2 above to generate app password

### Issue 3: "Connection timeout"

**Cause:** Firewall or network blocking SMTP port 587

**Solution:**

- Check firewall settings
- Try using port 465 with SSL:
  ```properties
  spring.mail.port=465
  spring.mail.properties.mail.smtp.ssl.enable=true
  ```

### Issue 4: "Email sent but not receiving"

**Possible causes:**

- Email in spam/junk folder → Check spam folder
- Wrong email address → Verify email spelling
- Email provider blocking → Check provider settings

### Issue 5: Backend shows errors in console

**Check for:**

```
❌ Failed to send OTP email: ...
```

**Common reasons:**

- SMTP server blocking connection
- Invalid authentication credentials
- Network connectivity issues

---

## 📝 Development Mode

For testing **without** email configuration:

The backend automatically includes the OTP in the API response during development:

```json
{
  "otpSent": true,
  "otp": "123456",
  "message": "OTP sent to your email"
}
```

The frontend will display: **"OTP sent to email@example.com! (Dev OTP: 123456)"**

This allows you to test the application without setting up email first.

---

## 🔒 Security Best Practices

### For Development:

- ✅ Use app-specific passwords
- ✅ Never commit credentials to Git
- ✅ Use environment variables for sensitive data

### For Production:

- ✅ Use environment variables for email credentials
- ✅ Remove OTP from API response (already handled)
- ✅ Use secure SMTP connections (TLS/SSL)
- ✅ Monitor email sending rates
- ✅ Implement rate limiting
- ✅ Use professional email service (SendGrid, AWS SES, etc.)

### Environment Variables (Production):

```bash
export MAIL_USERNAME='marrisrisaivarun@gmail.com'
export MAIL_PASSWORD='lvgawqmxoasecsxf'
```

Then in `application.properties`:

```properties
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

---

## 📊 Email Features

### OTP Email

- **Subject:** 🔐 Your Job Portal Login OTP
- **Contains:** 6-digit OTP code
- **Valid for:** 5 minutes
- **Max attempts:** 3

### Welcome Email

- **Subject:** 🎉 Welcome to Job Portal!
- **Sent:** After successful OTP verification
- **Contains:** Feature overview and getting started info

---

## 🎯 Email Sending Flow

```
1. User enters email → Click "Send OTP"
2. Backend generates 6-digit random OTP
3. OTP stored in memory with 5-minute expiry
4. Email sent via SMTP to user's address
5. User receives email with OTP code
6. User enters OTP → Click "Verify"
7. Backend verifies OTP (3 attempts max)
8. If valid → Login successful + Welcome email sent
9. If invalid → Error message + remaining attempts shown
```

---

## 📞 Need Help?

- **Gmail App Password Issues:** https://support.google.com/accounts/answer/185833
- **Outlook SMTP Settings:** https://support.microsoft.com/en-us/office/pop-imap-and-smtp-settings-8361e398-8af4-4e97-b147-6c6c4ac95353
- **Yahoo App Password:** https://help.yahoo.com/kb/generate-third-party-passwords-sln15241.html

---

**Note:** If you're unable to configure email, the application will still work in development mode with OTP codes shown in the API response and backend console logs.
