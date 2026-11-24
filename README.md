# Jakarta EE Course Project (Labs 1 to 4)

A Maven-based Jakarta EE web application built across the first four labs of a university Jakarta EE course (Horizon School of Digital Technologies, L3). All labs live in the same module (`jakartaee-course`) and deploy as one WAR. Labs 3 and 4 were tested on WildFly 41.

## Labs

### Lab 1: Hello Enterprise App
- Set up the JDK, IntelliJ IDEA Ultimate, WildFly and Payara.
- `HelloServlet` at `/hello` answers GET requests with "Hello, Enterprise World!".
- `GoodbyeServlet` at `/goodbye` is a second servlet in the same package.
- Compared how the two application servers deploy and run the app.

### Lab 2: Multi-page product catalog (JSP and filters)
A small shop built on top of Lab 1: product list, product detail, session cart and checkout.

| Page | Servlet | URL | View |
|---|---|---|---|
| Product list | `ProductListServlet` | `/catalog/products` (optional `?category=`) | `products.jsp` |
| Product detail | `ProductDetailServlet` | `/catalog/product?id=1` | `product-detail.jsp` |
| Cart | `CartServlet` | `/cart`, `/cart/add` (POST) | `cart.jsp` |
| Checkout | `CheckoutServlet` | `/checkout` | `checkout.jsp` |

How it works:
- Servlets handle routing and logic, then `forward()` to a JSP that only draws the page (EL and JSTL).
- The catalog JSPs live in `WEB-INF/views/`, so a browser cannot open them directly. Every visit to these pages goes through a servlet.
- The cart is a list stored in the HTTP session. Adding an item uses Post/Redirect/Get, so a refresh does not add it twice.
- `RequestLoggingFilter` (`/*`) logs the method, URI and duration of every request.
- `AuthenticationFilter` (`/cart/*`) is a placeholder that only passes the request on. Real login comes in a later lab.
- Product data comes from a static in-memory list (`ProductCatalog`, 5 products).
- This is how Lab 2 first worked. In Lab 3 the logic moves out of the servlets (see below).

### Lab 3: EJB service layer
A refactor of Lab 2. The user sees the same app, but all business logic moves from the servlets into a service layer. The servlets only translate HTTP requests into service calls.

| Service | Bean type | Role |
|---|---|---|
| `ProductService` | `@Stateless` | product lookups and category filtering |
| `CartService` | `@Stateful` + `@SessionScoped` | one cart per browser session, replaces the manual `HttpSession` code |
| `OrderService` | `@Stateless` + `@Transactional` | creates an order: rejects an empty cart, sends the notification, clears the cart |
| `NotificationService` | `@Singleton` | one shared instance, logs `[NOTIFY] Order confirmed for: ...` and keeps the last 5 confirmations |

How it works:
- The servlets get their services with `@Inject`, so none of them creates a service itself. The injected object is a container proxy, not the real instance.
- `CartService` is session-scoped, so each user gets their own cart. It must implement `Serializable`, and so does `ProductDTO`, which it stores.
- `OrderService.createOrder(...)` throws `IllegalStateException` if the cart is empty, before any notification is sent. The runtime exception makes the container roll the transaction back.
- `CartService` also offers `getTotal()` and `getDiscountedTotal()` (flat 10% discount), kept in the cart service on purpose because the rule is small.
- In Lab 3, product data still came from the in-memory `ProductCatalog`. Lab 4 replaces it with a database (see below).

### Lab 4: JPA database layer
The in-memory catalog is replaced by a real database accessed through JPA (Hibernate, provided by WildFly). The servlets and pages stay the same, and the product detail page now also lists customer reviews.

Entities (`model/`):

| Entity | Table | Relationships |
|---|---|---|
| `Product` | `products` | name, category, price |
| `Customer` | `customers` | `@OneToMany` orders |
| `Order` | `orders` | `@ManyToOne` customer, `@OneToMany` items (cascade all) |
| `OrderItem` | `order_items` | `@ManyToOne` order, `@ManyToOne` product, quantity |
| `Review` | `reviews` | `@ManyToOne` product, rating (out of 5), comment |

Repositories (`repository/`, `@Stateless` beans using `@PersistenceContext(unitName = "catalogPU")`):
- `ProductRepository`: `findById`, `findAll`, `findByCategory`, `findByPriceLessThan`, `findReviewsByProductId`, `save`.
- `OrderRepository`: `save`, `findByCustomer`. It is ready but not yet called by `OrderService`.

How it works:
- `ProductService` now injects `ProductRepository` and maps each `Product` entity to a `ProductDTO`, so the servlets and JSPs did not need to change. `ProductCatalog` is no longer used.
- `ProductDetailServlet` also loads the product's reviews and `product-detail.jsp` shows them (or "No reviews yet.").
- `src/main/resources/META-INF/persistence.xml` defines the persistence unit **`catalogPU`** (JTA) on the datasource **`java:jboss/datasources/CatalogDS`**, an **H2** database. `hibernate.hbm2ddl.auto=create` rebuilds the tables on every deployment and `hibernate.show_sql=true` prints the SQL in the server log.
- `src/main/resources/import.sql` is run by Hibernate after the tables are created and seeds 3 products and 3 reviews.
- No extra Maven dependency is needed: WildFly already includes Hibernate and the `h2` JDBC driver.

## Project structure
```
src/main/java/com/
├── course/
│   ├── lab1/              HelloServlet, GoodbyeServlet
│   └── catalog/
│       ├── model/         JPA entities (Product, Customer, Order, OrderItem, Review),
│       │                  ProductDTO, ProductCatalog (unused since Lab 4)
│       ├── repository/    ProductRepository, OrderRepository
│       ├── service/       ProductService, CartService, OrderService, NotificationService
│       └── web/           servlets and filters
└── example/jakartaeecourse/
                           HelloServlet (starter servlet at /hello-servlet)
src/main/resources/
├── META-INF/persistence.xml   persistence unit catalogPU
└── import.sql                 seed data (products, reviews)
src/main/webapp/
├── index.jsp              public landing page, links to /hello-servlet
└── WEB-INF/
    ├── web.xml
    └── views/             header, footer, products, product-detail, cart, checkout
```

## Tech stack
Java 21, Jakarta EE 11 API (Servlet 6.1, EJB, CDI, Transactions, JPA 3.2), Hibernate (bundled with WildFly), H2, JSP, JSTL 3.0, Maven (WAR packaging), WildFly 41, Payara 7, IntelliJ IDEA.

## Run it
1. Install JDK 21 and Maven, or use the included wrapper (`./mvnw`).
2. Build the WAR:
   ```bash
   ./mvnw clean package
   ```
3. **Lab 4 and later: register the datasource in WildFly before deploying.** Otherwise the deployment fails because `java:jboss/datasources/CatalogDS` does not exist. With WildFly running:
   ```bash
   $WILDFLY_HOME/bin/jboss-cli.sh --connect --command='data-source add --name=CatalogDS --jndi-name=java:jboss/datasources/CatalogDS --driver-name=h2 --connection-url="jdbc:h2:mem:catalog;DB_CLOSE_DELAY=-1" --user-name=sa --password=sa'
   ```
   This only needs to be done once, because it is saved in `standalone.xml`. It is an in-memory database, so data is reset on every restart and re-seeded from `import.sql`.
4. Deploy `target/jakartaee-course.war` to WildFly or Payara, or run it from IntelliJ with a server run configuration.
5. Open:
   - Lab 1: `http://localhost:8080/jakartaee-course/hello`
   - Labs 2 to 4: `http://localhost:8080/jakartaee-course/catalog/products` (after checkout the server console shows the `[NOTIFY]` line; `catalog/product?id=1` shows the reviews)

The context path is `/jakartaee-course` because `pom.xml` sets `<finalName>jakartaee-course</finalName>`.

## Author
Bara Chebbi
