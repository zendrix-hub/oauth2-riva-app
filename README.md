# 🤠 OAuth2-Riva: Cowboy Style OAuth2 Login

## Project Overview
This is a Spring Boot application demonstrating **OAuth2 login** using **Google** and **GitHub**. Users can:

- Login using Google or GitHub
- View and edit their profile (display name, bio, avatar)
- Safely logout with CSRF protection
- Enjoy a minimal cowboy-themed UI

---

## Features

- ✅ OAuth2 login (Google & GitHub)
- ✅ Profile page with edit functionality
- ✅ CSRF protection for forms
- ✅ Friendly cowboy-themed UI
- ✅ H2 in-memory database for quick testing
- ✅ Error handling with custom `/error` page

---

## Setup Instructions

1. **Clone the project**

```bash
git clone https://github.com/zendrix-hub/oauth2-riva-app.git
cd oauth2-riva


Architecture diagram
      ┌───────────────┐
      │   Browser     │
      └─────┬─────────┘
            │ Login
            ▼
      ┌───────────────┐        ┌─────────────┐
      │ Spring Boot   │<------>│ Google /    │
      │ OAuth2 Client │        │ GitHub OAuth│
      └─────┬─────────┘        └─────────────┘
            │
            ▼
      ┌───────────────┐
      │ Database      │
      │ (H2/MySQL)    │
      └───────────────┘
