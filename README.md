# Online Store
Online shopping with shopping lists management 

## Eclipse

### Project Setup
  - Clone to workspace ({workspace}/online-store)
  - File -> Import
  - Existing Maven Projects 
  - Browse -> Select online-store
  - Check pom.xml -> Finish

### How to run
  - Open /online-store/src/main/java/com/spring/onlinestore/OnlineStoreApplication.java
  - Run as "Java Application"
 

## REST API Request Mapping
Default request URL: http://localhost:8080

Register user: 
  - POST /user
  - No Auth
  - body template:
{
	"username": " ",
	"password": " "
}

For registered users, please use _Basic Auth_.

### Mapping for USER:
  - editUser: 
    - PUT /user/{name}
  - deleteUser: 
    - DELETE /user/{name}
  - retrieveListsForUser: 
    - GET /user/lists
  - createShoppinglist: 
    - POST /user/lists
  - editShoppinglist: 
    - PUT /user/lists/{id}
  - deleteShoppinglist: 
    - DELETE /user/lists/{id}
  - getShoppinglistProducts: 
    - GET /user/lists/{id}
  - addProductToShoppinglist: 
    - POST /user/lists/{id}/{id2}
  - retrieveAllCategories: 
    - GET /categs
  - retrieveAllSubcategories: 
    - GET /categs/{id}/sub
  - retrieveAllProducts: 
    - GET /categs/\*/sub/{id}/products
  - retrieveProduct: 
    - GET /categs/\*/sub/\*/products/{id}

### Mapping for ADMIN:  
  - All **USER** mapping +
  - createCategory: 
    - POST /categs
  - editCategory:
    - PUT /categs/{id}
  - deleteCategory: 
    - DELETE /categs/{id}
  - createSubcategory: 
    - POST /categs/{id}/sub
  - editSubcategory: 
    - PUT /categs/{id}/sub/{id2}
  - deleteSubcategory: 
    - DELETE /categs/\*/sub/{id}
  - createProduct: 
    - POST /categs/\*/sub/{id}/products
  - editProduct: 
    - PUT /categs/\*/sub/{id}/products/{id2}
  - deleteProduct: 
    - DELETE /categs/\*/sub/\*/products/{id}
