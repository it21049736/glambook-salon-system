# GlamBook - Beauty Salon Appointment Booking System

GlamBook is a Java web application for a beauty salon. Customers register, browse services and
stylists, book a free time slot, pay online and leave reviews. Salon admins manage users, services,
stylists, payments and reviews from a dashboard.

There is **no database**: every record is stored as one line in a pipe-delimited `.txt` file and
read/written with Java file I/O.

SE1020 Object Oriented Programming project.

## Features

| Component | What it does |
|-----------|--------------|
| **User Management** | Register, login/logout with sessions, update profile, delete account, admin user search and delete. Regular and Premium memberships. |
| **Service Management** | Add, search/filter by category, edit price/duration, delete. Hair, Skin and Makeup services calculate their price differently. |
| **Stylist Management** | Register stylists, search by name or specialty, update profile and weekly schedule, remove. Senior/Junior commission rules. |
| **Appointment Booking** | Check free slots, book, view, reschedule and cancel. Blocks past dates and double-booking. 10% discount for Premium customers. |
| **Payment and Billing** | Pay by card or cash, printable invoice, payment history, admin status updates, delete voided records only. |
| **Feedback and Reviews** | Submit, view per service or stylist with average rating, edit, delete. Verified reviews for completed visits and an admin moderation panel. |
| **Common** | Home page, shared navbar and footer, admin dashboard, role-based access, confirmation before delete, responsive pink and gold Bootstrap theme. |

## Tech stack

- Java 17
- Jakarta Servlets 6, JSP, JSTL 3
- Apache Tomcat 10.1
- Maven (war packaging)
- Bootstrap 5 and Bootstrap Icons (CDN)
- Plain `.txt` files for storage

## Project structure

```
glambook-salon-system/
├── pom.xml
├── data/                         sample data (copied to the data folder on first run)
│   ├── users.txt  services.txt  stylists.txt
│   └── appointments.txt  payments.txt  reviews.txt
├── docs/                         class diagram, report notes, viva questions, test steps
└── src/main/
    ├── java/com/glambook/
    │   ├── model/                User, Customer, AdminUser, Service, HairService, ... Review
    │   ├── dao/                  FileHandler + one DAO per entity
    │   ├── service/              business logic and validation
    │   ├── servlet/
    │   │   ├── common/           AuthFilter, SessionHelper, AppStartupListener
    │   │   ├── user/  service/  stylist/  appointment/  payment/  review/
    │   │   └── admin/            AdminDashboardServlet
    │   └── util/                 IdGenerator, Validator, DataPath
    └── webapp/
        ├── WEB-INF/web.xml
        ├── includes/             header.jsp, navbar.jsp, footer.jsp
        ├── css/  js/
        ├── index.jsp  error.jsp
        └── user/  service/  stylist/  appointment/  payment/  review/  admin/
```

Request flow: **JSP form → Servlet → Service → DAO → .txt file → Servlet → JSP**

## How to run in IntelliJ IDEA with Tomcat 10

### 1. Requirements
- JDK 17
- Apache Tomcat **10.1** (download the zip from https://tomcat.apache.org and extract it, e.g. to `C:\tomcat10`)
- IntelliJ IDEA (Ultimate has built-in Tomcat support; for Community see option B)

### 2. Open the project
1. `File → Open…` and choose the `glambook-salon-system` folder.
2. IntelliJ detects `pom.xml` and imports the Maven project. Set the Project SDK to Java 17
   (`File → Project Structure → Project`).

### Option A – IntelliJ Ultimate (Tomcat run configuration)
1. `Run → Edit Configurations… → + → Tomcat Server → Local`.
2. **Server** tab: next to *Application server* click **Configure…** and select your Tomcat 10.1 folder.
3. **Deployment** tab: click **+ → Artifact… → glambook-salon-system:war exploded**.
   Set *Application context* to `/glambook`.
4. Click **Run**. The browser opens `http://localhost:8080/glambook/`.

### Option B – IntelliJ Community (or without IntelliJ)
1. Build the war: open the Maven tool window and run `clean` then `package`, or run `mvn clean package` in a terminal.
2. Copy `target/glambook.war` into `<tomcat>/webapps/`.
3. Start Tomcat with `<tomcat>/bin/startup.bat` (Windows) or `startup.sh` (Linux/Mac).
4. Open `http://localhost:8080/glambook/`.

(The *Smart Tomcat* plugin can also run the project from IntelliJ Community.)

### 3. Log in
| Role | Email | Password |
|------|-------|----------|
| Admin | admin@glambook.lk | admin123 |
| Customer (Premium) | nimali@gmail.com | nimali123 |
| Customer (Regular) | tharushi.f@gmail.com | tharu123 |

## Where the data is stored

On start-up the app uses this folder for all `.txt` files:

1. the JVM option `-Dglambook.data.dir=<folder>`, if it is set
2. otherwise the `glambook.data.dir` context-param in `WEB-INF/web.xml`, if it is not empty
3. otherwise the default folder **`<user home>/glambook-data`** (for example `C:\Users\<you>\glambook-data`)

If a data file is missing, the sample file from `data/` (packaged into the war) is copied there.
The path is printed in the Tomcat console: `GlamBook data folder: ...`.

- **Reset the demo data:** stop Tomcat, delete the data folder, start again.
- **Use the project's `data/` folder directly:** in the Tomcat run configuration add the VM option
  `-Dglambook.data.dir=C:\path\to\glambook-salon-system\data`. The app will then edit the files in the project.

## File formats

| File | Fields (separated by `|`) |
|------|---------------------------|
| users.txt | userId, role, fullName, email, phone, password, membership/position |
| services.txt | serviceId, category, name, basePrice, durationMinutes, description, extra |
| stylists.txt | stylistId, level, name, phone, email, specialty, workingDays, shiftStart, shiftEnd, experienceYears |
| appointments.txt | appointmentId, customerId, serviceId, stylistId, date, timeSlot, originalPrice, discount, finalPrice, status |
| payments.txt | paymentId, method, appointmentId, customerId, amount, paymentDate, status, extra1, extra2 |
| reviews.txt | reviewId, type, customerId, customerName, targetType, targetId, rating, comment, reviewDate, status, appointmentId |

## Documentation

- [docs/class-diagram.puml](docs/class-diagram.puml) – PlantUML class diagram
- [docs/report-notes.md](docs/report-notes.md) – overview, components, OOP concepts, file formats
- [docs/test-steps.md](docs/test-steps.md) – step-by-step demo of every CRUD operation
- [docs/viva-questions.md](docs/viva-questions.md) – likely viva questions with answers
- [docs/commit-history.md](docs/commit-history.md) – git history of the project
