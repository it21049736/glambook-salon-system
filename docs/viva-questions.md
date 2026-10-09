# GlamBook – Viva Questions and Answers

Short answers focused on the backend Java code and file handling.

---

## 1. User Management

**1. Why is `User` an abstract class?**
A plain "user" never exists in the salon: every account is either a customer or an admin. Making `User` abstract stops anyone from writing `new User(...)` and forces each subclass to implement `getRole()`, `canAccessAdmin()` and `getHomePage()`.

**2. Where is polymorphism used in login?**
`UserService.login()` returns a `User`. `LoginServlet` then calls `user.getHomePage()`. At runtime Java calls `Customer.getHomePage()` (`/appointments/my`) or `AdminUser.getHomePage()` (`/admin/dashboard`). This is dynamic binding.

**3. How does role-based authentication work?**
`AuthFilter` runs before every `/admin/*` URL. It reads the user from the session and calls `user.canAccessAdmin()`. `Customer` returns `false` and `AdminUser` returns `true`, so customers are redirected to the home page.

**4. How does `User.fromFileString()` know which object to create?**
It splits the line with `split("\\|", -1)` and checks the second field (role). `ADMIN` creates an `AdminUser`, `CUSTOMER` creates a `Customer`. Any other value, or a broken line, returns `null`.

**5. What happens if `users.txt` has a broken line?**
`fromFileString()` catches the exception and returns `null`. `UserDAO.getAll()` only adds non-null users, so the bad line is skipped and the page still works.

**6. How is a user updated in the file?**
`UserDAO.update()` reads all users into an `ArrayList`, replaces the one with the same ID using `list.set(i, user)`, then `saveAll()` rewrites the whole file with `FileHandler.writeLines()`.

**7. How do you stop two accounts with the same email?**
`UserService.register()` calls `userDAO.findByEmail()`. If a user is found it returns the error "An account with this email already exists." The check uses `equalsIgnoreCase`.

**8. What is stored in the session and why?**
The logged-in `User` object, under the key `loggedUser`. Servlets and JSPs use it to know who is logged in and what role they have. Logout calls `session.invalidate()`.

**9. Why redirect after a successful registration instead of forwarding?**
Post/Redirect/Get. If we forwarded, refreshing the browser would send the POST again and try to register twice. A redirect makes the browser load `/login` with a GET.

**10. Which validations are done on register?**
Empty fields, email format (regex), phone number (10 digits starting with 0), password length (at least 6), password matches the confirmation, and a unique email. They are in `Validator` and `UserService`.

---

## 2. Service Management

**1. What does `calculatePrice()` do in each subclass?**
`HairService`: base price + LKR 500 for medium hair or + LKR 1000 for long hair. `SkinService`: base + 10% product charge. `MakeupService`: × 1.2 for party, × 1.5 for bridal, otherwise the base price.

**2. Why is `calculatePrice()` abstract in `Service`?**
Every service has a price, but the rule is different for each type. Making it abstract forces each subclass to provide its own rule (method overriding).

**3. How does the JSP show the right price for each service?**
`service-list.jsp` calls `${s.calculatePrice()}` on every item in a `List<Service>`. Java runs the subclass version for each object, which is polymorphism.

**4. What is `Service.create()`?**
A static factory method. It takes the category text (`HAIR`, `SKIN`, `MAKEUP`) and returns the right subclass. Both `fromFileString()` and `ServiceManager.addService()` use it.

**5. Why is the service layer class called `ServiceManager`?**
The model class is already called `Service`, so the business class gets a different name to avoid confusion.

**6. How do search and filter work?**
`ServiceManager.searchServices(keyword, category)` loops through all services and keeps those whose name or description contains the keyword (lower case) and whose category matches (or no category was chosen).

**7. How is a new service ID created?**
`IdGenerator.generateId("S", serviceDAO.getAllIds())` finds the highest number among IDs starting with `S`, adds 1 and pads it: `S010` → `S011`. IDs like `ST001` are skipped because `"T001"` is not a number.

**8. What validation is done when adding or editing?**
Name, price and duration are required; price must be a number > 0; duration must be a whole number from 1 to 480 minutes.

**9. How is a service deleted from the file?**
`ServiceDAO.delete()` reads all services, removes the matching one with `removeIf()` and rewrites the file.

**10. How do you stop a user from adding a service?**
The URL `/admin/services/add` starts with `/admin`, so `AuthFilter` blocks anyone whose `canAccessAdmin()` is false. The JSP also hides the buttons for non-admins.

---

## 3. Stylist Management

**1. What is the difference between `SeniorStylist` and `JuniorStylist`?**
They override `calculateCommission()`. Senior gets 20% of the service amount. Junior gets 10%, with a maximum of LKR 3000 (`Math.min`).

**2. How is the stylist schedule stored?**
As `workingDays` (for example `MON,TUE,WED`) plus `shiftStart` and `shiftEnd` in `HH:mm` format, all in one line of `stylists.txt`.

**3. What does `worksOn(DayOfWeek day)` do?**
It takes the first three letters of the day name (`MONDAY` → `MON`) and checks whether `workingDays` contains it.

**4. What does `isWithinShift(String time)` check?**
It parses the times with `LocalTime.parse()` and returns true when the slot is at or after the shift start and before the shift end.

**5. How can the level be changed when editing a stylist?**
`StylistService.updateStylist()` creates a new object with `Stylist.create(level, ...)` using the same ID, so a Junior can become a `SeniorStylist`. The DAO then replaces the old object in the list.

**6. How are working day checkboxes read in the servlet?**
`request.getParameterValues("workingDays")` returns a `String[]`, which is joined with `String.join(",", days)`.

**7. How do you search by name or specialty?**
`StylistService.searchStylists(keyword, specialty)` checks `name.toLowerCase().contains(keyword)` and `specialty.equals(...)`.

**8. Which validations are done for a stylist?**
Name and specialty are required, valid phone and email, at least one working day, shift end after shift start, and experience from 0 to 50 years.

**9. Why does the stylist specialty use the same values as service categories?**
So the booking can check that the stylist does that type of service (`stylist.getSpecialty().equals(service.getCategory())`).

**10. Where is the commission used?**
On the stylist list (for admins) and on the invoice, where `stylist.calculateCommission(payment.amount)` shows what the stylist earns.

---

## 4. Appointment Booking

**1. How do you prevent double-booking?**
`AppointmentService.isSlotAvailable()` loops through all appointments. If another active (BOOKED or RESCHEDULED) appointment has the same stylist, date and time slot, the slot is not available and booking returns an error.

**2. How do you stop bookings in the past?**
`Validator.isNotPastDate(date)` returns false if `date.isBefore(LocalDate.now())`. Today's slots that have already passed are also removed in `getAvailableSlots()`. The date picker also has a `min` value, but the server check is the real protection.

**3. What is the `SlotChecker` interface and who implements it?**
An interface with `isSlotAvailable()`, `getAvailableSlots()` and the constant `TIME_SLOTS`. `AppointmentService` implements it. This is abstraction: the interface says what is needed, the class decides how.

**4. Explain the polymorphic discount.**
`DiscountPolicy` is an interface with `calculateDiscount(amount)`. `RegularDiscount` returns 0 and `PremiumDiscount` returns 10%. The service picks one based on `customer.isPremium()` and calls `policy.calculateDiscount(price)` without knowing which class it is.

**5. Why does `getAvailableSlots()` take an `ignoreAppointmentId`?**
When rescheduling, the appointment's own current slot should count as free, so we skip the appointment with that ID during the check.

**6. Why is a cancelled appointment not deleted from the file?**
We keep it for history and reports. Setting the status to `CANCELLED` makes `isActive()` false, so the slot becomes free again.

**7. How is encapsulation shown in `Appointment`?**
All fields are private. `setStatus()` only accepts the four known statuses (anything else becomes BOOKED), and the price setters use `Math.max(0, value)` so prices can never be negative.

**8. What does `isChangeable()` mean?**
The appointment is active and its date is today or later. Only changeable appointments show the Reschedule and Cancel buttons, and the service checks it again.

**9. How is the date stored and read?**
As ISO text (`2026-10-13`). `toFileString()` uses `LocalDate.toString()` and `fromFileString()` uses `LocalDate.parse()`.

**10. Who can reschedule or cancel an appointment?**
`canManage(user, appointment)`: the customer who owns it, or any admin (`user.canAccessAdmin()`).

---

## 5. Payment and Billing

**1. Why is `Payment` abstract?**
Every payment has an ID, amount, date and status, but processing is different for cash and card. `processPayment()`, `getMethod()` and `getDetails()` are abstract, so each subclass must implement them.

**2. What does `CashPayment.processPayment()` do?**
If the amount tendered is less than the bill it returns false. Otherwise it calculates the change (`tendered - amount`), sets the status to PAID and returns true.

**3. What does `CardPayment.processPayment()` do?**
It checks that the card holder and the last 4 digits are present, then sets the status to PAID. The full card number is validated in `PaymentService` (16 digits, expiry MM/YY not expired, CVV 3 digits).

**4. Is the full card number saved?**
No. `CardPayment.setCardNumber()` keeps only the last 4 digits, and the CVV is never stored. Only the holder name and last 4 digits go into `payments.txt`.

**5. Where is polymorphism used in payments?**
In `PaymentService.pay()` a `Payment` variable holds either a `CashPayment` or a `CardPayment`, and `payment.processPayment()` runs the correct version. The invoice also calls `payment.getDetails()`, which is different for each type.

**6. Why can only VOIDED payments be deleted?**
So a real payment record is never lost by mistake. `PaymentService.deleteVoided()` returns an error if the status is not VOIDED.

**7. How does `fromFileString()` read the extra fields?**
Columns 8 and 9 mean different things per method. For CASH they are parsed as doubles (tendered and change). For CARD they are strings (holder and last 4 digits).

**8. What happens to the appointment after payment?**
`appointmentService.markCompleted()` sets its status to COMPLETED, so it cannot be paid twice. A completed appointment also lets the customer write a Verified review.

**9. Why does `pay()` throw `IllegalArgumentException` instead of returning an error string?**
It needs to return the new payment ID so the servlet can redirect to the invoice. Errors are thrown as exceptions with a user-friendly message, which the servlet catches and shows on the form.

**10. How does a customer stop seeing other customers' invoices?**
`InvoiceServlet` calls `paymentService.canView(user, payment)`. Only admins or the customer who owns the payment can open it; everyone else is redirected.

---

## 6. Feedback and Reviews

**1. What is the difference between `PublicReview` and `VerifiedReview`?**
A `VerifiedReview` is created when the customer has a COMPLETED appointment for that service or stylist, and it stores that `appointmentId`. Otherwise a `PublicReview` is created. They show different badges (`getBadge()`).

**2. How is "different display for admin vs user" done?**
`Review.getDisplayName(boolean forAdmin)`: admins see the full name with the customer ID; users see the first name and last initial ("Nimali P."). `VerifiedReview` overrides it to also show the booking ID to admins.

**3. How does the system decide whether a review is verified?**
`ReviewService.findCompletedAppointment()` loops through the customer's appointments and looks for one with status COMPLETED and the same service ID or stylist ID.

**4. Can a customer review the same service twice?**
No. `submitReview()` checks `findByTarget()` for a review with the same customer ID and returns "You have already reviewed this. You can edit your review instead."

**5. Who can edit and who can delete a review?**
Only the author can edit. The author or any admin can delete (`deleteReview()` checks `user.canAccessAdmin()` or ownership).

**6. What does "hide" do in the moderation panel?**
It sets the status to HIDDEN and rewrites the file. `getVisibleReviews()` skips hidden reviews, so customers no longer see them, but admins still do.

**7. How is the rating kept between 1 and 5?**
The service rejects values outside 1–5, and `Review.setRating()` also clamps it with `Math.max(1, Math.min(5, rating))` (encapsulation).

**8. How is the average rating calculated?**
`getAverageRating()` adds all ratings into an `int` total and divides by the list size, casting to `double` to keep the decimal part.

**9. What if a comment contains the `|` character?**
`Validator.clean()` replaces `|` with `/` and removes new lines before saving, so the comment can never break the file format.

**10. How does the review form send both the type and the ID?**
The select box value is `SERVICE:S001` or `STYLIST:ST001`. `SubmitReviewServlet` splits it on `:` into the target type and target ID.

---

## General / file handling (bonus)

- **Why `BufferedReader`/`BufferedWriter`?** They read and write in blocks, which is faster than one character at a time, and `readLine()` makes line-based records easy.
- **What is try-with-resources?** `try (BufferedReader r = ...) { }` closes the file automatically, even if an exception happens.
- **Why `synchronized` in `FileHandler`?** Tomcat serves many requests at once with threads. `synchronized` lets only one thread read or write a file at a time, so updates are not lost.
- **Why `split("\\|", -1)`?** `|` is a special regex character, so it is escaped. The `-1` keeps empty fields at the end of the line.
- **Where is the data folder?** `DataPath`: default `<user home>/glambook-data`. It can be changed with `-Dglambook.data.dir` or the `web.xml` context-param. `AppStartupListener` copies the sample files there on first start.
