#[======================================================================[.rst:
FindGMP
-------

Finds the GMP library.

Result Variables
^^^^^^^^^^^^^^^^

``GMP_FOUND``
``GMP_INCLUDE_DIRS``
``GMP_LIBRARIES``

#]======================================================================]

find_package(PkgConfig)
pkg_check_modules(PC_GMP QUIET GMP)

find_path(GMP_INCLUDE_DIR
    NAMES gmp.h gmpxx.h
    PATHS ${FC_GMP_INCLUDE_DIRS}
    PATH_SUFFIXES GMP
  )

# FIXME: CentOS (and probably other) installations set these variables in a .h that's
# included from gmp.h -- is there some way to pre-process gmp.h and look for these
# lines in the pre-processor output?
#
# Some (but not all) installations indicate the version with lines like:
#     #define __GNU_MP_VERSION            6
#     #define __GNU_MP_VERSION_MINOR      1
#     #define __GNU_MP_VERSION_PATCHLEVEL 2

set(GMP_VERSION_MAJOR 0)
set(GMP_VERSION_MINOR 0)
set(GMP_VERSION_PATCH 0)
if (GMP_INCLUDE_DIR)
  if (EXISTS "${GMP_INCLUDE_DIR}/gmp.h")
    file(STRINGS "${GMP_INCLUDE_DIR}/gmp.h" gmp_h_contents REGEX "# *define +__GNU_MP_VERSION")
    string(REGEX REPLACE ".*# *define +__GNU_MP_VERSION +([0-9]+).*"            "\\1" GMP_VERSION_MAJOR "${gmp_h_contents}")
    string(REGEX REPLACE ".*# *define +__GNU_MP_VERSION_MINOR +([0-9]+).*"      "\\1" GMP_VERSION_MINOR "${gmp_h_contents}")
    string(REGEX REPLACE ".*# *define +__GNU_MP_VERSION_PATCHLEVEL +([0-9]+).*" "\\1" GMP_VERSION_PATCH "${gmp_h_contents}")
    unset(gmp_h_contents)
  endif()
endif()
set(GMP_VERSION "${GMP_VERSION_MAJOR}.${GMP_VERSION_MINOR}.${GMP_VERSION_PATCH}")
message(STATUS "GMP version: ${GMP_VERSION}")

find_library(GMP_LIB gmp
    PATHS ${PC_GMP_LIBRARY_DIRS}
  )

find_library(GMP_GMPXX_LIB gmpxx
    PATHS ${PC_GMP_LIBRARY_DIRS}
  )


set(GMP_INCLUDE_DIRS ${GMP_INCLUDE_DIR})
set(GMP_LIBRARIES ${GMP_LIB} ${GMP_GMPXX_LIB})
# set(GMP_COMPILE_FLAGS "-DENABLE_GMP=1")

include(FindPackageHandleStandardArgs)
find_package_handle_standard_args(GMP
    FOUND_VAR GMP_FOUND
    REQUIRED_VARS
      GMP_LIBRARIES GMP_INCLUDE_DIR GMP_LIB GMP_GMPXX_LIB
  )

mark_as_advanced(GMP_LIB GMP_GMPXX_LIB GMP_INCLUDE_DIR)

file(APPEND ${CMAKE_BINARY_DIR}${CMAKE_FILES_DIRECTORY}/CMakeOutput.log
   "FindGMP.cmake:\n"
   "  GMP_VERSION: ${GMP_VERSION}\n"
   "  GMP_INCLUDE_DIR: ${GMP_INCLUDE_DIR}\n"
   "  GMP_LIB: ${GMP_LIB}\n"
   "  GMP_GMPXX_LIB: ${GMP_GMPXX_LIB}\n"
   "  GMP_LIBRARIES: ${GMP_LIBRARIES}\n")
   


