project(Mandelbrot)
cmake_minimum_required(VERSION 3.14)

list(APPEND CMAKE_MODULE_PATH "${CMAKE_SOURCE_DIR}/cmake")

find_package(Boost 1.40 COMPONENTS program_options REQUIRED )
include_directories( ${Boost_INCLUDE_DIR} ../CTPL-master)

find_package(GMP)

add_executable(MJGenerator MJGenerator.cpp MJSet.cpp Point.cpp ViewPort.cpp closenessToSet.c)
target_link_libraries(MJGenerator ${Boost_PROGRAM_OPTIONS_LIBRARY} ${GMP_LIBRARIES})






