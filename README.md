# Food Delivery Order System

Java project for processing and validating food delivery orders, with JUnit tests, Docker, Jenkins CI/CD, and Kubernetes deployment.

---

## Project Structure

```
food-delivery-order-system/
├── src/
│   ├── main/java/com/fooddelivery/
│   │   ├── Main.java               ← Entry point
│   │   ├── OrderItem.java          ← Single item in an order
│   │   ├── Order.java              ← Order model
│   │   ├── OrderValidator.java     ← Validation logic
│   │   └── OrderService.java       ← Order processing logic
│   └── test/java/com/fooddelivery/
│       ├── OrderValidatorTest.java ← Validation + invalid scenario tests
│       └── OrderServiceTest.java   ← Order processing tests
├── k8s/
│   ├── deployment.yaml
│   └── service.yaml
├── Dockerfile
├── Jenkinsfile
└── pom.xml
```

---

## Step 1 — Build Locally

```bash
cd food-delivery-order-system
mvn clean package
java -jar target/food-delivery-order-system-1.0.0.jar
```

## Step 2 — Run Tests

```bash
mvn test
```

## Step 3 — Push to GitHub

```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/food-delivery-order-system.git
git push -u origin main
```

## Step 4 — Jenkins Setup

1. Open Jenkins → http://localhost:8080
2. Install plugins: **Git**, **Pipeline**, **Docker Pipeline**
3. Add DockerHub credentials:
   - Dashboard → Manage Jenkins → Credentials
   - ID: `dockerhub-credentials`  ← must match Jenkinsfile exactly
4. New Item → **Pipeline** → name: `food-delivery-pipeline`
5. Pipeline → Definition: **Pipeline script from SCM**
6. SCM: Git → paste your GitHub repo URL
7. Script Path: `Jenkinsfile`
8. In Jenkinsfile replace:
   - `YOUR_DOCKERHUB_USERNAME` → your DockerHub username
   - `YOUR_GITHUB_USERNAME`   → your GitHub username

## Step 5 — Build Pipeline

Click **Build Now** in Jenkins. It will:
- ✅ Clone → Build → Run Tests → Docker Build → Push → Deploy to K8s

## Step 6 — Kubernetes

```bash
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl get pods
kubectl get svc
```

---

## Replace Before Use

| Placeholder             | Replace With            |
|-------------------------|-------------------------|
| YOUR_DOCKERHUB_USERNAME | Your DockerHub username |
| YOUR_GITHUB_USERNAME    | Your GitHub username    |
