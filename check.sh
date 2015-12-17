#!/bin/bash

diff expected.txt a.txt

diff_ok=$?

if [[ $diff_ok = 0 ]]; then 
    echo "OK"
else 
    echo "KO"
fi
exit $diff_ok
