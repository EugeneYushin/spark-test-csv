# spark-test-csv

Example of how to use Spark with CSV file format.

##### Usage
Build jar:
```
sbt clean assembly
[info] UtilsTest:
[info] confToMap
[info] - should return Map for passed Config
[info] confToMap
[info] - should return String representation on non-String value in Config
[info] Run completed in 892 milliseconds.
[info] Total number of tests run: 2
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 2, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
```
Submit example:
```
spark-submit \
  --name "spark-test-csv" \
  --driver-java-options "-Duser.timezone=UTC -Dlog4j.configuration=file:///home/cloudera/log4j.properties" \
  --class my.fancy.organization.Run \
  --master yarn \
  --deploy-mode client \
  /home/cloudera/spark-test-csv-assembly-1.0.jar \
  file:///media/sf_shared/spark-test-csv/sample/sample.txt /home/cloudera/application.conf
```

Log file: [log.txt](./log.txt)

Config file: [application.conf](./src/main/resources/application.conf)


##### Notes
Spark2+ csv plugin treat empty string as NULL field. Spark2 doesn't respect `treatEmptyValuesAsNulls` option, while 1.6 does. There's no way to change this behavior out-of-the-box. 

Please refer the following threads for more details:  
https://issues.apache.org/jira/browse/SPARK-17916  
https://issues.apache.org/jira/browse/SPARK-15125  
https://github.com/apache/spark/pull/12904