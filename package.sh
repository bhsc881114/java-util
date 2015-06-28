mvn clean
mvn package -Dmaven.test.skip=true source:jar -Dmaven.compile.fork=true -e

