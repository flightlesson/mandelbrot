#include <iostream>
#include <fstream>
#include <string>
#include "boost/program_options.hpp"
#include "MJGenerator.hpp"
#include "MJSet.hpp"
#include "ViewPort.hpp"

int main(int argc, char **argv) {
    namespace po = boost::program_options;
    std::string VERSION="1.0-current";

    // Options that can be specified in the config file and overridden on the command line:
    po::options_description fileOptions("Configuration file or command line options");
    fileOptions.add_options()
      ("viewport,p", po::value<std::string>()->default_value("1920x1080"),"size of viewport")
      ("center,c", po::value<std::string>()->default_value("0,0"),"center of window")
      ("delta,d", po::value<std::string>()->default_value("0.001"),"distance between pixels")
      ("maxIterations,m", po::value<int>()->default_value(100),"maximum number of interations")
      ("threads,t", po::value<int>()->default_value(4),"size of thread pool")
      ("zoom,z",po::value<double>()->default_value(0.9),"zoom factor")
      ("frames,f",po::value<int>()->default_value(1),"number of frames to create")
      ("ouput,o",po::value<std::string>()->default_value("mjset-{center}-{delta}-{maxIterations}.dat"),"output filename template")
    ;

    // Options that can only be specified on the command line:
    po::options_description cmdlineOnlyOptions("Command line only options");
    cmdlineOnlyOptions.add_options()
      ("version,v","print version string, then exit")
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

        std::cout << "using configuration:" << std::endl;
        for (const auto& it: vm) {
            std::cout << "    " << it.first.c_str() << " = ";
            auto& value = it.second.value();
            auto v = boost::any_cast<std::string>(&value);
            if (v) {
                std::cout << *v; 
            } else {
                auto v = boost::any_cast<int>(&value);
                if (v) {
                    std::cout << *v;
                } else {
                    auto v = boost::any_cast<double>(&value);
                    if (v) {
                        std::cout << *v;
                    }
                }
            }
            std::cout << std::endl;
        }

        po::notify(vm);

        
        //std::cout << "mjset: " << mjset << std::endl;
    } catch (po::error& e) {
        std::cerr << "ERROR: " << e.what() << std::endl;
    }
}
