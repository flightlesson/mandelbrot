#include "closenessToSet.h"

int closenessToSet(const mpf_t z0_real, const mpf_t z0_imag, const mpf_t c_real, const mpf_t c_imag, int maxIterations, mpf_t tmp[]) {
  int i;

  mpf_set(tmp[0], z0_real); // z[0].real
  mpf_set(tmp[1], z0_imag); // z[0].imag

  for (i=0; i < maxIterations; ++i) {
    mpf_mul(tmp[2], tmp[0], tmp[0]); // tmp[2] = z.real^2
    mpf_mul(tmp[3], tmp[1], tmp[1]); // tmp[3] = z.imag^2
    mpf_add(tmp[4],tmp[2],tmp[3]);     // tmp[4] = z.real^2 + z.imag^2 // (distance of z from 0)^2
    if (mpf_cmp_si(tmp[4],4) > 0) {
      return i+1; // z0 escaped the set after on the {i}th iteration.
    }
    mpf_mul(tmp[4], tmp[0], tmp[1]);
    mpf_mul_ui(tmp[4], tmp[4], 2);   // tmp[4] = 2 * z.real * z.imag
    mpf_sub(tmp[0],tmp[2],tmp[3]);
    mpf_add(tmp[0],tmp[0],c_real); // tmp[0] = (z.real^2 - z.imag^2) + c.real // z[i+1].real
    mpf_add(tmp[1],tmp[4],c_imag); // tmp[1] = 2 * z.real * z.imag + c.imag   // z[i+1].imag
  }

  return 0; // z0 is in the set
}

int* closenessToSetRow(int npoints, int results[], mpf_t delta,
			mpf_t z0_0_real, mpf_t z0_imag, mpf_t c_real, mpf_t c_imag, int maxIterations, mpf_t tmp[]) {
  int i;
  mpf_set(tmp[5], z0_0_real);
  for (i=0; i < npoints; ++i) {
    results[i] = closenessToSet(tmp[5],z0_imag,c_real,c_imag,maxIterations,tmp);
    mpf_add(tmp[5],tmp[5],delta);
  }
  return results;
}
