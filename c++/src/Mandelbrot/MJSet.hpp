#ifndef __Mandelbrot_MJSet_hpp__
#define __Mandelbrot_MJSet_hpp__

#include <iostream>
#include <string>

namespace Mandelbrot {
    class MJSet {
    public:
        MJSet(int,int,double,int);
    private:
        int widthInPixels;
        int heightInPixels;
        double delta;
        int maxIterations;
    };
}

std::ostream& operator<<(std::ostream& strm, const Mandelbrot::MJSet &obj);
#endif
