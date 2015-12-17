#!/bin/bash

java -jar target/uberjar/fat-jar-0.1.0-SNAPSHOT-standalone.jar -t . -o a
diff expected.txt ab.dat

diff_ok=$?

if [[ $diff_ok = 0 ]]; then 
    echo "OK"
else 
    echo "KO"
fi
exit $diff_ok
