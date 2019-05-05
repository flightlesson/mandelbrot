#ifndef Mandelbrot_MJSet_hpp
#define Mandelbrot_MJSet_hpp

#include <iostream>
#include <string>

#include "ViewPort.hpp"

namespace Mandelbrot {
    class MJSet {
    public:
        MJSet(const ViewPort&,double,int);
        const ViewPort& getViewPort() const;
    private:
        const ViewPort& viewPort;
        double delta;
        int maxIterations;
    };
}

std::ostream& operator<<(std::ostream& strm, const Mandelbrot::MJSet& obj);
#endif
