# GlamBook – Report Notes

SE1020 Object Oriented Programming project: Beauty Salon Appointment Booking System.

## 1. System overview

GlamBook is a Java web application that lets a beauty salon take bookings online.
Customers register, browse services and stylists, book a free time slot, pay and write reviews.
Admins manage users, services, stylists, payments and reviews from a dashboard.

The system uses no database. Every record is one line in a plain `.txt` file and the fields are
separated by the `|` character. Java file I/O (`BufferedReader`, `BufferedWriter`) is used to read and write the files.

**Technology:** Java 17, Jakarta Servlets 6, JSP + JSTL 3, Apache Tomcat 10.1, Maven (war), Bootstrap 5.

### Layered design

```
JSP form  ->  Servlet  ->  Service  ->  DAO  ->  .txt file
                 ^                                   |
                 +------- result / error ------------+
JSP page  <-  Servlet (forward on error, redirect on success)
```

| Layer | Package | Responsibility |
|-------|---------|----------------|
| Model | `com.glambook.model` | Classes for the data (encapsulation, inheritance, polymorphism, abstraction). Each model converts itself to and from a file line. |
| DAO | `com.glambook.dao` | File read and write only. `FileHandler` is shared; one DAO per entity. |
| Service | `com.glambook.service` | Business rules and validation (no double-booking, discounts, etc). |
| Servlet | `com.glambook.servlet.*` | Reads the request, calls the service, forwards to a JSP or redirects. |
| Util | `com.glambook.util` | `IdGenerator`, `Validator`, `DataPath`. |
| View | `src/main/webapp` | JSP pages per component, shared `includes/` (header, navbar, footer), `css/`, `js/`. |

### Cross-cutting parts

- **AuthFilter** (`servlet.common`) runs before protected URLs. If nobody is logged in it redirects to `/login`.
  For `/admin/*` URLs it calls `user.canAccessAdmin()`, which is overridden in `Customer` (false) and `AdminUser` (true).
- **SessionHelper** reads the logged-in user from the session and stores one-time "flash" messages that are shown after a redirect.
- **AppStartupListener** runs when Tomcat starts. It picks the data folder and copies the sample files into it if they are missing.
- **Post/Redirect/Get:** every successful POST ends with `sendRedirect`, so refreshing the page does not submit the form again.
  Validation errors use `forward` so the form can be shown again with the message and the typed values.

## 2. Components

### 2.1 User Management (`users.txt`)
- **Create:** register a customer account (`/register`).
- **Read:** login (`/login`), view profile (`/profile`), admin user list with search (`/admin/users?q=`).
- **Update:** edit name, email, phone, membership and password (`/profile`).
- **Delete:** customer deletes their own account (`/profile/delete`); admin deletes any user (`/admin/users/delete`).
- **Session:** login stores the `User` object in the session; logout invalidates it.
- **OOP:** abstract `User`; `Customer` and `AdminUser` override `getRole()`, `canAccessAdmin()` and `getHomePage()`.

### 2.2 Service Management (`services.txt`)
- **Create:** admin adds a service (`/admin/services/add`).
- **Read:** public list with keyword search and category filter (`/services?q=&category=`).
- **Update:** edit name, price, duration, description, extra detail (`/admin/services/edit?id=`).
- **Delete:** `/admin/services/delete` (with confirmation).
- **OOP:** abstract `Service`; `HairService`, `SkinService`, `MakeupService` override `calculatePrice()`:
  - Hair: base + LKR 500 (medium hair) or + LKR 1000 (long hair)
  - Skin: base + 10% product charge
  - Makeup: base × 1.2 (party) or × 1.5 (bridal)

### 2.3 Stylist Management (`stylists.txt`)
- **Create:** admin registers a stylist (`/admin/stylists/add`).
- **Read:** public list with search by name and specialty (`/stylists?q=&specialty=`).
- **Update:** edit profile and schedule (working days, shift start/end) (`/admin/stylists/edit?id=`).
- **Delete:** `/admin/stylists/delete`.
- **OOP:** abstract `Stylist`; `SeniorStylist` (20%) and `JuniorStylist` (10%, max LKR 3000) override `calculateCommission()`.
  The commission is shown to admins on the stylist list and on invoices.

### 2.4 Appointment Booking (`appointments.txt`)
- **Create:** book an appointment (`/appointments/book`). The page checks the free slots first.
- **Read:** "My Appointments" for customers, all appointments for admins, with a status filter (`/appointments/my`).
- **Update:** reschedule to a new date/slot (`/appointments/reschedule?id=`).
- **Delete:** cancel (`/appointments/cancel`). The record stays with status `CANCELLED` so history is kept, and the slot becomes free.
- **Rules:** no past dates; no double-booking (same stylist, date and slot); the stylist must work that day and the
  slot must be inside the shift; the stylist's specialty must match the service category.
- **OOP:**
  - `Appointment` uses encapsulation (setters reject negative prices and unknown statuses).
  - `SlotChecker` interface (abstraction) is implemented by `AppointmentService`.
  - `DiscountPolicy` interface with `RegularDiscount` (0%) and `PremiumDiscount` (10%): polymorphic discount.

### 2.5 Payment and Billing (`payments.txt`)
- **Create:** pay for an appointment by card or cash; this creates the invoice (`/payments/pay?appointmentId=`).
- **Read:** invoice view (`/payments/invoice?id=`) and payment history (`/payments/history`).
- **Update:** admin changes the status to PENDING, PAID or VOIDED (`/admin/payments/status`).
- **Delete:** admin deletes a record **only if it is VOIDED** (`/admin/payments/delete`).
- **OOP:** abstract `Payment`; `CashPayment` and `CardPayment` override `processPayment()` and `getDetails()`.
  Cash checks that enough money was given and calculates the change. Card checks the holder and card number,
  and stores only the last 4 digits (the full number and CVV are never saved).

### 2.6 Feedback and Reviews (`reviews.txt`)
- **Create:** customer submits a review for a service or a stylist (`/reviews/submit`).
- **Read:** reviews per service or stylist with the average rating (`/reviews?type=&id=`).
- **Update:** the author edits their review (`/reviews/edit?id=`); admin hides/shows reviews (`/admin/reviews/status`).
- **Delete:** the author or an admin (`/reviews/delete`).
- **OOP:** abstract `Review`; `PublicReview` and `VerifiedReview` override `getBadge()`; `VerifiedReview` also overrides
  `getDisplayName(boolean forAdmin)`. Admins see the full name, customer ID and booking ID; customers see "Nimali P.".
  A review becomes *verified* automatically when the customer has a COMPLETED appointment for that service or stylist.

### 2.7 Common UI and admin dashboard
- `index.jsp` home page, shared `header.jsp`, `navbar.jsp` (changes with the user role) and `footer.jsp`.
- Pink and gold salon theme in `css/style.css`; `js/main.js` asks for confirmation before delete/cancel and blocks past dates in date pickers.
- Admin dashboard (`/admin/dashboard`): customers, services, stylists, upcoming appointments, revenue, pending payments, review stats and latest feedback.

## 3. OOP concepts used

| Concept | Where (class names) |
|---------|---------------------|
| **Encapsulation** | All model classes have private fields with getters/setters. Setters protect data: `Appointment.setStatus()`, `Appointment.setFinalPrice()`, `Review.setRating()` (1–5), `Customer.setMembership()`, `HairService.setExtraDetail()`, `Payment.setStatus()`. `CardPayment` keeps only the last 4 card digits. |
| **Inheritance** | `Customer`, `AdminUser` extend `User`; `HairService`, `SkinService`, `MakeupService` extend `Service`; `SeniorStylist`, `JuniorStylist` extend `Stylist`; `CashPayment`, `CardPayment` extend `Payment`; `PublicReview`, `VerifiedReview` extend `Review`. |
| **Abstraction** | Abstract classes `User`, `Service`, `Stylist`, `Payment`, `Review`. Interfaces `SlotChecker` and `DiscountPolicy`. |
| **Polymorphism (overriding)** | `canAccessAdmin()` / `getHomePage()` (users), `calculatePrice()` (services), `calculateCommission()` (stylists), `calculateDiscount()` (discount policies), `processPayment()` / `getDetails()` (payments), `getBadge()` / `getDisplayName()` (reviews). |
| **Polymorphism (dynamic binding)** | `User user = userService.login(...)` then `user.getHomePage()`; `Payment payment = new CashPayment(...)` or `new CardPayment(...)` then `payment.processPayment()`; `DiscountPolicy policy = new PremiumDiscount()`. |
| **Interface implementation** | `AppointmentService implements SlotChecker`; `RegularDiscount`, `PremiumDiscount implement DiscountPolicy`. |
| **Static factory methods** | `User.fromFileString()`, `Service.create()`, `Stylist.create()`, `Payment.fromFileString()`, `Review.fromFileString()` pick the correct subclass. |

## 4. File formats

All files are UTF-8 text, one record per line, fields separated by `|`.
`Validator.clean()` replaces any `|` typed by a user with `/` so a record can never be broken by user input.

| File | Format | Example |
|------|--------|---------|
| `users.txt` | `userId\|role\|fullName\|email\|phone\|password\|membership-or-position` | `C001\|CUSTOMER\|Nimali Perera\|nimali@gmail.com\|0771234567\|nimali123\|PREMIUM` |
| `services.txt` | `serviceId\|category\|name\|basePrice\|durationMinutes\|description\|extra` | `S001\|HAIR\|Ladies Haircut and Blow Dry\|2500.0\|45\|Wash, cut...\|MEDIUM` |
| `stylists.txt` | `stylistId\|level\|name\|phone\|email\|specialty\|workingDays\|shiftStart\|shiftEnd\|experienceYears` | `ST001\|SENIOR\|Chamari Atapattu\|0771239876\|chamari@glambook.lk\|HAIR\|MON,TUE,WED,THU,FRI,SAT\|09:00\|17:00\|12` |
| `appointments.txt` | `appointmentId\|customerId\|serviceId\|stylistId\|date\|timeSlot\|originalPrice\|discount\|finalPrice\|status` | `A007\|C001\|S002\|ST001\|2026-10-13\|11:00\|9500.0\|950.0\|8550.0\|BOOKED` |
| `payments.txt` | `paymentId\|method\|appointmentId\|customerId\|amount\|paymentDate\|status\|extra1\|extra2` | `P003\|CASH\|A002\|C002\|3000.0\|2026-09-18\|PAID\|5000.0\|2000.0` |
| `reviews.txt` | `reviewId\|type\|customerId\|customerName\|targetType\|targetId\|rating\|comment\|reviewDate\|status\|appointmentId` | `R001\|VERIFIED\|C001\|Nimali Perera\|SERVICE\|S005\|5\|The gold facial...\|2026-09-15\|VISIBLE\|A001` |

**Payment extras:** CASH → amount tendered, change given. CARD → card holder, last 4 digits.

**ID prefixes** (`IdGenerator`): `C` customer, `AD` admin, `S` service, `ST` stylist, `A` appointment, `P` payment, `R` review.
The next ID is the highest existing number + 1, padded to three digits (`C008` → `C009`).

### How the DAO works
1. `FileHandler.readLines()` reads the whole file into an `ArrayList<String>` with a `BufferedReader` (try-with-resources).
   A missing file returns an empty list.
2. Each line is turned into an object with `fromFileString()`. A broken line returns `null` and is skipped, so one bad line never crashes the page.
3. **Create** appends one line (`FileHandler.appendLine()`, `FileWriter` in append mode).
4. **Update / Delete** change the list in memory and then rewrite the whole file (`FileHandler.writeLines()`).
5. `FileHandler` methods are `synchronized` so two requests cannot write the same file at the same time.

## 5. Validation (server side)

`Validator` + the service classes check:
- required fields are not empty
- email format (`name@domain.tld`), unique email on register/update
- password at least 6 characters, password and confirm password match
- Sri Lankan phone number: 10 digits starting with 0
- price > 0, duration 1–480 minutes, experience 0–50 years, rating 1–5, comment ≤ 500 characters
- appointment date is valid and not in the past; slot is free; stylist works that day and time
- card number 16 digits, expiry MM/YY not expired, CVV 3 digits; cash tendered ≥ amount

Errors are forwarded back to the same JSP and shown in a red Bootstrap alert.

## 6. How to run on Tomcat

See `README.md` for full steps. Short version:

1. Install JDK 17 and Apache Tomcat 10.1.
2. `mvn clean package` → creates `target/glambook.war`.
3. Copy `glambook.war` into `<tomcat>/webapps/` and start Tomcat (`bin/startup.bat`), **or** run it from IntelliJ with a Tomcat run configuration.
4. Open `http://localhost:8080/glambook/`.
5. Data is stored in `C:\Users\<you>\glambook-data` by default (sample data is copied there on first start).
   Change it with the JVM option `-Dglambook.data.dir=<folder>` or the `glambook.data.dir` context-param in `web.xml`.

**Demo logins:** admin `admin@glambook.lk` / `admin123`; customer `nimali@gmail.com` / `nimali123` (Premium).

## 7. Limitations and possible improvements
- Passwords are stored as plain text to keep the file format easy to explain; a real system would hash them (e.g. BCrypt).
- Each appointment takes one one-hour slot, even for long services such as bridal makeup.
- Text files are fine for a small salon; a database would be needed for many users at once.
