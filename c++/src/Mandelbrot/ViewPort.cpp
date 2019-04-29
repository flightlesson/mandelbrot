#include "ViewPort.hpp"

Mandelbrot::ViewPort::ViewPort(int width,int height):width(width),height(height){}

int Mandelbrot::ViewPort::getWidth() const {return width;}

int Mandelbrot::ViewPort::getHeight() const {return height;}

std::ostream& operator<<(std::ostream& strm, const Mandelbrot::ViewPort& obj) {
    std::cout << "{ViewPort's operator<<}" << std::endl;
    return strm << obj.getWidth() << "X" << obj.getHeight();
}