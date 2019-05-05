#ifndef Mandelbrot_Point_hpp
#define Mandelbrot_Point_hpp

#include <iostream>
#include <string>

#include "ViewPort.hpp"

namespace Mandelbrot {
    class Point {
    public:
        Point(std::string s);
        std::string getString() const;
    private:
        std::string s;
    };
}

std::ostream& operator<<(std::ostream& strm, const Mandelbrot::Point& obj);
#endif
