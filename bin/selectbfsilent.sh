#!/bin/sh
java -Xmx1000m -Xms1000m -cp ./selectbf.jar:../lib/commons-net-1.3.0.jar:../lib/jakarta-oro-2.0.8.jar:../lib/jdom.jar:../lib/commons-beanutils-1.7dev.jar:../lib/commons-collections-3.1.jar:../lib/commons-logging-1.0.4.jar:../lib/log4j-1.2.9.jar:../lib/swt.jar:../lib/mysql-connector-java-5.0.5-bin.jar: org.selectbf.SelectBf >> Selectbf.out
