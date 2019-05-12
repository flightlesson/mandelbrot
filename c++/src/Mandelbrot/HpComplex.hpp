#ifndef HpComplex_hpp // hi-precision complex
#define HpComplex_hpp

#include <iostream>
#include <string>
#include <gmp.h>

#include "HpDouble.hpp"

namespace Mandelbrot {

    class HpComplex {
    public: 
        HpComplex(HpDouble r, HpDouble i);
        HpDouble getReal() const;
        HpDouble getImag() const;
    private:
        HpDouble realv;
        HpDouble imagv;
    };
}

std::ostream& operator<<(std::ostream& strm, const Mandelbrot::HpComplex& obj);

#endif
