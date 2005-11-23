
JAVADOC = javadoc -d ./api -author -public -sourcepath . -use -windowtitle "SongDataBase API" -doctitle "SongDataBase API"

JAVAC = javac

JAVA = java

MYCLASSPATH = $(CLASSPATH):foreign-classes.zip

FILES = ./org/zephyrsoft/sdb/*.java \
		./org/zephyrsoft/sdb/structure/*.java \
		./org/zephyrsoft/sdb/dnd/*.java \
		./org/zephyrsoft/util/*.java

all:
	$(JAVAC) -classpath $(MYCLASSPATH) -deprecation $(FILES)

run:
	make all
	$(JAVA) -cp $(MYCLASSPATH) org.zephyrsoft.sdb.Start test.sdb

runpres:
	make all
	$(JAVA) -cp $(MYCLASSPATH) org.zephyrsoft.sdb.Start test.sdb --presentation

api: $(FILES)
	$(JAVADOC) $(FILES)	
