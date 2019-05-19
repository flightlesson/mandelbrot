#include "HpComplex.hpp"

Mandelbrot::HpComplex::HpComplex(HpDouble r, HpDouble i): realv(r), imagv(i) {
}

Mandelbrot::HpDouble Mandelbrot::HpComplex::getReal() const {
  return realv;
}

Mandelbrot::HpDouble Mandelbrot::HpComplex::getImag() const {
  return imagv;
}

std::ostream& operator<<(std::ostream& strm, const Mandelbrot::HpComplex& obj) {
  return strm << obj.getReal() << "," << obj.gitImag();
}
