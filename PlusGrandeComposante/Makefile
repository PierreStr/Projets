##
# source directory
##
SRC_DIR := src

##
# output directory
##
OUT_DIR := out

##
# JavaFX SDK base directory
##
JAVAFX_SDK_DIR := c:\CodeInformatique\Java\javafx\openjfx-22_windows-x64_bin-sdk\javafx-sdk-22\lib

##
# sources
##
SRCS := $(wildcard $(SRC_DIR)/*.java)


##
# classes
##
CLS := $(SRCS:$(SRC_DIR)/%.java=$(OUT_DIR)/%.class)

##
# compiler and compiler flags
##
JC := javac
JCFLAGS := -d $(OUT_DIR)/ -cp $(SRC_DIR)/ --module-path "$(JAVAFX_SDK_DIR)" --add-modules javafx.controls,javafx.fxml

##
# suffixes
##
.SUFFIXES: .java

##
# targets that do not produce output files
##
.PHONY: all clean run

##
# default target(s)
##
all: $(CLS)

$(CLS): $(OUT_DIR)/%.class: $(SRC_DIR)/%.java
	$(JC) $(JCFLAGS) $<

run: $(CLS)
	java --module-path "$(JAVAFX_SDK_DIR)" --add-modules javafx.controls,javafx.fxml \
	-cp "$(OUT_DIR);$(JAVAFX_SDK_DIR)\*;C:\CodeInformatique\Java\packages\jgraphx-4.2.2\lib\jgraphx.jar" Mainfile


##
# clean up any output files
##
clean:
	rm $(OUT_DIR)/*.class