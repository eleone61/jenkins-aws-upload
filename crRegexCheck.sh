#!/bin/bash

# Check for N/A on pre-prod stages
# Check for CR# greater than 5 characters

crCheck() {
if [[ "$1" =~ N/A ]] || [[ "$1" =~ n/a ]] || [[ "$1" =~ N/a ]] || [[ "$1" =~ n/A ]]; then # $1 because it corresponds to the posistion of the parameter after the function name
    echo "$1 is valid"
    break
else
    if [ ${#1} -ge 5 ]; then
        if [[ "$1" =~ [0-9] ]] || [[ "$1" =~ [a..z] ]]; then
            echo "$1 is valid!"
        else
            echo "$1 is not valid"
        fi
    else
        echo "$1 is less than 5 characters"
    fi
fi
}
echo -n "Enter CR Number: "
passingCR=""
read passingCR
crCheck $passingCR

CR1=N/A
CR2=n/a
CR3=N/a
CR4=abcdefg
CR5=abc
CR6=123
CR7=12345
CR8=12345abc
CR9=CR00581A

crCheck $CR1
crCheck $CR2
crCheck $CR3
crCheck $CR4
crCheck $CR5
crCheck $CR6
crCheck $CR7
crCheck $CR8
crCheck $CR9

#----------------------------------------------------------------