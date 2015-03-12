if [[ $# -eq 1 ]]
then
java FileServer $1
else
echo "usage : bash run.sh <portno>"
fi
