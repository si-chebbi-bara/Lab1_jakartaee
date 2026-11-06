# Jakarta EE Course Project (Labs 1 and 2)

A Maven-based Jakarta EE web application built across the first two labs of a university Jakarta EE course (Horizon School of Digital Technologies, L3). Both labs live in the same module (`jakartaee-course`) and deploy as one WAR to WildFly or Payara.

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

## Project structure
```
src/main/java/com/
├── course/
│   ├── lab1/              HelloServlet, GoodbyeServlet
│   └── catalog/
│       ├── model/         ProductDTO, ProductCatalog
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
Java 21, Jakarta Servlet 6.1, JSP, JSTL 3.0, Maven (WAR packaging), WildFly 41, Payara 7, IntelliJ IDEA.

## Run it
1. Install JDK 21 and Maven, or use the included wrapper (`./mvnw`).
2. Build the WAR:
   ```bash
   ./mvnw clean package
   ```
3. Deploy `target/jakartaee-course.war` to WildFly or Payara, or run it from IntelliJ with a server run configuration.
4. Open:
   - Lab 1: `http://localhost:8080/jakartaee-course/hello`
   - Lab 2: `http://localhost:8080/jakartaee-course/catalog/products`

The context path is `/jakartaee-course` because `pom.xml` sets `<finalName>jakartaee-course</finalName>`.

## Author
Bara Chebbi
