# Library Management System — API Documentation

> **Base URL:** `http://localhost:8080`
> **Date Format:** `YYYY-MM-DD`
> **Auth:** Student endpoints require a valid session (`studentId` stored in session).

---

## Table of Contents

- [Admin Endpoints](#admin-endpoints)
  - [Students](#1-admin-student-controller)
  - [Books](#2-admin-book-controller)
  - [Borrow Requests](#3-admin-borrow-request-controller)
  - [Penalties](#4-admin-penalty-controller)
- [Student Endpoints](#student-endpoints)
  - [Books](#1-student-book-controller)
  - [Borrow Requests](#2-student-borrow-request-controller)
  - [Borrowed Books](#3-student-borrowed-books-controller)
  - [Penalties](#4-student-penalty-controller)
  - [Profile](#5-student-profile-controller)
- [General Notes](#general-notes)

---

## Admin Endpoints

### 1. Admin Student Controller

#### `POST /admin/students`
Create a new student account.

**Request Body**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "12345",
  "department": "CS",
  "currentSemester": 3,
  "phoneNumber": "9876543210",
  "active": true
}
```

**Response `200 OK`**
```json
{
  "success": true,
  "message": "Student created successfully",
  "student": { "id": 1, "name": "John Doe", "email": "john@example.com", "..." }
}
```

> Email must be unique. Password is automatically hashed before storage.

---

#### `PUT /admin/students/{id}`
Update an existing student's details.

**Path Param:** `id` — Student ID

**Request Body** *(all fields optional)*
```json
{
  "department": "IT",
  "currentSemester": 4,
  "email": "newemail@example.com",
  "password": "newpass",
  "address": "New address",
  "phoneNumber": "9876543210",
  "gender": "M",
  "active": true
}
```

**Response `200 OK`**
```json
{
  "success": true,
  "student": { "id": 1, "..." }
}
```

> Only fields present in the request body are updated (partial update).

---

#### `DELETE /admin/students/{id}`
Delete a student by ID.

**Path Param:** `id` — Student ID

**Response `200 OK`**
```json
{
  "success": true,
  "message": "Student deleted successfully"
}
```

> Returns `400 Bad Request` if the student currently has borrowed books.

---

#### `GET /admin/students/{id}`
Fetch a student by ID.

**Path Param:** `id` — Student ID

**Response `200 OK`**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "department": "CS",
  "currentSemester": 3,
  "..."
}
```

---

#### `GET /admin/students/email/{email}`
Fetch a student by email address.

**Path Param:** `email` — Student email

**Response `200 OK`** — Same as above.

---

#### `GET /admin/students`
Fetch all students.

**Response `200 OK`**
```json
[
  { "id": 1, "name": "John Doe", "email": "john@example.com", "..." },
  { "..." }
]
```

---

### 2. Admin Book Controller

#### `POST /admin/books`
Add a new book to the library.

**Request Body**
```json
{
  "title": "Operating System Concepts",
  "author": "Silberschatz",
  "isbn": "9780470128725",
  "totalCopies": 10,
  "publisher": "Wiley",
  "category": "CS"
}
```

**Response `200 OK`**
```json
{
  "success": true,
  "message": "Book added successfully",
  "book": { "id": 1, "title": "Operating System Concepts", "availableCopies": 10, "..." }
}
```

> ISBN must be unique. `availableCopies` is automatically set equal to `totalCopies` on creation.

---

#### `PUT /admin/books/id/{id}`
Update a book by its ID.

**Path Param:** `id` — Book ID

**Request Body** *(all fields optional)*
```json
{
  "title": "Updated Title",
  "availableCopies": 5,
  "totalCopies": 7,
  "publisher": "New Publisher"
}
```

**Response `200 OK`**
```json
{
  "success": true,
  "message": "Book updated successfully",
  "book": { "id": 1, "..." }
}
```

> ISBN cannot be updated. `availableCopies` cannot be set lower than the currently borrowed count.

---

#### `PUT /admin/books/isbn/{isbn}`
Update a book by its ISBN. Same request/response as above.

---

#### `GET /admin/books/{id}`
Fetch a book by ID.

**Response `200 OK`**
```json
{
  "id": 1,
  "title": "Operating System Concepts",
  "author": "Silberschatz",
  "isbn": "9780470128725",
  "availableCopies": 8,
  "totalCopies": 10,
  "..."
}
```

---

#### `GET /admin/books/isbn/{isbn}`
Fetch a book by ISBN. Same response structure as above.

---

#### `GET /admin/books`
Fetch all books.

**Response `200 OK`** — Array of book objects.

---

#### `DELETE /admin/books/{id}`
Delete a book by ID.

**Response `200 OK`**
```json
{
  "success": true,
  "message": "Book has been deleted successfully."
}
```

> Returns `400 Bad Request` if the book is currently borrowed by any student.

---

#### `DELETE /admin/books/isbn/{isbn}`
Delete a book by ISBN. Same behavior as above.

---

### 3. Admin Borrow Request Controller

#### `GET /admin/borrow-requests/pending`
Fetch all pending borrow requests.

**Response `200 OK`**
```json
[
  {
    "id": 1,
    "studentId": 1,
    "bookId": 2,
    "status": "PENDING",
    "requestDate": "2026-06-02T12:00:00"
  }
]
```

---

#### `GET /admin/borrow-requests/rejected`
Fetch all rejected borrow requests. Same response structure as above.

---

#### `PUT /admin/borrow-requests/{id}/status`
Approve or reject a borrow request.

**Path Param:** `id` — Borrow Request ID

**Query Parameters**

| Param        | Required | Description                        | Example              |
|--------------|----------|------------------------------------|----------------------|
| `status`     | Yes      | `APPROVED` or `REJECTED`           | `?status=APPROVED`   |
| `borrowDate` | No       | Override borrow date               | `&borrowDate=2026-06-02` |
| `dueDate`    | No       | Override due date                  | `&dueDate=2026-06-16` |

**Response `200 OK`**
```json
{
  "success": true,
  "message": "Request APPROVED successfully",
  "data": { "..." }
}
```

> On `APPROVED`: book's `availableCopies` is decremented and a `BorrowedBook` record is created automatically.

---

### 4. Admin Penalty Controller

#### `GET /admin/penalties`
Fetch all penalties across all students.

**Response `200 OK`**
```json
[
  { "id": 1, "amount": 500.0, "reason": "Late Return", "studentId": 1, "..." },
  { "..." }
]
```

---

#### `GET /admin/penalties/student/{studentId}`
Fetch all penalties for a specific student.

**Path Param:** `studentId` — Student ID

**Response `200 OK`** — Array of penalty objects.

> Returns `404 Not Found` if no penalties exist for the given student.

---

#### `GET /admin/penalties/{id}`
Fetch a specific penalty by its ID.

**Response `200 OK`**
```json
{
  "id": 1,
  "amount": 500.0,
  "reason": "Late Return",
  "studentId": 1,
  "..."
}
```

---

## Student Endpoints

> All student endpoints require a valid session. The `studentId` is read from the session automatically — it does not need to be passed in the request.

---

### 1. Student Book Controller

#### `GET /student/books`
Fetch all books currently available for borrowing.

**Response `200 OK`**
```json
[
  { "id": 1, "title": "Operating System Concepts", "availableCopies": 5, "..." },
  { "..." }
]
```

> Only books with `availableCopies > 0` are returned.

---

#### `GET /student/books/{id}`
Fetch details of a specific book.

**Path Param:** `id` — Book ID

**Response `200 OK`** — Single book object.

---

### 2. Student Borrow Request Controller

#### `POST /student/borrow-requests/{bookId}`
Submit a borrow request for a book.

**Path Param:** `bookId` — ID of the book to request

**Request Body:** None

**Response `200 OK`**
```json
{
  "id": 1,
  "studentId": 1,
  "bookId": 2,
  "status": "PENDING",
  "requestDate": "2026-06-02T12:00:00"
}
```

---

#### `GET /student/borrow-requests`
Fetch all borrow requests made by the logged-in student.

**Response `200 OK`**
```json
[
  {
    "id": 1,
    "studentId": 1,
    "bookId": 2,
    "status": "PENDING",
    "requestDate": "2026-06-02T12:00:00"
  },
  { "..." }
]
```

---

### 3. Student Borrowed Books Controller

#### `GET /students/borrowed`
Fetch all books currently or previously borrowed by the logged-in student.

**Response `200 OK`**
```json
[
  {
    "id": 1,
    "book": { "id": 2, "title": "DBMS Concepts", "..." },
    "borrowDate": "2026-06-01",
    "dueDate": "2026-06-15",
    "returned": false
  }
]
```

---

#### `PUT /students/borrowed/{id}/return`
Return a borrowed book.

**Path Param:** `id` — Borrowed Book record ID

**Request Body**
```json
{
  "returnDate": "2026-06-18"
}
```

**Response `200 OK`**
```json
{
  "id": 1,
  "returned": true,
  "returnDate": "2026-06-18",
  "fineAmount": 300.0
}
```

> A fine is automatically calculated if `returnDate` is after `dueDate`. Fine amount is reflected immediately in the student's penalties.

---

### 4. Student Penalty Controller

#### `GET /students/penalties`
Fetch all penalties for the logged-in student.

**Response `200 OK`**
```json
[
  { "id": 1, "amount": 300.0, "reason": "Late Return", "..." },
  { "..." }
]
```

---

#### `GET /students/penalties/total`
Get the total outstanding penalty amount for the logged-in student.

**Response `200 OK`**
```
300.0
```

> Returns a plain numeric value (not a JSON object).

---

### 5. Student Profile Controller

#### `GET /students/me`
Fetch the logged-in student's own profile.

**Response `200 OK`**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "department": "CS",
  "currentSemester": 3,
  "phoneNumber": "9876543210",
  "active": true
}
```

---

## General Notes

| # | Note |
|---|------|
| 1 | All student endpoints read `studentId` from the session — never pass it in the request body. |
| 2 | Admin endpoints are unrestricted in the current config but are intended for admin use only. |
| 3 | All date fields (`borrowDate`, `dueDate`, `returnDate`) use the format `YYYY-MM-DD`. |
| 4 | All Admin controller responses include a `"success": true/false` field. |
| 5 | Fines are auto-calculated on book return; no manual input required. |
| 6 | `GET /students/penalties/total` returns a raw number, not a JSON object. |