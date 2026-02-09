# Resume Analysis Script
$resumeText = Get-Content "$PSScriptRoot\sample_resume.txt" -Raw

$payload = @{
    userId = "john-doe"
    personalInfo = @{
        name = "John Doe"
        email = "john.doe@email.com"
        phone = "+1-555-123-4567"
    }
    professionalSummary = "Results-driven Full Stack Software Engineer with 5+ years of experience"
    skills = @("Java", "JavaScript", "TypeScript", "Python", "React", "Angular", "Spring Boot", "Node.js", "Express", "PostgreSQL", "MySQL", "MongoDB", "Redis", "AWS", "Docker", "Kubernetes", "Jenkins", "CI/CD", "Git")
    education = @(@{
        institution = "University of California, Berkeley"
        degree = "Bachelor of Science"
        field = "Computer Science"
        startDate = "August 2014"
        endDate = "May 2018"
        gpa = "3.8/4.0"
    })
    experience = @(@{
        company = "TechCorp Inc."
        position = "Senior Software Engineer"
        startDate = "January 2022"
        endDate = "Present"
        current = $true
        description = @("Architected microservices platform", "Implemented CI/CD pipelines", "Led team of 5 engineers")
    })
    projects = @(@{
        name = "E-Commerce Platform"
        description = "Microservices architecture"
        technologies = @("Java", "Spring Boot", "Docker")
        link = "github.com/johndoe/project"
    })
    certifications = @("AWS Certified Solutions Architect", "Oracle Java Developer")
    areasOfInterest = @("Cloud Computing", "Machine Learning")
    rawText = $resumeText
}

$json = $payload | ConvertTo-Json -Depth 10

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/resume-score" -Method POST -Body $json -ContentType "application/json; charset=utf-8"
    
    Write-Host "`n==================== RESUME ANALYSIS RESULTS ====================" -ForegroundColor Green
    Write-Host "`nResume ID: $($response.resumeId)" -ForegroundColor Yellow
    Write-Host "`nOVERALL SCORE: $([math]::Round($response.overallScore, 1))/100" -ForegroundColor $(if($response.overallScore -ge 80){"Green"}elseif($response.overallScore -ge 60){"Yellow"}else{"Red"})
    Write-Host "`nDETAILED SCORES:"
    Write-Host "  ATS Score:           $([math]::Round($response.atsScore, 1))/100"
    Write-Host "  Keyword Match:       $([math]::Round($response.keywordMatchScore, 1))/100"
    Write-Host "  Format Score:        $([math]::Round($response.formatScore, 1))/100"
    Write-Host "`nEXTRACTED SKILLS ($($response.extractedSkills.Count)):" -ForegroundColor Cyan
    $response.extractedSkills | ForEach-Object { Write-Host "  • $_" -ForegroundColor Green }
    Write-Host "`nSECTION COMPLETENESS:" -ForegroundColor Cyan
    $response.sectionCompleteness.PSObject.Properties | ForEach-Object {
        $status = if($_.Value){"✓"}else{"✗"}
        $color = if($_.Value){"Green"}else{"Red"}
        Write-Host "  $status $($_.Name)" -ForegroundColor $color
    }
    Write-Host "`nSUGGESTIONS:" -ForegroundColor Magenta
    $i = 1
    $response.suggestions | ForEach-Object { Write-Host "  $i. $_"; $i++ }
    Write-Host "`n================================================================`n" -ForegroundColor Green
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
