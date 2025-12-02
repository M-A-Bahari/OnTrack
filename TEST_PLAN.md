# OnTrack - Test Plan

## 📋 Test Plan Overview

This test plan is organized by **PRIORITY** to help testers focus on the most critical features first. Start with High Priority tests, then move to Medium and Low priority tests as time permits.

---

## 🎯 Quick Start Guide

### **For Limited Time Testing:**
1. ✅ Run all **HIGH PRIORITY** tests (12 tests, ~15-20 minutes)
2. If time permits, run **MEDIUM PRIORITY** tests (10 tests, ~10-15 minutes)
3. If extra time, run **LOW PRIORITY** tests (8 tests, ~10 minutes)

### **Test Environment:**
- **Device**: Android phone/tablet or emulator
- **OS Version**: Android 8.0+ (API 26+)
- **Orientations**: Portrait and landscape

---

## 🔴 HIGH PRIORITY TESTS (Core Functionality)

**These tests MUST pass for the app to be functional. Focus here first.**

**Estimated Time**: 15-20 minutes

---

### **HP-01: User Registration - Success**

**Feature**: User Authentication
**Type**: Functional

**Steps**:
1. Launch app (should show Login screen)
2. Tap "Sign Up" redirect link
3. Enter the following:
   - First Name: "John"
   - Last Name: "Doe"
   - Email: "john.doe@test.com"
   - Password: "password123"
   - Confirm Password: "password123"
4. Tap "Sign Up" button

**Expected Results**:
- ✅ Toast message: "Signup successful! Please login."
- ✅ Redirected to Login screen
- ✅ No crashes

---

### **HP-02: User Registration - Duplicate Email**

**Feature**: User Authentication
**Type**: Negative (Error Handling)

**Steps**:
1. Try to register again with "john.doe@test.com" (email from HP-01)
2. Use different name but same email

**Expected Results**:
- ✅ Toast message: "Email already exists"
- ✅ User remains on Signup screen
- ✅ No duplicate user created

---

### **HP-03: User Login - Success**

**Feature**: User Authentication
**Type**: Functional

**Steps**:
1. On Login screen, enter:
   - Email: "john.doe@test.com"
   - Password: "password123"
2. Tap "Login" button

**Expected Results**:
- ✅ Toast message: "Login successful!"
- ✅ Redirected to Dashboard
- ✅ Welcome message shows: "Welcome back, John!"
- ✅ Dashboard displays correctly

---

### **HP-04: User Login - Invalid Credentials**

**Feature**: User Authentication
**Type**: Negative (Security)

**Steps**:
1. On Login screen, enter:
   - Email: "wrong@test.com"
   - Password: "wrongpassword"
2. Tap "Login"

**Expected Results**:
- ✅ Toast message: "Invalid email or password"
- ✅ User remains on Login screen
- ✅ Cannot access Dashboard without valid credentials

---

### **HP-05: Add Subscription - Success**

**Feature**: Subscription Management
**Type**: Functional (Core Feature)

**Steps**:
1. On Dashboard, tap the "+" (Add Subscription) button
2. Fill in the form:
   - Service Name: "Netflix"
   - Amount: "15.99"
   - Billing Cycle: Select "Monthly" from dropdown
   - Start Date: Select today's date
   - Cancel URL: Leave blank (optional)
3. Tap "Add Subscription" button

**Expected Results**:
- ✅ Toast: "Subscription added successfully!"
- ✅ Redirected to Dashboard
- ✅ New "Netflix" subscription appears in the list
- ✅ Statistics updated:
  - Active count shows "1 active"
  - Total paid shows "$15.99"
  - Monthly average shows "$15.99"

---

### **HP-06: Dashboard Statistics Display**

**Feature**: Dashboard
**Type**: Functional (Core Feature)

**Preconditions**: At least one subscription exists (from HP-05)

**Steps**:
1. Observe Dashboard statistics at the top
2. Check subscription list below

**Expected Results**:
- ✅ Statistics section displays:
  - Active count (number + "active")
  - Total paid amount
  - Monthly average
- ✅ Subscription card shows:
  - Service icon (emoji)
  - Service name
  - Category badge
  - Amount and billing cycle (e.g., "$15/month")
  - Next payment date

---

### **HP-07: Swipe to Cancel - Complete Flow**

**Feature**: Subscription Management
**Type**: Functional (Core Feature)

**Preconditions**: At least one active subscription exists

**Steps**:
1. On Dashboard, swipe LEFT or RIGHT on the subscription card
2. Dialog appears: "Do you want to visit the cancellation page for Netflix?"
3. Tap "Yes, Visit Page"
4. Browser opens with cancellation URL
5. Press BACK button to return to app
6. Dialog appears: "Did you complete the cancellation for Netflix?"
7. Tap "Yes, I Cancelled"

**Expected Results**:
- ✅ First dialog appears correctly
- ✅ Browser opens (URL should be: https://www.netflix.com/ca/account)
- ✅ Second dialog appears on return
- ✅ Toast: "Netflix marked as inactive."
- ✅ Subscription disappears from Dashboard
- ✅ Statistics update (active count decreases)

---

### **HP-08: History View - View Cancelled Subscriptions**

**Feature**: History
**Type**: Functional

**Preconditions**: At least one cancelled subscription (from HP-07)

**Steps**:
1. On Dashboard, tap the History button (clock icon)
2. Observe HistoryActivity

**Expected Results**:
- ✅ Toolbar with back button appears
- ✅ Previously cancelled subscription(s) displayed
- ✅ Cancellation date shown
- ✅ Back button returns to Dashboard

---

### **HP-09: Dashboard Rotation - Portrait to Landscape**

**Feature**: Configuration Change
**Type**: Critical Regression Test

**⚠️ IMPORTANT**: This was a MAJOR BUG that was fixed. Must verify!

**Preconditions**: User is logged in on Dashboard with subscriptions

**Steps**:
1. View Dashboard in portrait mode
2. Rotate device to landscape (or press Ctrl+F11 in emulator)
3. Observe the screen

**Expected Results**:
- ✅ **NO CRASH** (this was the bug - now fixed)
- ✅ Dashboard reloads correctly
- ✅ All subscriptions still visible
- ✅ Statistics remain accurate
- ✅ Welcome message still shows user's name

---

### **HP-10: History Rotation - Portrait to Landscape**

**Feature**: Configuration Change
**Type**: Critical Regression Test

**⚠️ IMPORTANT**: This was a MAJOR BUG that was fixed. Must verify!

**Preconditions**: User is in HistoryActivity with cancelled subscriptions

**Steps**:
1. View History in portrait mode
2. Rotate to landscape
3. Observe the screen

**Expected Results**:
- ✅ **NO CRASH** (this was the bug - now fixed)
- ✅ Layout changes to grid (2 columns)
- ✅ All subscriptions still visible
- ✅ Data preserved

---

### **HP-11: Monthly Cost Calculation - Different Billing Cycles**

**Feature**: Payment Calculations
**Type**: Calculation Verification

**Steps**:
1. Add three subscriptions with different cycles:
   - Subscription 1: "Spotify" - $10.00 - Weekly
   - Subscription 2: "Disney+" - $15.00 - Monthly
   - Subscription 3: "Amazon Prime" - $120.00 - Yearly
2. Return to Dashboard
3. Check "Monthly Average" statistic

**Expected Results**:
- ✅ Total paid: $145.00 (10 + 15 + 120)
- ✅ Monthly average: $68.30
  - Calculation breakdown:
    - Weekly: $10 × 4.33 = $43.30
    - Monthly: $15 × 1 = $15.00
    - Yearly: $120 ÷ 12 = $10.00
    - **Total: $68.30**

---

### **HP-12: Direct Cancellation Links**

**Feature**: Subscription Management
**Type**: Functional (Canadian Links)

**Steps**:
Test with different service names to verify correct URLs:

1. Add "Netflix" subscription, swipe to cancel, tap "Yes, Visit Page"
   - **Expected URL**: https://www.netflix.com/ca/account

2. Add "Spotify" subscription, swipe to cancel
   - **Expected URL**: https://www.spotify.com/ca/account/subscription/

3. Add "Disney+" subscription, swipe to cancel
   - **Expected URL**: https://help.disneyplus.com/article/disneyplus-en-ca-cancel

**Expected Results**:
- ✅ Each service opens the correct Canadian cancellation URL
- ✅ Unknown services fall back to Google search

---

## 🟡 MEDIUM PRIORITY TESTS (Important Features)

**Test these if time permits after completing High Priority tests.**

**Estimated Time**: 10-15 minutes

---

### **MP-01: User Registration - Password Mismatch**

**Feature**: User Authentication
**Type**: Input Validation

**Steps**:
1. Navigate to Signup screen
2. Fill all fields but enter:
   - Password: "password123"
   - Confirm Password: "password456" (different)
3. Tap "Sign Up"

**Expected Results**:
- ✅ Toast: "Passwords do not match"
- ✅ Registration blocked

---

### **MP-02: User Registration - Empty Fields**

**Feature**: User Authentication
**Type**: Input Validation

**Steps**:
1. On Signup screen, leave First Name empty
2. Fill other fields correctly
3. Tap "Sign Up"

**Expected Results**:
- ✅ Toast: "Please fill in all fields"
- ✅ Registration blocked

---

### **MP-03: Add Subscription - With Custom Cancel URL**

**Feature**: Subscription Management
**Type**: Functional

**Steps**:
1. Tap "+" to add subscription
2. Fill in:
   - Service Name: "Custom Service"
   - Amount: "25.00"
   - Billing Cycle: "Monthly"
   - Start Date: Today
   - Cancel URL: "https://example.com/cancel"
3. Tap "Add Subscription"
4. Swipe to cancel this subscription
5. Tap "Yes, Visit Page"

**Expected Results**:
- ✅ Browser opens with custom URL: https://example.com/cancel
- ✅ Custom URL takes priority over default service URL

---

### **MP-04: Swipe to Cancel - User Says No (Step 1)**

**Feature**: Subscription Management
**Type**: User Action Cancellation

**Steps**:
1. Swipe on a subscription
2. Dialog appears: "Do you want to visit the cancellation page?"
3. Tap "No"

**Expected Results**:
- ✅ Dialog dismisses
- ✅ Subscription card returns to normal position
- ✅ Subscription remains active
- ✅ No browser opens

---

### **MP-05: Swipe to Cancel - User Says Not Yet (Step 2)**

**Feature**: Subscription Management
**Type**: User Action Cancellation

**Steps**:
1. Swipe on subscription
2. Tap "Yes, Visit Page"
3. Browser opens, press back
4. Dialog: "Did you complete the cancellation?"
5. Tap "Not Yet"

**Expected Results**:
- ✅ Dialog dismisses
- ✅ Subscription remains active
- ✅ No cancellation occurs

---

### **MP-06: History View - Empty State**

**Feature**: History
**Type**: UI State

**Preconditions**: User has no cancelled subscriptions

**Steps**:
1. Login as new user (or user with no cancelled subs)
2. Navigate to History

**Expected Results**:
- ✅ Message: "No cancelled subscriptions yet"
- ✅ RecyclerView empty or hidden
- ✅ No crashes

---

### **MP-07: Next Payment Date Calculation**

**Feature**: Payment Calculations
**Type**: Calculation

**Steps**:
1. Add subscription with:
   - Start Date: 15th of current month
   - Billing Cycle: Monthly
2. Observe subscription card (assuming today is after the 15th)

**Expected Results**:
- ✅ Next payment date shows: 15th of NEXT month
- ✅ Date formatted as "Next payment: [Month] 15, [Year]"

---

### **MP-08: Dashboard - No Subscriptions**

**Feature**: Dashboard
**Type**: Empty State

**Preconditions**: User has no subscriptions

**Steps**:
1. Login as new user
2. Observe Dashboard

**Expected Results**:
- ✅ Welcome message displays
- ✅ Statistics show:
  - "0 active"
  - "$0.00" for total and monthly average
- ✅ RecyclerView is empty
- ✅ Add button functional

---

### **MP-09: Logout Functionality**

**Feature**: Dashboard
**Type**: Security

**Steps**:
1. On Dashboard, tap Logout button
2. Dialog: "Are you sure you want to logout?"
3. Tap "Yes"

**Expected Results**:
- ✅ Confirmation dialog appears
- ✅ User redirected to Login screen
- ✅ Toast: "Logged out successfully"
- ✅ Cannot press back to return to Dashboard

---

### **MP-10: Data Persistence After App Restart**

**Feature**: Database
**Type**: Data Integrity

**Preconditions**: User has subscriptions in database

**Steps**:
1. Note current subscriptions on Dashboard
2. Force stop the app (Settings → Apps → OnTrack → Force Stop)
3. Relaunch app
4. Login with same credentials

**Expected Results**:
- ✅ All subscriptions still present
- ✅ Statistics correct
- ✅ No data loss

---

## 🟢 LOW PRIORITY TESTS (Nice to Have)

**Test these only if you have extra time.**

**Estimated Time**: 10 minutes

---

### **LP-01: Navigation Between Login and Signup**

**Feature**: Navigation
**Type**: UI Flow

**Steps**:
1. On Login screen, tap "Sign Up" link
2. Observe Signup screen
3. Tap "Login" redirect link
4. Observe Login screen

**Expected Results**:
- ✅ Smooth navigation
- ✅ No data loss
- ✅ Correct screens displayed

---

### **LP-02: Days Until Payment - Urgent Badge (3 Days or Less)**

**Feature**: Payment Calculations
**Type**: UI Indicator

**Steps**:
1. Add subscription with payment due in 2 days
   - (Start date = 2 days ago, Weekly cycle)
2. Observe subscription card

**Expected Results**:
- ✅ Badge visible with text "2 days left"
- ✅ Badge color indicates urgency

---

### **LP-03: Days Until Payment - Hidden for Future Dates**

**Feature**: Payment Calculations
**Type**: UI Behavior

**Steps**:
1. Add subscription with payment 10+ days away
2. Observe subscription card

**Expected Results**:
- ✅ Days until renewal badge is HIDDEN
- ✅ Only "Next payment" date shows

---

### **LP-04: Long Service Names**

**Feature**: UI/UX
**Type**: Edge Case

**Steps**:
1. Add subscription with very long name: "This Is A Really Long Service Name That Should Be Handled Properly"
2. Observe Dashboard card

**Expected Results**:
- ✅ Text truncates with ellipsis (...)
- ✅ Card layout doesn't break
- ✅ Other elements remain visible

---

### **LP-05: Large Dollar Amounts**

**Feature**: UI/UX
**Type**: Edge Case

**Steps**:
1. Add subscription with amount: $9999.99
2. Observe Dashboard

**Expected Results**:
- ✅ Amount displays correctly
- ✅ Layout doesn't break
- ✅ Statistics calculate correctly

---

### **LP-06: Zero Dollar Subscription**

**Feature**: Edge Case
**Type**: Free Trial Scenario

**Steps**:
1. Add subscription with amount: $0.00

**Expected Results**:
- ✅ Subscription accepted (free trials exist)
- ✅ Statistics show $0.00 correctly
- ✅ No crashes

---

### **LP-07: Multiple Users - Data Isolation**

**Feature**: Database
**Type**: Security/Data Integrity

**Steps**:
1. Logout from current user
2. Create new user: "user2@test.com"
3. Login as user2, add 2 subscriptions
4. Logout
5. Login back as original user (john.doe@test.com)

**Expected Results**:
- ✅ Original user sees only THEIR subscriptions
- ✅ User2's subscriptions NOT visible
- ✅ Data correctly isolated

---

### **LP-08: Rapid Button Taps**

**Feature**: Edge Case
**Type**: Stress Test

**Steps**:
1. Rapidly tap "Add Subscription" button 5 times quickly

**Expected Results**:
- ✅ Only ONE AddSubscriptionActivity opens
- ✅ No multiple instances
- ✅ No crashes

---

## 📊 Test Summary Checklist

Use this to track your testing progress:

### **HIGH PRIORITY** (Must Complete)
- [ ] HP-01: User Registration Success
- [ ] HP-02: Duplicate Email Blocked
- [ ] HP-03: Login Success
- [ ] HP-04: Invalid Login Blocked
- [ ] HP-05: Add Subscription Success
- [ ] HP-06: Dashboard Statistics Display
- [ ] HP-07: Swipe to Cancel Flow
- [ ] HP-08: History View
- [ ] HP-09: Dashboard Rotation ⚠️ Critical
- [ ] HP-10: History Rotation ⚠️ Critical
- [ ] HP-11: Monthly Cost Calculation
- [ ] HP-12: Direct Cancellation Links

**Result**: ____ / 12 Passed

---

### **MEDIUM PRIORITY** (If Time Permits)
- [ ] MP-01: Password Mismatch
- [ ] MP-02: Empty Fields Validation
- [ ] MP-03: Custom Cancel URL
- [ ] MP-04: Cancel - User Says No
- [ ] MP-05: Cancel - User Says Not Yet
- [ ] MP-06: History Empty State
- [ ] MP-07: Payment Date Calculation
- [ ] MP-08: Dashboard Empty State
- [ ] MP-09: Logout
- [ ] MP-10: Data Persistence

**Result**: ____ / 10 Passed

---

### **LOW PRIORITY** (If Extra Time)
- [ ] LP-01: Navigation Flow
- [ ] LP-02: Urgent Payment Badge
- [ ] LP-03: Hidden Badge for Future Dates
- [ ] LP-04: Long Service Names
- [ ] LP-05: Large Dollar Amounts
- [ ] LP-06: Zero Dollar Subscription
- [ ] LP-07: Multiple Users Data Isolation
- [ ] LP-08: Rapid Button Taps

**Result**: ____ / 8 Passed

---

## 🎯 Minimum Acceptance Criteria

**App is ready for release if:**
- ✅ All 12 HIGH PRIORITY tests pass
- ✅ 8+ out of 10 MEDIUM PRIORITY tests pass
- ✅ No critical bugs found

---

## 🐛 Bug Reporting

If you find a bug, please note:

**Bug Details**:
- Test Case ID: [e.g., HP-05]
- Description: [What went wrong]
- Steps to Reproduce: [Brief steps]
- Expected vs Actual: [What should happen vs what happened]
- Severity: Critical / High / Medium / Low

---

## 📝 Key Testing Notes

### **⚠️ Critical Areas (Fixed Bugs - Verify)**
1. **Rotation Testing** (HP-09, HP-10): This was a MAJOR crash bug - must verify fix works
2. **Landscape Layout** (HP-10): RecyclerView had ID mismatch - must verify fixed

### **🎯 Core Features to Focus On**
1. User authentication (login/signup)
2. Add subscription
3. Swipe to cancel
4. Statistics calculations
5. Orientation changes

### **💡 Testing Tips**
- Use emulator for quick rotation testing (Ctrl+F11)
- Test with real-world subscription services (Netflix, Spotify, etc.)
- Verify calculations manually for accuracy
- Try edge cases if time permits

---

## ✅ Supported API Levels

- **Minimum SDK**: API 26 (Android 8.0 Oreo)
- **Target SDK**: API 35 (Android 15)
- **Compile SDK**: API 35
- **Device Compatibility**: 95%+ of active Android devices

---

**Estimated Total Testing Time**:
- High Priority Only: **15-20 minutes**
- High + Medium Priority: **25-35 minutes**
- All Tests: **35-45 minutes**

**Happy Testing! 🚀**
