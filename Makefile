run_backend_cmd = cd backend/shortesttm_path && ./mvnw spring-boot:run

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
	$(run_backend_cmd)

run-backend-nohup-background:
	nohup $(run_backend_cmd) &

wait-for-api:
	bash wait_for_api.sh