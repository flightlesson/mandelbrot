#define BOOST_TEST_MODULE test1
#include <iostream>
#include <boost/test/unit_test.hpp>
#include "../closenessToSet.h"

BOOST_AUTO_TEST_SUITE(closenessToSet_suite)

BOOST_AUTO_TEST_CASE(test1) {
  mpf_t z0_real;
  mpf_t z0_imag;
  mpf_t c_real;
  mpf_t c_imag;
  mpf_t tmp[5];
  int closeness;

  mpf_init2(z0_real,200);
  mpf_init2(z0_imag,200);
  mpf_init2(c_real,200);
  mpf_init2(c_imag,200);
  mpf_init2(tmp[0],200);
  mpf_init2(tmp[1],200);
  mpf_init2(tmp[2],200);
  mpf_init2(tmp[3],200);
  mpf_init2(tmp[4],200);

  mpf_set_d(z0_real,0.34621);
  mpf_set_d(z0_imag,-0.5784);
  mpf_set(c_real,z0_real);
  mpf_set(c_imag,z0_imag);

  closeness = closenessToSet(z0_real, z0_imag, c_real, c_imag, 1000, tmp);
  std::cout << "closeness: " << closeness << std::endl;
  BOOST_CHECK(closeness == 98);
}

BOOST_AUTO_TEST_SUITE_END()
