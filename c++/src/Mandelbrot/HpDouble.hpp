#ifndef HpDouble_hpp // hi-precision double
#define HpDouble_hpp

#include <iostream>
#include <string>
#include <gmp.h>

namespace Mandelbrot {

    class HpDouble {
    public: 
        HpDouble(std::string s);
        HpDouble(double d);
        double getDouble() const;
        std::string getString() const;
        char *gets(char *buf, int bufsiz) const;
        void getMpf(mpf_t returned) const;
    private:
        std::string s;
        mpf_t mpf;
        double d;
        bool dIsSet;
    };
}

std::ostream& operator<<(std::ostream& strm, const Mandelbrot::HpDouble& obj);

#endif