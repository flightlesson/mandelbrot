#include "MJSet.hpp"
#include "ViewPort.hpp"

Mandelbrot::MJSet::MJSet(const Mandelbrot::ViewPort& viewPort, double delta, int maxIterations)
        : viewPort(viewPort), delta(delta), maxIterations(maxIterations) {}

const Mandelbrot::ViewPort& Mandelbrot::MJSet::getViewPort() const { return viewPort; }

std::ostream& operator<<(std::ostream& strm, const Mandelbrot::MJSet& obj) {
    return strm << obj.getViewPort();
}