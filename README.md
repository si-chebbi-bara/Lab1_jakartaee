# Jakarta EE Course Project (Labs 1 to 3)

A Maven-based Jakarta EE web application built across the first three labs of a university Jakarta EE course (Horizon School of Digital Technologies, L3). All labs live in the same module (`jakartaee-course`) and deploy as one WAR. Lab 3 was tested on WildFly 41.

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
- Product data still comes from the in-memory `ProductCatalog`. It becomes a database in Lab 4.

## Project structure
```
src/main/java/com/
├── course/
│   ├── lab1/              HelloServlet, GoodbyeServlet
│   └── catalog/
│       ├── model/         ProductDTO, ProductCatalog
│       ├── service/       ProductService, CartService, OrderService, NotificationService
│       └── web/           servlets and filters
└── example/jakartaeecourse/
                           HelloServlet (starter servlet at /hello-servlet)
src/main/webapp/
├── index.jsp              public landing page, links to /hello-servlet
└── WEB-INF/
    ├── web.xml
    └── views/             header, footer, products, product-detail, cart, checkout
```

## Tech stack
Java 21, Jakarta EE 11 API (Servlet 6.1, EJB, CDI, Transactions), JSP, JSTL 3.0, Maven (WAR packaging), WildFly 41, Payara 7, IntelliJ IDEA.

## Run it
1. Install JDK 21 and Maven, or use the included wrapper (`./mvnw`).
2. Build the WAR:
   ```bash
   ./mvnw clean package
   ```
3. Deploy `target/jakartaee-course.war` to WildFly or Payara, or run it from IntelliJ with a server run configuration.
4. Open:
   - Lab 1: `http://localhost:8080/jakartaee-course/hello`
   - Labs 2 and 3: `http://localhost:8080/jakartaee-course/catalog/products` (same pages; after checkout the server console shows the `[NOTIFY]` line)

The context path is `/jakartaee-course` because `pom.xml` sets `<finalName>jakartaee-course</finalName>`.

## Author
Bara Chebbi
