##
# source directory
##
SRC_DIR := src

##
# output directory
##
OUT_DIR := out

##
# JavaFX SDK base directory (relative to SevenWonders)
##
BASE_DIR := $(CURDIR)/..
JAVAFX_SDK_DIR := $(BASE_DIR)/javafx/openjfx-22_windows-x64_bin-sdk/javafx-sdk-22/lib

##
# sources
##
SRCS := $(wildcard $(SRC_DIR)/*.java)
SRCS += $(wildcard $(SRC_DIR)/cartes/*.java)
SRCS += $(wildcard $(SRC_DIR)/designPatterns/*.java)
SRCS += $(wildcard $(SRC_DIR)/functions/*.java)
SRCS += $(wildcard $(SRC_DIR)/items/*.java)
SRCS += $(wildcard $(SRC_DIR)/merveilles/*.java)
SRCS += $(wildcard $(SRC_DIR)/jetons/*.java)
SRCS += $(wildcard $(SRC_DIR)/main/*.java)

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
	java --module-path "$(JAVAFX_SDK_DIR)" --add-modules javafx.controls,javafx.fxml -cp "$(OUT_DIR);$(JAVAFX_SDK_DIR)/*" main.Mainfile

##
# clean up any output files
##
clean:
	rm $(OUT_DIR)/*.class