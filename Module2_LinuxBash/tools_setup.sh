#!/bin/bash

readonly DEFAULT_USER="User from script"

readonly JDK_LINK="https://download.oracle.com/otn-pub/java/jdk/15.0.1+9/51f4f36ad4ef43e39d0dfdbaf6549e32/jdk-15.0.1_linux-x64_bin.rpm"
readonly JDK_NAME="jdk-15.0.1_linux-x64_bin.rpm"
readonly JAVA_VER=jdk-15.0.1

readonly MAVEN_LINK="https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz"
readonly MAVEN_NAME="apache-maven-3.6.3-bin.tar.gz"
readonly MAVEN_VER=apache-maven-3.6.3

readonly POSTGRESQL_LINK="https://download.postgresql.org/pub/repos/yum/reporpms/EL-8-x86_64/pgdg-redhat-repo-latest.noarch.rpm"
readonly POSTGRESQL_VER="pgsql-13"
readonly POSTGRESQL_FULL_NAME_VER="postgresql13"
readonly POSTGRESQL_SERVICE="postgresql-13"

readonly MYSQL_LINK="https://dev.mysql.com/get/mysql80-community-release-el8-1.noarch.rpm"
readonly MYSQL_SERVICE="mysqld"

#######################################
# Check argument for option
# Outputs:
#   Writes error message if argument is incorrect
#######################################
check_args () {
if [[ "${OPTARG}" =~ ^-[j/g/m/p/b/h/q/v]$ ]]; then
  echo "Bad argument $OPTARG for option $opt!"
  exit 1
fi
}

#######################################
# Installs oracle jdk and defines system variables such as JAVA_HOME, PATH, CLASSPATH 
# Globals:
#   JDK_LINK
#   JDK_NAME
#   JAVA_VER
# Arguments:
#   None
# Outputs:
#   Defined variables JAVA_HOME, PATH, CLASSPATH and installed oracle jdk
#######################################
jdk_install () {
if [[ ! -e "/usr/java/${JAVA_VER}" ]]; then
  wget -q --no-check-certificate -c --header "Cookie: oraclelicense=accept-securebackup-cookie" ${JDK_LINK}
  rpm -ivh "${JDK_NAME}"
  rm -fv "${JDK_NAME}"
  jdk_path="$(dirname "$(dirname "$(readlink -f "$(which java)")")")"
  echo "export JAVA_HOME=${jdk_path}" >> ~/.bashrc
  source ~/.bashrc
  echo "export PATH=${JAVA_HOME}/bin:${PATH}" >> ~/.bashrc
  source ~/.bashrc
  echo "export CLASSPATH=.:${JAVA_HOME}/jre/lib:${JAVA_HOME}/lib:${JAVA_HOME}/lib/tools.jar" >> ~/.bashrc
  source ~/.bashrc
fi
}

#######################################
# Display java version and variables: JAVA_HOME, PATH, CLASSPATH 
# Arguments:
#   None
#######################################
jdk_info () {
java -version
echo "JAVA_HOME define as $JAVA_HOME"
echo "PATH define as $PATH"
echo "CLASSPATH define as $CLASSPATH"
}

#######################################
# Installs git and defines username in git config 
# Globals:
#   DEFAULT_USER
# Arguments:
#   username for git config
# Outputs:
#   Installed git and defined username in git config
#######################################
git_install () {
if [[ ! -e "/usr/bin/git" ]]; then
  dnf install git -y
fi
if [[ -n "$1" ]]; then
  git config --global user.name "$1"
else
  git config --global user.name "${DEFAULT_USER}"
fi
}

#######################################
# Display git version and username in git config
# Arguments:
#   None
#######################################
git_info () {
git --version
echo "git config"
git config --list
}

#######################################
# Installs maven and defines system variables such as M2_HOME, PATH 
# Globals:
#   MAVEN_LINK
#   MAVEN_NAME
#   MAVEN_VER
# Arguments:
#   None
# Outputs:
#   Defined variables M2_HOME, PATH and installed maven
#######################################
maven_install () {
if [[ ! -e "/usr/${MAVEN_VER}" ]]; then
  wget -q -P /usr/ "${MAVEN_LINK}"
  tar -xvf "/usr/${MAVEN_NAME}" -C /usr
  rm -fv "/usr/${MAVEN_NAME}"
  echo "export M2_HOME=/usr/${MAVEN_VER}" >> ~/.bashrc
  source ~/.bashrc
  echo "export PATH=${M2_HOME}/bin:${PATH}" >> ~/.bashrc
  source ~/.bashrc
fi
}

#######################################
# Display variables: M2_HOME, PATH
# Arguments:
#   None
#######################################
maven_info () {
echo "M2_HOME define as $M2_HOME"
echo "PATH define as $PATH"
}

#######################################
# Installs postgresql
# Globals:
#   POSTGRESQL_LINK
#   POSTGRESQL_VER
#   POSTGRESQL_FULL_NAME_VER
#   POSTGRESQL_SERVICE
# Arguments:
#   None
# Outputs:
#   Installed postgresql and postgresql-server
#######################################
postgre_install () {	
if [[ ! -e "/usr/${POSTGRESQL_VER}" ]]; then
  # Install rpm from repository
  dnf install -y "${POSTGRESQL_LINK}"
  # Disable the built-in PostgreSQL module:
  dnf -qy module disable postgresql
  # Install PostgreSQL
  dnf install -y "${POSTGRESQL_FULL_NAME_VER}" "${POSTGRESQL_FULL_NAME_VER}-server"
  # Initialize the database
  /usr/${POSTGRESQL_VER}/bin/${POSTGRESQL_SERVICE}-setup initdb
fi
}

#######################################
# Start postgresql
# Globals:
#   POSTGRESQL_SERVICE
# Arguments:
#   None
# Outputs:
#   Active postgresql service
#######################################
postgre_run () {
if [[ "$(systemctl is-active ${POSTGRESQL_SERVICE})" == "inactive" ]]; then
  systemctl enable "${POSTGRESQL_SERVICE}"
  systemctl start "${POSTGRESQL_SERVICE}"
fi
}

#######################################
# Display postgresql version and postgresql service activity
# Arguments:
#   None
#######################################
postgre_info () {
echo "PostgreSQL service is $(systemctl is-active ${POSTGRESQL_SERVICE})"
psql --version
}

#######################################
# Installs mysql
# Globals:
#   MYSQL_LINK
# Arguments:
#   None
# Outputs:
#   Installed mysql and mysql-server
#######################################
mysql_install () {
if [[ ! -e "/usr/bin/mysql" ]]; then
  dnf install -y "${MYSQL_LINK}"
  dnf install -y mysql-server
fi
}

#######################################
# Start mysql
# Globals:
#   MYSQL_SERVICE
# Arguments:
#   None
# Outputs:
#   Active mysql service
#######################################
mysql_run () {
if [[ "$(systemctl is-active ${MYSQL_SERVICE})" == "inactive" ]]; then
  systemctl start "${MYSQL_SERVICE}"
fi
}

#######################################
# Display mysql version and mysql service activity
# Arguments:
#   None
#######################################
mysql_info () {
echo "MySQL service is $(systemctl is-active ${MYSQL_SERVICE})"
mysql --version
}

#######################################
# Installs all tools
# Arguments:
#   quiet or verbose mode
# Outputs:
#   installation process information
#######################################
install_all () {
if [[ -n "$1" && "$1" == "quiet" ]]; then
  exec 1>> tools_install.log 2>&1
fi
  jdk_install
  jdk_info > /dev/tty
  git_install
  git_info > /dev/tty
  maven_install
  maven_info > /dev/tty
  postgre_install
  postgre_run
  postgre_info > /dev/tty
  mysql_install
  mysql_run
  mysql_info > /dev/tty
}

#######################################
# Display help manual for script
# Arguments:
#   None
# Outputs:
#   manual for script
#######################################
show_help () {
help_descr="Script helps to install and simply configures such tools as JDK, Git, Maven, PosqreSQL, MySQL
Usage: $(basename "$0") [OPTIONS]
Where options include:
-h or --help - the only option required to view the manual for this script
-q - the only option required to run script in quiet mode (results messages only)
-v - the only option required to run script in verbose mode (run by default)
-j - install jdk only
-g=USERNAME - install git only, USERNAME define the username in git-config
-m - install maven only
-p - install postgresql only
-b - install mysql only
j/g/m/p/b/ - these options can be combined with each other"
echo "$help_descr"
}

#######################################
# Main line of script
#######################################
main () {
if [[ $# -lt 1 ]]; then
  install_all
  exit 1
elif [[ $# -eq 1 && $1 == "--help" ]]; then
  show_help
  exit 1
elif [[ $# -eq 1 && $1 == "-h" ]]; then
  show_help
  exit 1
elif [[ $# -eq 1 && $1 == "-q" ]]; then
  install_all quiet
  exit 1
elif [[ $# -eq 1 && $1 == "-v" ]]; then
  install_all
  exit 1
else
  while getopts "jg:mpb" opt; do
    case "$opt" in
	  j) 
        jdk_install
        jdk_info
        ;;
      g) 
	    check_args
	    git_install "$OPTARG"
	    git_info
	    ;;
	  m)
	    maven_install
	    maven_info
	    ;;
	  p)
	    postgre_install
	    postgre_run
	    postgre_info
	    ;;
	  b)
	    mysql_install
	    mysql_run
	    mysql_info
	    ;;
	  *) echo "Unexpected option ${opt}" ;;
    esac
  done
fi
}

main "$@"