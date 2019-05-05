#include "Point.hpp"

Mandelbrot::Point::Point(std::string s):s(s){}

std::string Mandelbrot::Point::getString() const { return s; }