# Meeting Room Booking - Runnable Scaffold (Java 17 + React 19 + Kafka + Kubernetes)

Includes:
- **backend/api-service**: Spring Boot 3 + Java 17 (JWT auth, rooms, bookings, Kafka producer)
- **backend/notification-service**: Spring Boot 3 + Java 17 (Kafka consumer -> email simulation logs)
- **frontend**: React 19 + Vite (login, search, book)
- **infra/docker-compose**: local run (MySQL + Kafka KRaft + services)
- **infra/k8s**: Kubernetes manifests (any cloud)

## Run locally (Docker Compose)
```bash
cd infra/docker-compose
docker compose up --build
```
- Frontend: http://localhost:5173
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

## Kubernetes (any cloud)
1) Build & push images:
```bash
docker build -t <repo>/api-service:1.0 backend/api-service
docker build -t <repo>/notification-service:1.0 backend/notification-service
docker build -t <repo>/frontend:1.0 frontend
docker push <repo>/api-service:1.0
docker push <repo>/notification-service:1.0
docker push <repo>/frontend:1.0
```
2) Update images in `infra/k8s/*` (replace `yourrepo/...`), then:
```bash
kubectl apply -f infra/k8s/namespace.yaml
kubectl apply -f infra/k8s/mysql.yaml
kubectl apply -f infra/k8s/kafka-kraft.yaml
kubectl apply -f infra/k8s/api-service.yaml
kubectl apply -f infra/k8s/notification-service.yaml
kubectl apply -f infra/k8s/frontend.yaml
kubectl apply -f infra/k8s/ingress.yaml
```
