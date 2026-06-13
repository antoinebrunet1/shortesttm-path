javadoc:
	cd backend/shortesttm_path && mvn clean javadoc:javadoc -Dcheckstyle.skip=true

checkstyle:
	cd backend/shortesttm_path && mvn clean package -DskipTests -Dmaven.javadoc.skip=true

cov:
	cd backend/shortesttm_path && mvn clean verify -DskipTests -Dmaven.javadoc.skip=true -Dcheckstyle.skip=true