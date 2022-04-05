# Online Store
Secured online b2b electronics store

This application expects basic authentication for using its features. Authorization is based on user roles (admin, user etc.).

All the shop's products are sorted by categories and subcategories, which any authenticated and authorized user will navigate through in order to check them. If the user wants to purchase a product, it can be added to a shopping list. 

The user can create and manage multiple lists (wishlists). When a shopping list is finished, the user can proceed to the checkout page, which displays the total cost and offers the possibility of applying a discount coupon (if any are available).


## Eclipse

### Project Setup
  - Git clone to workspace
  - File -> Import
  - Existing Maven Projects 
  - Browse -> Select online-store
  - Check pom.xml -> Finish

### How to run
  - Open /online-store/src/main/java/com/spring/onlinestore/OnlineStoreApplication.java
  - Run as "Java Application"
 
## REST API Documentation
The documentation is now available in two versions:
- Consumer:
  - api-doc-consumer.html
- Developer:
  - api-doc-developer.html

If you wish to just try the API, please refer to the consumer documentation.

If you wish to clone/fork and work on the project (or need full access inside), please refer to the developer documentation.
