#ifndef closenessToSet_h
#define closenessToSet_h

#include <stdio.h>
#include <stdarg.h>
#include <gmp.h>

#ifdef __cplusplus
extern "C" {
#endif

  /**
   * Computes a point's "closeness" to the Mandelbrot or Julia set, where closeness is 0 for points
   * in the set or the number of iterations before the point was determined to not be in the set.
   * I.e., the larger the closeness, the closer the point is to the set.
   *
   * The formula z[i+1] = z[i]^2 + c, where z and c are both complex numbers, is used to determine 
   * whether or not a point is in the set:
   *  - z[0] is the point being tested;
   *  - for the Mandelbrot set, c is also the point being tested;
   *  - for Julia sets, c is the constant that identifies the Julia set (i.e., there are 
   *    infinitely many Julia sets).
   *  - if z[i]'s absolute value (i.e., distance from zero on the complex plain) is .ge. 2
   *    then z[0] is closeness i+1 to the set.
   *
   * @param z0_real, z0_imag are the coordinates of the point being tested.
   * @param c_real, c_imag are the c in z[i+1] = z[i]^2 + c
   * @param maxIterations is self explanatory: if z[maxIterations-1]'s absolute value hasn't exceeded 2 
   *        then z[0] is presumed to be inside the set.
   * @param tmp is an array of 5 mpf_t that will be used to hold intermediate values. Each mpfr_t in 
   *        tmp is presumed to have been configured via mpf_init2().
   */
  int closenessToSet(const mpf_t z0_real, const mpf_t z0_imag, const mpf_t c_real, const mpf_t c_imag, int maxIterations, mpf_t tmp[]);

  /**
   * Computes the closeness of each point in a row to the Mandelbrot or Julia set.
   *
   * @pamar npoints is the number of points in the row.
   * @param results is an {npoints}-size array; this function will set results[i] to the i'th point's 
   *        distance from the set.
   * @param delta is the distance between points
   * @param z0_0_real and z0_imag are the coordinates (in set space) of the leftmost point in the row.
   * @param c_real and c_imag are either null (Mandelbrot set) or the constant that defines the Julia set.
   * @param maxIterations
   * @param tmp is an array of 8 mpf_t that will be used to hold intermediate values. Each mpfr_t is
   *        tmp is presumed to have been configured via mpf_init2().  
   *        Note that closenessToSet's tmp only has 5 entries.
   * 
   * @return results
   */
  int* closenessToSetRow(int npoints, int results[], mpf_t delta,
                           mpf_t z0_0_real, mpf_t z0_imag, mpf_t c_real, mpf_t c_imag, int maxIterations, mpf_t tmp[]);

#ifdef __cplusplus  
};
#endif

#endif



