cat > README.md << 'EOF'
# Lab 1 - Jakarta EE

Introductory Jakarta EE lab: servlets deployed on Payara.

## What's inside
- `HelloServlet`: mapped to `/hello`, answers GET requests with "Hello, Enterprise World!"
- `GoodbyeServlet`: second servlet in the same package
- `index.jsp`: landing page
- `web.xml`: deployment descriptor

## Tech
- Java, Jakarta Servlet API
- Maven
- Payara Server

## Run
1. Open the project in IntelliJ IDEA.
2. Run it with the Payara configuration.
3. Visit `http://localhost:8080/<context-path>/hello`

## Author
Bara Chebbi
EOF
git add README.md
git commit -m "Add README"
git push
