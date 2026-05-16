# ====== CONFIG ======
DB_NAME=bulletproof_db
DB_USER=postgres
DB_PASSWORD=postgres
DB_HOST=localhost
DB_PORT=5432

SPRING_RUN=./mvnw spring-boot:run
SPRING_BUILD=./mvnw clean install

# ====== DATABASE ======

create-db:
	psql -U $(DB_USER) -h $(DB_HOST) -p $(DB_PORT) -c "CREATE DATABASE $(DB_NAME);"

drop-db:
	psql -U $(DB_USER) -h $(DB_HOST) -p $(DB_PORT) -c "DROP DATABASE IF EXISTS $(DB_NAME);"

reset-db: drop-db create-db

psql:
	psql -U $(DB_USER) -h $(DB_HOST) -p $(DB_PORT)

# ====== SPRING ======

run:
	$(SPRING_RUN)

build:
	$(SPRING_BUILD)

clean:
	./mvnw clean

# ====== FULL SETUP ======

setup: create-db build run