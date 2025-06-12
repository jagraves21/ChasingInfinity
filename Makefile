SRC_DIR := ./src/main/java
RES_DIR := ./src/main/resources
BIN_DIR := ./bin
DIST_DIR := ./dist

JAVAC := javac -Xlint -Werror --release 8
JAVA := java
CLASSPATH := $(BIN_DIR):$(RES_DIR)
SOURCES := $(shell find $(SRC_DIR) -name "*.java")
CLASSES := $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# JAR info
GEOMETRIC_MAIN_CLASS := gui.GeometricFractals
GEOMETRIC_JAR := $(DIST_DIR)/GeometricFractals.jar
GEOMETRIC_EXE := $(DIST_DIR)/GeometricFractals.exe

LAUNCH4J_CONFIG := $(DIST_DIR)/launch4j-config.xml

.PHONY: all
all: $(CLASSES)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(dir $@)
	$(JAVAC) -cp $(SRC_DIR):$(RES_DIR) -d $(BIN_DIR) $<

$(GEOMETRIC_JAR): all
	@mkdir -p $(DIST_DIR)
	jar cfe $@ $(GEOMETRIC_MAIN_CLASS) -C $(BIN_DIR) .
	@cp -r $(RES_DIR)/* $(DIST_DIR)/


.PHONY: jar
jar: $(GEOMETRIC_JAR)

$(GEOMETRIC_EXE): $(GEOMETRIC_JAR)
	@echo "Running Launch4j..."
	launch4j $(LAUNCH4J_CONFIG)
	@echo "Windows EXE created: $(GEOMETRIC_EXE)"

.PHONY: clean
clean:
	rm -rf $(BIN_DIR) $(DIST_DIR)

.PHONY: compile
compile:
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -cp $(SRC_DIR):$(RES_DIR) -d $(BIN_DIR) $(SOURCES)

.PHONY: run
run:
	$(JAVA) -cp $(CLASSPATH) $(GEOMETRIC_MAIN_CLASS)

.PHONY: win-exe
win-exe: $(GEOMETRIC_EXE)
