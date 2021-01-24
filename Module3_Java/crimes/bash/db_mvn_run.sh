#!/bin/bash

readonly POSTGRESQL_SERVICE="postgresql-13"
readonly MAIN_CLASS="by.epam.crimes.App"

#######################################
# Display tools versions and variables: JAVA_HOME, M2_HOME 
# Arguments:
#   None
#######################################
info () {
java -version
mvn --version
echo "JAVA_HOME define as $JAVA_HOME"
echo "M2_HOME define as $M2_HOME"
echo "PostgreSQL service is $(systemctl is-active ${POSTGRESQL_SERVICE})"
psql --version
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
# Create or recreate database with sql-script
# Arguments:
#   None
# Outputs:
#   Blank database with tables
#######################################
create_db () {
sudo -i -u postgres
'cd /usr/apps/crimes/sql || exit 1'
psql -f db_script.sql
}

#######################################
# Clean target directory
# Arguments:
#   None
# Outputs:
#   Deleted target directory
#######################################
mvn_clean () {
'cd /usr/apps/crimes || exit 1'
mvn clean
}

#######################################
# Compile java classes
# Arguments:
#   None
# Outputs:
#   Target directory with classes
#######################################
mvn_compile () {
'cd /usr/apps/crimes || exit 1'
mvn compile
}

#######################################
# Execute java program and show help message
# Globals:
#   MAIN_CLASS
# Arguments:
#   None
# Outputs:
#   Java program help message
#######################################
mvn_exec_help () {
'cd /usr/apps/crimes || exit 1'
mvn exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="-help"
}

#######################################
# Execute java program and take input file with default date
# Globals:
#   MAIN_CLASS
# Arguments:
#   Input file for java program
# Outputs:
#   Running program
#######################################
mvn_exec () {
'cd /usr/apps/crimes || exit 1'
mvn exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--Dinputfile=$1"
}

#######################################
# Execute java program and take input file with concrete date
# Globals:
#   MAIN_CLASS
# Arguments:
#   Input file for java program
#  	Date for java program
# Outputs:
#   Running program
#######################################
mvn_exec_date () {
'cd /usr/apps/crimes || exit 1'
mvn exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--Dinputfile=$1 --Dmdate=$2"
}

#######################################
# Execute java program, take input file with default date and save data in output directory
# Globals:
#   MAIN_CLASS
# Arguments:
#   Input file for java program
#  	Output directory for java program
# Outputs:
#   Running program
#######################################
mvn_exec_outdir () {
'cd /usr/apps/crimes || exit 1'
mvn exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--Dinputfile=$1 --Doutdir=$2"
}

#######################################
# Execute java program, take input file with concrete date and save data in output directory
# Globals:
#   MAIN_CLASS
# Arguments:
#   Input file for java program
#  	Date for java program
#  	Output directory for java program
# Outputs:
#   Running program
#######################################
mvn_exec_outdir_date () {
'cd /usr/apps/crimes || exit 1'
mvn exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--Dinputfile=$1 --Dmdate=$2 --Doutdir=$3"
}

#######################################
# Display help manual for script
# Arguments:
#   None
# Outputs:
#   Manual for script
#######################################
show_help () {
help_descr="Script helps to check postgres service, create database, mvn operations to java program
Usage: $(basename "$0") [OPTIONS]
Where options include:
--help 			- view the manual for this script
--info 			- info about version and variables
--activepsql 	- activate psql-service
--createdb 		- run script for createdb
--cleandb 		- run script for createdb again
--mvnclean		- clean target directory
--mvncompile	- compile java class files to target directory
--mvnexechelp									- execute java program help
--mvnexec [inputfile]							- execute java programm to save data to the database with default date
--mvnexecdate [inputfile date] 					- execute java programm to save data to the database with concrete date
--mvnexecoutdir [inputfile outputdir]			- execute java programm to save data to the file with default date
--mvnexecoutdirdate [inputfile date outputdir]	- execute java programm to save data to the file with concrete date"
echo "$help_descr"
}

def_run () {
if [[ $# -eq 1 ]]; then
  info
  postgre_run
  create_db
  mvn_clean
  mvn_compile
  mvn_exec "$1"
else
  echo "Input file is required"
fi
}

#######################################
# Main line of script
#######################################
main () {
if [[ $# -lt 1 ]]; then
  echo "One argument is required"
  show_help
  exit 0
fi

if [[ $# -eq 1 ]]; then
  case $1 in
	--help)
            show_help
            exit 0
		    ;;
	*)    
			def_run "$1"
			exit 0
			;;
  esac
fi

while [[ $# -gt 0 ]]; do
  key="$1"
  case $key in
    --info)
				info
				shift
				;;
    --active_psql)
				postgre_run
				shift
				;;
    --create_db | --cleandb)
				create_db
				shift
				;;
	--mvn_clean)
				mvn_clean
				shift
				;;
	--mvn_compile)
				mvn_compile
				shift
				;;
	--mvn_exec_help)
				mvn_exec_help
				shift
				exit 0
				;;
	--mvnexec)	
				mvn_exec "$2"
				shift
				shift
				;;
	--mvnexecdate)
				mvn_exec_date "$2 $3"
				shift
				shift
				shift
				;;
	--mvnexecoutdir)
				mvn_exec_outdir "$2 $3"
				shift
				shift
				shift
				;;
	--mvnexecoutdirdate)
				mvn_exec_outdir_date "$2 $3 $4"
				shift
				shift
				shift
				shift
				;;			
  esac
done
}

main "$@"