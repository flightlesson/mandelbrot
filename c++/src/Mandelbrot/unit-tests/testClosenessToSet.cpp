#define BOOST_TEST_MODULE test1
#include <iostream>
#include <boost/test/unit_test.hpp>
#include "../closenessToSet.h"

BOOST_AUTO_TEST_SUITE(Mandelbrot_suite)

BOOST_AUTO_TEST_CASE(closenessToSetTest) {
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
  BOOST_CHECK(closeness == 98);
}

BOOST_AUTO_TEST_CASE(closenessToRowTest) {
  mpf_t delta;
  mpf_t z0_real;
  mpf_t z0_imag;
  mpf_t tmp[8];
  int results[100];

  mpf_init2(delta,200);
  mpf_init2(z0_real,200);
  mpf_init2(z0_imag,200);
  mpf_init2(tmp[0],200);
  mpf_init2(tmp[1],200);
  mpf_init2(tmp[2],200);
  mpf_init2(tmp[3],200);
  mpf_init2(tmp[4],200);
  mpf_init2(tmp[5],200);
  mpf_init2(tmp[6],200);
  mpf_init2(tmp[7],200);

  mpf_set_d(delta,0.001);
  mpf_set_d(z0_real,0.3);
  mpf_set_d(z0_imag,-0.5);

  closenessToSetRow(100, results, delta, z0_real, z0_imag, NULL, NULL, 1000, tmp);
  BOOST_CHECK(results[16] == 47);
  BOOST_CHECK(results[99] == 7);
  //std::cout << "results={0:" << results[0];
  //for (int i=1; i < 100; ++i) {
  //  std::cout << "," << i << ":" << results[i];
  //}
  //std::cout << "}" << std::endl;
}

BOOST_AUTO_TEST_SUITE_END()
