# Leave Management System

A comprehensive enterprise-grade Leave Management System built with Spring Boot, featuring role-based access control, JWT authentication, containerized deployment with Docker, automated CI/CD pipeline, and Kubernetes orchestration with Helm charts.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [API Documentation](#api-documentation)
- [JWT Authentication](#jwt-authentication)
- [Dockerfile](#dockerfile)
- [CI/CD Pipeline](#cicd-pipeline)
- [Helm Chart](#helm-chart)
- [ArgoCD Integration](#argocd-integration)
- [Deployment](#deployment)

---

## Project Overview

The Leave Management System is a RESTful web application that enables organizations to manage employee leave requests efficiently. The system provides separate workflows for employees and managers, ensuring proper authorization and approval processes.

### Key Features

- **Role-Based Access Control (RBAC)**: Employees and Managers have distinct roles and permissions
- **JWT Authentication**: Secure token-based authentication for all protected endpoints
- **Leave Application Workflow**: Employees can apply for leave, managers can approve/reject requests
- **Status Tracking**: Real-time tracking of leave application status (PENDING, APPROVED, REJECTED)
- **Health Monitoring**: Kubernetes health probes integration
- **Auto-Scaling**: Horizontal Pod Autoscaler (HPA) for dynamic resource scaling
- **GitOps Deployment**: Automated deployment using ArgoCD with Helm charts

### User Roles

1. **Employee**: Can apply for leave and track their leave applications
2. **Manager**: Can view all leave applications and approve/reject them

---

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.3.13
- **Language**: Java 17
- **Build Tool**: Maven 3.x
- **Security**: Spring Security with JWT
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA / Hibernate
- **API Documentation**: SpringDoc OpenAPI 3 (Swagger UI)

### Infrastructure & DevOps
- **Containerization**: Docker
- **Container Registry**: Docker Hub
- **CI/CD**: GitLab CI/CD
- **Security Scanning**: Trivy
- **Code Quality**: SonarQube (optional)
- **Orchestration**: Kubernetes
- **Package Manager**: Helm 3
- **GitOps**: ArgoCD

### Monitoring & Observability
- **Health Checks**: Spring Boot Actuator

---

## API Documentation

The application exposes RESTful APIs with three main controllers. All endpoints return a standardized `BackendResponse` format.

### Base URL
```
http://localhost:8005/lt
```
Note: The application uses a context path `/lt` as configured in `application.properties`.

### Swagger UI
Once the application is running, access the interactive API documentation at:
```
http://localhost:8005/lt/swagger-ui.html
```

---

### 1. Authentication Controller (`/api/auth`)

Public endpoints for user registration and login. No authentication required.

#### 1.1 Register Employee

**Endpoint**: `POST /api/auth/register/employee`

**Description**: Creates a new employee account with `EMPLOYEE` role.

**Request Body**:
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "SecurePassword123!"
}
```

**Response** (201 Created):
```json
{
  "message": "registration successful",
  "status": "success",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": null
  }
}
```

**Error Response** (400 Bad Request):
```json
{
  "message": "Fields 'name', 'Email', 'password' are mandatory",
  "status": "fail",
  "data": "empty"
}
```

---

#### 1.2 Register Manager

**Endpoint**: `POST /api/auth/register/manager`

**Description**: Creates a new manager account with `MANAGER` role.

**Request Body**:
```json
{
  "name": "Jane Manager",
  "email": "jane.manager@example.com",
  "password": "ManagerPass123!"
}
```

**Response** (201 Created):
```json
{
  "message": "registration successful",
  "status": "success",
  "data": {
    "id": 2,
    "name": "Jane Manager",
    "email": "jane.manager@example.com",
    "password": null
  }
}
```

---

#### 1.3 Login

**Endpoint**: `POST /api/auth/login`

**Description**: Authenticates user and returns JWT token.

**Request Body**:
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePassword123!"
}
```

**Response** (200 OK):
```json
{
  "message": "Login successful",
  "status": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsInJvbGUiOiJFTVBMT1lFRSIsImlhdCI6MTY0MDk5MjQwMCwiZXhwIjoxNjQxMDc4ODAwfQ.xyz123...",
    "tokenType": "Bearer"
  }
}
```

**Error Response** (401 Unauthorized):
```json
{
  "message": "Email and password are missing",
  "status": "fail",
  "data": "empty"
}
```

---

### 2. Employee Controller (`/employee`)

Protected endpoints for employees. Requires JWT token with `EMPLOYEE` role.

**Authorization Header**:
```
Authorization: Bearer <JWT_TOKEN>
```

#### 2.1 Apply for Leave

**Endpoint**: `POST /employee/{userId}/apply`

**Description**: Submits a leave application request.

**Path Parameters**:
- `userId` (Long): The employee's user ID

**Request Body**:
```json
{
  "startDate": "2024-12-20",
  "endDate": "2024-12-22",
  "reason": "Family vacation"
}
```

**Response** (201 Created):
```json
{
  "message": "Leave application submitted successfully",
  "status": "success",
  "data": null
}
```

**Error Response** (400 Bad Request):
```json
{
  "message": "Fields 'startDate', 'endDate', 'reason' are mandatory",
  "status": "fail",
  "data": "empty"
}
```

---

#### 2.2 Get Leave Application by ID

**Endpoint**: `GET /employee/{userId}/leaves/{leaveId}`

**Description**: Retrieves details of a specific leave application.

**Path Parameters**:
- `userId` (Long): The employee's user ID
- `leaveId` (Long): The leave application ID

**Response** (200 OK):
```json
{
  "message": "Leave details retrieved successfully",
  "status": "success",
  "data": {
    "id": 1,
    "startDate": "2024-12-20",
    "endDate": "2024-12-22",
    "reason": "Family vacation",
    "status": "PENDING",
    "comment": null,
    "employeeId": 1
  }
}
```

**Leave Status Values**:
- `PENDING`: Awaiting manager approval
- `APPROVED`: Manager approved the request
- `REJECTED`: Manager rejected the request

---

### 3. Manager Controller (`/manager`)

Protected endpoints for managers. Requires JWT token with `MANAGER` role.

**Authorization Header**:
```
Authorization: Bearer <JWT_TOKEN>
```

#### 3.1 Get All Leave Applications

**Endpoint**: `GET /manager/{managerId}/leaves`

**Description**: Retrieves all leave applications visible to the manager.

**Path Parameters**:
- `managerId` (Long): The manager's user ID

**Response** (200 OK):
```json
{
  "message": "Leave applications fetched successfully",
  "status": "success",
  "data": [
    {
      "id": 1,
      "startDate": "2024-12-20",
      "endDate": "2024-12-22",
      "reason": "Family vacation",
      "status": "PENDING",
      "comment": null,
      "employeeId": 1
    },
    {
      "id": 2,
      "startDate": "2024-12-25",
      "endDate": "2024-12-27",
      "reason": "Holiday trip",
      "status": "APPROVED",
      "comment": "Approved. Enjoy your vacation!",
      "employeeId": 2
    }
  ]
}
```

---

#### 3.2 Get Specific Leave Application

**Endpoint**: `GET /manager/{managerId}/leaves/{employeeId}/leave/{leaveId}`

**Description**: Retrieves a specific leave application for an employee.

**Path Parameters**:
- `managerId` (Long): The manager's user ID
- `employeeId` (Long): The employee's user ID
- `leaveId` (Long): The leave application ID

**Response** (200 OK):
```json
{
  "message": "Leave application fetched successfully",
  "status": "success",
  "data": {
    "id": 1,
    "startDate": "2024-12-20",
    "endDate": "2024-12-22",
    "reason": "Family vacation",
    "status": "PENDING",
    "comment": null,
    "employeeId": 1
  }
}
```

---

#### 3.3 Approve/Reject Leave

**Endpoint**: `PUT /manager/{managerId}/leaves/{employeeId}/checkLeave/{leaveId}`

**Description**: Approves or rejects a leave application.

**Path Parameters**:
- `managerId` (Long): The manager's user ID
- `employeeId` (Long): The employee's user ID
- `leaveId` (Long): The leave application ID

**Request Body**:
```json
{
  "status": true,
  "comment": "Approved. Enjoy your vacation!"
}
```

**Note**: 
- `status: true` = APPROVE
- `status: false` = REJECT

**Response** (200 OK):
```json
{
  "message": "Leave application approved successfully",
  "status": "success",
  "data": {
    "id": 1,
    "startDate": "2024-12-20",
    "endDate": "2024-12-22",
    "reason": "Family vacation",
    "status": "APPROVED",
    "comment": "Approved. Enjoy your vacation!",
    "employeeId": 1
  }
}
```

---

### 4. Test Controller (`/api/test`)

Public endpoints for health checks and testing.

#### 4.1 Readiness Probe

**Endpoint**: `GET /api/test/areYouInReadyState`

**Description**: Returns the current process ID. Used by Kubernetes readiness probe.

**Response** (200 OK):
```
12345
```
*(Returns process ID as plain text)*

---

## JWT Authentication

### How It Works

1. **Login**: User submits credentials to `/api/auth/login`
2. **Token Generation**: Server validates credentials and generates JWT token containing:
   - Subject (email)
   - Role (EMPLOYEE/MANAGER)
   - Expiration time
3. **Token Usage**: Client includes token in `Authorization: Bearer <token>` header for protected endpoints
4. **Token Validation**: `JwtAuthenticationFilter` intercepts requests, validates token, and sets authentication context

### Security Configuration

```java
// Public Endpoints (No authentication required)
- /api/auth/**          // Registration and login
- /api/test/**          // Health checks
- /actuator/prometheus  // Metrics
- /swagger-ui/**        // API documentation

// Protected Endpoints (Require authentication)
- /employee/**          // Requires ROLE_EMPLOYEE
- /manager/**           // Requires ROLE_MANAGER
```

### Example Usage

```bash
# 1. Login and get token
curl -X POST http://localhost:8005/lt/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePassword123!"
  }'

# 2. Use token to access protected endpoint
curl -X GET http://localhost:8005/lt/employee/1/leaves/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## Dockerfile

The Dockerfile is optimized for security, size, and best practices.

### Dockerfile Structure

```dockerfile
FROM eclipse-temurin:17-jre-ubi9-minimal

# Create non-root user
RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

# Copy JAR file
COPY --chown=spring:spring target/leave-tracker.jar leave-tracker.jar

# Switch to non-root user
USER spring:spring

EXPOSE 8005

# Use JAVA_OPTS from environment (set in deployment yaml)
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar leave-tracker.jar"]
```

### Key Features

1. **Minimal Base Image**: `eclipse-temurin:17-jre-ubi9-minimal` - Lightweight JRE-only image
2. **Non-Root User**: Application runs as `spring` user instead of root (security best practice)
3. **Fixed JAR Name**: JAR is named `leave-tracker.jar` (via `finalName` in `pom.xml`)
4. **JAVA_OPTS Support**: Accepts JVM options from Kubernetes deployment (e.g., memory limits, GC settings)
5. **Port Exposure**: Exposes port 8005 for container-to-container communication

### Building the Image

```bash
# Build JAR first
mvn clean package -DskipTests

# Build Docker image
docker build -t leave-tracker:latest .

# Run locally
docker run -p 8005:8005 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/leave-tracker-db \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  leave-tracker:latest
```

---

## CI/CD Pipeline

The GitLab CI/CD pipeline automates the entire build, test, security scanning, and deployment process.

### Pipeline Stages

```
┌──────────────┐
│ maven-build  │ → Compile and package application
└──────┬───────┘
       │
       ▼
┌──────────────┐
│ docker-build │ → Build Docker image with commit hash tag
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  trivy-scan  │ → Security vulnerability scanning
└──────┬───────┘
       │
       ▼
┌──────────────┐
│ docker-push  │ → Push image to Docker Hub
└──────┬───────┘
       │
       ▼
┌──────────────┐
│ helm-update  │ → Update Helm chart & create MR
└──────────────┘
```

### Stage Details

#### 1. Maven Build
- **Image**: `maven:3-eclipse-temurin-17`
- **Actions**:
  - Compiles Java source code
  - Runs tests (if enabled)
  - Packages application into JAR: `leave-tracker.jar`
- **Artifacts**: `target/*.jar` (expires in 1 hour)

#### 2. Docker Build
- **Runner**: Local runner with Docker support
- **Actions**:
  - Logs into Docker Hub
  - Builds Docker image: `${DOCKER_IMAGE}:${CI_COMMIT_SHORT_SHA}`
  - Tags as `latest`: `${DOCKER_IMAGE}:latest`
  - Saves image as tarball for Trivy scanning
- **Dependencies**: Requires `maven-build` artifacts

#### 3. Trivy Scan
- **Image**: `aquasec/trivy:latest`
- **Actions**:
  - Scans Docker image for vulnerabilities (ALL severities)
  - Generates reports:
    - `reports/trivy-report.txt` (table format)
    - `reports/trivy-report.json` (JSON format)
- **Artifacts**: Reports saved for 1 hour

#### 4. Docker Push
- **Runner**: Local runner
- **Actions**:
  - Pushes image with commit hash tag
  - Pushes `latest` tag (with retry logic for authentication)
- **Dependencies**: Requires `docker-build` and `trivy-scan` completion

#### 5. Helm Update
- **Image**: `alpine/git:latest`
- **Actions**:
  1. Updates `values.yaml` with new image tag (commit hash)
  2. Commits changes to `dev` branch
  3. Pushes to repository
  4. Creates or updates Merge Request (`dev` → `main`)
- **Dependencies**: Requires `docker-push` completion

### Pipeline Triggers

- **Branch**: `dev` (all development happens here)
- **Manual**: Can be triggered manually from GitLab UI

### GitLab CI/CD Variables

Required variables in GitLab Settings → CI/CD → Variables:

| Variable | Description | Example | Masked | Protected |
|----------|-------------|---------|--------|-----------|
| `DOCKER_HUB_USERNAME` | Docker Hub username | `your-username` | ❌ | ✅ |
| `DOCKER_HUB_PASSWORD` | Docker Hub password/token | `your-token` | ✅ | ✅ |
| `IMAGE_NAME` | Docker image name | `leave_tracker` | ❌ | ❌ |
| `GITLAB_API_TOKEN` | GitLab Personal Access Token (for MR creation) | `glpat-xxx...` | ✅ | ✅ |

### Pipeline Flow Example

1. Developer pushes code to `dev` branch
2. Pipeline automatically triggers
3. Maven builds the application
4. Docker image is built and tagged with commit hash
5. Trivy scans for vulnerabilities
6. Image is pushed to Docker Hub
7. Helm chart is updated with new image tag
8. MR is created/updated to `main` branch
9. ArgoCD detects change and deploys to Kubernetes

---

## Helm Chart

The Helm chart provides a templated Kubernetes deployment for the Leave Management System.

### Chart Structure

```
kubernetes-manifests/helm-chart/
├── Chart.yaml              # Chart metadata
├── values.yaml             # Default configuration values
└── templates/
    ├── deployment.yaml     # Kubernetes Deployment
    ├── service.yaml        # Kubernetes Service
    ├── secret.yaml         # Kubernetes Secret (database credentials)
    └── hpa.yaml            # Horizontal Pod Autoscaler
```

### Chart Configuration (`values.yaml`)

```yaml
# Application name
name: leave-tracker
namespace: leavetracking

# Replica count
replicaCount: 1

# Docker image configuration
image:
  repository: shivatejanam854/leave_tracker
  pullPolicy: Always
  tag: "63f0a4fc"  # Updated automatically by CI/CD

# Service configuration
service:
  name: leave-tracker-svc
  type: ClusterIP
  port: 8005
  targetPort: 8005

# Database credentials (base64 encoded)
database:
  url: amRiYzpteXNxbDovL2hvc3QubWluaWt1YmUuaW50ZXJuYWw6MzMwNi9sZWF2ZS10cmFja2VyLWRi
  username: cm9vdA==
  password: U2hpdkBAQjEy

# Resources (Guaranteed QoS)
resources:
  limits:
    cpu: 200m
    memory: 250Mi
  requests:
    cpu: 200m
    memory: 250Mi

# HPA configuration
hpa:
  enabled: true
  minReplicas: 1
  maxReplicas: 5
  targetCPUUtilizationPercentage: 70
  targetMemoryUtilizationPercentage: 80

# Health probes
probes:
  startup:    # TCP check on port 8005
  liveness:   # TCP check on port 8005
  readiness:  # HTTP GET /api/test/areYouInReadyState
```

### Key Features

#### 1. Deployment (`deployment.yaml`)
- **Resource Limits**: Guaranteed QoS class (requests = limits)
  - CPU: 200m
  - Memory: 250Mi
- **Health Probes**:
  - **Startup Probe**: TCP check (allows slow startup)
  - **Liveness Probe**: TCP check (restarts container if unhealthy)
  - **Readiness Probe**: HTTP GET `/api/test/areYouInReadyState` (determines if pod can receive traffic)
- **Environment Variables**: Database credentials from Kubernetes Secret
- **JAVA_OPTS**: Configurable JVM options

#### 2. Service (`service.yaml`)
- **Type**: ClusterIP (internal Kubernetes service)
- **Port**: 8005
- **Name**: `leave-tracker-svc`
- **Selector**: `app: leave-tracker`

#### 3. Secret (`secret.yaml`)
- **Type**: Opaque
- **Data**: Base64-encoded database credentials
- **Keys**:
  - `SPRING_DATASOURCE_URL`
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`

#### 4. Horizontal Pod Autoscaler (`hpa.yaml`)
- **Min Replicas**: 1
- **Max Replicas**: 5
- **Metrics**: CPU (70%) and Memory (80%)
- **Scaling Behavior**:
  - **Scale Up**: 1 pod every 15 seconds
  - **Scale Down**: 1 pod every 60 seconds (300s stabilization window)

### Installing the Chart

```bash
# Install/Upgrade
helm upgrade --install leave-tracker \
  ./kubernetes-manifests/helm-chart \
  --namespace leavetracking \
  --create-namespace \
  --set image.tag=63f0a4fc

# Verify deployment
kubectl get pods -n leavetracking
kubectl get svc -n leavetracking
kubectl get hpa -n leavetracking
```

---

## ArgoCD Integration

### What is ArgoCD?

ArgoCD is a GitOps continuous delivery tool for Kubernetes. It monitors your Git repository for changes and automatically syncs them to your Kubernetes cluster.

### How It Works

```
┌─────────────────────┐
│   Git Repository    │
│   (dev branch)      │
│   └── helm-chart/   │
│       └── values.yaml│
└──────────┬──────────┘
           │ ArgoCD watches
           ▼
┌─────────────────────┐
│      ArgoCD         │
│   (GitOps Engine)   │
│   - Detects changes │
│   - Syncs manifests │
└──────────┬──────────┘
           │ Applies changes
           ▼
┌─────────────────────┐
│  Kubernetes Cluster │
│   - Deployment      │
│   - Service         │
│   - HPA             │
└─────────────────────┘
```

### Deployment Flow

1. **CI/CD Pipeline** (`helm-update` stage):
   - Updates `values.yaml` with new Docker image tag
   - Commits to `dev` branch
   - Creates/updates MR to `main`

2. **ArgoCD Configuration**:
   - ArgoCD watches the `dev` branch (or `main` after MR merge)
   - Repository: `https://gitlab.com/leave-tracker1/leave-tracker`
   - Path: `kubernetes-manifests/helm-chart`
   - Namespace: `leavetracking`

3. **Automatic Sync**:
   - ArgoCD detects changes in `values.yaml` (new image tag)
   - Renders Helm templates
   - Compares with current cluster state
   - Applies changes (rolling update with new image)

4. **Deployment Verification**:
   - ArgoCD monitors deployment status
   - Reports sync status in ArgoCD UI
   - Health checks ensure pods are running

### ArgoCD Application Configuration

```yaml
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: leave-tracker
  namespace: argocd
spec:
  project: default
  source:
    repoURL: https://gitlab.com/leave-tracker1/leave-tracker.git
    targetRevision: dev
    path: kubernetes-manifests/helm-chart
    helm:
      valueFiles:
        - values.yaml
  destination:
    server: https://kubernetes.default.svc
    namespace: leavetracking
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
    syncOptions:
      - CreateNamespace=true
```

### Benefits

- **Automated Deployments**: No manual `kubectl` commands needed
- **Git as Source of Truth**: All configuration in Git repository
- **Rollback Capability**: Revert to previous versions via Git
- **Visibility**: ArgoCD UI shows deployment status and history
- **Self-Healing**: Automatically syncs if cluster state drifts

---

## Deployment

### Minikube Deployment (Local)

1. **Start Minikube**:
```bash
minikube start
```

2. **Set up MySQL** (outside Kubernetes):
```bash
# Run MySQL on host machine or use a service
```

3. **Update `values.yaml`**:
```yaml
database:
  # Use host.minikube.internal for accessing host MySQL
  url: amRiYzpteXNxbDovL2hvc3QubWluaWt1YmUuaW50ZXJuYWw6MzMwNi9sZWF2ZS10cmFja2VyLWRi
```

4. **Install Helm Chart**:
```bash
helm install leave-tracker ./kubernetes-manifests/helm-chart \
  --namespace leavetracking \
  --create-namespace
```

5. **Access Application**:
```bash
# Port forward to access locally
kubectl port-forward svc/leave-tracker-svc 8005:8005 -n leavetracking

# Access at http://localhost:8005/lt
```

### Production Deployment with ArgoCD

1. **Install ArgoCD**:
```bash
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

2. **Create ArgoCD Application**:
```bash
kubectl apply -f - <<EOF
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: leave-tracker
  namespace: argocd
spec:
  project: default
  source:
    repoURL: https://gitlab.com/leave-tracker1/leave-tracker.git
    targetRevision: dev
    path: kubernetes-manifests/helm-chart
  destination:
    server: https://kubernetes.default.svc
    namespace: leavetracking
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
EOF
```

3. **Monitor Deployment**:
```bash
# Check ArgoCD sync status
argocd app get leave-tracker

# Check Kubernetes resources
kubectl get all -n leavetracking
```

---

## Monitoring & Health Checks

### Health Endpoints

- **Health**: `http://localhost:8005/lt/actuator/health`
- **Readiness**: `http://localhost:8005/lt/api/test/areYouInReadyState`
- **Info**: `http://localhost:8005/lt/actuator/info`

### Kubernetes Health Probes

1. **Startup Probe** (TCP on port 8005):
   - Waits up to 150 seconds (30 attempts × 5s interval)
   - Ensures application has time to start

2. **Liveness Probe** (TCP on port 8005):
   - Checks every 10 seconds
   - Restarts pod if unhealthy

3. **Readiness Probe** (HTTP GET `/api/test/areYouInReadyState`):
   - Checks every 5 seconds
   - Removes pod from service if not ready

---

## Security Features

1. **JWT Authentication**: Secure token-based authentication
2. **Role-Based Access Control**: Employees and Managers have separate permissions
3. **Non-Root Container**: Application runs as non-root user
4. **Secret Management**: Database credentials stored in Kubernetes Secrets
5. **Security Scanning**: Trivy scans Docker images for vulnerabilities
6. **HTTPS Ready**: Configured for TLS termination at ingress level

---

## Project Structure

```
leave-tracker/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── service/             # Business Logic Interfaces
│   │   │   ├── serviceImpl/         # Business Logic Implementation
│   │   │   ├── repository/          # JPA Repositories
│   │   │   ├── entity/              # JPA Entities
│   │   │   ├── payload/             # DTOs (Data Transfer Objects)
│   │   │   ├── security/            # JWT Filter & Provider
│   │   │   ├── securityconfig/      # Spring Security Configuration
│   │   │   └── exception/           # Custom Exceptions
│   │   └── resources/
│   │       └── application.properties
│   └── test/                        # Unit Tests
├── kubernetes-manifests/
│   └── helm-chart/
│       ├── Chart.yaml
│       ├── values.yaml
│       └── templates/
│           ├── deployment.yaml
│           ├── service.yaml
│           ├── secret.yaml
│           └── hpa.yaml
├── Dockerfile
├── .dockerignore
├── .gitlab-ci.yml                   # CI/CD Pipeline
├── pom.xml                          # Maven Configuration
└── README.md                        # This file
```

---

**Last Updated**: 2024-11-15
