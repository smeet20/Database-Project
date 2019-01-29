sudo apt -y install mysql-server
sudo apt -y install mysql-client
mysql --version
sudo apt -y install openjdk-8-jdk-headless
wget -O mysql_con.deb "https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java_8.0.14-1ubuntu18.04_all.deb"
sudo apt -y  install ./mysql_con.deb
