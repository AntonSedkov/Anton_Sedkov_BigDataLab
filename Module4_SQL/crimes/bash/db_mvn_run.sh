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
echo "PATH define as $PATH"
echo "CLASSPATH define as $CLASSPATH"
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
  echo "PostgreSQL service is $(systemctl is-active ${POSTGRESQL_SERVICE})"
}

#######################################
# Create or recreate database with sql-script
# Arguments:
#   None
# Outputs:
#   Blank database with tables
#######################################
create_db () {
sudo -u postgres psql -f /usr/apps/crimes/sql/db_script.sql
}

#######################################
# Clean target directory
# Arguments:
#   None
# Outputs:
#   Deleted target directory
#######################################
mvn_clean () {
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
mvn -q exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="-help"
}

#######################################
# Execute java program street crime and take input file with default date
# Globals:
#   MAIN_CLASS
# Arguments:
#   Input file for java program
# Outputs:
#   Running program
#######################################
mvn_exec_sc () {
mvn -q exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--api_method=street_crime_db --inputfile=${1}"
}

#######################################
# Execute java program street crime and take input file with concrete date
# Globals:
#   MAIN_CLASS
# Arguments:
#   Input file for java program
#  	Date for java program
# Outputs:
#   Running program
#######################################
mvn_exec_sc_date () {
mvn -q exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--api_method=street_crime_db --inputfile=${1} --date=${2}"
}

#######################################
# Execute java program street crime, take input file with default date and save data in output directory
# Globals:
#   MAIN_CLASS
# Arguments:
#   Input file for java program
#  	Output directory for java program
# Outputs:
#   Running program
#######################################
mvn_exec_sc_outdir () {
mvn -q exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--api_method=street_crime_db --inputfile=${1} --outdir=${2}"
}

#######################################
# Execute java program street crime, take input file with concrete date and save data in output directory
# Globals:
#   MAIN_CLASS
# Arguments:
#   Input file for java program
#  	Date for java program
#  	Output directory for java program
# Outputs:
#   Running program
#######################################
mvn_exec_sc_outdir_date () {
mvn -q exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--api_method=street_crime_db --inputfile=${1} --date=${2} --outdir=${3}"
}

#######################################
# Execute java program stop and search with default date
# Globals:
#   MAIN_CLASS
# Arguments:
#   None
# Outputs:
#   Running program
#######################################
mvn_exec_sas () {
mvn -q exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--api_method=stop_and_search_db"
}

#######################################
# Execute java program stop and search with concrete date
# Globals:
#   MAIN_CLASS
# Arguments:
#  	Date for java program
# Outputs:
#   Running program
#######################################
mvn_exec_sas_date () {
mvn -q exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--api_method=stop_and_search_db --date=${1}"
}

#######################################
# Execute java program stop and search with default date and save data in output directory
# Globals:
#   MAIN_CLASS
# Arguments:
#  	Output directory for java program
# Outputs:
#   Running program
#######################################
mvn_exec_sas_outdir() {
mvn -q exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--api_method=stop_and_search_file --outdir=${1}"
}

#######################################
# Execute java program stop and search with concrete date and save data in output directory
# Globals:
#   MAIN_CLASS
# Arguments:
#  	Date for java program
#  	Output directory for java program
# Outputs:
#   Running program
#######################################
mvn_exec_sas_outdir_date () {
mvn -q exec:java -Dexec.mainClass="${MAIN_CLASS}" -Dexec.args="--api_method=stop_and_search_db --date=${1} --outdir=${2}"
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
--help - view the manual for this script
--info - info about version and variables
--activepsql - activate psql-service
--createdb - run script for createdb
--cleandb - run script for createdb again
--mvnclean - clean target directory
--mvncompile - compile java class files to target directory
--mvnexechelp - execute java program help
--mvnexecsc [inputfile] - execute java programm to save data to the database with default date
--mvnexecsc_d [inputfile date] - execute java programm to save data to the database with concrete date
--mvnexecsc_od [inputfile outputdir] - execute java programm to save data to the file with default date
--mvnexecsc_d_od [inputfile date outputdir] - execute java programm to save data to the file with concrete date
--mvnexecsas [] - execute java programm to save stop and search data to the database with default date
--mvnexecsas_d [date] - execute java programm to save stop and search data to the database with concrete date
--mvnexecsas_od [outputdir] - execute java programm to save stop and search data to the file with default date and concrete directory
--mvnexecsas_d_od [date outputdir] - execute java programm to save stop and search data to the file with concrete date"
echo "$help_descr"
}

def_run () {
if [[ $# -eq 1 ]]; then
  info
  postgre_run
  create_db
  mvn_clean
  mvn_compile
  mvn_exec_sc "$1"
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
	--info)
			info
			exit 0
			;;
  --activepsql)
			postgre_run
			exit 0
			;;
	--createdb | --cleandb)
			create_db
			exit 0
			;;
	--mvnclean)
			mvn_clean
			exit 0
			;;
	--mvncompile)
			mvn_compile
			exit 0
			;;
	--mvnexechelp)
			mvn_exec_help
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
  --activepsql)
			postgre_run
			shift
			;;
	--createdb | --cleandb)
			create_db
			shift
			;;
	--mvnclean)
				mvn_clean
				shift
				;;
	--mvncompile)
				mvn_compile
				shift
				;;
	--mvnexechelp)
				mvn_exec_help
				shift
				exit 0
				;;
	--mvnexecsc)
				mvn_exec_sc "$2"
				shift
				shift
				;;
	--mvnexecsc_d)
				mvn_exec_sc_date "$2" "$3"
				shift
				shift
				shift
				;;
	--mvnexecsc_od)
				mvn_exec_sc_outdir "$2" "$3"
				shift
				shift
				shift
				;;
	--mvnexecsc_d_od)
				mvn_exec_sc_outdir_date "$2" "$3" "$4"
				shift
				shift
				shift
				shift
				;;
  --mvnexecsas)
				mvn_exec_sas
				shift
				;;
	--mvnexecsas_d)
				mvn_exec_sas_date "$2"
				shift
				shift
				;;
	--mvnexecsas_od)
				mvn_exec_sas_outdir "$2"
				shift
				shift
				;;
	--mvnexecsas_d_od)
				mvn_exec_sas_outdir_date "$2" "$3"
				shift
				shift
				shift
				;;
  esac
done
}

main "$@"