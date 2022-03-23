# Online Store
Online shopping with shopping lists management 




Eclipse:

Project Setup
  - Clone to workspace ({workspace}/online-store)
  - File -> Import
  - Existing Maven Projects 
  - Browse -> Select online-store
  - Check pom.xml -> Finish

How to run:
  - Open /online-store/src/main/java/com/spring/onlinestore/OnlineStoreApplication.java
  - Run as "Java Application"




REST API Request Mapping (listed before Swagger implementation):
  - retrieveAllCategories: 
    - GET /categs
  - retrieveCategory: 
    - GET /categs/{id}
  - createCategory: 
    - POST /categs
  - editCategory: 
    - PUT /categs/{id}
  - deleteCategory: 
    - DELETE /categs/{id}
  - retrieveAllSubcategories: 
    - GET /categs/{id}/sub
  - createSubcategory: 
    - POST /categs/{id}/sub
  - editSubcategory: 
    - PUT /categs/{id}/sub/{id2}
  - deleteSubcategory: 
    - DELETE /categs/\*/sub/{id}
  - retrieveAllProducts: 
    - GET /categs/sub/{id}/products
  - retrieveProduct: 
    - GET /categs/sub/\*/products/{id}
  - createProduct: 
    - POST /categs/sub/{id}/products
  - editProduct: 
    - PUT /categs/sub/{id}/products/{id2}
  - deleteProduct: 
    - DELETE /categs/sub/\*/products/{id}
