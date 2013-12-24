filename=`echo $1 | sed -e 's/\..*//'`

time java BurrowsWheeler - < $1 | java MoveToFront - | java Huffman - > "$filename"_compressed.bwzip

time java Huffman + < "$filename"_compressed.bwzip | java MoveToFront + | java BurrowsWheeler + > "$filename"_decompressed.txt

#rm "$filename"_compressed.bwzip

diff $1 "$filename"_decompressed.txt
