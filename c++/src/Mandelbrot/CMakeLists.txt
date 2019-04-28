cmake_minimum_required(VERSION 3.14)

find_package(Boost 1.40 COMPONENTS program_options REQUIRED )
INCLUDE_DIRECTORIES( ${Boost_INCLUDE_DIR} ../CTPL-master)

add_executable(MJGenerator MJGenerator.cpp MJSet.cpp)
target_link_libraries(MJGenerator ${Boost_PROGRAM_OPTIONS_LIBRARY})

