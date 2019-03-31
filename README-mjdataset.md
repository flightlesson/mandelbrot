# MJDataSet (.mjd) Files 

MJDataSet files contain a binary representation of a view into a
Mandelbrot or Julia set.

MJDataSet files should have suffix `.mjd`.

MJDataSet files start with a human-readable header that describes the
view they contain.  This is followed by a binary representation of the
view.

## The human-readable header

The human readable header looks like:

~~~
MJDataSet-version=1.0
generator-function-z0=({BigDecimal},{BigDecimal})
generator-function-c={bigdecimalcomplex}
viewport-width={int}
viewport-height={int}
window-center-real={BigDecimal}
window-center-imag={BigDecimal}
window-delta={double}
max-iterations={int}

~~~

The header ends with a blank line; the final bytes of the header are
`0X0a0a`.

## The binary portion

The binary portion immediately follows the header.  It consists of
{viewport-width} times {viewport-height} 2 or 4 byte values that
represent the point's distance from the set.

If {max-iterations} is less than 65535 (2^16-1) then 2 byte values are
used; otherwise 4 byte values are used.

The first value correspands to the point at (x=0,y=0), the second
value to (x=1,y=0).  It's all arrays from there.
