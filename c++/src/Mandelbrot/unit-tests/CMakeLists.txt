cmake_minimum_required(VERSION 3.13)

list(APPEND CMAKE_MODULE_PATH "${CMAKE_SOURCE_DIR}/../cmake")

find_package(Boost COMPONENTS program_options unit_test_framework REQUIRED)
find_package(GMP)

include_directories(${Boost_INCLUDE_DIR} ../)

add_definitions(-DBOOST_TEST_DYN_LINK)
add_executable(testClosenessToSet testClosenessToSet.cpp ../closenessToSet.c )
target_link_libraries(testClosenessToSet ${Boost_LIBRARIES} ${GMP_LIBRARIES}) 

add_definitions(-DBOOST_TEST_DYN_LINK)
add_executable(testHpDouble testHpDouble.cpp ../HpDouble.hpp ../HpDouble.cpp )
target_link_libraries(testHpDouble ${Boost_LIBRARIES} ${GMP_LIBRARIES})

# enable_testing()
# add_test(UnitTest testClosenessToSet)

