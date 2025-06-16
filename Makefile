# --- project structure ---
SRC_DIR := ./src/main/java
RES_DIR := ./src/main/resources
BIN_DIR := ./bin
DIST_DIR := ./dist

# --- java toolchain ---
JAVAC := javac -Xlint -Werror --release 8
JAVA := java
CLASSPATH := $(BIN_DIR)
SOURCES := $(shell find $(SRC_DIR) -name "*.java")
CLASSES := $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# --- app packaging ---
GEOMETRIC_MAIN_CLASS := gui.GeometricFractalsApp
GEOMETRIC_JAR := $(DIST_DIR)/GeometricFractalsApp.jar
GEOMETRIC_EXE := $(DIST_DIR)/GeometricFractalsApp.exe

LSYSTEM_MAIN_CLASS := gui.LSystemFractalsApp
LSYSTEM_JAR := $(DIST_DIR)/LSystemFractalsApp.jar
LSYSTEM_EXE := $(DIST_DIR)/LSystemFractalsApp.exe

LAUNCH4J_CONFIG := launch4j-config.xml

# --- bundled jre ---
JRE_ZIP := third_party/OpenJDK21U-jre_x64_windows_hotspot_21.0.7_6.zip
JRE_DIR := $(DIST_DIR)/jre



# --- compilation ---
.PHONY: all
all: $(CLASSES) copy-resources

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(dir $@)
	$(JAVAC) -cp $(SRC_DIR) -d $(BIN_DIR) $<

.PHONY: compile
compile: copy-resources
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -cp $(SRC_DIR) -d $(BIN_DIR) $(SOURCES)

.PHONY: copy-resources
copy-resources:
	@mkdir -p $(BIN_DIR)
	@cp -r $(RES_DIR)/* $(BIN_DIR)/



# --- jar creation ---
.PHONY: jar
jar: $(GEOMETRIC_JAR) $(LSYSTEM_JAR)

$(GEOMETRIC_JAR): $(CLASSES)
	@mkdir -p $(DIST_DIR)
	jar cfe $@ $(GEOMETRIC_MAIN_CLASS) -C $(BIN_DIR) .

$(LSYSTEM_JAR): $(CLASSES)
	@mkdir -p $(DIST_DIR)
	jar cfe $@ $(LSYSTEM_MAIN_CLASS) -C $(BIN_DIR) .



# --- jre Extraction ---
jre: $(JRE_DIR)

$(JRE_DIR): $(JRE_ZIP)
	@echo "Extracting JRE..."
	@mkdir -p $(DIST_DIR)
	@unzip -q $(JRE_ZIP) -d $(DIST_DIR)
	@JRE_UNPACKED_DIR=$$(find $(DIST_DIR) -maxdepth 1 -type d -name "jdk-*-jre" | head -n 1); \
	if [ -n "$$JRE_UNPACKED_DIR" ]; then \
		mv "$$JRE_UNPACKED_DIR" $(JRE_DIR); \
		echo "JRE moved to $(JRE_DIR)"; \
	else \
		echo "Error: Could not find extracted JRE directory."; \
		exit 1; \
	fi



# --- create exe ---
$(GEOMETRIC_EXE): $(GEOMETRIC_JAR) $(JRE_DIR) $(LAUNCH4J_CONFIG)
	@echo "Running Launch4j..."
	launch4j $(LAUNCH4J_CONFIG)
	@echo "Windows EXE created: $(GEOMETRIC_EXE)"

.PHONY: win-exe
win-exe: $(GEOMETRIC_EXE)






.PHONY: run
run:
	$(JAVA) -cp $(CLASSPATH) $(GEOMETRIC_MAIN_CLASS)

.PHONY: clean
clean:
	rm -rf $(BIN_DIR) $(DIST_DIR)
