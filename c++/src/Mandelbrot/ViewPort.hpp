#ifndef __Mandelbrot_Point_hpp__
#define __Mandelbrot_Point_hpp__

#include <iostream>

namespace Mandelbrot {
    class ViewPort {
    public:
        ViewPort(int width,int height);
        int getWidth() const;
        int getHeight() const;
    private:
        int width;
        int height;
    };
}

std::ostream& operator<<(std::ostream& strm, const Mandelbrot::ViewPort& obj);

#endif
