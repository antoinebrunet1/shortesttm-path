cd_cmd = cd backend/shortesttm_path

javadoc:
	$(cd_cmd) && mvn clean javadoc:javadoc -Dcheckstyle.skip=true

checkstyle:
	$(cd_cmd) && mvn clean package -DskipTests -Dmaven.javadoc.skip=true

cov:
	$(cd_cmd) && mvn clean verify -DskipTests -Dmaven.javadoc.skip=true -Dcheckstyle.skip=true