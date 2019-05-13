project(Mandelbrot)
cmake_minimum_required(VERSION 3.13)

list(APPEND CMAKE_MODULE_PATH "${CMAKE_SOURCE_DIR}/cmake")

find_package(Boost 1.40 COMPONENTS
		   	program_options
		   	unit_test_framework
			REQUIRED )

find_package(GMP)

include_directories( ${Boost_INCLUDE_DIR} ${GMP_INCLUDE_DIR} ../CTPL-master)

add_executable(MJGenerator
	closenessToSet.c closenessToSet.h
	HpComplex.cpp   HpComplex.hpp
	HpDouble.cpp      HpDouble.hpp
	MJGenerator.cpp   MjGenerator.hpp
 	MJSet.cpp	     MjSet.hpp
	Point.cpp              Point.hpp
	ViewPort.cpp         ViewPort.hpp
	)
target_link_libraries(MJGenerator
	${Boost_PROGRAM_OPTIONS_LIBRARY}
	${GMP_LIBRARIES}
	)

add_subdirectory("unit-tests")

enable_testing()
add_test(NAME testClosenessToSet COMMAND unit-tests/testClosenessToSet)
add_test(NAME testHpDouble          COMMAND unit-tests/testHpDouble)





