#  Restaurant Food Delivery App

A native Android application built with **Kotlin** for ordering food from restaurants. The app features a complete flow from User Authentication to Order Placement and Order History tracking.

---

# Features

* **User Authentication:** Custom Login and Sign-up with API integration.
* **Home Screen:** View All Restaurants and Popular Food Items.
* **Product Details:** View item description, price, and image.
* **Smart Cart:** Local cart management (Add/Remove items) that persists even if the app is closed.
* **Checkout:** Enter delivery details (Address & Phone) and place orders via API.
* **Order History:** View past orders with status (Pending, Completed, etc.).
* **Profile:** User details and Logout functionality.

---

# Tech Stack

* **Language:** Kotlin
* **Architecture:** MVC (Model-View-Controller) pattern
* **Networking:** Retrofit 2 & Gson Converter
* **Image Loading:** Glide
* **Local Storage:** SharedPreferences (for User Session & Cart Data)
* **UI Components:** ConstraintLayout, RecyclerView, CardView, Material Design

---

# How to Run the Project

1.  **Open in Android Studio:**
    * Open Android Studio -> File -> Open -> Select the project folder.

2.  **Sync Gradle:**
    * Wait for the project to download dependencies (Internet required).

3.  **Run:**
    * Connect a physical device or launch an Emulator.
    * Click the **Run (Play)** button.

---

# Configuration (Important)

# How to Change the API Base URL
If you move your backend to a new server or domain, you only need to change one line of code.

1.  Go to: `app/java/com.example.resturent_app/api/RetrofitClient.kt`
2.  Update the `BASE_URL`:

// Change this URL to your new server link
private const val BASE_URL = "[https://hotel-managment-api-xi.vercel.app/](https://hotel-managment-api-xi.vercel.app/)"

====================================
    Project Structure Guide
==================================== 
here is a quick guide to understand where the code is located:

# 1.api/
Contains the networking logic.

* **ApiService.kt:** Defines all API Endpoints (Login, Register, Place Order, Get Products).
* **RetrofitClient.kt:** Configures the connection to the server.

# 2. models/
Defines the structure of data (JSON) used in the app.

* **AuthModels.kt:** Login/Register request & response.
* **OrderModels.kt:** Checkout request & Order History structure.
* **Product.kt:** Food item details.
* **CartItem.kt:** Structure for items inside the cart.

# 3. utils/ (Helper Files)
Contains the core logic for Data Management.
 
* **UserSession.kt:** Handles User Login state. It saves User ID, Name, and Email locally so the user stays logged in after closing the app.
* **CartManager.kt:** Handles the Shopping Cart. It uses Gson to save the cart list into the phone's local storage. This ensures the cart is not lost when the app is restarted.

# 4. adapters/
Contains logic for Lists (RecyclerViews).

* **ProductAdapter.kt:** Handles the list of food items on Home Screen.
* **CartAdapter.kt:** Handles the list of items in the Cart Screen.
* **OrdersAdapter.kt:** Handles the list of "My Orders".

# 5. activities/ & fragments/
Contains the UI Screens.

**MainActivity.kt:** The main container with Bottom Navigation.
**CheckoutActivity.kt:** Handles Address/Phone input and API Order Placement.
**MyOrdersActivity.kt:** Fetches and displays user order history.

# Feature         ||Method || Endpoint
Login,            || POST  || /v1/api/login
Register          || POST  || /v1/api/register-user
Get Restaurants   || GET   || /v1/api/all-restaurants
Get Foods         || GET   || /v1/api/all-fooditems
Place Order       || POST  || /v1/api/order/checkout
Order History     || GET   || /v1/api/order/user/{id}