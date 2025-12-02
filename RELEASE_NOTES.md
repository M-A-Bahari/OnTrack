# OnTrack - Release Notes

## Version 1.0.0 (December 2025)

### 📋 Release Summary

OnTrack v1.0.0 is the initial release of the subscription tracking application. This version includes core features for managing recurring subscriptions, user authentication, and expense tracking.

---

## ✅ Completed Features

### **1. User Authentication System**
- ✅ User registration with validation
  - First name, last name, email, and password fields
  - Email format validation
  - Password confirmation matching
  - Duplicate email prevention
- ✅ User login functionality
  - Email and password authentication
  - Input validation
  - Error messaging for invalid credentials
- ✅ Secure session management
  - User context maintained across activities
  - Email-based data isolation

### **2. Dashboard**
- ✅ Main subscription overview interface
  - RecyclerView with custom subscription cards
  - Service icon display (emoji-based)
  - Category badges (Entertainment, Music, Shopping, Video, Other)
  - Price and billing cycle display
  - Next payment date calculation
  - Days until renewal badge (for payments due within 3 days)
- ✅ Real-time statistics display
  - Total active subscriptions count
  - Total monthly spending (sum of all active subscriptions)
  - Monthly average (normalized across billing cycles)
- ✅ Personalized welcome message with user's first name
- ✅ Quick action buttons
  - Add subscription (+)
  - View history (clock icon)
  - Logout

### **3. Subscription Management**
- ✅ Add new subscriptions
  - Service name input
  - Amount (double precision)
  - Billing cycle selection (Weekly, Monthly, Yearly)
  - Start date picker
  - Optional cancellation URL
- ✅ Swipe-to-cancel gesture
  - Left or right swipe on subscription cards
  - Confirmation dialog before opening cancellation page
  - Direct links to official cancellation pages
- ✅ Two-step cancellation flow
  - Step 1: Confirm intention to cancel
  - Step 2: Confirm completion of cancellation
  - Prevents accidental cancellations
- ✅ Direct cancellation links for popular services
  - Netflix (Canada): Account management page
  - Disney+ (Canada): Help center cancellation page
  - Amazon Prime (Canada): Memberships page
  - Spotify (Canada): Account subscription page
  - Apple Music: Apple Support cancellation guide
  - YouTube Premium: Paid memberships page
  - HBO Max/Max: Subscription management
  - Google search fallback for unlisted services

### **4. History View**
- ✅ Dedicated activity for inactive subscriptions
  - Separate view from active subscriptions
  - Same card layout as dashboard
  - Cancellation date tracking
- ✅ Adaptive layout
  - Linear layout for portrait mode
  - Grid layout (2 columns) for landscape mode
- ✅ Empty state handling
  - "No cancelled subscriptions yet" message
  - Helpful guidance for users

### **5. Payment Calculations**
- ✅ Automatic next payment date calculation
  - Handles weekly, monthly, and yearly cycles
  - Accounts for calendar variations
  - Always shows next future payment date
- ✅ Days until payment calculation
  - Real-time countdown
  - Special badges for urgent payments (≤3 days)
  - "Due today" and "Overdue" indicators
- ✅ Monthly cost normalization
  - Weekly → Monthly conversion (×4.33)
  - Yearly → Monthly conversion (÷12)
  - Accurate monthly spending projection

### **6. Database & Data Persistence**
- ✅ Room Database implementation
  - SQLite backend
  - Type-safe database queries
- ✅ User table
  - Email (primary key)
  - First name, last name
  - Password storage
- ✅ Subscription table
  - Auto-generated ID
  - User email (foreign key concept)
  - Service details
  - Active/inactive status
  - Cancellation tracking
- ✅ Repository pattern
  - Clean separation of data access logic
  - Coroutine-based async operations
  - LiveData for reactive UI updates

### **7. Architecture & Code Quality**
- ✅ MVVM architecture pattern
  - ViewModels for business logic
  - LiveData for observable data
  - Clear separation of concerns
- ✅ Kotlin coroutines for async operations
  - ViewModelScope for lifecycle-aware operations
  - Dispatchers.IO for database operations
  - No blocking on main thread
- ✅ RecyclerView with DiffUtil
  - Efficient list updates
  - Smooth animations
  - ListAdapter implementation
- ✅ Material Design components
  - CardView for subscriptions
  - MaterialToolbar for navigation
  - Modern UI patterns

### **8. User Experience**
- ✅ Responsive UI design
  - Adapts to different screen sizes
  - Material Design color scheme
  - Consistent typography
- ✅ Orientation support
  - Configuration change handling
  - State preservation across rotations
  - Landscape-specific layouts where appropriate
- ✅ Input validation
  - Empty field checks
  - Email format validation
  - Password matching verification
  - Numeric input validation for amounts
- ✅ User feedback
  - Toast messages for actions
  - Loading states
  - Error messages
  - Success confirmations
- ✅ Intuitive navigation
  - Clear button labels
  - Back button support
  - Consistent navigation patterns

### **9. Accessibility Features**
- ✅ Content descriptions for interactive elements
- ✅ Touch target sizes (48dp minimum)
- ✅ High contrast text and backgrounds
- ✅ Clear visual hierarchy

---

## ❌ Incomplete Features

### **1. Advanced Statistics**
- ❌ Spending trends over time (monthly/yearly graphs)
- ❌ Category-wise spending breakdown
- ❌ Comparison of current month vs previous months
- ❌ Annual spending projections

### **2. Notifications & Reminders**
- ❌ Push notifications for upcoming renewals
- ❌ Customizable reminder settings (1 day, 3 days, 1 week before)
- ❌ Daily/weekly spending summaries
- ❌ Budget alerts

### **3. Data Management**
- ❌ Cloud backup and sync
- ❌ Export data to CSV/PDF
- ❌ Import subscriptions from other apps
- ❌ Data restore from backup

### **4. Account Management**
- ❌ Change password functionality
- ❌ Update profile information
- ❌ Delete account option
- ❌ Forgot password recovery
- ❌ Email verification

### **5. Subscription Enhancements**
- ❌ Edit existing subscriptions
- ❌ Add notes/descriptions to subscriptions
- ❌ Upload custom service icons
- ❌ Subscription sharing/splitting costs
- ❌ Receipt/invoice attachment
- ❌ Multiple payment methods tracking

### **6. Advanced Search & Filtering**
- ❌ Search subscriptions by name
- ❌ Filter by category
- ❌ Filter by billing cycle
- ❌ Sort by price, date, or name
- ❌ Price range filters

### **7. Budgeting Features**
- ❌ Set monthly spending budget
- ❌ Budget vs actual spending comparison
- ❌ Alerts when approaching budget limit
- ❌ Category-specific budgets

### **8. Social Features**
- ❌ Share subscription recommendations
- ❌ Popular subscriptions discovery
- ❌ Cost comparison with other users (anonymized)

### **9. Security Enhancements**
- ❌ Password hashing (currently plain text)
- ❌ Biometric authentication (fingerprint/face)
- ❌ PIN code lock
- ❌ Auto-lock after inactivity

### **10. Customization**
- ❌ Dark mode / light mode toggle
- ❌ Custom color themes
- ❌ Currency selection (CAD/USD/EUR/etc.)
- ❌ Date format preferences

### **11. Widgets**
- ❌ Home screen widget showing total monthly cost
- ❌ Upcoming payments widget
- ❌ Quick add subscription widget

### **12. Multi-platform**
- ❌ iOS version
- ❌ Web dashboard
- ❌ Tablet-optimized UI

---

## 🐛 Known Issues / Limitations

### **Minor Issues**
1. **Password Security**: Passwords stored in plain text (not production-ready)
2. **No Email Validation**: Email addresses are not verified
3. **Single Device**: No multi-device sync
4. **Manual Entry**: All subscriptions must be added manually

### **Future Considerations**
- Implement proper authentication backend (Firebase, AWS, etc.)
- Add encrypted password storage
- Consider subscription auto-detection from emails/SMS
- Add support for family/shared subscriptions

---

## 🔧 Technical Changes

### **Bug Fixes**
- ✅ Fixed crash on device rotation (Issue with Intent extras and ViewBinding)
- ✅ Fixed landscape layout ID mismatch in RecyclerView adapter
- ✅ Resolved null pointer exceptions on configuration changes

### **Performance Improvements**
- ✅ Optimized RecyclerView with DiffUtil for efficient updates
- ✅ Implemented coroutines for non-blocking database operations
- ✅ Used LiveData for reactive UI updates (no manual refresh needed)

### **Code Quality**
- ✅ 100% Kotlin codebase
- ✅ MVVM architecture throughout
- ✅ Proper lifecycle management
- ✅ Clean code principles followed

---

## 📊 Supported API Levels

- **Minimum SDK**: API 26 (Android 8.0 Oreo)
- **Target SDK**: API 35 (Android 15)
- **Compile SDK**: API 35

### **Device Compatibility**
- ✅ Phones (all sizes)
- ✅ Tablets (basic support)
- ✅ Portrait and landscape orientations
- ✅ Android 8.0 and above (covers 95%+ of active devices)

---

## 🚀 Future Roadmap (v2.0.0)

### **Planned for Next Release**
1. Push notifications for payment reminders
2. Edit subscription functionality
3. Dark mode support
4. Data export to CSV
5. Password hashing and security improvements
6. Spending trends charts
7. Budget setting and tracking
8. Biometric authentication

---

## 📝 Notes for Testers

- This is a **local-only app** - all data stored on device
- **Test different billing cycles** to verify calculation accuracy
- **Test rotation** extensively (was a major bug area)
- **Try edge cases** like empty states, long service names, large amounts
- **Verify cancellation flow** end-to-end

---

## 📞 Feedback

Please report any bugs or feature requests to the development team.

---

**Release Date**: December 2025
**Build Type**: Final Project Release
**Status**: ✅ Stable
