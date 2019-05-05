#ifndef Mandelbrot_MJGenerator_hpp
#define Mandelbrot_MJGenerator_hpp

#include <iostream>

namespace Mandelbrot {
    class MJGenerator {
    public:
    private:
        friend std::ostream& operator<<(std::ostream&, const Mandelbrot::MJGenerator&);
    };
}

std::ostream& operator<<(std::ostream&, const Mandelbrot::MJGenerator&);

#endif