# GlamBook – Demo / Test Steps

Base URL: `http://localhost:8080/glambook/`

**Accounts in the sample data**

| Role | Email | Password | Notes |
|------|-------|----------|-------|
| Admin | admin@glambook.lk | admin123 | Salon Manager |
| Customer | nimali@gmail.com | nimali123 | PREMIUM (10% discount) |
| Customer | tharushi.f@gmail.com | tharu123 | REGULAR |
| Customer | ishara.g@gmail.com | ishara123 | REGULAR, has a completed haircut (A004) |

> **Tip:** to reset to the original sample data, stop Tomcat, delete the data folder
> (`C:\Users\<you>\glambook-data`) and start again. The sample files are copied back automatically.
>
> The sample appointments have dates around October 2026. If you demo later, use a future date when booking.
> Appointments that are already in the past cannot be rescheduled or cancelled, which is correct behaviour.

---

## 0. Home page and navigation
1. Open the base URL. The home page shows the hero section and the three service categories.
2. Click **Services**, **Stylists**, **Reviews** in the navbar. These pages work without logging in.
3. Open a page that does not exist (`/glambook/abc`) and check that the friendly error page appears.

## 1. User Management
| # | Operation | Steps | Expected result |
|---|-----------|-------|-----------------|
| 1.1 | Validation | Register with email `abc`, phone `123`, password `1` | Red error message; typed values stay in the form |
| 1.2 | **Create** (register) | Register: `Sanduni Weerasinghe`, `sanduni@gmail.com`, `0771112233`, password `sanduni123` (twice), membership Premium | Redirect to login with "Registration successful". New line `C009\|CUSTOMER\|...` in `users.txt` |
| 1.3 | Duplicate email | Register again with `sanduni@gmail.com` | "An account with this email already exists." |
| 1.4 | Login / session | Log in as Sanduni | Redirect to My Appointments; name shown in the navbar |
| 1.5 | **Update** | Profile → change phone to `0719998887`, membership Regular → Save | "Profile updated"; the line in `users.txt` changes |
| 1.6 | Role check | While logged in as a customer, open `/glambook/admin/users` | Redirect to home with "Only salon admins can open that page." |
| 1.7 | Logout | Click name → Logout | Back to login with "You have been logged out." |
| 1.8 | **Read / search** (admin) | Log in as admin → Admin → Users → search `nimali` | Only Nimali Perera is listed |
| 1.9 | **Delete** (admin) | Click Delete next to `C009` → confirm the popup | "User C009 was deleted"; the line is removed from `users.txt` |
| 1.10 | **Delete** own account | Register a test customer, log in, Profile → Delete my account → confirm | Logged out; account removed |

## 2. Service Management (log in as admin)
| # | Operation | Steps | Expected result |
|---|-----------|-------|-----------------|
| 2.1 | **Read** + filter | Services → choose category **Skin** → Search | Only skin services; prices include the 10% product charge (Gold Radiance Facial 6,500 → LKR 7,150.00) |
| 2.2 | Search | Type `bridal` → Search | Bridal Makeup (Kandyan) only, price LKR 52,500.00 (35,000 × 1.5) |
| 2.3 | Validation | Add Service with price `-5` | "Price must be a number greater than 0." |
| 2.4 | **Create** | Add Service: Makeup, `Engagement Makeup`, 10000, 90 min, occasion Party | Redirect to list; card shows LKR 12,000.00; new line `S011` |
| 2.5 | **Update** | Edit `S001`: price 2800, duration 50, hair length Long | Price shown becomes LKR 3,800.00 (2800 + 1000) |
| 2.6 | **Delete** | Click the bin icon on `S011` → confirm | "Service S011 was deleted" |

## 3. Stylist Management (log in as admin)
| # | Operation | Steps | Expected result |
|---|-----------|-------|-----------------|
| 3.1 | **Read** / search | Stylists → specialty **Makeup** | Shanika (SENIOR) and Yasara (JUNIOR); admin sees commission 20% / 10% |
| 3.2 | Search by name | Type `chamari` | Only Chamari Atapattu |
| 3.3 | Validation | Add Stylist with shift start 17:00 and end 09:00 | "Shift end time must be after the start time."; ticked days stay ticked |
| 3.4 | **Create** | Add `Kumudu Jayasuriya`, `0771234567`, `kumudu@glambook.lk`, Senior, Skin, MON + FRI, 09:00–15:00, 6 years | New stylist `ST009` in the list |
| 3.5 | **Update schedule** | Edit `ST009`: level Junior, days SAT only, 10:00–14:00 | Card shows JUNIOR, SAT, 10:00 - 14:00 |
| 3.6 | **Delete** | Remove `ST009` → confirm | "Stylist ST009 was removed" |

## 4. Appointment Booking (log in as `nimali@gmail.com`)
| # | Operation | Steps | Expected result |
|---|-----------|-------|-----------------|
| 4.1 | **Check slots** | Book Now → service *Ladies Haircut and Blow Dry* → stylist *Chamari Atapattu* → date `2026-10-13` → **Check available slots** | Slots 09:00–16:00 except **11:00** (already taken by A007) |
| 4.2 | Stylist day off | Same, but date `2026-10-11` (Sunday) | "Chamari Atapattu works on MON,TUE,WED,THU,FRI,SAT only." |
| 4.3 | Past date | Choose a date before today | "That date is in the past..." |
| 4.4 | **Create** (book) | Back to 4.1, pick **10:00** → Confirm booking | Redirect to My Appointments; new appointment with price 3,000, saved 300 (Premium 10%), final 2,700 |
| 4.5 | No double-booking | Log in as `tharushi.f@gmail.com`, try the same stylist/date | 10:00 and 11:00 are no longer offered |
| 4.6 | **Update** (reschedule) | As Nimali, click Reschedule on the new appointment → date `2026-10-16` → Check slots → pick 14:00 → Save | Status RESCHEDULED, new date and time |
| 4.7 | **Delete** (cancel) | Click Cancel → confirm | Status CANCELLED; the slot is free again |
| 4.8 | Admin view | Log in as admin → Appointments → filter BOOKED | All customers' booked appointments with customer names |

## 5. Payment and Billing
| # | Operation | Steps | Expected result |
|---|-----------|-------|-----------------|
| 5.1 | Card validation | As Nimali, Appointments → **Pay** on A007 → Card, number `1234` | "Card number must have 16 digits." |
| 5.2 | Cash validation | Choose Cash, enter `100` | "Payment failed. The cash amount must cover LKR 8,550.00." |
| 5.3 | **Create** (pay / invoice) | Card: `Nimali Perera`, `4111 1111 1111 1234`, `12/28`, CVV `123` → Pay | Invoice page: `Card - Nimali Perera, **** **** **** 1234`, status PAID; A007 becomes COMPLETED |
| 5.4 | **Read** history | Payments in the navbar | Nimali sees only her own payments |
| 5.5 | Invoice privacy | As Nimali, open `/glambook/payments/invoice?id=P003` | Redirect with "Invoice not found" (belongs to another customer) |
| 5.6 | Admin invoice | As admin, open invoice P005 | Shows stylist commission (Senior 20%) |
| 5.7 | Delete rule | As admin, open Payments and look at the PAID and PENDING rows | No delete button on them; only the VOIDED record (P002) has one. The server also refuses: "Only voided payments can be deleted." |
| 5.8 | **Update** status | Set P009 to **PAID** → Update | "Payment P009 is now PAID"; revenue on dashboard increases |
| 5.9 | **Delete** voided | Click the bin on P002 (VOIDED) → confirm | "Voided payment P002 was deleted" |

## 6. Feedback and Reviews
| # | Operation | Steps | Expected result |
|---|-----------|-------|-----------------|
| 6.1 | **Read** per service | Reviews → choose *Gold Radiance Facial* | Review by "Nimali P." with **Verified Visit** badge and the average rating |
| 6.2 | Hidden review | Reviews list | R010 (parking complaint) is not shown because it is HIDDEN |
| 6.3 | Validation | Log in as `ishara.g@gmail.com` → Write a review without a rating | Browser/server asks for a rating from 1 to 5 |
| 6.4 | **Create** (verified) | Review stylist *Ruwan Kumarasinghe*, 5 stars, comment | Saved as VERIFIED (Ishara's completed appointment A004 was with Ruwan) |
| 6.5 | **Create** (public) | Review service *Ladies Haircut and Blow Dry* | Saved as PUBLIC, "Customer Review" badge |
| 6.6 | One per target | Review *Gents Haircut* again | "You have already reviewed this..." |
| 6.7 | **Update** | Click Edit on your review → change to 4 stars → Save | Review updated |
| 6.8 | **Delete** (user) | Click Delete on your public review → confirm | Review removed |
| 6.9 | Admin display | Log in as admin → Admin → Review Moderation | Full names with customer IDs and booking IDs |
| 6.10 | Moderate | Click **Hide** on R002, then **Show** | Status toggles; hidden reviews disappear from the public page |
| 6.11 | **Delete** (admin) | Delete R010 → confirm | Removed from `reviews.txt` |

## 7. Admin dashboard
1. Log in as admin. You land on **Admin Dashboard**.
2. Check the cards: customers / premium members, admins, services, stylists, upcoming appointments, revenue, reviews.
3. After doing steps 4–6, refresh: the numbers change (more appointments, more revenue, more reviews).

## 8. File handling robustness (optional, for the viva)
1. Stop nothing. Open `users.txt` in the data folder and add a line `this is a broken line`. Save.
2. Open Admin → Users. The page still works; the broken line is skipped.
3. Rename `reviews.txt` to `reviews.bak` and open Reviews. The page shows "No reviews yet" instead of crashing.
4. Rename it back.
