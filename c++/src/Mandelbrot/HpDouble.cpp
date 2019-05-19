#include <iostream>
#include <sstream>
#include <gmpxx.h>
#include "HpDouble.hpp"

Mandelbrot::HpDouble::HpDouble(std::string s): s(s) {
  // s is set, need to also set mpf and d
  mpf_init2(mpf,200); // FIXME: analyze s to determine the bit-width
  mpf_set_str(mpf,s.c_str(),10);
  d = mpf_get_d(mpf);

  //char buf[200];
  //int prec = 3 * mpf_get_prec(mpf) / 10 + 1;
  //gmp_sprintf(buf,"%.*Ff (%d)",prec,mpf,prec);
  //std::cout << "HpDouble: " << buf << std::endl;
}

Mandelbrot::HpDouble::HpDouble(double d): d(d) {
  // d is set, need to also set s and mpf
  std::ostringstream ss;
  mpf_init2(mpf,200);
  mpf_set_d(mpf,d);
  ss << mpf;
  s = ss.str();
}

double Mandelbrot::HpDouble::getDouble() const {
  return d;
}

std::string Mandelbrot::HpDouble::getString() const {
  return s;
}

char *Mandelbrot::HpDouble::gets(char *buf, int bufsiz) const {
  gmp_snprintf(buf,bufsiz,"%.50Ff",mpf);
  return buf;
}

void Mandelbrot::HpDouble::getMpf(mpf_t returned) const {
  mpf_set(returned,mpf);
}

bool Mandelbrot::HpDouble::isNegative() const {
  return mpf_sgn(mpf) < 0;
}

std::ostream& operator<<(std::ostream& strm, const Mandelbrot::HpDouble& obj) {
  mpf_t returned;
  obj.getMpf(returned);
  return strm << returned;
}
