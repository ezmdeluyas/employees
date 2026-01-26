# Employees API – CORS Tester UI

This tool is a lightweight frontend utility for validating Cross-Origin Resource Sharing (CORS) behavior of the Employees REST API.

It is intended for **development, UAT, and pre-production verification** to ensure browsers can correctly perform:

- Simple requests (GET)
- Preflighted requests (POST / PUT / DELETE)
- Authorization header handling
- OPTIONS preflight responses

---

## 🚀 Features

- Supports GET, POST, PUT, DELETE testing
- Displays response headers and body
- Triggers real browser preflight (OPTIONS)
- Supports HTTP Basic Authorization header
- Works with any environment (DEV / UAT / PROD)
- No external dependencies (pure HTML + JS)

---

## 📂 Location

This file is intentionally placed outside application runtime assets.

Recommended location:

⚠ Do NOT deploy this file with production static assets.

---

## 🛠 How To Use

### 1) Start Backend API

Example:

Ensure correct Spring profile is active:


---

### 2) Open UI

Open directly in browser:


Or use Live Server / local static server:


---

### 3) Configure Settings

| Field           | Description                      |
|-----------------|----------------------------------|
| API Base URL    | Backend server URL               |
| API Path Prefix | API base path (example: /api/v1) |
| Username        | Basic Auth username              |
| Password        | Plain text password              |
| Use Basic Auth  | Enables Authorization header     |

---

## ✅ CORS Success Criteria

### GET (Simple Request)

Expected behavior:

- Only ONE request in Network tab
- No OPTIONS request
- Response contains:

---

### POST / PUT / DELETE (Preflighted Requests)

Expected behavior:

1. Browser sends:

2. Server responds with:

3. Browser sends real request:


---

### Preflight MUST contain headers:

1. Access-Control-Allow-Origin
2. Access-Control-Allow-Methods
3. Access-Control-Allow-Headers


---

## 🔐 Security Behavior Notes

This tool validates browser access only.

It does NOT bypass:

- Authentication
- Authorization (roles)
- API security rules

Expected responses:

| Status | Meaning |
-------|--------
200 / 201 / 204 | Success
401 | Authentication required
403 | Insufficient role
404 | Invalid path or ID

CORS is considered working as long as:

- Browser does NOT block the request
- OPTIONS succeeds
- Real request is sent

---

## 🧪 Manual Preflight Button

The "Manual OPTIONS" button simulates a browser preflight request.

Use it to:

- Debug allowed headers
- Validate allowed methods
- Inspect preflight response headers

---

## ⚠ Production Safety

This tool should:

- NOT be publicly hosted
- NOT contain real production credentials
- Be used only by developers / testers

---

## 📌 Recommended Workflow

1. Validate GET CORS
2. Validate POST preflight
3. Validate PUT preflight
4. Validate DELETE preflight
5. Verify PROD allowed origins configuration

---

## 🏁 Result

If all tests pass without browser CORS errors, the API is considered:

✅ Browser-compatible  
✅ Frontend-safe  
✅ Production-ready

---

Author: Internal Developer Tooling  
Purpose: API Security Validation
