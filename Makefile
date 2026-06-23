javadoc:
	cd backend/shortesttm_path && mvn clean javadoc:javadoc -Dcheckstyle.skip=true

checkstyle-backend:
	cd backend/shortesttm_path && mvn clean package -DskipTests -Dmaven.javadoc.skip=true

cov-backend:
	cd backend/shortesttm_path && mvn clean verify -Dmaven.javadoc.skip=true -Dcheckstyle.skip=true

cov-frontend:
	cd frontend/shortesttm-path && npx ng test --watch=false --browsers=ChromeHeadless --code-coverage \
	| grep "Lines        :" | cut -d ':' -f 2 | cut -d '%' -f 1 | cut -d ' ' -f 2 | awk '{ if ($$1 < 80) exit 1 }'

api-tests:
	cd api_testing/shortesttm_path_api_testing && mvn clean test

run-backend:
	cd backend/shortesttm_path && ./mvnw spring-boot:run

run-backend-nohup-background:
	cd backend/shortesttm_path && chmod +x mvnw && nohup ./mvnw spring-boot:run &

wait-for-api:
	bash wait_for_api.sh