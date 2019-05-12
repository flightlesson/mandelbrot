#define BOOST_TEST_MODULE testHpDouble
#include <iostream>
#include <boost/test/unit_test.hpp>
#include <gmpxx.h>
#include "HpDouble.hpp"

BOOST_AUTO_TEST_SUITE(Mandelbrot_suite)

BOOST_AUTO_TEST_CASE(HpDoubleStringTest) {
  Mandelbrot::HpDouble d1234("1.2345678901234567890123456789012345");
  double d = d1234.getDouble();
  std::string s = d1234.getString();
  mpf_class m(200);
  std::cout.precision(20);
  std::cout << "d: " << d << std::endl;
  BOOST_CHECK( d > 1.234&& d < 1.235);
  std::cout << "s: " << s << std::endl;
  //BOOST_CHECK( s.compare("1.234") == 0 );
  d1234.getMpf(m.get_mpf_t());
  std::cout.precision(38);
  std::cout << "m: " << m << std::endl;
  d = m.get_d();
  BOOST_CHECK( d > 1.23456 && d < 1.23457);
  char buf[200];
  std::cout << "gets: " << d1234.gets(buf,200) << std::endl;
}

BOOST_AUTO_TEST_CASE(HpDoubleDoubleTest) {
  Mandelbrot::HpDouble d1234(1.234);
  double d;
  std::string s = d1234.getString();
  mpf_t m;
  //std::cout << "[HpDoubleDoubleTest] s: " << s << std::endl;
  BOOST_CHECK( s.compare("1.234") == 0 );
  mpf_init2(m,200);
  d1234.getMpf(m);
  d = mpf_get_d(m);
  BOOST_CHECK( d > 1.23399 && d < 1.23401);
}

BOOST_AUTO_TEST_SUITE_END()

