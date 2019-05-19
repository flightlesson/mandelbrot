#include <iostream>
#include <fstream>
#include <string>
#include "boost/program_options.hpp"
#include "boost/algorithm/string.hpp"
#include "MJGenerator.hpp"
#include "MJSet.hpp"
#include "ViewPort.hpp"

int main(int argc, char **argv) {
    namespace po = boost::program_options;
    std::string VERSION="1.0-current";

    // Options that can be specified in the config file and overridden on the command line:
    po::options_description fileOptions("Configuration file or command line options");
    fileOptions.add_options()
      ("viewport,p",      po::value<std::string>()->default_value("1920x1080"),"size of viewport")
      ("center,c",        po::value<std::string>()->default_value("0,0"),      "center of window")
      ("delta,d",         po::value<std::string>()->default_value("0.001"),    "distance between pixels")
      ("maxIterations,m", po::value<int>()->default_value(100),                "maximum number of interations")
      ("threads,t",       po::value<int>()->default_value(4),                  "size of thread pool")
      ("zoom,z",          po::value<double>()->default_value(0.9),             "zoom factor")
      ("frames,f",        po::value<int>()->default_value(1),                  "number of frames to create")
      ("output,o",        po::value<std::string>()->default_value("mjset-{center}-{delta}-{maxIterations}.dat"),"output filename template")
      ("verbosity,v",     po::value<int>()->default_value(1),                  "verbosity: 0 is quiet, 1 is info, >1 is debug")
    ;

    // Options that can only be specified on the command line:
    po::options_description cmdlineOnlyOptions("Command line only options");
    cmdlineOnlyOptions.add_options()
      ("version","print version string, then exit")
      ("help,h","print this message")
      ("configfile,C", po::value<std::string>()->default_value("MJGenerator.conf"),"configuration file")
    ;

    po::options_description cmdlineOptions;
    cmdlineOptions.add(fileOptions).add(cmdlineOnlyOptions);

    po::variables_map vm;
    try {
        po::store(po::parse_command_line(argc, argv, cmdlineOptions),vm);
        if (vm.count("help")) {
            std::cout << cmdlineOptions << std::endl;
            return 0;
        }
        std::cout << VERSION << std::endl;
        if (vm.count("version")) {
            return 0;
        }

        std::string configfilename = vm["configfile"].as<std::string>();
        std::ifstream configfile(configfilename);
        if (configfile) {
            std::cout << "Reading config file " << configfilename << std::endl;
            po::store(po::parse_config_file(configfile,fileOptions),vm);
        } else {
            std::cout << "Didn't find configfile " << configfilename << std::endl;
        }

        int verbosity = vm["verbosity"].as<int>();

        if (verbosity > 1) {
            std::cout << "using configuration:" << std::endl;
            for (const auto& it: vm) {
                std::cout << "    " << it.first.c_str() << " = ";
                const auto& value = it.second.value();
                const auto& v = boost::any_cast<std::string>(&value);
                if (v) {
                    std::cout << *v; 
                } else {
                    const auto& v = boost::any_cast<int>(&value);
                    if (v) {
                        std::cout << *v;
                    } else {
                        const auto& v = boost::any_cast<double>(&value);
                        if (v) {
                            std::cout << *v;
                        }
                    }
                }
                std::cout << std::endl;
            }
        }

        po::notify(vm);

        std::vector<std::string> viewport;
        boost::split(viewport,vm["viewport"].as<std::string>(),boost::is_any_of("xX"));
        std::vector<std::string> center;
        boost::split(center,vm["center"].as<std::string>(),boost::is_any_of(","));
        Mandelbrot::MJGenerator generator(std::atoi(viewport[0].c_str()), 
                                          std::atoi(viewport[1].c_str()),
                                          center[0],
                                          center[1],
                                          vm["delta"].as<std::string>(), 
                                          vm["maxIterations"].as<int>(),
                                          vm["output"].as<std::string>(),
                                          vm["frames"].as<int>(),
                                          vm["zoom"].as<double>(),
                                          vm["threads"].as<int>(),
                                          verbosity);
        generator.run();

    } catch (po::error& e) {
        std::cerr << "ERROR: " << e.what() << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "ERROR: " << e.what() << std::endl;
    } catch (...) {
        std::cerr << "Caught exception" << std::endl;
    }
}


Mandelbrot::MJGenerator::MJGenerator(const int viewportWidth, const int viewportHeight,
                    const std::string& centerReal, const std::string& centerImag,
                    const std::string& delta, const int maxIterations,
                    const std::string outFilenameTemplate,
                    const int numberOfFramesToCompute,
                    const double zoomFactor,
                    const int nThreads,
                    const int verbosity): 
        viewportWidth(viewportWidth), viewportHeight(viewportHeight),
        centerReal(centerReal), centerImag(centerImag),
        delta(delta), maxIterations(maxIterations),
        outFilenameTemplate(outFilenameTemplate),
        numberOfFramesToCompute(numberOfFramesToCompute),
        zoomFactor(zoomFactor), nThreads(nThreads), verbosity(verbosity) {
    if (verbosity > 0) {
        std::cout << "# Using:" << std::endl;
        std::cout << "viewportWidth = " << viewportWidth << std::endl;
        std::cout << "viewportHeight = " << viewportHeight << std::endl;
        std::cout << "centerReal = " << centerReal << std::endl;
        std::cout << "centerImag = " << centerImag << std::endl;
        std::cout << "delta = " << delta << std::endl;
        std::cout << "maxIterations = " << maxIterations << std::endl;
        std::cout << "outFilenameTemplate = " << outFilenameTemplate << std::endl;
        std::cout << "numberOfFramesToCompute = " << numberOfFramesToCompute << std::endl;
        std::cout << "zoomFactor = " << zoomFactor << std::endl;
        std::cout << "nThreads = " << nThreads << std::endl;
    }
}

void Mandelbrot::MJGenerator::run() {
}
