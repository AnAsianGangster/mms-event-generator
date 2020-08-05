# Define required macros here
REMOVE = rm
CC = javac

PROGRAM_NAME = CsvGenerator
# PROGRAM_NAME = EventGenerator


# Explicit rules, all the commands you can call with make 
# (note: the <tab> in the command line is necessary for make to work) 
# target:  dependency1 dependency2 ...
#       <tab> command

#Called by: make targetname 
#also executed when you just called make. This calls the first target. 
all: target_main

target_main: $(PROGRAM_NAME).java
	$(CC) $(PROGRAM_NAME).java

clean:
	$(REMOVE) $(PROGRAM_NAME).class
