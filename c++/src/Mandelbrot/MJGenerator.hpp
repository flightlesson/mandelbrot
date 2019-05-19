#ifndef Mandelbrot_MJGenerator_hpp
#define Mandelbrot_MJGenerator_hpp

#include <iostream>

namespace Mandelbrot {
    class MJGenerator {
    public:
        MJGenerator(const int viewportWidth,
                    const int viewportHeight,
                    const std::string& centerReal, 
                    const std::string& centerImag,
                    const std::string& delta, 
                    const int maxIterations,
                    const std::string outFilenameTemplate,
                    const int numberOfFramesToCompute,
                    const double zoomFactor,
                    const int nThreads,
                    const int verbosity);
        void run();
    private:
        const int viewportWidth;
        const int viewportHeight;
        const std::string& centerReal;
        const std::string& centerImag;
        const std::string& delta;
        const int maxIterations;
        const std::string outFilenameTemplate;
        const int numberOfFramesToCompute;
        const double zoomFactor;
        const int nThreads;
        const int verbosity;

        friend std::ostream& operator<<(std::ostream&, const Mandelbrot::MJGenerator&);
    };
}

std::ostream& operator<<(std::ostream&, const Mandelbrot::MJGenerator&);

#endif