#ifndef Mandelbrot_Point_hpp
#define Mandelbrot_Point_hpp

#include <iostream>

namespace Mandelbrot {

    /**
     * Provides the size, in pixels, of the viewport.
     */
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
