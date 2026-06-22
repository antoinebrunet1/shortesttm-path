cd_cmd = cd backend/shortesttm_path

javadoc:
	$(cd_cmd) && mvn clean javadoc:javadoc -Dcheckstyle.skip=true

checkstyle-backend:
	$(cd_cmd) && mvn clean package -DskipTests -Dmaven.javadoc.skip=true

cov-backend:
	$(cd_cmd) && mvn clean verify -Dmaven.javadoc.skip=true -Dcheckstyle.skip=true

cov-frontend:
	cd frontend/shortesttm-path && npx ng test --watch=false --browsers=ChromeHeadless --code-coverage \
	| grep "Lines        :" | cut -d ':' -f 2 | cut -d '%' -f 1 | cut -d ' ' -f 2 | awk '{ if ($$1 < 80) exit 1 }'