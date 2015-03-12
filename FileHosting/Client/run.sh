if [[ $# -eq 2 ]]
then
java Client $1 $2 $3
else
echo "usage : bash run.sh <filename> <port no>"
fi
