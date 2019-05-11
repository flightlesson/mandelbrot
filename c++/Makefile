# This file works under both GNU Make and BSD Make:

BUILD_ARTIFACTS=build-artifacts
CMAKE=cmake
INSTALL_ARTIFACTS=install-artifacts

# All build artifacts will be created under the $(BUILD_ARTIFACTS) directory.
#
# After cd'ing into $(BUILD_ARTIFACTS) run cmake to create or update Makefiles,
# then run make to build using those Makefiles.
#
.PHONY: all
all:
	mkdir -p $(BUILD_ARTIFACTS)
	$(CMAKE) -S src/Mandelbrot -B $(BUILD_ARTIFACTS)
	cd $(BUILD_ARTIFACTS) && make

# Similarly, all install artifacts will be created under the $(INSTALL_ARTIFACTS) directory.
#
# Whereas $(BUILD_ARTIFACTS) includes transient files and other artifacts not intended for
# the application's end user, $(INSTALL_ARTIFACTS) should include everything -- and only
# those things -- needed by the application's end user.
.PHONY: install
install: all
	mkdir -p $(INSTALL_ARTIFACTS)/bin
	cp $(BUILD_ARTIFACTS)/MJGenerator $(INSTALL_ARTIFACTS)/bin

.PHONY: clean
clean:
	rm -rf $(BUILD_ARTIFACTS) $(INSTALL_ARTIFACTS)
