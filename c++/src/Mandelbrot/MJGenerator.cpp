#include <iostream>
#include <string>
#include "boost/program_options.hpp"
#include "MJSet.hpp"

int main(int argc, char **argv) {
    namespace po = boost::program_options;
    po::options_description cmdline("Options");
    cmdline.add_options()
      ("help,h","print this message")
      ("configfile,C", "configuration file")
      ("viewport,p", po::value<std::string>()->default_value("1920x1080"),"size of viewport")
      ("center,c", po::value<std::string>()->default_value("0,0"),"center of window")
      ("delta,d", po::value<std::string>()->default_value("0.001"),"distance between pixels")
      ("maxIterations,m", po::value<int>()->default_value(100),"maximum number of interations")
      ("threads,t", po::value<int>()->default_value(4),"size of thread pool")
      ("zoom,z",po::value<double>()->default_value(0.9),"zoom factor")
      ("frames,f","number of frames to create")
      ("ouput,o","output filename template")
    ;
    po::variables_map vm;
    try {
        po::store(po::parse_command_line(argc, argv, cmdline),vm);
        if (vm.count("help")) {
            std::cout << cmdline << std::endl;
            return 0;
        }
        std::cout << "threads: " << vm["threads"].as<int>() << std::endl;

        Mandelbrot::MJSet mjset(2000,1000,1.2,234);
        std::cout << "mjset: " << mjset << std::endl;
    } catch (po::error& e) {
        std::cerr << "ERROR: " << e.what() << std::endl;
    }
}
