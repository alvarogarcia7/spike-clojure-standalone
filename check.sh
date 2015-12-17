#!/bin/bash

diff expected.txt ab.dat

diff_ok=$?

if [[ $diff_ok = 0 ]]; then 
    echo "OK"
else 
    echo "KO"
fi
exit $diff_ok
