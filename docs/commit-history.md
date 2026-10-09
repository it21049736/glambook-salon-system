# GlamBook – Commit History

Output of `git log --oneline --graph --all` for the project.
The project was built with one feature branch per component; each branch was merged into
`main` with a merge commit (`git merge --no-ff`) after it built successfully with `mvn clean package`.

Regenerate at any time with:

```
git log --oneline --graph --all
```

## Branches

- `docs/documentation`
- `feature/appointment`
- `feature/payment`
- `feature/review`
- `feature/service-management`
- `feature/stylist-management`
- `feature/ui-common`
- `feature/user-management`
- `main`

## Log

```
* 30a4139 docs: update README with setup guide and structure
* c4b776f docs: add demo test steps for every CRUD operation
* c80d935 docs: add viva questions for all components
* 86a5bd1 docs: add report notes with OOP concepts and file formats
* a569ad7 docs: add PlantUML class diagram
*   ee1c38c Merge branch 'feature/review' into main
|\  
| * 813af9c data(review): add sample reviews
| * 2ed33f8 feat(admin): complete dashboard with reviews and quick actions
| * 68bc2b7 feat(review): add admin moderation panel
| * e932a97 feat(review): add submit and edit review forms
| * ae58102 feat(review): add review list page with ratings
| * db04906 feat(review): add admin moderation servlets
| * b4a733d feat(review): add review list, submit, edit and delete servlets
| * 569e02a feat(review): add ReviewService with verified review detection
| * a1328d0 feat(review): add ReviewDAO for reviews.txt
| * f24b639 feat(review): add PublicReview and VerifiedReview subclasses
| * af5cde2 feat(review): add Review base model with admin and user display
|/  
*   3751598 Merge branch 'feature/payment' into main
|\  
| * e3ad451 data(payment): add sample payments and paid appointments
| * fdb0f56 feat(admin): show revenue and pending payments on dashboard
| * 75f526e feat(appointment): add pay button to appointment list
| * 6908de0 feat(payment): add invoice and payment history pages
| * c023b8c feat(payment): add payment page with card and cash options
| * 7fa6ed0 feat(payment): add history, status update and voided delete servlets
| * 79d2e2b feat(payment): add pay and invoice servlets
| * 37ad80e feat(payment): add PaymentService with card checks and voided delete rule
| * 0d17b5a feat(payment): add PaymentDAO for payments.txt
| * 6d2853e feat(payment): add cash and card payments with processPayment
| * b451281 feat(payment): add abstract Payment model
|/  
*   a9b493f Merge branch 'feature/appointment' into main
|\  
| * 37d38e3 data(appointment): add sample appointments
| * 058b466 feat(admin): show upcoming appointments on dashboard
| * 12e5d8a feat(appointment): add my appointments and reschedule pages
| * 4792821 feat(appointment): add booking page with live slot list
| * 322b223 feat(appointment): add my appointments, reschedule and cancel servlets
| * 5c7cd39 feat(appointment): add booking servlet with slot check
| * 94bfeaf feat(user): add id to name lookup for other modules
| * feca51e feat(appointment): add AppointmentService with slot checks and discounts
| * 82beb43 feat(appointment): add AppointmentDAO for appointments.txt
| * b1d1838 feat(appointment): add regular and premium discount policies
| * e5187b4 feat(appointment): add SlotChecker interface for slot availability
| * 41ae334 feat(appointment): add Appointment model with status rules
|/  
*   fa93e24 Merge branch 'feature/stylist-management' into main
|\  
| * d1c9f24 data(stylist): add sample stylists
| * 3724478 feat(admin): show stylist count on dashboard
| * dce7842 feat(stylist): add stylist registration and edit forms
| * 453a44f feat(stylist): add stylist list page
| * 028804e feat(stylist): add admin add, edit and remove servlets
| * 7193e01 feat(stylist): add stylist list servlet with search
| * bdb7572 feat(stylist): add StylistService with schedule validation
| * a80fda9 feat(stylist): add StylistDAO for stylists.txt
| * cfebb40 feat(stylist): add Senior and Junior stylists with commission rules
| * a126452 feat(stylist): add abstract Stylist model with schedule helpers
|/  
*   1a1d192 Merge branch 'feature/service-management' into main
|\  
| * 9357287 fix(ui): set default locale so prices are always formatted
| * 3359954 data(service): add sample salon services
| * 60a4bba feat(admin): show service count on dashboard
| * 23e0c84 feat(service): add add and edit service forms
| * 46785ba feat(service): add service list page with search and filter
| * 5c0198f feat(service): add admin add, edit and delete servlets
| * a89484b feat(service): add public service list servlet with filter
| * 16634d4 feat(service): add ServiceManager with search and validation
| * e9b1d36 feat(service): add ServiceDAO for services.txt
| * 6bd0ea3 feat(service): add Hair, Skin and Makeup service subclasses
| * 1258884 feat(service): add abstract Service model
|/  
*   4ddeded Merge branch 'feature/user-management' into main
|\  
| * 6bfc72b data(user): add sample users
| * bbc7675 feat(admin): add dashboard page
| * 5f41b4e feat(user): add admin user list page
| * 9384266 feat(user): add profile page with account delete
| * 7a814aa feat(user): add register and login pages
| * 6bdbd3b feat(ui): show redirect error messages in header
| * a5d4f8e feat(admin): add dashboard servlet with user stats
| * 2b07f62 feat(user): add admin user list, search and delete
| * a6828d5 feat(user): add profile update and delete account servlets
| * d721318 feat(user): add register, login and logout servlets
| * d2413c7 feat(auth): add session helper and role based AuthFilter
| * 4ce934a feat(user): add UserService with validation and search
| * 88798f8 feat(user): add UserDAO with file based CRUD
| * 92c4c09 feat(user): add Customer and AdminUser subclasses
| * 5298465 feat(user): add abstract User model
|/  
*   ce4f90e Merge branch 'feature/ui-common' into main
|\  
| * 0fd0f45 feat(ui): add friendly error page
| * 49e95e5 feat(ui): add home page with service categories
| * fa16726 feat(ui): add role-aware navbar
| * a88a59a feat(ui): add shared header and footer includes
| * 4f87cf7 feat(ui): add delete confirmation and date picker script
| * 9937d9d style(ui): add pink and gold salon theme stylesheet
| * e0a4e13 chore(web): add web.xml with data dir param and error pages
| * 724fdb6 feat(util): add Validator for server-side checks
| * 6b10673 feat(util): add IdGenerator for prefixed record ids
| * 6516f20 feat(dao): add shared FileHandler for txt read/write
| * c997d15 feat(common): add configurable data folder and startup listener
|/  
* 893fb5c chore: initial project setup with maven and gitignore
```
